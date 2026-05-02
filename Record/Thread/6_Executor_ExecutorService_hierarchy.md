# 🌳 Executor & ExecutorService Hierarchy

At the top:

👉 Executor

```id="5clhzv"
Executor
   └── ExecutorService
           └── ScheduledExecutorService
```

---

# 🧠 1. Executor (Basic Interface)

👉 Executor

### 🔹 Purpose:

* Simple abstraction to run tasks (threads)

### 🔹 Method:

```java
void execute(Runnable command);
```

---

### ❌ Limitation:

* No lifecycle control
* No result handling
* No shutdown

👉 Very basic → rarely used directly

---

# 🚀 2. ExecutorService (Advanced)

👉 ExecutorService

Extends Executor and adds powerful features.

---

## 🔥 Key Capabilities:

### ✅ 1. Submit tasks

```java
Future<Integer> future = executor.submit(() -> 10 + 20);
```

---

### ✅ 2. Get result (Future)

👉 Future

```java
Integer result = future.get();
```

---

### ✅ 3. Shutdown control

```java
executor.shutdown();
executor.shutdownNow();
```

---

### ✅ 4. Bulk operations

```java
executor.invokeAll(tasks);
executor.invokeAny(tasks);
```

---

# ⏰ 3. ScheduledExecutorService

👉 ScheduledExecutorService

---

### 🔹 Purpose:

Run tasks:

* after delay
* periodically

---

### 🔥 Example:

```java
scheduler.schedule(() -> {
    System.out.println("Run after 5 sec");
}, 5, TimeUnit.SECONDS);
```

---

### 🔁 Periodic task:

```java
scheduler.scheduleAtFixedRate(() -> {
    System.out.println("Runs every 10 sec");
}, 0, 10, TimeUnit.SECONDS);
```

---

# 🏭 Implementation Classes (Important)

You don’t create these manually.

Use:
👉 Executors

---

## 🔧 Common Thread Pools:

### 1. Fixed Thread Pool

```java
ExecutorService executor = Executors.newFixedThreadPool(5);
```

👉 5 threads only

---

### 2. Cached Thread Pool

```java
Executors.newCachedThreadPool();
```

👉 Dynamic threads (can grow unlimited)

---

### 3. Single Thread Executor

```java
Executors.newSingleThreadExecutor();
```

👉 Only 1 thread (sequential execution)

---

### 4. Scheduled Thread Pool

```java
Executors.newScheduledThreadPool(3);
```

---

# 🧠 Visual Flow

```id="wy4fbo"
Runnable/Callable Task
        ↓
Executor / ExecutorService
        ↓
Thread Pool
        ↓
Worker Thread executes task
```

---

# ⚠️ Runnable vs Callable

| Feature      | Runnable     | Callable |
| ------------ | ------------ | -------- |
| Return value | ❌ No         | ✅ Yes    |
| Exception    | ❌ No checked | ✅ Yes    |
| Method       | run()        | call()   |

---

# 🔥 Real-Life Use Case

### Example: Parallel API Calls

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

Future<String> f1 = executor.submit(() -> callServiceA());
Future<String> f2 = executor.submit(() -> callServiceB());

String result = f1.get() + f2.get();
```

👉 Improves performance (parallel execution)

---

# ⚠️ Common Mistakes

❌ Not shutting down executor

```java
executor.shutdown(); // ALWAYS do this
```

❌ Using too many threads → memory issue
❌ Using cached pool blindly → can create unlimited threads

---

# ✅ Best Practices

✔ Use fixed thread pool for controlled concurrency
✔ Use scheduled executor instead of Timer
✔ Always shutdown
✔ Handle exceptions in tasks

---

# 🎯 Final Summary

* Executor → basic task execution
* ExecutorService → thread pool + control + result
* ScheduledExecutorService → scheduling
