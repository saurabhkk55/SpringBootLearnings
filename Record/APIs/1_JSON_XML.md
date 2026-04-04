# ✅ 1. Concept (Simple Understanding)

Your API should:

* **Accept** → JSON *or* XML (client sends data)
* **Return** → JSON *or* XML (client chooses format)

👉 Controlled by headers:

| Purpose         | Header         |
| --------------- | -------------- |
| Request format  | `Content-Type` |
| Response format | `Accept`       |

---

# ✅ 2. Required Dependency

Add XML support:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

---

# ✅ 3. DTO (Important for XML)

```java
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "user")
public class UserDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("age")
    private int age;

    // getters & setters
}
```

👉 `@JacksonXmlRootElement` ensures proper XML root tag

---

# ✅ 4. Single API Design

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @PostMapping(
        value = "/user",
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"}
    )
    public UserDTO createUser(@RequestBody UserDTO user) {

        // Example logic
        user.setName(user.getName().toUpperCase());

        return user;
    }
}
```

---

# ✅ 5. Postman Testing (Step-by-Step)

## 🔹 Case 1: JSON → JSON

### Request

* Method: POST
* URL: `http://localhost:8080/api/user`

Headers:

```id="g4s7y0"
Content-Type: application/json
Accept: application/json
```

Body:

```json
{
  "name": "saurabh",
  "age": 25
}
```

### Response

```json
{
  "name": "SAURABH",
  "age": 25
}
```

---

## 🔹 Case 2: JSON → XML

Headers:

```id="5wt71d"
Content-Type: application/json
Accept: application/xml
```

Response:

```xml
<user>
    <name>SAURABH</name>
    <age>25</age>
</user>
```

---

## 🔹 Case 3: XML → JSON

Headers:

```id="1grq6s"
Content-Type: application/xml
Accept: application/json
```

Body:

```xml
<user>
    <name>saurabh</name>
    <age>25</age>
</user>
```

Response:

```json
{
  "name": "SAURABH",
  "age": 25
}
```

---

## 🔹 Case 4: XML → XML

Headers:

```id="e3hdt1"
Content-Type: application/xml
Accept: application/xml
```

Response:

```xml
<user>
    <name>SAURABH</name>
    <age>25</age>
</user>
```

---

# ✅ 6. Internal Working (VERY IMPORTANT 🔥)

### Step-by-step flow:

### 🔹 1. Request comes

Spring checks:

* `Content-Type` → how to **read input**
* `Accept` → how to **send output**

---

### 🔹 2. Request Body Conversion

| Content-Type | Converter Used                           |
| ------------ | ---------------------------------------- |
| JSON         | `MappingJackson2HttpMessageConverter`    |
| XML          | `MappingJackson2XmlHttpMessageConverter` |

👉 Converts request → Java object (`UserDTO`)

---

### 🔹 3. Controller Logic Runs

```java
user.setName(user.getName().toUpperCase());
```

---

### 🔹 4. Response Conversion

Based on `Accept` header:

| Accept           | Output |
| ---------------- | ------ |
| application/json | JSON   |
| application/xml  | XML    |
