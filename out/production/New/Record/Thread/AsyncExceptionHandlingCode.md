# 📘 Spring `@Async` + `CompletableFuture`

## Complete, Correct & Production-Grade Documentation

---

## 🔰 Common Setup (Mandatory)

### Main Application

```java
@SpringBootApplication
@EnableAsync
public class AsyncDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsyncDemoApplication.class, args);
    }
}
```

### 🧠 Explanation

* `@EnableAsync` → Spring ko batata hai ki `@Async` methods
  **separate thread pool** me chalenge
* MVC (caller) thread:

    * wait nahi karta
    * block nahi hota

---

## 🧵 Thread Model (VERY IMPORTANT)

```
HTTP Request
   ↓
MVC Thread (Tomcat)
   ↓
@Async Method → Spring TaskExecutor Thread
   ↓
CompletableFuture completion
```

---

Bilkul sahi pakda 👍
Documentation **tabhi complete hoti hai jab service + controller + output + explanation** sab ho.
Neeche **Case-1 ko fully FIX karke** de raha hoon — **production-style documentation** ke format me.

---

# 1️⃣ Case-1: `@Async` + `void` ❌ (Fire & Forget)

> **Is pattern ka use sirf non-critical background kaamon ke liye hota hai.
> Caller ko kabhi bhi success / failure ka response nahi milta.**

---

## 1.1 ❌ Problematic Code

### 📌 Service

```java
@Service
public class EmailService {

    @Async
    public void sendEmail() {
        throw new RuntimeException("SMTP server down");
    }
}
```

---

### 📌 Controller

```java
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail() {
        emailService.sendEmail();
        return "Email triggered";
    }
}
```

---

## ❌ Output (Without Exception Handler)

### HTTP Response

```
HTTP 200 OK
Response Body: Email triggered
```

### Console

```
(no exception printed)
```

---

## 🧠 Explanation (Why this happens?)

* Controller thread:

    * `sendEmail()` call karta hai
    * turant response return kar deta hai
* `@Async` method:

    * alag thread me execute hota hai
* Exception:

    * ❌ caller tak propagate nahi hoti
    * ❌ Spring MVC lifecycle se bahar hoti hai

👉 **Silent failure = dangerous in production**

---

## 1.2 ✅ Correct Handling (Logging / Monitoring only)

> ⚠️ Ye **caller ko error dene ke liye nahi**,
> sirf **logging / alerting / monitoring** ke liye hota hai.

---

### 📌 Async Configuration

```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            System.out.println("Async error in method: " + method.getName());
            System.out.println("Exception message: " + ex.getMessage());
        };
    }
}
```

---

## ✅ Output (With Exception Handler)

### HTTP Response

```
HTTP 200 OK
Response Body: Email triggered
```

### Console Logs

```
Async error in method: sendEmail
Exception message: SMTP server down
```

---

## 🧠 Important Clarification

✔ Exception **logged ho gayi**
❌ Client ko **ab bhi kuch pata nahi**

> **`@Async void` ka matlab hai:
> "Fire & Forget" — no feedback to caller**

---

## 🎯 When should you use this pattern?

### ✅ Suitable Use Cases

✔ Email sending
✔ SMS / Push notifications
✔ Audit logging
✔ Metrics publishing

### ❌ DO NOT Use For

❌ Payment processing
❌ Order creation
❌ Business-critical logic
❌ Anything requiring client confirmation

---

## 💣 Interview One-Liner

> **`@Async void` methods caller ko exception propagate nahi karti.
> Failures sirf `AsyncUncaughtExceptionHandler` me handle kiye ja sakte hain,
> isliye ye pattern sirf fire-and-forget use cases ke liye suitable hai.**

---

Bilkul sahi 👍
Documentation ka **Case-2 bhi Case-1 jaise fully end-to-end** hona chahiye.
Neeche main **Case-2 ko complete, runnable, with controller + output + explanation** ke saath fix kar raha hoon.

---

# 2️⃣ Case-2: `@Async` + `Future` ❌ (Legacy & Blocking)

> **Ye pattern purana (pre-Java 8) hai aur modern Spring applications me avoid karna chahiye.**

---

## 2.1 ❌ Problematic Code

### 📌 Service

```java
@Service
public class OrderService {

    @Async
    public Future<Integer> processOrder() {
        int result = 10 / 0; // Exception
        return new AsyncResult<>(result);
    }
}
```

---

### 📌 Controller

```java
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order")
    public String placeOrder() {
        try {
            Future<Integer> future = orderService.processOrder();

            // ❌ BLOCKING call
            Integer result = future.get();

            return "Order processed. Result: " + result;
        } catch (Exception e) {
            return "Order failed. Error: " + e.getCause().getMessage();
        }
    }
}
```

---

## ❌ Output

### HTTP Response

```
HTTP 200 OK
Response Body: Order failed. Error: / by zero
```

---

### Console (Exception flow)

```
java.util.concurrent.ExecutionException: java.lang.ArithmeticException: / by zero
```

---

## 🧠 Explanation (What went wrong?)

### 1️⃣ Blocking Behaviour

```java
future.get(); // BLOCKS MVC thread
```

* MVC (Tomcat) thread:

    * yahin ruk jaata hai
    * async ka fayda khatam

---

### 2️⃣ Exception Wrapping

* Actual exception:

  ```java
  ArithmeticException
  ```
* Controller ko milta hai:

  ```java
  ExecutionException
      -> getCause()
  ```

👉 Extra boilerplate + confusing error handling

---

### 3️⃣ Old Java Pattern

* `Future`:

    * Java 5 era
    * callback / chaining support nahi
* Manual try-catch required

---

## ❌ Why this is BAD in production?

| Issue              | Impact                   |
| ------------------ | ------------------------ |
| Thread blocking    | Poor scalability         |
| Exception wrapping | Ugly error handling      |
| No chaining        | Hard to compose          |
| No timeout API     | Risk of hanging requests |

---

## ✅ What should be used instead?

### ✔ Replace with `CompletableFuture`

```java
@Async
public CompletableFuture<Integer> processOrder() {
    return CompletableFuture.failedFuture(
            new RuntimeException("Order processing failed"));
}
```

```java
@GetMapping("/order")
public CompletableFuture<Integer> placeOrder() {
    return orderService.processOrder()
            .exceptionally(ex -> 0);
}
```

✔ Non-blocking
✔ Clean error handling
✔ Functional style

---

## 🎯 Key Takeaway

> **`@Async` + `Future` forces you to block the caller thread using `get()`,
> which defeats the purpose of async programming.
> Modern Spring applications should always prefer `CompletableFuture`.**

---

## 💣 Interview One-Liner

> **`Future` with `@Async` is a legacy pattern because it requires blocking calls and wraps exceptions inside `ExecutionException`.
> `CompletableFuture` provides non-blocking, composable, and cleaner async handling.**

---

# 3️⃣ Case-3: `@Async` + `CompletableFuture` ✅ BEST PRACTICE

---

## ✅ Correct Service (NO `supplyAsync`)

```java
@Service
public class PriceService {

    @Async
    public CompletableFuture<Integer> calculatePrice() {
        throw new RuntimeException("Price service down");
    }
}
```

### 🧠 Why no `supplyAsync()`?

> `@Async` already method ko async bana deta hai.
> Spring khud exception ko `CompletableFuture` me wrap kar deta hai.

---

## 3.1 ✅ Exception Handling at CALLER (Recommended)

### Controller

```java
@GetMapping("/price")
public CompletableFuture<Integer> price() {
    return priceService.calculatePrice()
            .exceptionally(ex -> {
                System.out.println("Handled error: " + ex.getMessage());
                return 0;
            });
}
```

### ✅ Output

```
HTTP 200 OK
Response Body: 0
```

### 🧠 Why this is BEST?

✔ Non-blocking
✔ Caller decides fallback
✔ Clean responsibility separation
✔ Reactive-style flow

---

# 3.2 ✅ Exception Handling INSIDE Service

### (Correct & Fixed Version)

❌ **WRONG (double async)**
`@Async + supplyAsync()` → **never do this**

---

### ✅ Correct Way (Service-level fallback)

```java
@Service
public class PriceService {

    @Async
    public CompletableFuture<Integer> calculatePrice() {
        try {
            throw new RuntimeException("Boom");
        } catch (Exception ex) {
            System.out.println("Fallback inside service");
            return CompletableFuture.completedFuture(0);
        }
    }
}
```

### 🧠 When to use this?

✔ Internal async jobs
✔ Kafka consumers
✔ Batch / scheduler tasks

Caller clean rehta hai 👍

---

# 4️⃣ Async REST + Proper HTTP Mapping ✅

### Service

```java
@Async
public CompletableFuture<Integer> getAmount() {
    throw new RuntimeException("Payment gateway down");
}
```

### Controller

```java
@GetMapping("/payment")
public CompletableFuture<ResponseEntity<Integer>> payment() {
    return paymentService.getAmount()
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex ->
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .body(0)
            );
}
```

### ✅ Output

```
HTTP 503 SERVICE_UNAVAILABLE
Body: 0
```

✔ Proper HTTP semantics
✔ Async REST
✔ MVC thread free

---

# 5️⃣ `@Async` + `@ControllerAdvice` ❌ (Interview Trap)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}
```

### ❌ Why it fails?

* `@Async` → different thread
* MVC lifecycle ke bahar
* ControllerAdvice sirf MVC thread ke liye

---

# 6️⃣ Production-Grade Pattern 🔥

```java
@Async
public CompletableFuture<String> callRemoteApi() {
    return CompletableFuture.completedFuture("SUCCESS");
}
```

```java
service.callRemoteApi()
       .orTimeout(2, TimeUnit.SECONDS)
       .exceptionally(ex -> "FALLBACK");
```

✔ Timeout
✔ Fallback
✔ Resilience4j compatible
✔ Clean async flow

---

# 🚫 MOST IMPORTANT RULE (Remember Forever)

> ❌ **Never use `@Async` and `CompletableFuture.supplyAsync()` together**
> ✅ **Choose exactly ONE async mechanism**

---

# 🧠 Decision Matrix (Interview GOLD)

| Requirement                    | Correct Choice                  |
| ------------------------------ | ------------------------------- |
| Fire & forget                  | `@Async void`                   |
| Async + return value           | `@Async + CompletableFuture<T>` |
| Caller decides fallback        | `.exceptionally()` in caller    |
| Service decides fallback       | `completedFuture()`             |
| Custom executor without Spring | `supplyAsync(executor)`         |
| `@Async` + `supplyAsync`       | ❌ NEVER                         |

---

# 💣 Final Interview One-Liner

> **`@Async` already executes the method asynchronously using Spring’s executor and automatically completes the returned `CompletableFuture`.
> Using `supplyAsync()` again causes double async execution and loss of executor control, so only one async mechanism should be used.**

---

Agar chaahe next step me main ye bhi bana deta hoon 👇
✔ **Async unit testing (JUnit + Awaitility)**
✔ **Thread-name proof with logs**
✔ **@Async vs WebFlux – same example**

Bas bolo 💪
