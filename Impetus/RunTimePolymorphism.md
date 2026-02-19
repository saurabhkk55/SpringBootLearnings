### ✅ Output

```
Hello m1
Hello m2 from child
```

---

### 🔎 Step-by-step explanation (simple wording)

#### 1️⃣ Object creation

```java
Parent c = new Child();
```

* **Reference type → Parent**
* **Actual object → Child**

This is called **runtime polymorphism**.

---

#### 2️⃣ Method call

```java
c.m1();
```

* `m1()` is defined in **Parent**
* Child does **not override m1()**
  ➡ So **Parent.m1()** runs.

Output:

```
Hello m1
```

---

#### 3️⃣ Inside `m1()`

```java
this.m2();
```

Important rule:

> `this` refers to **actual object type**, not reference type.

Actual object = **Child**

So Java checks:

> Does Child override m2()?

Yes ✔

Therefore:

```
Child.m2() runs
```

Output:

```
Hello m2 from child
```

---

### 🎯 Core Concept Tested Here

This question tests:

> **Dynamic Method Dispatch (Runtime Method Binding)**

Method selection depends on:

* actual object type
* not reference type

---

### 📌 Rule to Remember

| Method Type         | Resolution Time |
| ------------------- | --------------- |
| Static              | Compile time    |
| Instance overridden | Runtime         |

---

### 🧠 Interview One-liner Answer

> Parent reference pointing to Child object calls Parent.m1(), but inside it `this.m2()` invokes Child’s overridden m2() due to runtime polymorphism.
