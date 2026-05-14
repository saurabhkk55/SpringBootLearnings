# ThreadLocal in Java

`ThreadLocal` is used to store data that is unique for each thread.

Meaning:

```text
Each thread gets its own separate copy of variable
```

So one thread’s data is not shared with another thread.

---

# Why Do We Need ThreadLocal?

Normally, shared variables are accessible by all threads.

Example:

```java
class Test {
    static int count = 0;
}
```

All threads access same `count`.

This may cause:

* race conditions
* synchronization issues
* data corruption

---

# ThreadLocal Solution

With `ThreadLocal`:

```java
ThreadLocal<Integer> local = new ThreadLocal<>();
```

each thread gets independent value.

---

# Example

```java
class Test {

    private static ThreadLocal<Integer> threadLocal =
            new ThreadLocal<>();

    public static void main(String[] args) {

        Runnable task = () -> {

            threadLocal.set((int) (Math.random() * 100));

            System.out.println(
                    Thread.currentThread().getName()
                    + " -> "
                    + threadLocal.get()
            );
        };

        new Thread(task, "Thread-1").start();
        new Thread(task, "Thread-2").start();
    }
}
```

Possible Output:

```text
Thread-1 -> 45
Thread-2 -> 89
```

Each thread has separate value.

---

# Internal Working

Internally:

```text
Each Thread object maintains its own ThreadLocalMap
```

So:

```text
Thread -> ThreadLocalMap -> value
```

Hence values are isolated per thread.

---

# Important Methods

## 1. set()

Store value for current thread.

```java
threadLocal.set("Saurabh");
```

---

## 2. get()

Retrieve value for current thread.

```java
threadLocal.get();
```

---

## 3. remove()

Remove value from current thread.

```java
threadLocal.remove();
```

Important in thread pools to avoid memory leaks.

---

# Real-Life Use Cases

## 1. User Context

Store logged-in user details per request.

Example:

```java
ThreadLocal<User> currentUser
```

---

# 2. Database Connection Per Thread

Each thread maintains separate DB connection.

---

# 3. Transaction Context

Frameworks like Spring internally use ThreadLocal.

---

# 4. Request Tracking / Correlation ID

Store request-specific metadata.

---

# Important Problem — Memory Leak

In thread pools:

```text
Threads are reused
```

If `remove()` is not called:

```text
Old data may remain attached to reused thread
```

leading to:

* memory leak
* wrong data visibility

---

# Best Practice

Always clean ThreadLocal:

```java
try {

    threadLocal.set(value);

} finally {

    threadLocal.remove();
}
```

---

# Important Interview Point

`ThreadLocal` does NOT make object thread-safe.

It simply provides:

```text
One separate copy of variable per thread
```

so synchronization often becomes unnecessary for that variable.
