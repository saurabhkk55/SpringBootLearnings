`thenApply()`, `thenAccept()`, and `thenRun()` are three continuation methods of `CompletableFuture`. They are used **after a previous asynchronous task completes**, but they differ in whether they:

* Receive the previous result.
* Return a new result.
* Return nothing.

Think of them like this:

| Method         | Receives Previous Result? | Returns New Result? | Use Case                              |
| -------------- | ------------------------- | ------------------- | ------------------------------------- |
| `thenApply()`  | ✅ Yes                     | ✅ Yes               | Transform the result                  |
| `thenAccept()` | ✅ Yes                     | ❌ No                | Consume/use the result                |
| `thenRun()`    | ❌ No                      | ❌ No                | Execute another task after completion |

---

```java
package Record;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    static void main(String[] args) {
        // thenApply() - Transform the Result
        // Demo-1
        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("Fetching user...");
                    return "Saurabh";
                });

        CompletableFuture<String> upperCaseFuture =
                future.thenApply(name -> {
                    return name.toUpperCase();
                });

        System.out.println(upperCaseFuture.join());

        // thenAccept() - Consume the Result
        // Demo-1
        CompletableFuture<Void> future1 =
                CompletableFuture.supplyAsync(() -> "Hello")
                        .thenAccept(value -> {
                            System.out.println(value);
                        });

        future1.join();

        // Demo-2
        CompletableFuture.supplyAsync(() -> fetchOrder())
                .thenAccept(order -> saveOrder(order));

        // thenRun() - Execute Another Task
        // Demo-1
        CompletableFuture<Void> future2 =
                CompletableFuture.supplyAsync(() -> {
                    return "Done";
                }).thenRun(() -> {
                    System.out.println("Cleanup completed.");
                });

        future2.join();
    }

    static int fetchOrder() {
        return 1;
    }

    static void saveOrder(int id) {
        System.out.println("Order id: " + id);
    }
}
```

---

# Summary

| Method                      | Input                 | Output           | Returns                   |
| --------------------------- | --------------------- | ---------------- | ------------------------- |
| `thenApply(Function<T, R>)` | Previous result (`T`) | New result (`R`) | `CompletableFuture<R>`    |
| `thenAccept(Consumer<T>)`   | Previous result (`T`) | Nothing          | `CompletableFuture<Void>` |
| `thenRun(Runnable)`         | No input              | Nothing          | `CompletableFuture<Void>` |

A simple way to remember them is:

* **`thenApply`** → **Transforms** a value (like `map()` in streams).
* **`thenAccept`** → **Consumes** a value (like `forEach()` for a single result).
* **`thenRun`** → **Runs** a follow-up action regardless of the previous value, useful for cleanup, notifications, or triggering the next independent step after completion.
