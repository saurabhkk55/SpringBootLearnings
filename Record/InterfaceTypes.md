# 1. What is an Interface in Java?

An interface is a reference type in Java that defines a contract of methods that implementing classes must fulfill. It's a blueprint of behavior without implementation details (prior to Java 8).

## Key Characteristics:
- All methods are implicitly **public** and **abstract** (before Java 8)
- Can contain **constants** (`public static final` by default)
- Cannot be instantiated directly
- A class can implement multiple interfaces (multiple inheritance of type)
- Interfaces support polymorphism

---

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

- Contains exactly one abstract method
- Can have multiple default/static methods
- Used with lambda expressions and method references

Introduced mainly for:

* Lambda expressions
* Functional programming
* Streams API


```java
@FunctionalInterface  // Optional but recommended
interface Calculator {
    int operate(int a, int b);  // Single abstract method

    default void print() {
        System.out.println("Calculating...");
    }
}

// Usage with lambda
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.operate(5, 3));      // 8
System.out.println(multiply.operate(5, 3)); // 15
```

## Common Built-in Functional Interfaces

From Java Development Kit `java.util.function` package:

| Interface     | Purpose                    |
| ------------- | -------------------------- |
| Predicate<T>  | Returns boolean            |
| Function<T,R> | Takes input returns output |
| Consumer<T>   | Consumes input             |
| Supplier<T>   | Supplies data              |
| Runnable      | Thread task                |

---

# 3. Marker Interface

- No **methods** or **constants**
- Used to signal or mark a class for special behavior

```java
public interface Serializable { }  // Marks objects as serializable
public interface Cloneable { }     // Marks objects as cloneable

// Custom marker interface
interface Auditable { }  // Marks entities that need auditing

class Transaction implements Auditable {
    // This class can now be processed by auditing framework
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

# 6. Interface with Default Methods (Java 8+)

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
