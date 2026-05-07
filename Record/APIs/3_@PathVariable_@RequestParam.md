# 🧩 1. Using @PathVariable

👉 Used when value is part of URL path

### Example:

```java
@RestController
@RequestMapping("/users")
public class UserController {

    // Example: GET /users/101
    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long userId) {
        return "User ID is: " + userId;
    }
}
```

### 🔍 Call:

```id="b7g1y2"
GET /users/101
```

👉 Output:

```id="d1f0rm"
User ID is: 101
```

---

# 🧩 2. Using @RequestParam

👉 Used for query parameters (`?key=value`)

### Example:

```java
@RestController
@RequestMapping("/users")
public class UserController {

    // Example: GET /users/search?name=Saurabh&age=25
    @GetMapping("/search")
    public String searchUser(
            @RequestParam("name") String name,
            @RequestParam("age") int age) {

        return "Name: " + name + ", Age: " + age;
    }
}
```

### 🔍 Call:

```id="nfv9v3"
GET /users/search?name=Saurabh&age=25
```

👉 Output:

```id="cydp8p"
Name: Saurabh, Age: 25
```

---

# 🧩 3. Optional RequestParam

```java
@GetMapping("/filter")
public String filterUser(
        @RequestParam(required = false) String city) {

    return "City: " + city;
}
```

### 🔍 Calls:

```id="7ykf4b"
GET /users/filter?city=Delhi
GET /users/filter
```

👉 If not passed → `city = null`

---

# 🧩 4. Default Value

```java
@GetMapping("/default")
public String defaultUser(
        @RequestParam(defaultValue = "Guest") String name) {

    return "Hello " + name;
}
```

---

# 🧩 5. Using Both Together

```java
// Example: GET /users/101/orders?status=delivered
@GetMapping("/{id}/orders")
public String getUserOrders(
        @PathVariable Long id,
        @RequestParam String status) {

    return "User: " + id + ", Order Status: " + status;
}
```

---

# 🧠 Key Difference

| Feature   | @PathVariable | @RequestParam         |
| --------- | ------------- | --------------------- |
| Location  | URL path      | Query string          |
| Mandatory | Usually yes   | Optional possible     |
| Example   | `/users/101`  | `/users?name=Saurabh` |

---

# 🎯 When to Use What?

👉 Use **@PathVariable** when:

* Resource identifier (id)
* RESTful design

👉 Use **@RequestParam** when:

* Filtering / searching
* Optional inputs

---

# 💡 Real-Life Example

```id="zptk7h"
GET /products/10                            → specific product
GET /products?category=mobile&sort=price    → filter
```

---

If you want, I can also show:
- ✅ POST API with @RequestBody
- ✅ Validation (@Valid)
- ✅ Real microservice controller design
