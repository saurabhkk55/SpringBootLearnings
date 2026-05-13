# 1. Normal Interface

A regular interface containing abstract methods (and optionally default/static methods).

```java
interface Vehicle {
    void start();
    void stop();
}
```

```java
class Car implements Vehicle {

    @Override
    public void start() {
        System.out.println("Car started");
    }

    @Override
    public void stop() {
        System.out.println("Car stopped");
    }
}
```

## Usage

* Achieve abstraction
* Loose coupling
* Multiple inheritance
* Define common behavior

## Real-life Example

Different payment methods implementing same contract.

```java
interface Payment {
    void pay(double amount);
}
```

---

# 2. Functional Interface (SAM Interface)

An interface having **exactly one abstract method**.

Introduced mainly for:

* Lambda expressions
* Functional programming
* Streams API

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}
```

Using Lambda:

```java
Calculator c = (a, b) -> a + b;

System.out.println(c.add(2, 3));
```

## Usage

* Lambda expressions
* Stream operations
* Callbacks
* Event handling

## Common Built-in Functional Interfaces

From Java Development Kit `java.util.function` package:

| Interface     | Purpose                    |
| ------------- | -------------------------- |
| Predicate<T>  | Returns boolean            |
| Function<T,R> | Takes input returns output |
| Consumer<T>   | Consumes input             |
| Supplier<T>   | Supplies data              |
| Runnable      | Thread task                |

Example:

```java
Predicate<Integer> isEven = n -> n % 2 == 0;
```

---

# 3. Marker Interface

An interface with **no methods**.

```java
interface Serializable {
}
```

## Usage

Used to provide metadata/information to JVM or framework.

## Built-in Examples

| Marker Interface | Purpose                  |
| ---------------- | ------------------------ |
| Serializable     | Object can be serialized |
| Cloneable        | Object cloning allowed   |
| Remote           | RMI support              |

Example:

```java
class Employee implements Serializable {
}
```

JVM/framework checks:

> "Does this class implement Serializable?"

---

# 4. Nested Interface

An interface declared inside another class/interface.

```java
class Outer {

    interface Inner {
        void show();
    }
}
```

Implementation:

```java
class Demo implements Outer.Inner {

    @Override
    public void show() {
        System.out.println("Nested Interface");
    }
}
```

## Usage

* Group related interfaces
* Better encapsulation
* Hide implementation details

---

# 5. Sealed Interface (Java 17+)

Restricts which classes/interfaces can implement it.

```java
sealed interface Shape permits Circle, Rectangle {
}
```

```java
final class Circle implements Shape {
}
```

```java
final class Rectangle implements Shape {
}
```

## Usage

* Controlled inheritance
* Better security/design
* Useful in domain modeling

Example:

* PaymentStatus
* API response hierarchy
* AST parsers

---

# 6. Empty Interface vs Functional Interface

| Feature          | Marker Interface | Functional Interface |
| ---------------- | ---------------- | -------------------- |
| Abstract methods | 0                | Exactly 1            |
| Purpose          | Metadata         | Lambda behavior      |
| Example          | Serializable     | Runnable             |

---

# 7. Interface with Default Methods (Java 8+)

Java 8 introduced `default` methods.

```java
interface Vehicle {

    void start();

    default void fuelType() {
        System.out.println("Petrol");
    }
}
```

## Usage

* Backward compatibility
* Add methods without breaking old implementations

---

# 8. Interface with Static Methods

```java
interface MathUtil {

    static int square(int n) {
        return n * n;
    }
}
```

Usage:

```java
System.out.println(MathUtil.square(5));
```

## Usage

Utility/helper methods inside interfaces.

---

# 9. Private Methods in Interface (Java 9+)

Used internally inside default methods.

```java
interface Test {

    default void show() {
        print();
    }

    private void print() {
        System.out.println("Private Method");
    }
}
```

## Usage

* Code reuse inside interface
* Avoid duplication

---

# Quick Summary

| Type                     | Main Purpose           |
| ------------------------ | ---------------------- |
| Normal Interface         | Contract/abstraction   |
| Functional Interface     | Lambda expressions     |
| Marker Interface         | Metadata/tagging       |
| Nested Interface         | Grouping               |
| Sealed Interface         | Restricted hierarchy   |
| Default Method Interface | Backward compatibility |
| Static Method Interface  | Utility methods        |
| Private Method Interface | Internal reuse         |

---

# Most Important Interview Point

> Interface = "What to do"
> Abstract Class = "What + partial How"
