# 📘 PUT vs PATCH – Final Documentation (Updated)

---

## 1️⃣ PUT API

### 🔹 Definition

**PUT** method ka use **entire resource ko replace/update** karne ke liye hota hai.

> Client ko **complete object** bhejna hota hai, chahe sirf ek field hi change kyun na ho.

---

### 🔹 Characteristics

* Full update (replace)
* Idempotent (same request → same result)
* Missing fields ka **high risk**
* Mostly admin / configuration type operations me use hota hai

---

### 🔹 Example

#### Existing Resource (DB me)

```json
{
  "id": 1,
  "name": "Saurabh",
  "email": "saurabh@gmail.com",
  "age": 25
}
```

---

### ✅ Correct PUT Request (All fields present)

```http
PUT /users/1
```

```json
{
  "name": "Saurabh Kumar",
  "email": "sk@gmail.com",
  "age": 26
}
```

✔️ Safe full replacement

---

## ⚠️ Risk with PUT (VERY IMPORTANT)

### ❌ Problem Scenario

Agar client **koi field bhejna bhool jaye**:

```json
{
  "name": "Saurabh Kumar",
  "email": "sk@gmail.com"
}
```

👉 `age` request me missing hai

---

### ❗ What can go wrong?

Agar PUT implementation **full replacement** logic follow karti hai:

```java
existingUser.setName(newUser.getName());
existingUser.setEmail(newUser.getEmail());
existingUser.setAge(newUser.getAge()); // newUser.getAge() = null
```

👉 Result:

```json
{
  "name": "Saurabh Kumar",
  "email": "sk@gmail.com",
  "age": null   ❌
}
```

🚨 **Unintentional data loss**

---

### 🔥 Interview One-Liner (Risk)

> **PUT me agar complete payload na bheja jaye, to missing fields NULL ya default ho sakti hain.**

---

### 🛑 Dangerous PUT Implementation (Avoid This)

```java
newUser.setId(id);
userRepository.save(newUser);
```

❌ Ye approach:

* Missing fields → NULL
* Data loss risk → VERY HIGH

---

### ✅ Safe PUT Best Practices

* PUT request me **ALL fields mandatory** rakho
* DTO + validation use karo
* Partial update **allow mat karo**
* Existing entity ko pehle DB se load karo

---

## 2️⃣ PATCH API

### 🔹 Definition

**PATCH** method ka use **partial update** ke liye hota hai.

> Client sirf wahi fields bhejta hai jo change karni hain (1 ya multiple).

---

### 🔹 Characteristics

* Partial update
* Multiple fields update supported
* Low risk of data loss
* Idempotency implementation pe depend karti hai
* Real-world apps me zyada preferred

---

### 🔹 Example

#### PATCH Request

```http
PATCH /users/1
```

```json
{
  "age": 27,
  "name": "Saurabh Kumar"
}
```

#### Result

```json
{
  "id": 1,
  "name": "Saurabh Kumar",
  "email": "saurabh@gmail.com",
  "age": 27
}
```

✔️ Sirf provided fields update hui
✔️ Baaki fields unchanged

---

### 🔹 Spring Boot Implementation (PATCH)

```java
@PatchMapping("/users/{id}")
public User patchUser(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates) {

    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    updates.forEach((key, value) -> {
        switch (key) {
            case "name":
                user.setName((String) value);
                break;
            case "email":
                user.setEmail((String) value);
                break;
            case "age":
                user.setAge((Integer) value);
                break;
        }
    });

    return userRepository.save(user);
}
```

---

## 3️⃣ PUT vs PATCH – Comparison Table

| Feature         | PUT          | PATCH          |
| --------------- | ------------ | -------------- |
| Update type     | Full replace | Partial update |
| Fields required | All          | Only changed   |
| Multiple fields | Yes          | Yes            |
| Missing fields  | NULL risk    | Safe           |
| Idempotent      | Yes          | Depends        |
| Data loss risk  | High         | Low            |
| Preferred usage | Config/Admin | User updates   |

---

## 4️⃣ Real-World Usage Guidelines

### ✅ Use PUT when:

* Complete resource replace chahiye
* Configuration overwrite karni ho
* Admin-level operations

### ✅ Use PATCH when:

* Profile/settings update
* Mobile/Web apps
* Frequent partial updates

---

## 5️⃣ Interview One-Liners (Must Remember)

* **PUT replaces the entire resource**
* **PATCH modifies only the provided fields**
* **PATCH can update multiple fields**
* **PUT is idempotent, PATCH may or may not be**

---

## 6️⃣ Best Practices (Industry Standard)

* PUT → DTO + validation mandatory
* PATCH → DTO / Map + null checks
* Never do: `save(newEntityWithId)` for PUT
* Always load existing entity before update
* Handle invalid fields explicitly

---

## 🎯 Final Summary

> **PUT = Full replacement of a resource**
> 
> **PATCH = Partial modification of a resource**
