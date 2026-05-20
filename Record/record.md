## What is a Record in Java?

A **record** is a special type of class introduced to reduce boilerplate code for classes whose primary purpose is to **hold data**.

Normally in Java, we write:

* `Fields`
* `Constructor (Canonical Constructor)`
* `Getter methods (but not traditional getXyz())`
* `equals()`
* `hashCode()`
* `toString()`

A record generates most of these automatically.

### Normal Class

```java
public class Employee {

    private final Long id;
    private final String name;
    private final double salary;

    public Employee(Long id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object obj) {
        // generated
    }

    @Override
    public int hashCode() {
        // generated
    }

    @Override
    public String toString() {
        // generated
    }
}
```

### Same using Record

```java
public record Employee(
        Long id,
        String name,
        double salary
) {
}
```

Much shorter and cleaner.

---

## What Record Automatically Generates?

When you create a record:

```java
public record Employee(
        Long id,
        String name,
        double salary
) {
}
```

Java automatically generates:

### 1. Constructor

```java
public Employee(Long id, String name, double salary)
```

Usage:

```java
Employee emp = new Employee(
        1L,
        "Saurabh",
        50000
);
```

---

### 2. Accessor Methods (Getter-like methods)

Record does **not generate traditional getters** like `getName()`.

Instead, it generates methods with the same name as fields.

Generated methods:

```java
id()
name()
salary()
```

Usage:

```java
System.out.println(emp.id());
System.out.println(emp.name());
System.out.println(emp.salary());
```

❌ Wrong:

```java
emp.getName();
```

✅ Correct:

```java
emp.name();
```

---

### 3. `equals()`

Automatically generated.

Example:

```java
Employee e1 = new Employee(1L, "Saurabh", 50000);

Employee e2 = new Employee(1L, "Saurabh", 50000);

System.out.println(e1.equals(e2));
```

Output:

```java
true
```

Because values are same.

---

### 4. `hashCode()`

Automatically generated.

Useful when storing records in:

* `HashMap`
* `HashSet`

Example:

```java
Set<Employee> employees = new HashSet<>();

employees.add(new Employee(1L, "Saurabh", 50000));
employees.add(new Employee(1L, "Saurabh", 50000));

System.out.println(employees.size());
```

Output:

```java
1
```

Because `equals()` + `hashCode()` work correctly.

---

### 5. `toString()`

Automatically generated.

Example:

```java
System.out.println(emp);
```

Output:

```java
Employee[
    id=1,
    name=Saurabh,
    salary=50000.0
]
```

---

## Does Record Create Setters?

❌ No.

Records are **immutable**.

Example:

```java
public record Employee(Long id, String name) {
}
```

You cannot do:

```java
emp.setName("Rahul"); // ERROR
```

or

```java
emp.name = "Rahul"; // ERROR
```

Both are invalid.

Why?

Because internally fields are:

```java
private final
```

Meaning once object is created, values cannot change.

---

## Can We Define Methods in Record?

✅ Yes.

You can define helper methods.

Example:

```java
public record Employee(String name, double salary) {

    public double yearlySalary() {
        return salary * 12;
    }
}
```

Usage:

```java
Employee emp = new Employee("Saurabh", 50000);

System.out.println(emp.yearlySalary());
```

Output:

```java
600000
```

### When is it Recommended?

Good for:

* Helper methods
* Derived/calculated values

Example:

```java
public record Rectangle(double length, double width) {

    public double area() {
        return length * width;
    }
}
```

This is clean and recommended.

---

## Can We Create Constructor in Record?

✅ Yes.

There are mainly 2 ways.

# 1. Compact Constructor (Recommended)

Mostly used for:

* Validation
* Data normalization

Example:

```java
public record Employee(String name, double salary) {

    public Employee {

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }

        name = name.trim();
    }
}
```

Usage:

```java
Employee emp = new Employee(" Saurabh ", 50000);
```

What happens?

* Validation runs
* Name is trimmed
* Constructor is still auto-generated

Internally Java still creates:

```java
public Employee(String name, double salary)
```

### Why compact constructor?

Cleaner syntax.

No need to manually assign:

```java
this.name = name;
```

Recommended approach.

---

# 2. Canonical Constructor

You can define full constructor manually.

Example:

```java
public record Employee(String name, double salary) {

    public Employee(String name, double salary) {

        if (salary < 0) {
            throw new IllegalArgumentException("Invalid salary");
        }

        this.name = name;
        this.salary = salary;
    }
}
```

Less commonly used.

Prefer compact constructor unless full control is required.

---

## Recommended Use Cases for Record

### 1. DTO / Response Object

Very common in Spring Boot.

Example:

```java
public record EmployeeResponse(
        Long id,
        String name,
        String email
) {
}
```

Used in controller:

```java
@GetMapping("/{id}")
public EmployeeResponse getEmployee() {

    return new EmployeeResponse(1L, "Saurabh", "abc@gmail.com");
}
```

Best use case.

---

### 2. Immutable Value Objects

Example:

```java
public record Money(double amount, String currency) {
}
```

Good for immutable values.

---

### 3. Validation + Data Carrier

Example:

```java
public record User(String email, int age) {

    public User {

        if (age < 18) {
            throw new IllegalArgumentException("Age must be >= 18");
        }
    }
}
```

Good usage.

---

### 4. Helper/Calculated Methods

Example:

```java
public record Rectangle(double length, double width) {

    public double perimeter() {
        return 2 * (length + width);
    }
}
```

Good usage.

---

## When NOT to Use Record?

Avoid putting heavy business logic.

❌ Bad Example:

```java
public record Payment(String paymentId) {

    public void processPayment() {

        // DB calls
        // Kafka publishing
        // External API calls
    }
}
```

Why bad?

Record should mainly be:

> Data holder + small helper logic

Business logic belongs in service classes.

Example:

```java
@Service
public class PaymentService {

    public void processPayment() {

    }
}
```

Better design.

---

## Record vs Class

| Feature                     |       Record | Normal Class |
| --------------------------- | -----------: | -----------: |
| Constructor auto-generated  |            ✅ |            ❌ |
| Getter auto-generated       | ✅ (`name()`) |            ❌ |
| Setter auto-generated       |            ❌ |            ❌ |
| `equals()` auto-generated   |            ✅ |            ❌ |
| `hashCode()` auto-generated |            ✅ |            ❌ |
| `toString()` auto-generated |            ✅ |            ❌ |
| Mutable                     |            ❌ |            ✅ |
| Best for DTO                |            ✅ |            ❌ |

---

## Important Interview Points

### 1. Are records immutable?

✅ Yes.

Fields are internally `private final`.

---

### 2. Does record create setters?

❌ No.

---

### 3. Does record create getters?

✅ Yes, but accessor methods:

```java
name()
```

not

```java
getName()
```

---

### 4. Can we write methods inside record?

✅ Yes.

Recommended for helper/calculated methods.

---

### 5. Can we create constructor inside record?

✅ Yes.

Prefer:

* Compact constructor → recommended
* Canonical constructor → when more control needed

---

### 6. Main purpose of record?

To reduce boilerplate for:

* DTOs
* Immutable data holders
* Value objects

---

## Easy Thumb Rule

Use record when:

> “I mainly need immutable data + small validation/helper methods.”

Avoid record when:

> “This class contains heavy business logic or mutable state.”
