Bhai Saurabh, **Future vs CompletableFuture mein exception handling** interview + real-project dono ke liye bahut important hai.
Seedha, practical aur **best practices** ke saath samjhaata hoon — bina faltu pointers 😄

---

## 1️⃣ `Future` mein Exception Handling (old & painful way)

### Example

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

Future<Integer> future = executor.submit(() -> {
    if (true) {
        throw new RuntimeException("Something went wrong");
    }
    return 10;
});

try {
    Integer result = future.get(); // blocking call
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // best practice
} catch (ExecutionException e) {
    // Actual exception yahin milti hai
    System.out.println(e.getCause().getMessage());
}
```

### Reality check ❌

* `get()` **blocking** hai
* Exception ko **ExecutionException** mein wrap kar deta hai
* Functional / clean handling possible nahi

👉 **Future ke saath yahi ek tarika hai** (try-catch mandatory)

---

## 2️⃣ `CompletableFuture` – BEST & MODERN WAY 😎

CompletableFuture **non-blocking + functional exception handling** deta hai.

---

## 2.1 `exceptionally()` – fallback value dena ho

### Use case

> Error aaye toh default value return karni hai

```java
CompletableFuture<Integer> cf =
        CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("DB down");
        })
        .exceptionally(ex -> {
            System.out.println(ex.getMessage());
            return 0; // fallback value
        });

System.out.println(cf.join());
```

### Output

```
DB down
0
```

✔ Most commonly used
✔ Simple & clean
❌ Sirf error handle karta hai, success ka access nahi

---

## 2.2 `handle()` – success + failure dono ek jagah

### Use case

> Result bhi chahiye, exception bhi

```java
CompletableFuture<Integer> cf =
        CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("Boom");
            return 10;
        })
        .handle((result, ex) -> {
            if (ex != null) {
                System.out.println(ex.getMessage());
                return 0;
            }
            return result;
        });

System.out.println(cf.join());
```

✔ Most powerful
✔ Real-life microservices mein useful
✔ Centralized handling

---

## 2.3 `whenComplete()` – sirf logging / side effect

### Use case

> Exception log karni hai, flow change nahi karna

```java
CompletableFuture<Integer> cf =
        CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Service failed");
        })
        .whenComplete((res, ex) -> {
            if (ex != null) {
                System.out.println("Error logged: " + ex.getMessage());
            }
        });

cf.join(); // exception yahin throw hogi
```

✔ Logging / monitoring
❌ Exception handle nahi karta, bas observe karta hai

---

## 2.4 `join()` vs `get()` (important interview point)

```java
cf.get();  // checked exception
cf.join(); // unchecked CompletionException
```

### Best Practice

➡ **CompletableFuture ke saath `join()` use karo**
➡ Cleaner code, no try-catch clutter

---

## 3️⃣ Multiple CompletableFuture mein exception handling

### Example

```java
CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("API failed");
});

CompletableFuture<Integer> combined =
        cf1.thenCombine(cf2, Integer::sum)
           .exceptionally(ex -> 0);

System.out.println(combined.join());
```

✔ Kisi ek mein failure aaye → controlled output

---

## 4️⃣ REAL PROJECT BEST PRACTICES (Spring / Microservices)

### ✅ Rule of thumb

* **Future** → avoid in new code
* **CompletableFuture** → always
* Use:

    * `exceptionally()` → fallback
    * `handle()` → business decision
    * `whenComplete()` → logging
* Always wrap async calls at **service layer**

---

## 5️⃣ streams.One-line Interview Answer 🔥

> **Future mein exception sirf `get()` ke through handle hoti hai (blocking + wrapped).
> CompletableFuture functional, non-blocking exception handling deta hai using `exceptionally`, `handle` & `whenComplete`.**
