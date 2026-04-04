# ✅ 1. Why API Versioning?

Suppose you have:

### V1 response

```json
{
  "name": "Saurabh"
}
```

Later you change to:

### V2 response

```json
{
  "firstName": "Saurabh",
  "lastName": "Kardam"
}
```

❌ This will break old clients
👉 So we version APIs

---

# ✅ 2. Types of API Versioning (Spring Boot)

There are **4 main approaches**:

| Method              | Example                                   | Usage               |
| ------------------- | ----------------------------------------- | ------------------- |
| URI Versioning      | `/api/v1/users`                           | ⭐ Most common       |
| Request Param       | `/api/users?version=1`                    | Rare                |
| Header Versioning   | `X-API-VERSION: 1`                        | Used in enterprises |
| Content Negotiation | `Accept: application/vnd.company.v1+json` | Advanced            |

---

# ✅ 3. Best Approach (Real Industry Insight)

👉 **Use URI Versioning + Header Versioning (optional)**

* Simple
* Clear
* Easy to debug
* Widely used in companies

---

# ✅ 4. Implementation (Step-by-Step)

---

## 🔹 Approach 1: URI Versioning (Recommended ⭐)

### Controller

```java
@RestController
@RequestMapping("/api")
public class UserController {

    // V1 API
    @GetMapping("/v1/user")
    public UserV1 getUserV1() {
        return new UserV1("Saurabh");
    }

    // V2 API
    @GetMapping("/v2/user")
    public UserV2 getUserV2() {
        return new UserV2("Saurabh", "Kardam");
    }
}
```

---

### DTOs

```java
public class UserV1 {
    private String name;

    public UserV1(String name) {
        this.name = name;
    }
}
```

```java
public class UserV2 {
    private String firstName;
    private String lastName;

    public UserV2(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
```

---

### Postman Test

```
GET /api/v1/user
GET /api/v2/user
```

---

## 🔹 Approach 2: Header Versioning

```java
@GetMapping(value = "/user", headers = "X-API-VERSION=1")
public UserV1 getUserV1() {
    return new UserV1("Saurabh");
}

@GetMapping(value = "/user", headers = "X-API-VERSION=2")
public UserV2 getUserV2() {
    return new UserV2("Saurabh", "Kardam");
}
```

---

### Postman

Headers:

```
X-API-VERSION: 1
```

---

## 🔹 Approach 3: Content Negotiation (Advanced)

```java
@GetMapping(value = "/user", produces = "application/vnd.company.v1+json")
public UserV1 getUserV1() {
    return new UserV1("Saurabh");
}

@GetMapping(value = "/user", produces = "application/vnd.company.v2+json")
public UserV2 getUserV2() {
    return new UserV2("Saurabh", "Kardam");
}
```

---

### Postman

```
Accept: application/vnd.company.v2+json
```

---

# ✅ 5. How Spring Decides Version

Spring matches request based on:

* URL (`/v1/...`)
* Headers (`X-API-VERSION`)
* `Accept` (media type)

👉 It routes to correct method automatically

---

# ✅ 6. Best Practices (Very Important 🔥)

### ✔ 1. Never break existing API

Always keep old version running

---

### ✔ 2. Use separate DTOs

```java
UserV1
UserV2
```

👉 Don’t reuse same class

---

### ✔ 3. Version only when needed

❌ Don’t version for small changes
✔ Version for breaking changes

---

### ✔ 4. Use clear naming

```
/api/v1/users
/api/v2/users
```

---

### ✔ 5. Deprecate old APIs

```java
@Deprecated
@GetMapping("/v1/user")
```

---

# ✅ 7. Real-Life Analogy

Think like mobile apps:

* Old app → uses v1 API
* Updated app → uses v2 API

👉 Both must work simultaneously

---

# ✅ 8. Which One Should YOU Use?

For your experience (Java backend + real projects):

👉 **Go with:**

```
URI Versioning (/v1, /v2)
```

👉 Add later (if needed):

```
Header versioning
```
