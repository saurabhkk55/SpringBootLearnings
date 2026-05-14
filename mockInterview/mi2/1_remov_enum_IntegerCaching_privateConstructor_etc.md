# Java Core Concepts Notes

# 1. `list.remove(1)` Behavior

```java
List<Integer> list = new ArrayList<>(List.of(10, 20, 30));

list.remove(1);

System.out.println(list);
```

Output:

```java
[10, 30]
```

Reason:

```java
remove(int index)
```

gets called, so the element present at index `1` is removed.

---

If you want to remove the value `1` as an element:

```java
list.remove(Integer.valueOf(1));
```

Example:

```java
List<Integer> list = new ArrayList<>(List.of(1, 2, 3));

list.remove(Integer.valueOf(1));

System.out.println(list);
```

Output:

```java
[2, 3]
```

---

# 2. Integer Caching

```java
Integer a = 127;
Integer b = 127;

System.out.println(a == b); // true
```

Java caches `Integer` objects in range:

```java
-128 to 127
```

So both variables point to the same object.

---

```java
Integer c = 128;
Integer d = 128;

System.out.println(c == d); // false
```

Outside cache range, separate objects are created.

---

Important:

```java
==        -> compares reference
.equals() -> compares value
```

Example:

```java
Integer c = 128;
Integer d = 128;

System.out.println(c.equals(d)); // true
```

---

# 3. What is Enum?

Enum is a special type in Java used to represent a fixed set of constants.

Example:

```java
enum Status {
    PENDING,
    SUCCESS,
    FAILED
}
```

Usage:

```java
Status status = Status.SUCCESS;
```

---

# Why use Enum?

Instead of:

```java
String status = "SUCCESS";
```

which may lead to invalid values:

```java
status = "SUCESS"; // typo
```

Enum provides:

* type safety
* fixed set of values
* better readability

---

# Features of Enum

Enums can contain:

* variables
* methods
* constructors

Example:

```java
enum Role {

    ADMIN("Full Access"),
    USER("Limited Access");

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
```

---

# Important Points About Enum

## Enum constants are implicitly:

```java
public static final
```

Example:

```java
enum Status {
    SUCCESS,
    FAILED
}
```

Internally it behaves approximately like:

```java
class Status {

    public static final Status SUCCESS = new Status();
    public static final Status FAILED = new Status();
}
```

Meaning:

* `public` -> accessible everywhere
* `static` -> belongs to class
* `final` -> cannot be reassigned

---

# Important Clarification

Only enum constants are implicitly:

```java
public static final
```

NOT all fields inside enum.

Example:

```java
enum Role {

    ADMIN,
    USER;

    private String desc;
}
```

Here:

```java
ADMIN
USER
```

are implicitly:

```java
public static final
```

But:

```java
private String desc;
```

is a normal instance variable.

---

# Additional Enum Facts

Enums implicitly extend:

```java
java.lang.Enum
```

So enum cannot extend another class.

Example:

```java
class Test extends Status // NOT allowed
```

---

Enum constructors are implicitly private.

So:

```java
new Status() // NOT allowed
```

is invalid.

---

# 4. Can a Constructor Be Private?

Yes.

Example:

```java
class Test {

    private Test() {
        System.out.println("Object created");
    }
}
```

Outside class:

```java
new Test(); // ERROR
```

---

# Use Cases of Private Constructor

## 1. Singleton Design Pattern

Only one object allowed.

```java
class Singleton {

    private static Singleton obj = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return obj;
    }
}
```

---

## 2. Utility Class

Prevent object creation.

```java
class MathUtil {

    private MathUtil() {}

    public static int add(int a, int b) {
        return a + b;
    }
}
```

Usage:

```java
MathUtil.add(2, 3);
```

---

# 5. Synchronization for Reading and Writing

## Reading Only

If multiple threads are only reading shared data and no thread modifies it, synchronization is usually not required.

Example:

```java
Map<Integer, String> map = new HashMap<>();
```

If no thread modifies the map, concurrent reads are generally safe.

---

# Writing Requires Synchronization

When multiple threads modify shared data, synchronization is required to avoid:

* race conditions
* inconsistent data
* corrupted state

Example:

```java
count++;
```

Internally:

```text
1. read
2. modify
3. write
```

Two threads may overwrite each other.

---

# Reading May Also Need Synchronization

If one thread writes while another reads:

```text
Thread 1 -> writing
Thread 2 -> reading
```

Without synchronization, reader thread may see:

* stale data
* partially updated data
* inconsistent state

---

# Solutions

* synchronized
* volatile
* Lock/ReentrantLock
* concurrent collections

---

# 6. API Returning null in Spring Boot

Example:

```java
@GetMapping
public User getUser() {
    return null;
}
```

Usually response becomes:

```http
200 OK
```

with empty body.

---

# Why?

Spring considers:

* request processed successfully
* controller method executed successfully

So status remains `200 OK`.

---

# Better Approaches

## 1. Return 404 NOT FOUND

```java
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {

    User user = service.find(id);

    if (user == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(user);
}
```

---

## 2. Throw Custom Exception

```java
throw new ResourceNotFoundException("User not found");
```

Handle using:

```java
@ControllerAdvice
```

---

# Important

Returning `null` silently is usually bad API design because client cannot clearly identify:

* resource not found
* empty response
* actual issue

Using proper HTTP status codes is preferred.
