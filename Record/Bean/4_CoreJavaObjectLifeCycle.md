# 📘 Java Object Lifecycle – Complete Documentation (Core Java)

This document explains the **lifecycle of a Java object in Core Java**, **without Spring or Spring Boot**. It focuses purely on how the **JVM creates, uses, and destroys objects**.

This is **fundamental Java knowledge** and often tested in **interviews**, especially to check whether a developer clearly distinguishes **Java lifecycle vs Spring Bean lifecycle**.

---

## 🔁 High-Level Java Object Lifecycle

```
Class Loaded
    ↓
Object Created (new keyword)
    ↓
Memory Allocation
    ↓
Constructor Execution
    ↓
Object Ready to Use
    ↓
Object Becomes Unreachable
    ↓
Garbage Collection
    ↓
Memory Reclaimed
```

---

# 🧠 1️⃣ Class Loading in Java — Deep & Clear Explanation

Before **any object** is created, **class loading** hota hai.
Ye step **sirf JVM handle karta hai**, developer ka direct control nahi hota.

---

## 🔹 1.1️⃣ `.class` File Kaha Se Aati Hai?

### Source Code ➝ Bytecode

Developer likhta hai:

```java
Person.java
```

### Compile Command

```bash
javac Person.java
```

📌 Ye command:

* Java source code ko
* **Platform-independent bytecode** me convert karta hai

📌 Output:

```text
Person.class
```

🧠 **Important Insight**

> `.class` file JVM ka **input** hoti hai, not output.

---

## 🔹 1.2️⃣ Class Loading Kya Hota Hai?

Class loading ka matlab:

> JVM `.class` file ko **memory me laata hai** taaki use execute kiya ja sake.

Jab tak class load nahi hoti:
❌ Object create nahi ho sakta
❌ Static members access nahi ho sakte

---

## 🔹 1.3️⃣ JVM Class Loading – 3 Internal Phases

JVM **ClassLoader subsystem** ke through ye 3 steps follow karta hai:

---

## 🔹 Phase 1: **Loading**

### What happens here?

* JVM `.class` file locate karta hai
* Bytecode read karta hai
* Class ka **binary representation** banata hai

📌 Ye data store hota hai:

> **Method Area / Metaspace**

🧠 **Who loads the class?**

* Bootstrap ClassLoader (`java.lang.*`)
* Extension ClassLoader
* Application ClassLoader

---

## 🔹 Phase 2: **Linking**

Linking ke 3 sub-steps hote hain 👇

---

### 🔸 a) Verification

📌 JVM check karta hai:

* Bytecode valid hai ya nahi
* Security violation to nahi
* Stack overflow / illegal access nahi

🧠 **Insight**

> Agar verification fail → `VerifyError`

---

### 🔸 b) Preparation ⭐ (VERY IMPORTANT)

📌 Yahin pe:

* **Static variables ke liye memory allocate hoti hai**
* **Default values set hoti hain**

Example:

```java
static int x = 10;
```

👉 Preparation phase ke baad:

```text
x = 0   (default)
```

🧠 **Key Insight**

> Actual assignment (10) abhi nahi hua hai

---

### 🔸 c) Resolution

📌 Symbolic references ko actual memory references se replace kiya jata hai

Example:

```java
System.out.println();
```

* `System`
* `PrintStream`
* `println`

➡ JVM in sabko actual memory addresses se link karta hai

---

## 🔹 Phase 3: **Initialization**

📌 Ab JVM:

* Static variables ko **actual values assign karta hai**
* Static blocks execute karta hai

Example:

```java
static int x = 10;

static {
    System.out.println("Static block");
}
```

👉 Initialization phase me:

```text
x = 10
static block executed
```

🧠 **Golden Rule**

> Static variables + static blocks **top-to-bottom order** me execute hote hain

---

## 🔹 1.4️⃣ Static Members Kaha Store Hote Hain?

### 🔸 Static Variables

Stored in:

> **Method Area / Metaspace**

❌ Heap me nahi
❌ Stack me nahi

📌 Sirf **ek copy** hoti hai per class

---

### 🔸 Static Methods

Also stored in:

> **Method Area / Metaspace**

🧠 Static method ka:

* `this` reference nahi hota
* Object create karne ki zarurat nahi

---

### 🔸 Instance Variables

Stored in:

> **Heap** (inside object)

---

### 🔸 Local Variables

Stored in:

> **Stack** (method call ke time)

---

## 🔹 1.5️⃣ Example – Complete Flow

```java
class Person {

    static int x = 10;

    static {
        System.out.println("Static block executed");
    }

    Person() {
        System.out.println("Constructor called");
    }
}
```

### Execution:

```java
public static void main(String[] args) {
    Person p1 = new Person();
    Person p2 = new Person();
}
```

### Output:

```
Static block executed
Constructor called
Constructor called
```

🧠 **Why static block only once?**

> Because **class is loaded only once**, but objects can be many

---

## 🔹 1.6️⃣ When Does Class Loading Happen?

Class loading happens when:

* `new Person()` is used
* Static variable accessed
* Static method called
* `Class.forName("Person")` used

---

## 🔹 1.7️⃣ Interview-Ready streams.One-Liners 🔥

✔ `.class` file is generated using `javac`
✔ JVM loads class before object creation
✔ Static variables get memory in **preparation phase**
✔ Static values assigned in **initialization phase**
✔ Static members stored in **Method Area / Metaspace**
✔ Class loading happens **only once**

---

## 🔹 1.8️⃣ Java vs Spring (Quick Clarity)

| Topic            | Java  | Spring    |
| ---------------- | ----- | --------- |
| Class loading    | JVM   | JVM       |
| Object creation  | `new` | Container |
| Static lifecycle | JVM   | JVM       |

Spring **does not control** class loading.

---

## 🎯 Final Mental Model

```
javac → .class
        ↓
ClassLoader
        ↓
Loading → Linking → Initialization
        ↓
Static members ready
        ↓
Objects can be created
```

🧠 **Key Insight**

> Class loading happens **only once**, no matter how many objects are created.

---

## 2️⃣ Object Creation (`new` keyword)

### What happens

```java
Person p = new Person();
```

* JVM allocates memory in **Heap**
* Reference variable `p` is created in **Stack**

🧠 **Important**

> Object creation does NOT mean constructor is the first step — memory allocation happens before constructor execution.

---

## 3️⃣ Memory Allocation (Default Values Are Set Here)

### What happens

* JVM allocates memory for the object in **Heap**
* **All instance variables are automatically assigned default values**

### Default values in Java

| Data Type                 | Default Value |
| ------------------------- | ------------- |
| byte / short / int / long | `0`           |
| float / double            | `0.0`         |
| char                      | `�`           |
| boolean                   | `false`       |
| Object reference          | `null`        |

### Important Rules

* Default values are set **before constructor execution**
* This applies **only to instance variables**
* Local variables do **NOT** get default values

```java
class Person {
    int age;        // 0
    String name;    // null
}
```

🧠 **Key Insight**

> Memory allocation + default value assignment happens **before** constructor runs.

---

## 4️⃣ Constructor Execution (User-Defined Values Are Set Here)

### What happens

* Constructor is invoked **after default values are assigned**
* Developer-written initialization logic executes
* **User-defined values override default values**

```java
class Person {
    int age;
    String name;

    Person() {
        age = 25;           // overrides default 0
        name = "Saurabh";  // overrides default null
    }
}
```

### Order Inside Constructor

1. `super()` call (implicit or explicit)
2. Instance initializer blocks (if any)
3. Constructor body

🧠 **Key Insight**

> Constructor is the **first place where developer controls object state**.

---

## 5️⃣ Object Ready to Use

At this stage:

* Object memory is allocated
* Default values were set by JVM
* User-defined values were set by constructor
* Object is fully initialized

```java
Person p = new Person();
System.out.println(p.age);   // 25
System.out.println(p.name);  // Saurabh
```

🧠 **Lifecycle Insight**

> JVM sets defaults → Developer sets actual values → Object becomes usable

---

## 6️⃣ Object Becomes Unreachable

An object becomes **eligible for garbage collection** when **no live reference** points to it.

### Common scenarios

```java
p = null;                // reference removed
p = new Person();        // old object lost
method();                // local variable out of scope
```

🧠 **Important**

> Unreachable ≠ destroyed immediately

---

## 7️⃣ Garbage Collection (GC)

### What GC does

* Automatically removes unreachable objects
* Reclaims heap memory

### Key points

* GC is handled by JVM
* Developer **cannot force GC**
* `System.gc()` is only a request

🧠 **Insight**

> Java follows **automatic memory management** — unlike C/C++.

---

## 8️⃣ Finalization (Deprecated Concept)

### `finalize()` method

```java
@Override
protected void finalize() throws Throwable {
    System.out.println("Finalize called");
}
```

### Important facts

* Called by GC **before object removal** (not guaranteed)
* Unpredictable
* Deprecated since **Java 9**

❌ **Do NOT use in modern Java**

---

## 9️⃣ Memory Reclamation

After GC:

* Heap memory is reclaimed
* Object is permanently removed

🧠 **Final state**

> Object no longer exists in JVM

---

## 🧪 Complete Example with Output

```java
class Demo {

    static {
        System.out.println("1. Class loaded");
    }

    Demo() {
        System.out.println("2. Constructor executed");
    }

    public static void main(String[] args) {
        Demo d = new Demo();
        d = null;
        System.gc();
    }
}
```

### Possible Output

```
1. Class loaded
2. Constructor executed
```

⚠️ No guarantee that GC or finalize will run

---

## 🔥 Java Object Lifecycle vs Spring Bean Lifecycle

| Java Object Lifecycle  | Spring Bean Lifecycle       |
| ---------------------- | --------------------------- |
| Managed by JVM         | Managed by Spring Container |
| `new` keyword          | Container creates bean      |
| GC handles destruction | Callbacks & hooks           |
| No init/destroy hooks  | Multiple lifecycle hooks    |

---

## 🎯 Interview-Ready Summary (streams.One Page)

* Class loads once
* Object created using `new`
* Memory allocated before constructor
* Constructor initializes object
* Object lives while reachable
* Unreachable objects become GC candidates
* GC reclaims memory automatically
* `finalize()` is deprecated

---

## ✅ Final Takeaway

> **Java Object Lifecycle is JVM-driven, simple, and automatic.**
> **Spring Bean Lifecycle is container-driven, complex, and configurable.**

Understanding this difference is **critical for strong Java fundamentals**.
