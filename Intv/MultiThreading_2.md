These methods belong to Java's `CompletableFuture`.

| Method          | Use Case                                        | Returns                          |
| --------------- | ----------------------------------------------- | -------------------------------- |
| `thenCombine()` | Combine results of **2 independent futures**    | Result of both futures           |
| `thenCompose()` | Chain futures where **second depends on first** | Flattened single future          |
| `allOf()`       | Wait for **all futures** to complete            | `CompletableFuture<Void>`        |
| `anyOf()`       | Return when **any one future** completes        | Result of first completed future |

### 1. `thenCombine()`

Two tasks run independently, then their results are combined.

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> " World");

CompletableFuture<String> result =
        f1.thenCombine(f2, (a, b) -> a + b);
```

**Output:** `"Hello World"`

---

### 2. `thenCompose()`

Second task depends on the result of the first task.

```java
CompletableFuture<String> result =
        CompletableFuture.supplyAsync(() -> "123")
                .thenCompose(id ->
                        CompletableFuture.supplyAsync(() -> "User-" + id));
```

Think of it as:

```java
userId -> fetchUser(userId)
```

Similar to `flatMap()` in streams.

---

### 3. `allOf()`

Wait for all futures to finish.

```java
CompletableFuture<Void> all =
        CompletableFuture.allOf(f1, f2, f3);

all.join();
```

Use when you need:

* User details
* Orders
* Payments

and want to proceed only after all are fetched.

---

### 4. `anyOf()`

Proceed as soon as the first future completes.

```java
CompletableFuture<Object> first =
        CompletableFuture.anyOf(f1, f2, f3);
```

Use when querying multiple sources and taking the fastest response.

---

### Easy Memory Trick

* **thenCombine** → **Combine** two independent results.
* **thenCompose** → **Compose/Chain** dependent async calls.
* **allOf** → **All** must finish.
* **anyOf** → **Any one** can finish.
