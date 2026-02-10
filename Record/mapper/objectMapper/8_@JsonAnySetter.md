# 🔹 What is `@JsonAnySetter`?

`@JsonAnySetter` tells **Jackson**:

> “If you find a JSON field that does NOT match any Java property, don’t fail — put it here.”

📌 Works during **deserialization only** (JSON → Java)
📌 Used with `Map<String, Object>`

---

# ❌ Without `@JsonAnySetter` (Problem)

### Incoming JSON

```json
{
  "id": 1,
  "name": "Saurabh",
  "email": "saurabh@gmail.com",
  "age": 28,
  "country": "India",
  "linkedin": "linkedin.com/in/saurabh"
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

❌ `UnrecognizedPropertyException` (or fields get lost if ignoreUnknown=true)

---

# ✅ With `@JsonAnySetter` (Solution)

```java
public class User {

    private Long id;
    private String name;
    private String email;

    private Map<String, Object> extraFields = new HashMap<>();

    @JsonAnySetter
    public void addExtraField(String key, Object value) {
        extraFields.put(key, value);
    }

    // getters
}
```

### Result

```java
extraFields = {
  "age": 28,
  "country": "India",
  "linkedin": "linkedin.com/in/saurabh"
}
```

✔ No exception
✔ No data loss
✔ Future-proof

---


## Capturing Extra Data Instead of Ignoring It

| Approach             | Result         |
| -------------------- | -------------- |
| `ignoreUnknown=true` | Data lost      |
| `@JsonAnySetter`     | Data preserved |

---

# 🧠 Interview One-Liner

> `@JsonAnySetter` captures unknown JSON properties into a Map during deserialization, allowing flexible and forward-compatible parsing.

---

# 🧩 Full Example with Controller

```java
@PostMapping("/users")
public void receiveUser(@RequestBody User user) {
    System.out.println(user.getExtraFields());
}
```

---

# 🏁 Final Summary

✔ Handles unknown JSON fields
✔ Prevents deserialization failure
✔ Preserves extra data
✔ Best for integrations & dynamic APIs
