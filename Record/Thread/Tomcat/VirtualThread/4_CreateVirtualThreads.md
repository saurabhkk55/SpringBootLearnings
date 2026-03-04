# ✅ 1️⃣ Simplest Way (Directly Creating Virtual Thread)

### 🔹 streams.One Virtual Thread

```java
public class VirtualThreadExample {
    public static void main(String[] args) throws InterruptedException {

        Thread vt = Thread.startVirtualThread(() -> {
            System.out.println("Running in: " + Thread.currentThread());
        });

        vt.join();
    }
}
```

### ✔ What this does:

* Creates a **virtual thread**
* JVM mounts it to a carrier thread
* Executes the task

---

# ✅ 2️⃣ Real Project Style — Using ExecutorService (Recommended)

In real-world applications, we don’t manually create threads.

We use **Executors**.

### 🔹 Virtual Thread Per Task Executor

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadExecutorExample {

    public static void main(String[] args) {

        try (ExecutorService executor =
                 Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 10000; i++) {
                int taskId = i;

                executor.submit(() -> {
                    System.out.println("Task " + taskId +
                        " executed by " + Thread.currentThread());
                });
            }
        }
    }
}
```

### 🔥 Why this is used in real projects?

* Creates a new virtual thread **per task**
* Automatically managed
* Best for:

    * DB calls
    * REST calls
    * Microservices communication
    * File operations

This is the **most common production usage pattern**.

---

# ✅ 3️⃣ Real Backend Example (Simulating DB Call)

In microservices (like your Spring Boot projects), most work is I/O heavy.

```java
import java.util.concurrent.*;

public class OrderService {

    private static final ExecutorService executor =
            Executors.newVirtualThreadPerTaskExecutor();

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            int orderId = i;

            executor.submit(() -> processOrder(orderId));
        }
    }

    static void processOrder(int orderId) {
        try {
            System.out.println("Processing order: " + orderId);

            // Simulate DB call
            Thread.sleep(2000);

            System.out.println("Order completed: " + orderId);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 🚀 What happens internally?

* 1000 virtual threads created
* Only few carrier threads exist (equal to CPU cores)
* When `Thread.sleep()` happens:

    * Virtual thread detaches
    * Carrier thread runs another task

This is why scalability improves.

---

# ✅ 4️⃣ Spring Boot Real-Life Usage

In modern Spring Boot (3.2+), you can enable virtual threads easily.

Spring Boot internally runs request handling using virtual threads.

### 🔹 application.properties

```properties
spring.threads.virtual.enabled=true
```

That’s it.

Now:

* Every HTTP request runs on a virtual thread
* Perfect for REST APIs with DB calls
