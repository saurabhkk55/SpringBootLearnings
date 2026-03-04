## ✅ Best & Recommended: **Jackson (ObjectMapper)**

### Why Jackson is the best?

* ✅ **Default in Spring Boot**
* ✅ Fast & mature
* ✅ Excellent annotation support
* ✅ Auto-integrated with `@RestController`
* ✅ Handles complex/nested JSON easily

---

## 🔹 1. Automatic Conversion (MOST COMMON & BEST)

### JSON → Java Object

```java
@PostMapping("/users")
public ResponseEntity<String> createUser(@RequestBody User user) {
    return ResponseEntity.ok("User created: " + user.getName());
}
```

➡️ Spring Boot internally uses **Jackson** to deserialize JSON into `User`.

---

### Java Object → JSON

```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable int id) {
    return new User(id, "Saurabh", "saurabh@gmail.com");
}
```

➡️ Spring converts `User` into JSON automatically.

📌 **No ObjectMapper code needed**
📌 **This is the best practice for APIs**

---

## 🔹 2. Manual Conversion using `ObjectMapper` (When Needed)

Use this when:

* Reading JSON from **file**
* Consuming **external API**
* Custom transformations

### JSON → Java

```java
ObjectMapper mapper = new ObjectMapper();

User user = mapper.readValue(jsonString, User.class);
```

### Java → JSON

```java
String json = mapper.writeValueAsString(user);
```

---

## 🔹 3. Useful Jackson Annotations (VERY IMPORTANT)

### Ignore fields

```java
@JsonIgnore
private String password;
```

### Change JSON key name

```java
@JsonProperty("user_name")
private String userName;
```

### Ignore unknown fields

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class User { }
```

### Control serialization

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
```

---

## 🔹 4. Date Handling (Common Interview Question)

```java
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate dob;
```

Or globally:

```java
@Bean
ObjectMapper objectMapper() {
    return JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
}
```

---

## ❌ Alternatives (Not Recommended in Spring Boot)

| Library  | Reason                    |
| -------- | ------------------------- |
| Gson     | No default Spring support |
| org.json | Manual & verbose          |
| JAXB     | XML-focused               |

📌 **Jackson is industry standard for Spring Boot**

---

## 🏆 Final Verdict

> **Best Way in Spring Boot:**
> ✅ **Jackson (Auto conversion via `@RequestBody` & `@ResponseBody`)**

### Interview streams.One-Liner 💡

> *Spring Boot uses Jackson internally to automatically serialize Java objects to JSON and deserialize JSON to Java objects using ObjectMapper.*
