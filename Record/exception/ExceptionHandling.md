Bhai 🔥
Ab ye **proper Spring Boot–style mini project** samajh — **har use-case ke liye alag controller + service**, jaise real enterprise codebase hota hai.

Main **4 alag scenarios** bana raha hoon, **same project ke andar**, taaki confusion zero ho.

---

# 📁 Complete Project Structure (REALISTIC)

```
spring-exception-demo
│
├── src/main/java/com/example/demo
│
│   ├── DemoApplication.java
│
│   ├── model
│   │   └── User.java
│
│   ├── exception
│   │   ├── UserNotFoundException.java
│   │   ├── OrderNotFoundException.java
│   │   ├── ApiError.java
│   │   └── GlobalExceptionHandler.java
│
│   ├── user
│   │   ├── UserController.java
│   │   └── UserService.java
│
│   ├── order
│   │   ├── OrderController.java
│   │   └── OrderService.java
│
│   └── product
│       ├── ProductController.java
│       └── ProductService.java
```

---

# 1️⃣ Custom Exception banana & use karna

(Alag Controller + Service)

## 🎯 Use-case

User exist nahi karta → **custom exception throw**

---

## 📄 `UserNotFoundException.java`

```java
package com.example.demo.exception;

/**
 * Custom exception for user not found scenario
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
```

👉 `RuntimeException` isliye:

* Spring automatically propagate karta hai
* `throws` likhne ka jhanjhat nahi

---

## 📄 `UserService.java`

```java
package com.example.demo.user;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getUserById(Long id) {

        // Dummy DB logic
        if (id <= 0) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        return new User(id, "Saurabh");
    }
}
```

---

## 📄 `UserController.java`

```java
package com.example.demo.user;

import com.example.demo.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
```

👉 Abhi exception handle nahi ho rahi — sirf **throw** ho rahi hai

---

# 2️⃣ Method-level / Class-level Exception Handling

(Alag Controller + Service)

## 🎯 Use-case

Order related exception **sirf OrderController ke andar handle karni hai**

---

## 📄 `OrderNotFoundException.java`

```java
package com.example.demo.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
```

---

## 📄 `OrderService.java`

```java
package com.example.demo.order;

import com.example.demo.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public String getOrder(Long id) {

        if (id == 0) {
            throw new OrderNotFoundException("Order not found");
        }

        return "Order-" + id;
    }
}
```

---

## 📄 `OrderController.java`

### (Method-level exception handling)

```java
package com.example.demo.order;

import com.example.demo.exception.OrderNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    /**
     * Handles ONLY OrderNotFoundException
     * Scope: this controller only
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderException(OrderNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
```

### 🧠 Important

* Ye handler **sirf OrderController** ke liye valid
* User/Product controllers pe apply nahi hoga

---

# 3️⃣ Global Exception Handling (BEST PRACTICE 🔥)

## 1️⃣ Problem kya hoti hai bina GlobalExceptionHandler ke?

Socho tumhare paas 10 REST APIs hain:

* `/users`
* `/orders`
* `/payments`

Agar har controller me tum ye likhne lago:

```java
try {
   // logic
} catch(Exception e) {
   return ResponseEntity.status(500).body("Something went wrong");
}
```

👉 Code **repeat** ho jata hai
👉 Response **inconsistent** ho jata hai
👉 Controller **messy** ho jata hai

---

## 2️⃣ GlobalExceptionHandler kya karta hai? (Simple definition)

> **GlobalExceptionHandler** ek **central place** hai jahan tum
> poore application ki **exceptions handle** karte ho.

📌 Matlab:

* Controller clean
* Error response consistent
* Business logic alag, error handling alag

---


## 4️⃣ Core Annotations (Most Important)

| Annotation          | Meaning                              |
| ------------------- | ------------------------------------ |
| `@ControllerAdvice or @RestControllerAdvice` | Batata hai ye **global handler** hai |
| `@ExceptionHandler` | Kaunsi exception handle hogi         |
| `@ResponseStatus`   | HTTP status set karta hai            |

---

## 5️⃣ Step-by-Step Code Example

### 📌 Step 1: Custom Exception banao

```java
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

👉 Jab user nahi milega, ye exception throw karenge

---

### 📌 Step 2: Controller me exception throw karo

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public String getUser(@PathVariable int id) {

        if (id != 1) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        return "User Found";
    }
}
```

❌ Yahan **try-catch nahi** likha
✅ Clean controller

---

### 📌 Step 3: GlobalExceptionHandler banao

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }
}
```

---

## 6️⃣ Flow kaise kaam karta hai? (Important)

1. Request aayi → `/users/5`
2. Controller ne exception throw ki
3. Spring dekhta hai:

    * Kya koi `@ControllerAdvice` hai? ✅
4. Matching `@ExceptionHandler` milta hai
5. Wahi se response return hota hai

📌 Controller ko pata bhi nahi chalta

---

## 7️⃣ Proper Error Response (Best Practice)

Production me sirf string return mat karo
👉 **Standard error response banao**

### ErrorResponse class

```java
import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // getters
}
```

---

### Updated GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

---

## 8️⃣ Sample API Response

```json
{
  "message": "User not found with id: 5",
  "status": 404,
  "timestamp": "2026-02-02T14:30:12"
}
```

✔ Clean
✔ Consistent
✔ Frontend-friendly

---

## 9️⃣ Common Exceptions jo tum handle kar sakte ho

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
@ExceptionHandler(NullPointerException.class)
@ExceptionHandler(IllegalArgumentException.class)
@ExceptionHandler(HttpMessageNotReadableException.class)
```

---

## 🔥 Interview Line (Use this)

> “GlobalExceptionHandler helps in centralized exception handling using `@ControllerAdvice`, keeping controllers clean and providing consistent error responses across the application.”

---

# 4️⃣ `@ControllerAdvice` vs `@RestControllerAdvice`

## 🔹 `@ControllerAdvice`

```java
@ControllerAdvice
public class MyAdvice {
}
```

👉 Used with:

* MVC apps
* JSP / Thymeleaf
* Returns **View names**

---

## 🔹 `@RestControllerAdvice` ✅

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
}
```

👉 Combination of:

```
@ControllerAdvice + @ResponseBody
```

👉 Used for:

* REST APIs
* JSON responses
* Microservices

---

## 🧠 Interview One-liner

> **REST API? → `@RestControllerAdvice`
> MVC App? → `@ControllerAdvice`**

---

# 🔚 Final Mental Model (Yaad rakhne layak)

```
Controller
   ↓
Service
   ↓
Exception
   ↓
(Local handler OR Global handler)
   ↓
HTTP Response
```

---

Agar bole to next level pe le ja sakta hoon:

* ✅ `@Valid` + validation exceptions
* ✅ Spring Security exception handling
* ✅ Error codes + enums
* ✅ Real DB (JPA) exceptions

Bas bol bhai 😎
