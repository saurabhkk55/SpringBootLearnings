## 1. What is the `Optional` Class?

`Optional<T>` is a **container object** used to represent the presence or absence of a value of type `T`. Instead of returning `null` to indicate no value, you return an `Optional` instance that either contains a value (`present`) or is empty.

It is part of `java.util` package.

---

## 2. Purpose of Using Optional Class

The primary purposes are:

### ✅ Avoid `NullPointerException`
- Explicitly forces the caller to handle the case of missing value.

### ✅ Improve Code Readability
- Makes the API contract clear: a method returns an `Optional` → value may be absent.

### ✅ Eliminate Null Checks
- Reduces boilerplate `if (obj != null)` code.

### ✅ Encourage Functional Programming
- Works well with streams, lambda expressions, and method references.

### ✅ Better Documentation
- The return type itself documents that the value might be missing.

---

## 3. Different Methods of Optional Class

### 🔹 **Creating Optional Objects**

| Method                       | Description                                                                            |
|------------------------------|----------------------------------------------------------------------------------------|
| `Optional.of(value)`         | Returns an `Optional` with the specified non-null value (throws NPE if value is null). |
| `Optional.ofNullable(value)` | Returns an `Optional` with the specified value (allows null → returns empty Optional). |
| `Optional.empty()`           | Returns an empty `Optional` (no value).                                                |

### 🔹 **Checking Presence/Absence**

| Method                                          | Description                                       |
|-------------------------------------------------|---------------------------------------------------|
| `isPresent()`                                   | Returns `true` if a value is present.             |
| `isEmpty()` (Java 11+)                          | Returns `true` if no value is present.            |
| `ifPresent(Consumer)`                           | Executes given action if a value is present.      |
| `ifPresentOrElse(Consumer, Runnable)` (Java 9+) | Executes one action if present, another if empty. |

### 🔹 **Retrieving Value**

| Method                     | Description                                                                                 |
|----------------------------|---------------------------------------------------------------------------------------------|
| `get()`                    | Returns the value if present, otherwise throws `NoSuchElementException` (use with caution). |
| `orElse(T other)`          | Returns the value if present, otherwise returns `other`.                                    |
| `orElseGet(Supplier)`      | Returns the value if present, otherwise returns result of supplier.                         |
| `orElseThrow()` (Java 10+) | Returns the value if present, otherwise throws `NoSuchElementException`.                    |
| `orElseThrow(Supplier)`    | Returns the value if present, otherwise throws exception from supplier.                     |

### 🔹 **Transforming and Filtering**

| Method              | Description                                                                                      |
|---------------------|--------------------------------------------------------------------------------------------------|
| `map(Function)`     | If value present, applies function and returns new `Optional` (empty if function returns null).  |
| `flatMap(Function)` | Similar to `map` but function must return an `Optional` (avoids nested `Optional<Optional<T>>`). |
| `filter(Predicate)` | If value present and matches predicate, returns same `Optional`, else returns empty.             |

### 🔹 **Other Utility Methods**

| Method               | Description                                                       |
|----------------------|-------------------------------------------------------------------|
| `equals(Object)`     | Compares two `Optional` objects.                                  |
| `hashCode()`         | Returns hash code of the value (0 for empty).                     |
| `toString()`         | Returns string representation.                                    |
| `stream()` (Java 9+) | Returns a sequential stream of the value (empty stream if empty). |

---

## 4. Example Usage

```java
import java.util.Optional;

public class OptionalDemo {
    public static void main(String[] args) {
        // Creating Optional
        Optional<String> nonEmpty = Optional.of("Hello");
        Optional<String> empty = Optional.empty();
        Optional<String> nullable = Optional.ofNullable(null);

        // Checking presence
        nonEmpty.ifPresent(System.out::println);  // Prints Hello

        // Retrieving value safely
        String result = empty.orElse("Default");
        System.out.println(result);  // Default

        String result2 = empty.orElseGet(() -> "Computed");
        System.out.println(result2); // Computed

        // Transforming
        Optional<Integer> length = nonEmpty.map(String::length);
        length.ifPresent(System.out::println);  // 5

        // Filtering
        Optional<String> filtered = nonEmpty.filter(s -> s.length() > 2);
        System.out.println(filtered.isPresent()); // true

        // Exception if empty
        try {
            empty.orElseThrow(() -> new RuntimeException("No value"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```

---

## 5. When NOT to Use Optional

- ❌ As a field in a class (not serializable by default)
- ❌ As a method parameter (creates unnecessary overhead, none of the callers may use it)
- ❌ In collections (e.g., `List<Optional<T>>` — indicates design issue)
- ❌ For simple cases where `null` check is clear and safe

---

## 6. Summary Table

| Category   | Important Methods                           |
|------------|---------------------------------------------|
| Creation   | `of`, `ofNullable`, `empty`                 |
| Check      | `isPresent`, `isEmpty`, `ifPresent`         |
| Retrieve   | `get`, `orElse`, `orElseGet`, `orElseThrow` |
| Transform  | `map`, `flatMap`, `filter`                  |
| Utility    | `equals`, `hashCode`, `toString`, `stream`  |

Would you like a real-world example comparing `null` handling vs `Optional`?