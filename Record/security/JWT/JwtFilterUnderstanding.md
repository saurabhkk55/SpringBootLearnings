# Question:
let say I send 3 request and each of them having valid JwtToken so how many times authentication object will be set into SecurityContextHolder?

---

## 🔹 Answer:

Spring Security is **stateless when using JWT**, which means:

JWT is used to implement stateless authentication where the server doesn’t store session data, and each request is self-contained with authentication information.

* Each HTTP request is **independent**
* `SecurityContextHolder` is **cleared after every request**
* Authentication is **re-created every time**

---

## 🔹 Flow for your scenario (3 requests)

### 🟢 Request 1

* JWT valid ✅
* `JwtFilter` runs
* Authentication object created
* Stored in `SecurityContextHolder`

➡️ Request completes → context cleared

---

### 🟢 Request 2

* Same process again
* New Authentication object created
* Stored again

➡️ Cleared again

---

### 🟢 Request 3

* Again same flow
* New Authentication object created

---

## 🔹 Important Insight

👉 It is **NOT reused**
👉 It is **re-created every time**

So:

| Request | Authentication Created? |
| ------- | ----------------------- |
| 1       | ✅                       |
| 2       | ✅                       |
| 3       | ✅                       |

👉 Total = **3 times**

---

## 🔹 Why does Spring do this?

Because of stateless design:

* No session storage
* No memory of previous requests
* Each request must **prove identity again**

---

## 🔹 Internal Mechanism (important for interviews)

* `SecurityContextHolder` uses **ThreadLocal**
* Each request runs on a **separate thread**
* Context exists **only for that request lifecycle**

---

## 🔹 One-liner (interview ready)

> “In JWT-based stateless authentication, the Authentication object is created and set into SecurityContextHolder for every request independently.”
