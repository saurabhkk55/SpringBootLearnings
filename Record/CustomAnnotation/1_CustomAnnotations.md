# Spring Boot Custom Annotation – Complete Documentation

---

## 1. Introduction

Custom annotations in Spring Boot allow developers to define their own validation rules or metadata and apply them declaratively on classes, fields, methods, or parameters. They are heavily used in **validation**, **AOP**, **security**, and **framework-level abstractions**.

This document focuses on **custom validation annotations** using **Bean Validation (JSR-380)** in Spring Boot.

---

## 2. What is a Custom Annotation?

A custom annotation is a user-defined annotation that:

* Encapsulates a reusable rule or behavior
* Can be applied declaratively
* Is processed by the framework at runtime (in most Spring use cases)

### Example Use Cases

* Age must be ≥ 18
* Number must be prime
* Start date must be before end date
* Conditional validation based on create/update operations

---

## 3. Core Building Blocks of a Custom Validation Annotation

A custom validation annotation consists of **three mandatory parts**:

1. **Annotation Interface** – Defines how the annotation looks
2. **Validator Class** – Contains the validation logic
3. **Usage Location** – DTO / Entity / Method where validation is applied

---

## 4. Annotation Meta-Annotations Explained

### 4.1 @Target – Where the annotation can be applied

`@Target` defines **the program element(s)** on which an annotation can be used.

#### Common Target Types

| ElementType     | Meaning                                   |
| --------------- | ----------------------------------------- |
| FIELD           | Class fields (most common for validation) |
| METHOD          | Methods                                   |
| PARAMETER       | Method parameters                         |
| TYPE            | Class / Interface                         |
| CONSTRUCTOR     | Constructors                              |
| ANNOTATION_TYPE | Other annotations                         |
| TYPE_USE        | Generic or type usage                     |

#### Example

```java
@Target(ElementType.FIELD)
```

This means the annotation can be applied only on fields.

Multiple targets:

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
```

---

### 4.2 @Retention – How long the annotation exists

`@Retention` defines **the lifecycle of an annotation**.

#### Java Program Lifecycle

```
.java → compile → .class → class loading → runtime execution
```

#### Retention Types

| Retention | Exists In   | Reflection Access | Typical Usage                    |
| --------- | ----------- | ----------------- | -------------------------------- |
| SOURCE    | .java only  | ❌ No              | Compiler checks (e.g. @Override) |
| CLASS     | .class file | ❌ No              | Bytecode tools                   |
| RUNTIME   | JVM memory  | ✅ Yes             | Spring, Hibernate, Validation    |

#### Important Rule for Spring

Spring **requires**:

```java
@Retention(RetentionPolicy.RUNTIME)
```

Because Spring reads annotations **using reflection at runtime**.

---

### 4.3 @Constraint – Links annotation to validation logic

`@Constraint` tells Spring:

> Which validator class contains the logic for this annotation

```java
@Constraint(validatedBy = AgeValidator.class)
```

---

## 5. Mandatory Methods in a Custom Constraint Annotation

Every custom validation annotation must declare the following methods:

```java
String message();
Class<?>[] groups();
Class<? extends Payload>[] payload();
```

### 5.1 message

Defines the validation error message.

```java
String message() default "Invalid value";
```

---

### 5.2 groups – Conditional validation

`groups` allow validation rules to run **only in specific scenarios**.

#### Why groups are needed

Example scenarios:

* User creation
* User update

Rules can differ between scenarios.

#### Marker Interfaces

```java
public interface OnCreate {}
public interface OnUpdate {}
```

#### Usage

```java
@ValidAge(groups = OnCreate.class)
private Integer age;
```

#### Controller Level Activation

```java
@PostMapping("/user")
public ResponseEntity<?> createUser(
    @Validated(OnCreate.class) @RequestBody UserDTO dto) {
    return ResponseEntity.ok("Created");
}
```

#### Important Notes

* Group interfaces are **marker interfaces** (no methods)
* **Any name is allowed**, but meaningful names are recommended
* If no group is specified, validation runs in the **Default group**

---

### 5.3 payload – Metadata attachment

`payload` allows attaching **extra metadata** to a constraint.

#### Key Points

* Does NOT affect validation logic
* Mostly used for logging, severity levels, or custom error handling
* Rarely used in real projects

#### Example Payload

```java
public class Severity {
    public static class INFO implements Payload {}
    public static class ERROR implements Payload {}
}
```

#### Usage

```java
@ValidAge(payload = Severity.ERROR.class)
```

---

## 6. Complete Example – Custom @ValidAge Annotation

### 6.1 Annotation Definition

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface ValidAge {

    String message() default "Age must be 18 or above";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

---

### 6.2 Validator Class

```java
public class AgeValidator implements ConstraintValidator<ValidAge, Integer> {

    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext context) {
        if (age == null) {
            return false;
        }
        return age >= 18;
    }
}
```

---

### 6.3 Usage in DTO

```java
public class UserDTO {

    @ValidAge
    private Integer age;
}
```

---

### 6.4 Controller Trigger

```java
@PostMapping("/user")
public ResponseEntity<String> createUser(
        @Valid @RequestBody UserDTO user) {
    return ResponseEntity.ok("Valid User");
}
```

---

## 7. Common Mistakes

| Mistake                                        | Impact                   |
| ---------------------------------------------- | ------------------------ |
| Missing @Retention(RUNTIME)                    | Validation never runs    |
| Forgetting @Constraint                         | Validator not linked     |
| Using @Valid instead of @Validated with groups | Group validation ignored |
| Overusing payload                              | Unnecessary complexity   |

---

## 8. Best Practices

* Always use `RetentionPolicy.RUNTIME` for Spring annotations
* Keep group names simple and meaningful
* Place group interfaces in a separate package
* Avoid business logic inside validators
* Use annotations for **rules**, not workflows

---

## 9. Interview-Ready Summary

* Custom annotations encapsulate reusable validation rules
* `@Target` defines where annotations can be applied
* `@Retention` defines how long annotations exist
* Spring requires `RUNTIME` retention
* `groups` enable scenario-based validation
* `payload` attaches metadata, rarely used

---

## 10. Conclusion

Custom annotations are a powerful way to keep validation logic clean, reusable, and declarative. Understanding retention policies, groups, and payloads ensures correct and maintainable implementations in Spring Boot applications.
