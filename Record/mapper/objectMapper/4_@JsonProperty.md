# 🔹 What is `@JsonProperty`?

`@JsonProperty` tells **Jackson**:

> “This Java field maps to THIS JSON key name and THIS access rule.”

It controls:

* ✅ JSON key name
* ✅ Serialization (Java → JSON → response)
* ✅ Deserialization (JSON → Java → request)

---

# 🔹 Basic Syntax

```java
@JsonProperty("json_key_name")
private String javaFieldName;
```

---

# 🔁 1. While **Hitting API** (Request: JSON → Java)

### Client sends JSON

```json
{
  "user_name": "Saurabh",
  "email_id": "saurabh@gmail.com"
}
```

### DTO

```java
public class UserRequestDTO {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("email_id")
    private String email;

    // getters & setters
}
```

### Result in Backend

```java
userRequestDTO.getUserName(); // "Saurabh"
userRequestDTO.getEmail();    // "saurabh@gmail.com"
```

✅ JSON keys don’t need to match Java naming
✅ Jackson maps correctly during **deserialization**

---

# 🔁 2. While **Receiving Response** (Response: Java → JSON)

### Response DTO

```java
public class UserResponseDTO {

    @JsonProperty("user_id")
    private Long id;

    @JsonProperty("user_name")
    private String name;

    @JsonProperty("email_id")
    private String email;
}
```

### Controller

```java
@GetMapping("/users/{id}")
public UserResponseDTO getUser() {
    return new UserResponseDTO(1L, "Saurabh", "saurabh@gmail.com");
}
```

### JSON Response

```json
{
  "user_id": 1,
  "user_name": "Saurabh",
  "email_id": "saurabh@gmail.com"
}
```

✅ Java camelCase → JSON snake_case
✅ Clean, client-friendly API

---

# 🔐 3. Control Read / Write (VERY IMPORTANT)

## 3.1. WRITE_ONLY: ✔ Accept data, ❌ Don’t send back

### Password example (BEST PRACTICE)

```java
public class UserDTO {

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
```

### Request (Client → API)

```json
{
  "name": "Saurabh",
  "password": "secret123"
}
```

✔ Password is read into Java

### Response (API → Client)

```json
{
  "name": "Saurabh"
}
```

❌ Password not sent back

📌 **WRITE_ONLY = request only**

---

## 3.2. READ_ONLY: ✔ Send data, ❌ Don’t accept from client

```java
@JsonProperty(access = JsonProperty.Access.READ_ONLY)
private Long id;
```

✔ Sent in response
❌ Ignored if client sends it

---

# 🧠 Interview streams.One-Liner (Must Remember)

> `@JsonProperty` controls JSON field naming and read/write access during serialization and deserialization.

---

# 🏁 Final Summary

| Scenario                | Annotation             |
| ----------------------- | ---------------------- |
| Change JSON key name    | `@JsonProperty("key")` |
| Accept but don’t return | `WRITE_ONLY`           |
| Return but don’t accept | `READ_ONLY`            |
| Ignore completely       | `@JsonIgnore`          |
