# 1️⃣ Why do we use

```java
@JsonIgnoreProperties(ignoreUnknown = true)
```

### What problem does it solve?

When **incoming JSON has extra fields** that your Java class **does NOT have**, Jackson throws an error:

❌ **Without it**

```text
UnrecognizedPropertyException:
Unrecognized field "age" (class User)
```

---

## 📌 Example (Problem)

### Incoming JSON (from frontend / external API)

```json
{
  "id": 1,
  "name": "Saurabh",
  "email": "saurabh@gmail.com",
  "age": 28,
  "country": "India"
}
```

### Java class

```java
public class User {
    private Long id;
    private String name;
    private String email;
}
```

❌ Jackson fails because `age` & `country` are **unknown**

---

## ✅ With `@JsonIgnoreProperties(ignoreUnknown = true)`

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String name;
    private String email;
}
```

### Result

✔ `age` → ignored
✔ `country` → ignored
✔ No exception
✔ API works smoothly

---

# 🧠 Interview streams.One-Liner

> `@JsonIgnoreProperties(ignoreUnknown = true)` makes deserialization resilient by ignoring unknown JSON fields instead of failing.

---

# 3️⃣ What If the Class is 3rd-Party (You CAN’T modify it)?

## ✅ Option 1: Configure ObjectMapper (BEST)

```java
ObjectMapper mapper = new ObjectMapper();

mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```

📌 Applies globally (or per mapper)
📌 No need to touch the class
📌 Best for 3rd-party DTOs

---

### Spring Boot Global Configuration (Recommended)

```java
@Bean
public ObjectMapper objectMapper() {
    return JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
}
```

✔ Works for all APIs
✔ Safe for integrations
