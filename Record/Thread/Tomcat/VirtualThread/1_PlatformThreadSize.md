In Java, **each thread consumes memory mainly for its stack**.

## 1️⃣ Default Thread Stack Size

Each Java thread gets its own **stack memory**.

Typical default stack size (depends on OS & JVM):

* **Windows (64-bit JVM)** → ~1 MB per thread
* **Linux (64-bit JVM)** → ~1 MB per thread
* Sometimes → 512 KB (depending on JVM settings)

So roughly:

> ✅ **1 thread ≈ 1 MB stack memory (by default)**

---

## 2️⃣ What is stored in Thread Stack?

Thread stack stores:

* Local variables
* Method call frames
* Primitive data
* References (not actual objects)

⚠️ Important:
Objects created inside methods are stored in **Heap**, not in stack.

---

## 3️⃣ How to Check or Change Stack Size?

You can control stack size using:

```
-Xss<size>
```

Example:

```
-Xss512k
-Xss2m
```

This sets stack size per thread.

Example:

```
java -Xss512k MyApp
```

Now each thread will use 512 KB stack.

---

## 4️⃣ Total Memory Impact

If:

* Stack size = 1 MB
* You create 1000 threads

Memory used just for stack:

```
1000 × 1 MB = 1000 MB (~1 GB)
```

That’s why creating too many threads causes:

```
java.lang.OutOfMemoryError: unable to create new native thread
```

---

## 5️⃣ Real-World Advice (Important for Backend Developer 🚀)

Since you're working with Spring Boot & microservices:

👉 Avoid creating too many threads manually
👉 Use:

* ExecutorService (Thread Pool)
* ForkJoinPool
* Virtual Threads (Java 21+)

---

## 6️⃣ What About Virtual Threads? (Java 21)

In **Java 21**, Virtual Threads use much smaller memory (few KB initially).

They are lightweight compared to platform threads.

---

# Final Summary

| Type                     | Memory Used      |
| ------------------------ | ---------------- |
| Platform Thread          | ~1 MB (default)  |
| Virtual Thread (Java 21) | Few KB initially |
