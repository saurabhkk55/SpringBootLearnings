# Downstream Service Takes 6 Seconds but Upstream Timeout is 5 Seconds

Example:

```text
Client -> Service A -> Service B
```

* Service B response time = 6 sec
* Service A timeout = 5 sec

So Service A fails before Service B responds.

---

# Possible Solutions

# 1. Increase Timeout (Simple Solution)

Increase upstream timeout greater than downstream response time.

Example:

```yaml
readTimeout: 10s
connectTimeout: 3s
```

or in Spring:

```properties
spring.mvc.async.request-timeout=10000
```

---

# But Increasing Timeout Is NOT Always Best

Long timeout may cause:

* thread blocking
* poor scalability
* resource exhaustion
* bad user experience

So first identify:

```text
Why is downstream taking 6 sec?
```

---

# 2. Optimize Downstream Service (Preferred)

Reduce response time by:

* DB indexing
* caching
* pagination
* async processing
* batch processing
* query optimization
* reducing external API calls

Best solution is usually:

```text
Make downstream faster
```

instead of increasing timeout.

---

# 3. Use Async Processing

Instead of making client wait:

```text
1. Accept request
2. Return acknowledgement immediately
3. Process in background
4. Notify later
```

Example:

```text
202 ACCEPTED
```

Use cases:

* report generation
* file processing
* email sending
* ticket creation
* heavy workflows

Technologies:

* Kafka
* RabbitMQ
* SQS
* CompletableFuture
* @Async
