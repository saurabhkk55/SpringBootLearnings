This is a very common problem in synchronous microservice communication.

Scenario:

```text
Service A  ---> REST Call ---> Service B
```

Under normal traffic:

* works fine

Under heavy load:

* Service B becomes slow
* connections pile up
* threads get blocked
* requests timeout

This is exactly where:

* Timeouts
* Circuit Breaker
* Bulkhead
* Retry
* Rate limiting
* Connection pooling
* Async processing

become important.

---

# Why Timeout Happens Under Heavy Load

Usually because of one or more reasons:

| Cause                      | Explanation                  |
| -------------------------- | ---------------------------- |
| Thread exhaustion          | Too many incoming requests   |
| DB slow queries            | Backend becomes slow         |
| Connection pool exhausted  | No HTTP connection available |
| Blocking synchronous calls | Threads wait too long        |
| Retry storm                | Retries increase load more   |
| GC pauses                  | JVM memory pressure          |
| Downstream dependency slow | Another API/DB is slow       |
| No timeout configured      | Calls wait forever           |

---

# Biggest Problem in Sync Calls

Suppose:

```text
User Request
 → Service A
    → calls Service B
       → B becomes slow
```

Now:

* A's threads wait
* request queue increases
* thread pool exhausts
* entire system slows

This is called:

```text
Cascading failure
```

One slow service can bring down whole system.

---

# Correct Solution Architecture

You should use:

```text
RestClient/WebClient
+ Timeout
+ Circuit Breaker
+ Bulkhead
+ Retry
+ Connection Pool
+ Fallback
```

Most companies use:

* Resilience4j
* Spring Cloud Circuit Breaker
* Redis
* Apache Kafka

---

# 1. Configure Proper Timeout

NEVER keep default infinite timeout.

---

## Connection Timeout

How long to establish connection.

```text
Example:
2 seconds
```

---

## Read Timeout

How long to wait for response.

```text
Example:
5 seconds
```

---

# Spring Boot RestClient Example

```java
@Bean
RestClient restClient() {

    HttpComponentsClientHttpRequestFactory factory =
            new HttpComponentsClientHttpRequestFactory();

    factory.setConnectTimeout(2000);
    factory.setReadTimeout(5000);

    return RestClient.builder()
            .requestFactory(factory)
            .build();
}
```

---

# 2. Use Resilience4j TimeLimiter

This prevents waiting forever.

---

## Dependency

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
</dependency>
```

---

# Example

```java
@TimeLimiter(name = "serviceB")
public CompletableFuture<String> callServiceB() {

    return CompletableFuture.supplyAsync(() ->
            restClient.get()
                    .uri("/api/data")
                    .retrieve()
                    .body(String.class)
    );
}
```

---

# Config

```yaml
resilience4j:
  timelimiter:
    instances:
      serviceB:
        timeout-duration: 3s
```

After 3 seconds:

* request cancelled
* fallback triggered

---

# 3. Use Circuit Breaker

If Service B is failing continuously:

* stop calling temporarily

Otherwise:

```text
Failing service keeps receiving more traffic
```

making outage worse.

---

# Example

```java
@CircuitBreaker(name = "serviceB", fallbackMethod = "fallback")
public String getData() {

    return restClient.get()
            .uri("/api/data")
            .retrieve()
            .body(String.class);
}
```

---

# Config

```yaml
resilience4j:
  circuitbreaker:
    instances:
      serviceB:
        failure-rate-threshold: 50
        sliding-window-size: 10
        wait-duration-in-open-state: 10s
```

---

# Circuit Breaker States

```text
CLOSED
  ↓ failures increase
OPEN
  ↓ wait duration
HALF_OPEN
  ↓ success?
CLOSED again
```

---

# 4. Use Bulkhead (VERY IMPORTANT)

Most important under heavy load.

Bulkhead prevents:

```text
One slow dependency exhausting all threads
```

---

# Example

```java
@Bulkhead(name = "serviceB")
public String getData() {
    return callExternalApi();
}
```

---

# Config

```yaml
resilience4j:
  bulkhead:
    instances:
      serviceB:
        max-concurrent-calls: 20
```

Meaning:

* only 20 concurrent calls allowed
* extra requests rejected immediately

This protects your application.

---

# 5. Retry Carefully

Retries can kill system under load.

Bad:

```text
Timeout
→ retry
→ more load
→ more timeout
→ retry storm
```

---

# Safe Retry Strategy

Retry only:

* small number
* transient failures
* exponential backoff

---

## Example

```yaml
resilience4j:
  retry:
    instances:
      serviceB:
        max-attempts: 3
        wait-duration: 2s
```

---

# 6. Use Connection Pooling

Without pooling:

* new TCP connection every request
* huge overhead under load

Use:

* Apache HttpClient pool
* Reactor Netty pool

---

# Example

```java
PoolingHttpClientConnectionManager cm =
        new PoolingHttpClientConnectionManager();

cm.setMaxTotal(200);
cm.setDefaultMaxPerRoute(50);
```

---

# 7. Add Fallback

When downstream fails:

* return cached/default/degraded response

---

## Example

```java
public String fallback(Exception ex) {
    return "Service temporarily unavailable";
}
```

---

# 8. Use Async Communication for Heavy Operations

Sometimes sync REST is wrong architecture.

Instead use:

```text
Service A
   ↓ Kafka/RabbitMQ
Service B processes asynchronously
```

Best for:

* email
* report generation
* notifications
* heavy processing

---

# Real Production Problem

Without protection:

```text
1000 requests
↓
Service B slow
↓
All Tomcat threads blocked
↓
A also crashes
↓
Entire system outage
```

---

# Production-Level Fix

Most companies use:

| Protection      | Purpose                      |
| --------------- | ---------------------------- |
| Timeout         | Prevent infinite wait        |
| Circuit Breaker | Stop calling failing service |
| Bulkhead        | Prevent thread exhaustion    |
| Retry           | Recover transient failures   |
| Rate Limit      | Protect service              |
| Kafka           | Async decoupling             |
| Redis cache     | Reduce downstream calls      |

---

# Recommended Architecture

```text
Client
  ↓
Service A
  ↓
Circuit Breaker
  ↓
Bulkhead
  ↓
Timeout
  ↓
REST Call
  ↓
Service B
```

---

# Golden Rule

```text
Never trust downstream service latency.
Always assume remote call can fail or become slow.
```

---

# Interview-Level Summary

If interviewer asks:

> "How do you handle timeout issues between microservices under heavy load?"

Good answer:

```text
We use:
- connection/read timeout
- Resilience4j TimeLimiter
- Circuit Breaker
- Bulkhead isolation
- controlled retries with backoff
- connection pooling
- fallback mechanisms
- async messaging for heavy operations
to prevent cascading failures and thread exhaustion.
```
