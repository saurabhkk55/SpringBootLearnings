# 🔹 What is `@JsonInclude(Include.NON_NULL)`?

It tells **Jackson**:

> **“While converting Java → JSON, include only fields whose value is NOT null.”**

📌 It affects **serialization only (response)**
📌 It does **NOT affect request handling**

---

# ❌ Without `@JsonInclude`

### Java Object

```java
UserResponseDTO user = new UserResponseDTO();
user.setId(1L);
user.setName("Saurabh");
user.setEmail(null);
user.setPhone(null);
```

### JSON Response

```json
{
  "id": 1,
  "name": "Saurabh",
  "email": null,
  "phone": null
}
```

❌ Noisy
❌ Confusing
❌ Client has to handle nulls

---

# ✅ With `@JsonInclude(NON_NULL)`

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
}
```

### JSON Response

```json
{
  "id": 1,
  "name": "Saurabh"
}
```

✔ Clean
✔ Compact
✔ Clear intent

---

# 🎯 Why We Use It (Real Reasons)

## 1️⃣ Clean & Professional API Responses

* Removes unnecessary `null` clutter
* Improves readability
* Better client experience

---

## 2️⃣ Smaller Response Size (Performance)

* Fewer fields → less JSON
* Faster network transfer
* Important for mobile & large payloads

---

# 🧩 Where to Apply It?

## ✔ Class level (Most common)

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO { }
```

## ✔ Field level (Selective)

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
private String phone;
```

## ✔ Global level (Whole application)

```java
@Bean
public ObjectMapper objectMapper() {
    return JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();
}
```

---

# 🔥 Variants You Should Know

| Variant     | Meaning                                  |
| ----------- | ---------------------------------------- |
| NON_NULL    | Exclude null fields                      |
| NON_EMPTY   | Exclude null + empty string + empty list |
| NON_DEFAULT | Exclude default values                   |
| ALWAYS      | Include everything (default)             |

Example:

```java
@JsonInclude(Include.NON_EMPTY)
private List<String> roles;
```

---

# 🧠 Interview streams.One-Liner

> `@JsonInclude(NON_NULL)` excludes null-valued fields from JSON responses to produce clean and efficient APIs.
