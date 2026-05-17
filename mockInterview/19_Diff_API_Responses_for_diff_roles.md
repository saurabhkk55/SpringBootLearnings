A clean way to implement **different API responses for different roles** in Spring Boot is to separate:

1. **Authentication & Authorization** → Who can access?
2. **Response shaping** → What fields should each role see?

Example:

* `ADMIN` → sees everything
* `USER` → limited fields
* `SUPPORT` → partial sensitive data

---

# Recommended Approaches

## Approach 1 (Most Common & Best) → Use Different DTOs per Role

This is the cleanest and most maintainable approach.

---

# Example Scenario

Suppose we have a `User` entity:

```java
@Entity
public class User {

    @Id
    private Long id;

    private String name;
    private String email;
    private Double salary;
    private String ssn;
}
```

---

# Requirement

| Role    | Fields Visible  |
| ------- | --------------- |
| ADMIN   | all fields      |
| USER    | only id, name   |
| SUPPORT | id, name, email |

---

# Step 1: Create Different DTOs

## Admin Response

```java
@Data
@AllArgsConstructor
public class AdminUserResponse {

    private Long id;
    private String name;
    private String email;
    private Double salary;
    private String ssn;
}
```

---

## User Response

```java
@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
}
```

---

## Support Response

```java
@Data
@AllArgsConstructor
public class SupportResponse {

    private Long id;
    private String name;
    private String email;
}
```

---

# Step 2: Get Current Logged-In Role

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

String role = authentication.getAuthorities()
        .stream()
        .findFirst()
        .get()
        .getAuthority();
```

---

# Step 3: Return Response Based on Role

```java
@GetMapping("/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {

    User user = userService.getUser(id);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    String role = authentication.getAuthorities()
            .stream()
            .findFirst()
            .get()
            .getAuthority();

    if ("ROLE_ADMIN".equals(role)) {

        return ResponseEntity.ok(
                new AdminUserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getSalary(),
                        user.getSsn()
                )
        );
    }

    if ("ROLE_SUPPORT".equals(role)) {

        return ResponseEntity.ok(
                new SupportResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                )
        );
    }

    return ResponseEntity.ok(
            new UserResponse(
                    user.getId(),
                    user.getName()
            )
    );
}
```

---

# Why This Approach is Best

## Advantages

### 1. Very Clear

Each role has its own response structure.

---

### 2. Secure

Sensitive fields are never accidentally exposed.

---

### 3. Easy to Maintain

Easy to add new role responses later.

---

### 4. Swagger/API Docs Become Clear

Different DTOs clearly show different outputs.

---
