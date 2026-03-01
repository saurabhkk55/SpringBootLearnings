# 1️⃣ Platform (Normal) Threads

In **Java**, traditional threads are called **platform threads**.

### 🔹 Where is their stack stored?

👉 **Outside the heap**
👉 In **native OS memory**

When JVM creates a platform thread:

* OS allocates stack memory (e.g., 1 MB)
* This stack is **not part of Java heap**
* It is native memory

So:

```
Heap → Objects
Stack (platform thread) → Native OS memory
```

That’s why too many threads cause:

```
OutOfMemoryError: unable to create native thread
```

Because native memory gets exhausted.

---

# 2️⃣ Virtual Threads (Java 21)

Virtual threads are different.

### 🔹 Where is their stack stored?

👉 **Inside the heap**

Yes ✅

Virtual thread stack:

* Stored as stack chunks
* Allocated on **heap**
* Grows & shrinks dynamically
* Managed by JVM (not OS)

So:

```
Heap → Objects + Virtual thread stacks
```

---

# 3️⃣ Clear Comparison

| Feature            | Platform Thread | Virtual Thread |
| ------------------ | --------------- | -------------- |
| Stack location     | Native memory   | Heap memory    |
| Fixed size?        | Yes (e.g., 1MB) | No             |
| Grows dynamically? | No              | Yes            |
| Managed by         | OS              | JVM            |

---

# 5️⃣ Important Clarification

Even in virtual threads:

* Local variables still behave like stack variables
* Concept of stack still exists logically
* But implementation is heap-based

---

# ✅ Final Answer

✔ Platform thread stack → **Native memory (outside heap)**
✔ Virtual thread stack → **Stored inside heap**
