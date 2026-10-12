# Java Memory Areas (Correct)

## 1. Heap Memory

* Stores **all objects (instances)** and **arrays**.
* Also stores the objects referenced by instance variables.
* Shared among all threads.
* Managed by the Garbage Collector (GC).

Example

```java
Student s = new Student();
```

Memory

```
Heap
-----
Student Object
name = "John"
age = 25
```

---

## 2. Method Area

The Method Area stores **class-level information** that is shared by all threads.

It contains:

* Class metadata

    * Class name
    * Parent class
    * Interfaces
    * Modifiers
* Runtime Constant Pool
* Method bytecode
* Method information
* Field information
* Static variables
* JIT compiler related information (implementation dependent)

**It does NOT store objects.**

Example

```java
class Employee {
    static int companyId = 10;
    void work() {}
}
```

Method Area stores

```
Employee metadata
-----------------
Class Name
Parent Class
Methods
Bytecode
Static variable companyId
Constant Pool
```

---

## 3. Stack Memory

Each thread gets **its own Stack**.

Whenever a method is called, a **Stack Frame** is created.

Each Stack Frame contains

* Local variables
* Method parameters
* Primitive values
* References to Heap objects
* Operand Stack (used during execution)
* Return address

Example

```java
public void test() {
    int age = 25;
    Student s = new Student();
}
```

Stack

```
Stack Frame
------------
age = 25

s ---------->
              Heap Object
```

Notice

The **reference** (`s`) is on the Stack.

The **actual object** is on the Heap.

---

## 4. Program Counter (PC) Register

Every thread has its own **PC Register**.

It stores the address of the **next JVM bytecode instruction** to execute.

Its purpose is to allow the thread to resume execution correctly after:

* method calls
* loops
* exceptions
* context switching

Example

```java
int a = 5;
int b = 10;
int c = a + b;
```

While executing,

```
Instruction 1
↓

Instruction 2
↓

Instruction 3
```

The PC Register keeps track of the current/next instruction.

Without it, after thread switching, the JVM wouldn't know where to continue execution.

---

# Overall JVM Memory

```
                 JVM Memory

         +----------------------+
         |     Method Area      |
         |----------------------|
         | Class Metadata       |
         | Static Variables     |
         | Bytecode             |
         | Constant Pool        |
         +----------------------+

         +----------------------+
         |        Heap          |
         |----------------------|
         | Objects              |
         | Arrays               |
         +----------------------+

Thread 1
---------
PC Register

Stack
------
Frame 1
Frame 2

Thread 2
---------
PC Register

Stack
------
Frame 1
Frame 2
```

---

# Java 7 and Earlier (PermGen)

Before Java 8, the Method Area was implemented using a special memory region called **Permanent Generation (PermGen)**.

```
JVM Memory

Heap
------
Young Generation

Old Generation

Permanent Generation (PermGen)
```

PermGen stored

* Class metadata
* Static variables
* String pool (until Java 6; moved to the heap in Java 7)
* Runtime Constant Pool
* Method bytecode

### Problems with PermGen

### 1. Fixed Size

PermGen had a fixed maximum size.

Example

```
-XX:PermSize=64m
-XX:MaxPermSize=128m
```

If many classes were loaded (for example, in application servers like Tomcat), PermGen could become full.

Result

```
java.lang.OutOfMemoryError:
PermGen space
```

---

### 2. Class Loader Memory Leaks

Application servers often reload applications without restarting the JVM.

```
Deploy App
↓

Load Classes

↓

Undeploy

↓

Deploy Again
```

If old classes were not unloaded because of lingering references, PermGen usage kept growing until it was exhausted.

---

### 3. Difficult Tuning

Developers had to manually estimate PermGen size.

Too small → frequent `OutOfMemoryError`.

Too large → wasted memory.

---

# Java 8 and Later (Metaspace)

Java 8 removed PermGen and replaced it with **Metaspace**.

Now the Method Area is implemented using Metaspace.

```
Heap
------
Young

Old

Metaspace
----------
(outside Heap)
```

## What does Metaspace store?

Same kind of data as before:

* Class metadata
* Method metadata
* Bytecode
* Runtime Constant Pool
* Field metadata

**Static variables are associated with the class, but since Java 8 they are stored on the Heap as part of the `Class` object rather than in Metaspace.**

---

# Biggest Difference

## Before Java 8

```
Heap
↓

PermGen (inside JVM-managed memory)

Fixed Size
```

## After Java 8

```
Heap

Metaspace
↓

Uses native (OS) memory

Can grow automatically
```

Instead of reserving a fixed block like PermGen, Metaspace allocates memory from the operating system as needed (up to limits you configure or until native memory is exhausted).

---

# Can Metaspace also become full?

Yes.

If unlimited,

```
Native Memory
↓

Keeps Growing
```

Eventually,

```
java.lang.OutOfMemoryError:
Metaspace
```

You can still limit it.

Example

```
-XX:MaxMetaspaceSize=256m
```

---

# Comparison

| Feature                      | Java 7 (PermGen)                  | Java 8+ (Metaspace)                         |
| ---------------------------- | --------------------------------- | ------------------------------------------- |
| Stores class metadata        | ✅                                 | ✅                                           |
| Stores method metadata       | ✅                                 | ✅                                           |
| Stores runtime constant pool | ✅                                 | ✅                                           |
| Stores bytecode              | ✅                                 | ✅                                           |
| Static variables             | PermGen                           | Heap (as part of the `Class` object)        |
| Location                     | JVM-managed memory                | Native (OS) memory                          |
| Default size                 | Fixed                             | Grows automatically                         |
| Tunable                      | `-XX:PermSize`, `-XX:MaxPermSize` | `-XX:MetaspaceSize`, `-XX:MaxMetaspaceSize` |
| Common error                 | `OutOfMemoryError: PermGen space` | `OutOfMemoryError: Metaspace`               |
| Available in Java 8+         | ❌ Removed                         | ✅ Current implementation                    |

### Summary

* **Heap** stores objects and arrays.
* **Method Area** stores shared class-level information such as metadata, bytecode, runtime constant pool, and static members (though the storage location of static fields changed in Java 8).
* **Stack** is per-thread and stores stack frames containing local variables, parameters, operand stack, return information, and references to heap objects.
* **PC Register** is per-thread and tracks the next JVM instruction to execute.
* **Before Java 8**, the Method Area was implemented using **PermGen**, a fixed-size JVM-managed memory region that was prone to `OutOfMemoryError: PermGen space`.
* **From Java 8 onward**, PermGen was replaced by **Metaspace**, which stores class metadata in native memory and can grow dynamically, reducing manual tuning while still allowing limits via JVM options.
