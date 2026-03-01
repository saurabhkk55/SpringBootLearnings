# 1️⃣ Which Thread Pool Does Tomcat Use?

When Spring Boot runs with embedded **Apache Tomcat**, it uses Tomcat’s internal **Executor thread pool**.

By default, Tomcat uses:

### 👉 `org.apache.tomcat.util.threads.ThreadPoolExecutor`

This is Tomcat’s customized version built on top of:

```
java.util.concurrent.ThreadPoolExecutor
```

It is used by the **Connector** (HTTP NIO connector).

---

### Default Configuration (Spring Boot)

```properties
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=10
```

So:

* **Max threads = 200**
* **Min idle threads = 10**

---

# 2️⃣ What Happens If More Than 200 Concurrent Requests Come?

Let’s break it clearly.

## Case 1: All 200 Threads Busy

When 200 requests are actively being processed:

* The thread pool is exhausted.
* New requests go into a **request queue (accept queue)**.

Tomcat has a property:

```properties
server.tomcat.accept-count=100
```

Default ≈ 100

So:

* 200 → actively processing
* 100 → waiting in queue
* Total = 300 requests handled temporarily

---

## Case 2: Queue Also Full

If:

* 200 threads busy
* 100 requests waiting
* New request arrives

👉 Tomcat **rejects the connection**

The client may see:

* Connection refused
* 503 error
* Timeout

---

# 3️⃣ Simple Visualization

```
Incoming Requests
        ↓
[ Thread Pool (200 max) ]
        ↓
[ Waiting Queue (100) ]
        ↓
[ Rejected ]
```

---

# 4️⃣ Important: It Is Blocking

Because **Spring MVC + Tomcat is blocking**:

* If a request waits 2 seconds for DB
* That thread is stuck for 2 seconds

So 200 slow requests = server saturated.

---

# 5️⃣ How To Handle High Concurrency?

Options:

### ✅ 1. Increase thread pool

(Not always good — more threads = more memory + context switching)

### ✅ 2. Optimize DB calls

Reduce blocking time.

### ✅ 3. Use async controller

```java
@GetMapping("/test")
public Callable<String> test() {
    return () -> "Hello";
}
```

### ✅ 4. Use reactive stack

With **Netty** via Spring WebFlux → fewer threads, more scalability.

---

# 🔥 Interview-Ready Answer

> Tomcat uses a ThreadPoolExecutor for request processing. By default, it allows 200 concurrent threads. If more than 200 requests arrive, they are placed into a waiting queue (accept-count). If both the thread pool and queue are full, additional requests are rejected.
