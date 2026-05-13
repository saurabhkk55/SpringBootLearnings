# Static Variable vs Instance Variable Memory Storage in Java

## Instance Variable

Instance variables belong to:

```text
Object (instance)
```

Each object gets its own separate copy.

Example:

```java
class Employee {
    int salary;
}
```

Memory:

```text
emp1.salary → separate
emp2.salary → separate
```

Stored in:

```text
Heap memory (inside object)
```

---

# Static Variable

Static variables belong to:

```text
Class itself
```

Only one copy exists for entire class.

Example:

```java
class Employee {
    static String company;
}
```

Memory:

```text
Employee.company
```

shared across all objects.

Stored in:

```text id="’wini0s"
Method Area / Metaspace (class-level memory)
```

NOT inside object.

---

# Example

```java
class Employee {

    static String company = "Accenture";

    int salary;
}
```

Objects:

```java
Employee e1 = new Employee();
Employee e2 = new Employee();
```

Memory:

```text
Heap:
e1.salary
e2.salary

Metaspace:
Employee.company
```

Only one `company` variable exists.

---

# Key Difference

| Feature      | Instance Variable | Static Variable         |
| ------------ | ----------------- | ----------------------- |
| Belongs To   | Object            | Class                   |
| Copies       | One per object    | Only one                |
| Stored In    | Heap memory       | Method Area / Metaspace |
| Created When | Object creation   | Class loading           |
| Accessed By  | Object reference  | Class name preferred    |
| Shared?      | No                | Yes                     |

---

# Lifecycle

## Instance Variable

Created when:

```text
object is created
```

Destroyed when:

* object becomes unreachable
* GC removes object

---

## Static Variable

Created when:

```text
class loads into JVM
```

Destroyed when:

* class unloaded
* JVM stops

---

# Real-Life Analogy

## Instance Variable

Each employee has own:

```text
salary
```

---

## Static Variable

All employees share same:

```text
company name
```
