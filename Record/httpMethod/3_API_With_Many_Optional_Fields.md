Perfect 👍
Neeche maine **controller + DTO + service + repository + best practices** sab ko **ek single, clean, end-to-end documentation** mein merge kar diya hai.
Ye **README / Confluence / Design Doc / Interview prep** — sab ke liye ready hai.

---

# 📘 Spring Boot API Design Documentation

## Handling APIs with Many Optional Fields

---

## 1️⃣ Problem Statement

Humein ek Spring Boot REST API design karni hai jisme:

* Total fields ≈ **20**
* Mandatory fields ≈ **5**
* Optional fields ≈ **15**
* API **clean, readable, scalable** honi chahiye
* Partial updates bhi support karni hain
* Future mein naye fields easily add ho saken

---

## 2️⃣ ❌ Anti-Pattern (Avoid This)

### Using multiple `@RequestParam`

```java
@PostMapping("/user")
public void createUser(
        @RequestParam String name,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) Integer age,
        @RequestParam(required = false) String phone
        // ... many more fields
) {
}
```

### ❌ Problems

* Method signature **ugly & unreadable**
* Validation **hard to manage**
* New field add → controller method change
* **JSON-based REST APIs ke against**

---

## 3️⃣ ✅ Recommended Design Approach

### 👉 **Request DTO + JSON Request Body**

### Core Idea

* Client **JSON payload** bhejega
* Optional fields **absent ho sakti hain**
* Spring Boot automatic **JSON → Object binding** karega
* Mandatory fields par validation lagegi

---

## 4️⃣ Request DTO Design

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    // Mandatory fields
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    // Optional fields
    private Integer age;
    private String city;
    private String phone;
    private String company;
    private Boolean isActive;

    // ... remaining optional fields
}
```

### Validation Rules

| Field Type              | Rule                              |
| ----------------------- | --------------------------------- |
| Mandatory               | `@NotNull`, `@NotBlank`, `@Email` |
| Optional                | No validation annotation          |
| Missing optional fields | Set as `null`                     |

---

## 5️⃣ Controller Layer

```java
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(
            @Valid @RequestBody UserRequest request) {

        userService.createUser(request);
        return ResponseEntity.ok("User created successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        userService.updateUser(id, request);
        return ResponseEntity.ok("User updated successfully");
    }
}
```

---

## 6️⃣ Sample JSON Requests

### Create User (POST)

```json
{
  "name": "Saurabh",
  "email": "saurabh@gmail.com",
  "city": "Delhi"
}
```

✔️ Missing fields → `null`
✔️ API works smoothly

---

### Partial Update (PATCH)

```json
{
  "age": 27,
  "city": "Bangalore"
}
```

✔️ Sirf provided fields update hongi
✔️ Baaki fields untouched rahengi

---

## 7️⃣ Entity Design

```java
@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private Integer age;
    private String city;
    private String phone;
    private String company;
    private Boolean isActive;
}
```

---

## 8️⃣ Repository Layer

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

---

## 9️⃣ Service Layer Design

### Service Interface

```java
public interface UserService {

    void createUser(UserRequest request);

    void updateUser(Long userId, UserRequest request);
}
```

---

## 🔹 CREATE User – Service Implementation

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(UserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setCity(request.getCity());
        user.setPhone(request.getPhone());
        user.setCompany(request.getCompany());

        // Default value handling
        user.setIsActive(
                request.getIsActive() != null ? request.getIsActive() : true
        );

        userRepository.save(user);
    }
```

### Why this works

* Optional fields → `null` allowed
* Defaults explicitly handled
* Clean & predictable behavior

---

## 🔹 UPDATE User (PATCH) – Service Implementation

### ❗ Problem to Avoid

Blind overwrite:

```java
user.setCity(request.getCity()); // ❌ can overwrite with null
```

---

### ✅ Correct Partial Update Logic

```java
    @Override
    public void updateUser(Long userId, UserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getCompany() != null) {
            user.setCompany(request.getCompany());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        userRepository.save(user);
    }
}
```

---

## 1️⃣0️⃣ PUT vs PATCH (Design Decision)

| Aspect | PUT | PATCH |
|----|----|---|
|Payload | Full object required | Partial data allowed |
|Null risk | High (overwrite) | Safe |
|Many optional fields | ❌ Not recommended | ✅ Best choice |

---

## 1️⃣1️⃣ Key Design Principles Used

* DTO-based API design
* JSON-first REST approach
* Service-layer business logic
* Null-safe partial updates
* Clean controller methods
* Future-proof & scalable

---

## 🔥 Interview-Ready Summary

> *For APIs with many optional fields, I use a JSON request body with a dedicated DTO. Mandatory fields are validated via annotations, optional fields are nullable, and partial updates are handled using PATCH with non-null checks in the service layer to prevent data loss.*
