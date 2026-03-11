Here is a **more compact version** without losing important knowledge.

---

# Java String vs StringBuffer vs StringBuilder

Java provides three classes for working with strings:

* `String`
* `StringBuffer`
* `StringBuilder`

The main differences are **immutability** and **thread safety**.

---

# 1️⃣ String

`String` is **immutable**.

➡ Once a `String` object is created, **its value cannot change**.
If you modify it, **Java creates a new object**.

### Example

```java
String s = "Hello";
s = s + " World";
```

Internal behavior:

1. `"Hello"` object created
2. `"Hello World"` new object created
3. `s` now points to the new object

Frequent modifications → **many objects created → more memory usage**.

### When to use

Use `String` when:

* Value **does not change frequently**
* Used as **constant or configuration**
* Mostly **read operations**

Example:

```java
String name = "Saurabh";
```

---

# 2️⃣ StringBuffer

`StringBuffer` is **mutable**.

➡ The same object is modified instead of creating new objects.

### Example

```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World");
```

Result:

```
Hello World
```

### Key Feature

`StringBuffer` is **thread-safe**.

Methods are **synchronized**:

```java
public synchronized StringBuffer append(String str)
```

This allows **multiple threads** to safely modify it.

But synchronization makes it **slower than StringBuilder**.

### When to use

Use when:

* **Multiple threads** modify the string
* **Thread safety** is required

Examples:

* Multithreaded logging
* Shared string modification

---

# 3️⃣ StringBuilder

`StringBuilder` is also **mutable**, but **not thread-safe**.

➡ No synchronization → **better performance**.

### Example

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
```

Output:

```
Hello World
```

### When to use

Use when:

* **Single-threaded environment**
* **Frequent string modifications**
* **High performance required**

Examples:

* Building large text
* File processing
* JSON creation

---

# 🔑 Comparison

| Feature         | String              | StringBuffer       | StringBuilder                    |
| --------------- | ------------------- | ------------------ | -------------------------------- |
| Mutable         | ❌ No                | ✅ Yes              | ✅ Yes                            |
| Thread Safe     | ❌ No                | ✅ Yes              | ❌ No                             |
| Synchronization | ❌                   | ✅                  | ❌                                |
| Performance     | Slow (many objects) | Medium             | Fast                             |
| Use Case        | Fixed text          | Multithreaded apps | Single-threaded high performance |

---

# 🎯 Performance Example

❌ Inefficient

```java
String s = "";
for(int i = 0; i < 1000; i++){
    s = s + i;
}
```

Creates **1000+ objects**.

---

✅ Efficient

```java
StringBuilder sb = new StringBuilder();
for(int i = 0; i < 1000; i++){
    sb.append(i);
}
```

Only **one object modified** → much faster.

---

# ⭐ Easy Rule

```
String        → Immutable text
StringBuffer  → Mutable + Thread-safe
StringBuilder → Mutable + Fast (single-thread)
```
