In Java, **Java** has three common classes to work with strings:

* `String`
* `StringBuffer`
* `StringBuilder`

The **main differences** are about **immutability** and **thread safety**.

---

# 1️⃣ String

`String` objects are **immutable**.

👉 Immutable means **once a string object is created, its value cannot be changed**.

If you modify it, **Java creates a new object**.

### Example

```java
String s = "Hello";
s = s + " World";
```

What happens internally:

1. `"Hello"` object created.
2. `"Hello World"` new object created.
3. `s` now points to new object.

So many operations = **many objects created** → **memory waste**.

### When to use String

Use `String` when:

* The value **will not change frequently**
* Simple **read operations**
* **Constants** or fixed text

Example:

```java
String name = "Saurabh";
```

---

# 2️⃣ StringBuffer

`StringBuffer` is **mutable**.

👉 Mutable means **the object can be modified without creating new objects**.

### Example

```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World");
```

Memory behavior:

Only **one object** is modified.

Result:

```
Hello World
```

### Important Feature

`StringBuffer` is **thread-safe**.

It uses **synchronized methods**.

So multiple threads can safely modify it.

Example:

```java
sb.append("Java");
```

Internally:

```
public synchronized StringBuffer append(String str)
```

But synchronization makes it **slower**.

### When to use StringBuffer

Use when:

* **Multiple threads modify the string**
* Thread safety is required

Example:

```
Multithreaded logging
Shared string modification
```

---

# 3️⃣ StringBuilder

`StringBuilder` is also **mutable**, like `StringBuffer`.

But:

❗ **It is NOT thread-safe**

It **does not use synchronization**.

Because of this, it is **faster**.

### Example

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
```

Output:

```
Hello World
```

### When to use StringBuilder

Use when:

* **Single-threaded environment**
* String modification happens frequently
* **High performance needed**

Example:

* Building large text
* File processing
* JSON creation

---

# 🔑 Simple Comparison

| Feature         | String                 | StringBuffer       | StringBuilder             |
| --------------- | ---------------------- | ------------------ | ------------------------- |
| Mutable         | ❌ No                   | ✅ Yes              | ✅ Yes                     |
| Thread Safe     | ❌ No                   | ✅ Yes              | ❌ No                      |
| Performance     | Slow (creates objects) | Medium             | Fast                      |
| Synchronization | ❌                      | ✅                  | ❌                         |
| Use Case        | Fixed text             | Multithreaded apps | Single thread performance |

---

# 🎯 Real Life Example

Suppose you are **building a sentence in a loop**.

❌ Bad approach

```java
String s = "";
for(int i=0;i<1000;i++){
    s = s + i;
}
```

This creates **1000 objects**.

---

✅ Better

```java
StringBuilder sb = new StringBuilder();
for(int i=0;i<1000;i++){
    sb.append(i);
}
```

Only **one object used** → much faster.

---

# ⭐ Easy Rule to Remember

```
String        → Fixed text
StringBuffer  → Multi-threaded modification
StringBuilder → Single-threaded modification (BEST performance)
```
