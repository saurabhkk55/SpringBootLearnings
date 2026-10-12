# 1. `thenCompose()` – Chain Dependent Async Operations

Use `thenCompose()` when **the second asynchronous task depends on the result of the first**.

Think of it as **`flatMap()`** for `CompletableFuture`.

### Problem without `thenCompose()`

Suppose:

* Fetch User ID (async)
* Using that ID, fetch User Details (async)

```java
CompletableFuture<CompletableFuture<String>> future =
    CompletableFuture.supplyAsync(() -> 101)
        .thenApply(id ->
            CompletableFuture.supplyAsync(() -> "User-" + id)
        );
```

Result:

```text
CompletableFuture<CompletableFuture<String>>
```

You end up with a **nested future**, which is usually not what you want.

---

### Solution: `thenCompose()`

```java
import java.util.concurrent.CompletableFuture;

public class ThenComposeExample {

    public static void main(String[] args) {

        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> 101)
                        .thenCompose(id ->
                                CompletableFuture.supplyAsync(() -> "User-" + id)
                        );

        System.out.println(future.join());
    }
}
```

Output:

```text
User-101
```

Flow:

```text
Fetch User ID --> 101 --> thenCompose() --> Fetch User Details --> User-101
```

**Rule:**

* First task finishes.
* Its result is used to start another async task.
* Final result is a **single** `CompletableFuture`.

---

# 2. `thenCombine()` – Combine Two Independent Async Tasks

Use `thenCombine()` when **two asynchronous tasks are independent** and you want to combine their results.

Example:

* Fetch employee name.
* Fetch employee salary.

Both can run at the same time.

```java
import java.util.concurrent.CompletableFuture;

public class ThenCombineExample {

    public static void main(String[] args) {

        CompletableFuture<String> nameFuture =
                CompletableFuture.supplyAsync(() -> "Saurabh");

        CompletableFuture<Integer> salaryFuture =
                CompletableFuture.supplyAsync(() -> 120000);

        CompletableFuture<String> result =
                nameFuture.thenCombine(
                        salaryFuture,
                        (name, salary) -> name + " earns ₹" + salary
                );

        System.out.println(result.join());
    }
}
```

Output:

```text
Saurabh earns ₹120000
```

Flow:

```text
Fetch Name --------\
                    \
                     ---> thenCombine ---> Final Result
                    /
Fetch Salary ------/
```

**Rule:**

* Both tasks start independently.
* `thenCombine()` waits for both.
* It combines their results.

---

# 3. `CompletableFuture.allOf()`

Use `allOf()` when you need to **wait until all tasks finish**.

Example:

```java
import java.util.concurrent.CompletableFuture;

public class AllOfExample {

    public static void main(String[] args) {

        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(() -> "Java");

        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> "Spring");

        CompletableFuture<String> f3 =
                CompletableFuture.supplyAsync(() -> "Kafka");

        CompletableFuture<Void> all =
                CompletableFuture.allOf(f1, f2, f3);

        all.join();

        System.out.println(f1.join());
        System.out.println(f2.join());
        System.out.println(f3.join());
    }
}
```

Output:

```text
Java
Spring
Kafka
```

Flow:

```text
Task1 ----\
Task2 ----- > allOf() ---> Continue
Task3 ----/
```

### Important

`allOf()` returns:

```java
CompletableFuture<Void>
```

It **does not** return a list of results.

You retrieve the individual results after `allOf()` completes:

```java
all.join();

String a = f1.join();
String b = f2.join();
String c = f3.join();
```

---

# Real Spring Boot Example

Suppose your API needs:

* Customer details
* Order history
* Payment details

All three services are independent.

```java
CompletableFuture<Customer> customer =
        CompletableFuture.supplyAsync(() -> customerService.getCustomer());

CompletableFuture<List<Order>> orders =
        CompletableFuture.supplyAsync(() -> orderService.getOrders());

CompletableFuture<Payment> payment =
        CompletableFuture.supplyAsync(() -> paymentService.getPayment());

CompletableFuture.allOf(customer, orders, payment).join();

Customer c = customer.join();
List<Order> o = orders.join();
Payment p = payment.join();
```

Instead of waiting for each service one by one, they all execute concurrently.

---

# When to Use Which?

| Method          | Scenario                                                    | Runs Tasks             |
| --------------- | ----------------------------------------------------------- | ---------------------- |
| `thenApply()`   | Transform a result                                          | Sequential             |
| `thenCompose()` | Second async task depends on first                          | Sequential (dependent) |
| `thenCombine()` | Two independent async tasks whose results need to be merged | Parallel               |
| `allOf()`       | Wait for many independent tasks to finish                   | Parallel               |
| `thenAccept()`  | Consume a result                                            | Sequential             |
| `thenRun()`     | Run a task after completion without using the result        | Sequential             |

---

# Visual Comparison

### `thenCompose()`

```text
Fetch User ID --> 101 --> Fetch User Details --> User
```

The second async call **depends on** the first.

---

### `thenCombine()`

```text
Fetch Name ------\
                  \
                   ---> Combine ---> Result
                  /
Fetch Salary ----/
```

Both async calls run **at the same time**.

---

### `allOf()`

```text
Task1 ----\
Task2 -----\
Task3 ------> allOf() ---> Continue
Task4 -----/
Task5 ----/
```

Wait for **all** tasks to complete before moving on.

---

## Interview Tip

A common interview question is: **"What is the difference between `thenCompose()` and `thenCombine()`?"**

* **`thenCompose()`**: Use when the **second async operation depends on the result of the first** (e.g., get user ID → then fetch user profile).
* **`thenCombine()`**: Use when **both async operations are independent** and their results need to be merged (e.g., fetch customer details and product catalog simultaneously).
* **`allOf()`**: Use when you have **many independent futures** and only need to proceed after **every one of them** has completed.
