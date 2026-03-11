Understanding **String Constant Pool**, `==`, and `.equals()` is a **very common Java interview trap** in **Java**. I'll explain it simply and then give **tricky questions**.

---

# 1️⃣ What is String Constant Pool (SCP)?

The **String Constant Pool** is a **special memory area inside the Heap** where Java stores **string literals**.

Purpose:

* **Avoid duplicate objects**
* **Save memory**

### Example

```java
String a = "Hello";
String b = "Hello";
```

Memory:

```
String Pool
------------
"Hello"
```

Both variables point to the **same object**.

```
a ----|
      |----> "Hello"
b ----|
```

---

# 2️⃣ `==` vs `.equals()`

### `==`

Checks **reference (memory address)**.

```
Do both variables point to the same object?
```

---

### `.equals()`

Checks **content (value)**.

```
Do both strings contain the same characters?
```

---

# 3️⃣ Tricky Example 1 (Most famous)

```java
String a = "Hello";
String b = "Hello";

System.out.println(a == b);
System.out.println(a.equals(b));
```

Output

```
true
true
```

Why?

Because both point to **same SCP object**.

---

# 4️⃣ Tricky Example 2

```java
String a = new String("Hello");
String b = new String("Hello");

System.out.println(a == b);
System.out.println(a.equals(b));
```

Output

```
false
true
```

Memory:

```
Heap
-----
a -> "Hello"
b -> "Hello"

String Pool
------------
"Hello"
```

`new` **forces a new object in heap**.

---

# 5️⃣ Tricky Example 3 (Interview Favorite)

```java
String a = "Hello";
String b = new String("Hello");

System.out.println(a == b);
System.out.println(a.equals(b));
```

Output

```
false
true
```

Because:

```
a -> String Pool
b -> Heap
```

Different references.

---

# 6️⃣ Tricky Example 4 (VERY tricky)

```java
String a = "Hello";
String b = "Hel" + "lo";

System.out.println(a == b);
```

Output

```
true
```

Why?

Java compiler does **compile-time optimization**.

It converts:

```
"Hel" + "lo"
```

into

```
"Hello"
```

So both go to **String Pool**.

---

# 7️⃣ Tricky Example 5

```java
String a = "Hello";
String part = "Hel";

String b = part + "lo";

System.out.println(a == b);
System.out.println(a.equals(b));
```

Output

```
false
true
```

Why?

Because this happens at **runtime**.

Runtime concatenation creates a **new object in heap**.

---

# 8️⃣ Tricky Example 6 (Super Interview Trick)

```java
String a = "Hello";
String b = new String("Hello").intern();

System.out.println(a == b);
```

Output

```
true
```

### `intern()`

Moves/returns the **string from the String Pool**.

So:

```
b -> "Hello" (SCP)
```

Now both references are same.

---

# 9️⃣ Tricky Example 7

```java
String a = "Hello";
String b = "Hello ";
String c = b.trim();

System.out.println(a == c);
```

Output

```
false
```

Why?

`trim()` creates **new object**.

But:

```
a.equals(c) → true
```

---

# 🔟 Very Tricky (Senior Java Interview)

```java
String a = "Java";
String b = "Ja";
String c = "va";

String d = b + c;

System.out.println(a == d);
```

Output

```
false
```

Because:

```
b + c
```

happens **at runtime** → new heap object.

---

# 🔑 Simple Rule (Remember for Interviews)

| Case                       | Result         |
| -------------------------- | -------------- |
| Two literals               | `== true`      |
| Using `new`                | `== false`     |
| Runtime concatenation      | `== false`     |
| Compile-time concatenation | `== true`      |
| `.equals()`                | compares value |

---

# 🎯 One Last Trick (Many developers fail)

```java
String a = "Java";
String b = "Java";

System.out.println(a == b); // ?
```

Output

```
true
```

But:

```java
String a = new String("Java");
String b = new String("Java");

System.out.println(a == b); // ?
```

Output

```
false
```

---

✅ **Best practice**

Always use:

```java
a.equals(b)
```

Not:

```java
a == b
```

for string comparison.
