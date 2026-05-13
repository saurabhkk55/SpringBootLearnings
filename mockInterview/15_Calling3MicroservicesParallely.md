# Calling 3 Microservices in Parallel using `CompletableFuture` + `ExecutorService`

Instead of calling APIs one-by-one:

```text
service1 -> wait
service2 -> wait
service3 -> wait
```

we call them in parallel:

```text
service1 ─┐
service2 ─┼──> combine result
service3 ─┘
```

This improves performance because total time becomes approximately:

```text
max(service1, service2, service3)
```

instead of:

```text
service1 + service2 + service3
```

---

# Why use `ExecutorService`?

By default, `CompletableFuture.supplyAsync()` uses:

```java
ForkJoinPool.commonPool()
```

But in real microservice applications, we usually create our own thread pool to:

* control thread count
* avoid exhausting common pool
* improve performance
* isolate API calls

---

# Example Scenario

Suppose:

* User Service
* Order Service
* Payment Service

Need to be called together.

---

# DTO

```java
public class CombinedResponse {

    private UserDto user;
    private OrderDto order;
    private PaymentDto payment;

    // getters setters constructor
}
```

---

# ExecutorService Bean

```java
@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }
}
```

---

# Service Implementation

```java
@Service
public class AggregatorService {

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CombinedResponse getData() {

        CompletableFuture<UserDto> userFuture =
                CompletableFuture.supplyAsync(() ->
                        getUser(), executorService);

        CompletableFuture<OrderDto> orderFuture =
                CompletableFuture.supplyAsync(() ->
                        getOrder(), executorService);

        CompletableFuture<PaymentDto> paymentFuture =
                CompletableFuture.supplyAsync(() ->
                        getPayment(), executorService);

        // wait for all futures
        CompletableFuture.allOf(
                userFuture,
                orderFuture,
                paymentFuture
        ).join();

        // combine result
        return new CombinedResponse(
                userFuture.join(),
                orderFuture.join(),
                paymentFuture.join()
        );
    }

    private UserDto getUser() {
        return restTemplate.getForObject(
                "http://user-service/users/1",
                UserDto.class
        );
    }

    private OrderDto getOrder() {
        return restTemplate.getForObject(
                "http://order-service/orders/1",
                OrderDto.class
        );
    }

    private PaymentDto getPayment() {
        return restTemplate.getForObject(
                "http://payment-service/payments/1",
                PaymentDto.class
        );
    }
}
```

---

# Important Methods

| Method            | Purpose                                  |
| ----------------- | ---------------------------------------- |
| `supplyAsync()`   | Run task asynchronously and return value |
| `runAsync()`      | Run async task without return            |
| `allOf()`         | Wait for all futures                     |
| `join()`          | Get result                               |
| `thenApply()`     | Transform result                         |
| `thenCombine()`   | Combine 2 futures                        |
| `exceptionally()` | Handle exception                         |

---

# Exception Handling Example

```java
CompletableFuture<UserDto> userFuture =
        CompletableFuture
                .supplyAsync(() -> getUser(), executorService)
                .exceptionally(ex -> {
                    log.error("User service failed");
                    return new UserDto();
                });
```

---

# With Timeout

```java
CompletableFuture<UserDto> future =
        CompletableFuture
                .supplyAsync(() -> getUser(), executorService)
                .orTimeout(3, TimeUnit.SECONDS);
```

---

# Interview Points

## Why parallel calls?

To reduce overall response time.

Example:

```text
Service1 = 2 sec
Service2 = 3 sec
Service3 = 1 sec
```

Sequential:

```text
2 + 3 + 1 = 6 sec
```

Parallel:

```text
max(2,3,1) = ~3 sec
```

---

# `join()` vs `get()`

| join()              | get()                 |
| ------------------- | --------------------- |
| unchecked exception | checked exception     |
| cleaner code        | must handle exception |
| commonly used       | older style           |
