# Java `equals()` and `hashCode()` — Complete Guide

In Java, every object automatically inherits two important methods from the Java `Object` class:

* `equals()`
* `hashCode()`

These methods play a very important role when working with collections like:

* `HashMap`
* `HashSet`
* `Hashtable`

Understanding them properly is essential for writing correct and efficient Java applications.

---

# 1. Default Behavior of `hashCode()`

Every object in Java has a memory address internally.

The default implementation of `hashCode()` in the `Object` class typically uses this memory-related information to generate a hash code.

Example:

```java
Object obj1 = new Object();
Object obj2 = new Object();

System.out.println(obj1.hashCode());
System.out.println(obj2.hashCode());
```

Since both objects are different in memory, they usually produce different hash codes.

---

# 2. Overriding `hashCode()`

In custom classes, we can override `hashCode()` and provide our own logic.

A good `hashCode()` implementation should:

* Generate unique hash codes as much as possible
* Reduce collisions
* Improve performance of hash-based collections

---

# 3. Hash Collision

Two different objects can legally have the same hash code.

This situation is called a **hash collision**.

Example:

```java
String str1 = "FB";
String str2 = "Ea";

System.out.println(str1.hashCode()); // 2236
System.out.println(str2.hashCode()); // 2236
```

Even though `"FB"` and `"Ea"` are different strings, they produce the same hash code.

This is completely valid.

---

# 4. Important Rules of `hashCode()` and `equals()`

## Rule 1

If two objects are equal according to `equals()`:

```java
a.equals(b) == true
```

then their hash codes **must also be equal**:

```java
a.hashCode() == b.hashCode()
```

---

## Rule 2

If two objects have the same hash code:

```java
a.hashCode() == b.hashCode()
```

it does **NOT** guarantee that objects are equal.

They may still be different objects.

---

# Simple Understanding

| Condition      | Result                          |
| -------------- | ------------------------------- |
| Equal objects  | Same hash code (mandatory)      |
| Same hash code | Objects may or may not be equal |

---

# 5. Default Behavior of `equals()`

The default `equals()` method from the `Object` class compares objects using memory reference comparison (`==`).

Example:

```java
Object obj1 = new Object();
Object obj2 = new Object();

System.out.println(obj1.equals(obj2));
```

Output:

```java
false
```

Because both references point to different memory locations.

---

# 6. Why Override `equals()`?

In real-world applications, we usually want to compare objects based on their data rather than memory addresses.

Example:

Two `Person` objects having the same `id` and `name` should be considered equal.

---

# 7. Example — Overriding `equals()`

```java
class Person {

    int id;
    String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {

        // Same memory reference
        if (this == obj)
            return true;

        // Null check and class check
        if (obj == null || getClass() != obj.getClass())
            return false;

        // Typecasting
        Person person = (Person) obj;

        // Property comparison
        return id == person.id &&
               name.equals(person.name);
    }
}
```

Usage:

```java
public class Main {

    public static void main(String[] args) {

        Person p1 = new Person(1, "Alice");
        Person p2 = new Person(1, "Alice");

        System.out.println(p1.equals(p2));
    }
}
```

Output:

```java
true
```

---

# 8. Rules for Overriding `equals()`

When overriding `equals()`, follow these contracts carefully.

---

## 1. Reflexive

Object must be equal to itself.

```java
a.equals(a) == true
```

---

## 2. Symmetric

If:

```java
a.equals(b) == true
```

then:

```java
b.equals(a) == true
```

must also be true.

---

## 3. Transitive

If:

```java
a.equals(b) == true
b.equals(c) == true
```

then:

```java
a.equals(c) == true
```

must also be true.

---

## 4. Consistent

Multiple calls should consistently return the same result unless object data changes.

---

## 5. Non-null

```java
a.equals(null)
```

must always return:

```java
false
```

---

# 9. Behavior in `HashSet`

Now let's understand all important scenarios.

---

# Scenario 1 — No Override of `equals()` and `hashCode()`

## Code

```java
import java.util.HashSet;

class Person {

    int id;
    String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

public class Main {

    public static void main(String[] args) {

        HashSet<Person> set = new HashSet<>();

        Person p1 = new Person(1, "Alice");
        Person p2 = new Person(1, "Alice");

        set.add(p1);
        set.add(p2);

        System.out.println(set.size());
    }
}
```

## Output

```java
2
```

## Explanation

* Default `equals()` compares memory locations
* Default `hashCode()` generates different hash codes
* Both objects are treated as different

---

# Scenario 2 — Override `equals()` but NOT `hashCode()`

## Code

```java
import java.util.HashSet;

class Person {

    int id;
    String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Person person = (Person) obj;

        return id == person.id &&
               name.equals(person.name);
    }
}

public class Main {

    public static void main(String[] args) {

        HashSet<Person> set = new HashSet<>();

        Person p1 = new Person(1, "Alice");
        Person p2 = new Person(1, "Alice");

        set.add(p1);
        set.add(p2);

        System.out.println(set.size());
    }
}
```

## Output

```java
2
```

## Explanation

Although `equals()` says objects are equal:

```java
p1.equals(p2) == true
```

their hash codes are still different because `hashCode()` was not overridden.

So both objects go into different buckets in `HashSet`.

---

# Scenario 3 — Override `hashCode()` but NOT `equals()`

## Code

```java
import java.util.HashSet;

class Person {

    int id;
    String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

public class Main {

    public static void main(String[] args) {

        HashSet<Person> set = new HashSet<>();

        Person p1 = new Person(1, "Alice");
        Person p2 = new Person(1, "Alice");

        set.add(p1);
        set.add(p2);

        System.out.println(set.size());
    }
}
```

## Output

```java
2
```

## Explanation

Both objects generate the same hash code.

However, default `equals()` still compares memory locations.

So objects are treated as different.

---

# Scenario 4 — Override BOTH `equals()` and `hashCode()`

## Code

```java
import java.util.HashSet;

class Person {

    int id;
    String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Person person = (Person) obj;

        return id == person.id &&
               name.equals(person.name);
    }

    @Override
    public int hashCode() {

        int result = Integer.hashCode(id);

        result = 31 * result + name.hashCode();

        return result;
    }
}

public class Main {

    public static void main(String[] args) {

        HashSet<Person> set = new HashSet<>();

        Person p1 = new Person(1, "Alice");
        Person p2 = new Person(1, "Alice");

        set.add(p1);
        set.add(p2);

        System.out.println(set.size());
    }
}
```

## Output

```java
1
```

## Explanation

* `hashCode()` puts objects in the same bucket
* `equals()` confirms they are equal
* Duplicate object is not added

This is the correct implementation.

---

# 10. Internal Working of `HashSet`

`HashSet` internally works like this:

## Step 1 — Generate Hash Code

```java
hashCode()
```

determines the bucket.

---

## Step 2 — Compare Using `equals()`

If another object already exists in the same bucket:

```java
equals()
```

is used to check duplication.

---

# Visual Flow

```text
hashCode() → Bucket Selection
equals()  → Duplicate Check
```

---

# 11. Best Practice

Whenever you override `equals()`:

✅ Always override `hashCode()` as well.

Otherwise collections like:

* `HashSet`
* `HashMap`
* `Hashtable`

may behave incorrectly.

---

# 12. Recommended Modern Implementation

Java provides `Objects.hash()` for simpler implementation.

Example:

```java
import java.util.Objects;

@Override
public int hashCode() {
    return Objects.hash(id, name);
}
```

---

# 13. Complete Summary Table

| Scenario                 | equals() | hashCode() | Behavior                            | Output |
| ------------------------ | -------- | ---------- | ----------------------------------- | ------ |
| No Override              | Default  | Default    | Compared by memory address          | 2      |
| Override equals() only   | Custom   | Default    | Equal objects but different buckets | 2      |
| Override hashCode() only | Default  | Custom     | Same bucket but memory comparison   | 2      |
| Override both            | Custom   | Custom     | Proper duplicate detection          | 1      |

---

# 14. Final Takeaway

## `equals()`

Used to compare object content.

---

## `hashCode()`

Used to determine object storage bucket in hash-based collections.

---

## Golden Rule

If two objects are equal:

```java
equals() == true
```

then:

```java
hashCode() must also be same
```

Always override both methods together for consistent and correct behavior in Java collections.
