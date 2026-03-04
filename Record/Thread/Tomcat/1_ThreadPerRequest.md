Yes ✅ — **by default, Spring Boot with embedded Tomcat follows the *Thread-Per-Request* model.**

---

## 1️⃣ What happens internally?

When you use:

* `spring-boot-starter-web`
* Embedded **Apache Tomcat**

Then:

* Tomcat maintains a **thread pool**
* For every incoming HTTP request:

    * streams.One thread from the pool is assigned
    * That thread handles the complete lifecycle of the request
    * After response → thread returns to pool

So it is:

> **streams.One request → streams.One thread (from pool)**

---

## 2️⃣ Simple Flow

```
Client Request
      ↓
Tomcat Thread Pool
      ↓
Thread assigned
      ↓
Controller → Service → DB Call
      ↓
Response sent
      ↓
Thread released back to pool
```

---

## 3️⃣ Important Clarification

It is NOT:

* ❌ streams.One new thread created per request
* ✅ It uses a **fixed-size thread pool**

Default max threads in Tomcat (Spring Boot) ≈ **200**

You can configure it:

```properties
server.tomcat.threads.max=300
```

---

## 4️⃣ Why This Matters

Since it is blocking:

* If thread is waiting for DB
* Or waiting for another microservice

👉 That thread is blocked
👉 Cannot handle another request
👉 If all threads are busy → new requests wait

This is why high-latency systems can suffer with thread-per-request.

---

## 5️⃣ When This Model Changes?

If you use:

* `spring-boot-starter-webflux`
* With **Project Reactor**
* And **Netty**

Then it follows:

> Event-loop / Non-blocking model
> Few threads → handle many requests

---

# 🔥 Interview Summary Answer

If interviewer asks:

> "Does Spring Boot follow thread-per-request model?"

You can say:

> Yes, when using Spring MVC with embedded Tomcat, it follows a thread-per-request model using a thread pool. Each HTTP request is processed by one thread from the pool until completion.
