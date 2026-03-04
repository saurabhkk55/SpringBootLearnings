# ✅ Java Records – Complete Interview Notes (Beginner → Advanced)

---

## 1️⃣ What is a Record in Java?

### 📌 Theory

* A **record** is a special type of class used to **store immutable data**.
* Introduced to **remove boilerplate code** from DTO / data-carrier classes.
* Records focus on **data**, not **behavior**.

### 📌 Java Version

* Java 14 → Preview
* Java 15 → Preview
* **Java 16 → Stable release**

### 📌 Interview Line

> Java Record is a concise, immutable data carrier introduced in Java 16.

---

## 2️⃣ Why Records Were Introduced?

### 📌 Problem with Traditional Classes

* Too many lines for:

    * Constructor
    * Getters
    * equals()
    * hashCode()
    * toString()

### 📌 Solution

* Record generates all of them automatically.

---

## 3️⃣ Basic Record Syntax

```java
public record User(int id, String name) {}
```

### 📌 What Java Generates Automatically

* `private final` fields
* Canonical constructor
* Public accessor methods
* `equals()`, `hashCode()`, `toString()`

---

## 4️⃣ Working Example with Output

### Record Definition

```java
public record Employee(int id, String name, double salary) {}
```

### Usage

```java
public class TestRecord {
    public static void main(String[] args) {

        Employee emp = new Employee(101, "Saurabh", 75000);

        // Accessors (not getters)
        System.out.println(emp.id());
        System.out.println(emp.name());
        System.out.println(emp.salary());

        // toString() auto-generated
        System.out.println(emp);
    }
}
```

### Output

```
101
Saurabh
75000.0
Employee[id=101, name=Saurabh, salary=75000.0]
```

---

## 5️⃣ Can Records Have Static Fields?

### 📌 Theory

* ✅ Yes, records **can have static fields and methods**
* Static members belong to the class, not to instances

### Code Example

```java
public record CompanyEmployee(int id, String name) {

    // Static field
    public static String COMPANY_NAME = "OpenAI";

    // Static method
    public static void printCompanyName() {
        System.out.println(COMPANY_NAME);
    }
}
```

### Usage

```java
public class TestStatic {
    public static void main(String[] args) {
        System.out.println(CompanyEmployee.COMPANY_NAME);
        CompanyEmployee.printCompanyName();
    }
}
```

### Output

```
OpenAI
OpenAI
```

### 📌 Interview Line

> Records support static fields and static methods.

---

## 6️⃣ Are All Record Fields Public Final?

### ❌ Common Misconception

> Record fields are public final ❌

### ✅ Correct Theory

* Record components are:

    * `private`
    * `final`
* Public **accessor methods** are generated

### Internal Representation

```java
public record User(int id, String name) {}
```

Internally behaves like:

```java
private final int id;
private final String name;

public int id() { return id; }
public String name() { return name; }
```

### Code Proof

```java
User user = new User(1, "Saurabh");

// ❌ Compilation error
// System.out.println(user.id);

// ✅ Correct
System.out.println(user.id());
```

### 📌 Interview Line

> Record fields are private final, accessed through public accessor methods.

---

## 7️⃣ Immutability in Records (Shallow Immutability)

### 📌 Theory

* Records are **shallow immutable**
* Field references cannot change
* Mutable objects inside can still change

### Example

```java
import java.util.List;

public record Person(String name, List<String> skills) {}
```

### Usage

```java
Person p = new Person("Saurabh", List.of("Java"));

// ❌ Reference can't change
// p.name = "Amit";

// ⚠️ But internal list can change
p.skills().add("Spring"); // Runtime error (if List.of)
```

📌 If mutable list is used, modification is possible.

### 📌 Interview Line

> Records provide shallow immutability, not deep immutability.

---

## 8️⃣ Canonical Constructor (Very Important)

### 📌 Theory

* A **canonical constructor**:

    * Has same parameters
    * Same order
    * Same types as record components
* Auto-generated if not written

### Example Record

```java
public record Product(int id, double price) {}
```

### Auto-Generated Constructor

```java
public Product(int id, double price) {
    this.id = id;
    this.price = price;
}
```

---

## 9️⃣ Explicit Canonical Constructor

### 📌 Use Case

* Add validation logic

### Code

```java
public record Product(int id, double price) {

    public Product(int id, double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.id = id;
        this.price = price;
    }
}
```

### Output

```
Exception in thread "main" java.lang.IllegalArgumentException: Price must be positive
```

---

## 🔟 Compact Canonical Constructor (Special streams.One ⭐)

### 📌 Theory

* Shorter version of canonical constructor
* No parameters
* No field assignments
* Java assigns automatically

### Code

```java
public record Product(int id, double price) {

    public Product {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}
```

### 📌 Interview Line

> Compact canonical constructor is preferred for validation logic.

---

## 1️⃣1️⃣ Non-Canonical Constructor

### 📌 Rule

* Must call canonical constructor

### Code

```java
public record User(int id, String name) {

    public User(int id) {
        this(id, "Unknown");
    }
}
```

---

## 1️⃣2️⃣ Methods Inside Records

### 📌 Theory

* Records can have:

    * Instance methods
    * Static methods
* Business logic should be minimal

### Code

```java
public record Rectangle(int length, int width) {

    public int area() {
        return length * width;
    }
}
```

### Output

```
50
```

---

## 1️⃣3️⃣ What Records Cannot Do ❌

### 📌 Restrictions

* Cannot extend another class
* Cannot have mutable fields
* Cannot have setters
* Cannot be abstract
* Are implicitly final

```java
public record A(int x) extends B {} // ❌
```

---

## 1️⃣4️⃣ Records vs Normal Class

| Feature     | Class          | Record       |
| ----------- | -------------- | ------------ |
| Boilerplate | High           | Very low     |
| Mutability  | Mutable        | Immutable    |
| Inheritance | Yes            | No           |
| Use case    | Business logic | Data carrier |

---

## 1️⃣5️⃣ Records vs Lombok @Data

| Feature      | Record  | Lombok   |
| ------------ | ------- | -------- |
| Java feature | Yes     | No       |
| Dependency   | None    | Lombok   |
| Immutability | Default | Optional |

📌 Prefer records when possible.

---

## 1️⃣6️⃣ Records in Spring Boot

### Request DTO

```java
public record LoginRequest(String username, String password) {}
```

### Controller

```java
@PostMapping("/login")
public String login(@RequestBody LoginRequest request) {
    return request.username();
}
```

---

## 1️⃣7️⃣ Records & JPA (Interview Trap ⚠️)

❌ Avoid using records as entities
✅ Use them as:

* DTOs
* Projections

---

## 1️⃣8️⃣ Interview Quick Answers

| Question               | Answer            |
| ---------------------- | ----------------- |
| Java version?          | Java 16           |
| Immutable?             | Shallow immutable |
| Static fields allowed? | Yes               |
| Fields public?         | No, private final |
| Extend class?          | No                |
| Use in Spring Boot?    | Yes               |

---

## ⭐ Final streams.One-Line Summary

> Java Records are final, immutable, concise data carriers introduced in Java 16 that eliminate boilerplate code and are ideal for DTOs and value objects.

---

If you want next:

* 🔥 **Record + MapStruct deep examples**
* 🔥 **Record interview MCQs**
* 🔥 **Record vs DTO vs Entity**
* 🔥 **Deep immutability patterns**

Just tell me 👍
