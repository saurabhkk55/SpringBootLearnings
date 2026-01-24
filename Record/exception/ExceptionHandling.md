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

## 🎯 Use-case

Poore application ke liye **centralized error handling**

---

## 📄 `ApiError.java`

(Standard response structure)

```java
package com.example.demo.exception;

import java.time.LocalDateTime;

/**
 * Common error response format
 */
public class ApiError {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // getters & setters
}
```

---

## 📄 `GlobalExceptionHandler.java`

```java
package com.example.demo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Global handler for ALL controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(OrderNotFoundException ex) {

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Fallback handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error"
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

### 🧠 Flow

```
ANY Controller
   ↓
Service throws exception
   ↓
GlobalExceptionHandler
   ↓
Consistent JSON response
```

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
