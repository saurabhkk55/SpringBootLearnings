# 🔹 HTTP OPTIONS Method – Kya Hai?

### 👉 Simple definition

**OPTIONS** method ka use hota hai ye jaanne ke liye:

> **Is URL / resource pe kaun-kaun se HTTP methods allowed hain?**

Matlab:

* GET allowed?
* POST allowed?
* PUT / PATCH / DELETE allowed?
* Headers allowed?
* CORS allowed?

---

## 🧠 Real-Life Analogy

Socho tum kisi building ke gate pe ho aur andar jaane se pehle pooch rahe ho:

> “Is gate se entry allowed hai?
> Kis time?
> Kis permission ke saath?”

👉 Ye kaam **OPTIONS** karta hai.

---

## 🔥 Most Common Use Case – CORS (Browser level)

Browser **actual request bhejne se pehle** ek request bhejta hai:

```http
OPTIONS /users/1
```

Isse kehte hain **CORS Preflight Request**

---

### 🔁 Browser kya poochta hai?

* `PUT` allowed hai?
* `PATCH` allowed hai?
* Custom headers allowed hain?
* Cross-origin request allowed hai?

---

### Example: Preflight Request

```http
OPTIONS /users/1
Origin: http://frontend.com
Access-Control-Request-Method: PUT
Access-Control-Request-Headers: Authorization
```

---

### Server ka Response

```http
HTTP/1.1 200 OK
Allow: GET, POST, PUT, PATCH, DELETE, OPTIONS
Access-Control-Allow-Origin: http://frontend.com
Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE
Access-Control-Allow-Headers: Authorization
```

✔️ Ab browser actual PUT/PATCH request bhejega

---

## 📌 OPTIONS ka kaam kya hota hai?

| Use               | Description                            |
| ----------------- | -------------------------------------- |
| Discover methods  | Kaunse HTTP methods allowed hain       |
| CORS preflight    | Cross-origin request allow hai ya nahi |
| Security          | Client ko boundary pata chalti hai     |
| API introspection | API capabilities check karna           |

---

## 🧑‍💻 Spring Boot Example

### Default behavior

Spring Boot automatically OPTIONS handle kar leta hai agar:

* `@RestController`
* `@RequestMapping` present ho

Browser ko response mil jata hai.

---

### Custom OPTIONS (rarely needed)

```java
@RequestMapping(value = "/users/{id}", method = RequestMethod.OPTIONS)
public ResponseEntity<Void> options() {

    return ResponseEntity.ok()
            .allow(HttpMethod.GET, HttpMethod.POST,
                   HttpMethod.PUT, HttpMethod.PATCH,
                   HttpMethod.DELETE, HttpMethod.OPTIONS)
            .build();
}
```

---

## ❌ OPTIONS kya nahi karta?

* ❌ Data modify nahi karta
* ❌ Business logic execute nahi karta
* ❌ DB hit karna zaroori nahi

---

## 🎯 Interview One-Liners

* **OPTIONS tells what HTTP methods are supported by a resource**
* **Used mainly for CORS preflight**
* **OPTIONS is safe & idempotent**
* **Browser automatically sends OPTIONS, not the developer**

---

## ⚠️ Common Interview Traps

### Q:

> Kya hume manually OPTIONS API banana padta hai?

👉 **Answer:**
❌ Nahi, Spring Boot / frameworks by default handle kar lete hain (especially for CORS).

---

## 🧠 PUT / PATCH / OPTIONS Relation

* PUT / PATCH se pehle browser **OPTIONS call karta hai**
* Agar OPTIONS fail → actual request block ho jati hai

---

## 📌 Final Summary

> **OPTIONS = Permission check before actual HTTP action**
