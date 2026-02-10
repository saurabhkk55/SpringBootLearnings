**`@JsonIgnore` in REAL API flow** — **while hitting API (request)** and **while receiving response**.

We’ll take a **User** example with a **password** field.

---

## 🧩 Scenario

* Client **sends password** → backend **uses it**
* Backend **never sends password back** in response ❌

---

## 1️⃣ User Entity / DTO

```java
import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private Long id;
    private String name;
    private String email;

    @JsonIgnore
    private String password;

    // getters & setters
}
```

📌 `@JsonIgnore` works for:

* ❌ Not considered for **Serialization (Java → JSON)**
* ❌ Not considered for **Deserialization (JSON → Java)**

---

## 2️⃣ While **Hitting API** (Request JSON → Java Object)

### 🔸 Client Request

```json
{
  "id": 1,
  "name": "Saurabh",
  "email": "saurabh@gmail.com",
  "password": "secret123"
}
```

### 🔸 Controller

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
    System.out.println(user.getPassword()); // ❌ null
    return ResponseEntity.ok(user);
}
```

### 🔸 What happens?

❌ `password` is **ignored during request deserialization**

➡️ `user.getPassword()` → `null`

⚠️ **Important**
If you want to **ACCEPT password but NOT SEND it back**, this approach is ❌ **WRONG**

---

## 3️⃣ Correct Way: Accept password, Hide in Response ✅

### Use `@JsonIgnore` on **getter only**

```java
import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

---

## 4️⃣ Now Flow Works Correctly

### 🔹 While Hitting API (JSON → Java)

```json
{
  "name": "Saurabh",
  "email": "saurabh@gmail.com",
  "password": "secret123"
}
```

✔️ `user.getPassword()` → `"secret123"`

---

### 🔹 While Receiving Response (Java → JSON)

```json
{
  "id": 1,
  "name": "Saurabh",
  "email": "saurabh@gmail.com"
}
```

❌ `password` is NOT returned

---

## 5️⃣ Controller Example (End-to-End)

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
    // password available here
    System.out.println(user.getPassword());

    return ResponseEntity.ok(user);
}
```

---

## 🧠 Interview Tip (VERY IMPORTANT)

### ❓ What does `@JsonIgnore` do?

> It prevents a field or getter/setter from participating in JSON serialization and/or deserialization.

### ❓ How to ignore only response but accept request?

> Apply `@JsonIgnore` on the **getter**, not on the field.

---

## 🔥 Bonus: Best Practice (Production)

### Use DTOs instead of Entity

```java
class UserRequestDTO {
    public String name;
    public String email;
    public String password;
}

class UserResponseDTO {
    public Long id;
    public String name;
    public String email;
}
```

✔️ Clean
✔️ Secure
✔️ Interview-friendly
