# 🔐 Spring Security Internal Login Flow (Username & Password)

> **Scope of this document**
>
> * JWT ❌ NOT included
> * Sirf **form login / username-password based authentication**
> * Focus: **Spring Boot internally kya karta hai**
> * Clear explanation of:
    >
    >   * unauthenticated → authenticated flow
>   * `Authentication` object ka lifecycle
>   * `SecurityContext` kab aur kaise fill hota hai

Language: **Simple Hindi + real Spring classes**

---

## 🧠 CORE IDEA (1 Line)

> **Har request pehle UNAUTHENTICATED hoti hai.**
> **Sirf password verify hone ke baad Spring us request ko AUTHENTICATED banata hai.**

---

## 🧩 IMPORTANT INTERFACES & CLASSES (Cast of Characters)

| Role           | Class                                       |
| -------------- | ------------------------------------------- |
| Entry Filter   | `UsernamePasswordAuthenticationFilter`      |
| Auth Manager   | `AuthenticationManager` (`ProviderManager`) |
| Auth Logic     | `DaoAuthenticationProvider`                 |
| User Loader    | `UserDetailsService`                        |
| User Model     | `UserDetails` (`CustomUserDetails`)         |
| Context Holder | `SecurityContextHolder`                     |

---

## 🪜 COMPLETE INTERNAL FLOW (STEP BY STEP)

---

## 🔹 STEP 0: Client Request Comes In

```http
POST /login
username=saurabh
password=1234
```

📌 **Is moment par kya state hoti hai?**

* ❌ User authenticated nahi hai
* ❌ SecurityContext EMPTY hota hai
* ❌ Koi `Authentication` object context me nahi hota

👉 Matlab: **request UNAUTHENTICATED hai**

---

## 🔹 STEP 1: Security Filter Chain Intercepts Request

📍 Class:

```
SecurityFilterChain
```

Spring Security har incoming request ko filter chain se pass karta hai.

👉 Login request par next important filter active hota hai 👇

---

## 🔹 STEP 2: UsernamePasswordAuthenticationFilter

📍 Class:

```
UsernamePasswordAuthenticationFilter
```

Ye filter:

* `/login` request ko detect karta hai
* Username & password extract karta hai

### 🔸 YAHAN PEHLA Authentication OBJECT BANTA HAI

```java
UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

// OR

Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
```

📌 Is object ki state:

| Property      | Value             |
| ------------- | ----------------- |
| authenticated | ❌ false           |
| principal     | username (String) |
| credentials   | raw password      |
| authorities   | ❌ EMPTY           |

👉 **Isko kehte hain UNAUTHENTICATED Authentication**

---

## 🔹 STEP 3: AuthenticationManager Ko Call

```java
Authentication result = authenticationManager.authenticate(authRequest);
```

📍 Actual class:

```
ProviderManager
```

👉 Spring decide karta hai kaunsa provider handle karega

---

## 🔹 STEP 4: DaoAuthenticationProvider (CORE LOGIC)

📍 Class:

```
DaoAuthenticationProvider
```

Ye class **actual authentication ka kaam** karti hai.

---

### 🔸 STEP 4.1: Load User from DB

```java
UserDetails userDetails = userDetailsService.loadUserByUsername(username);
```

📌 Yahin:

* `CustomUserDetails` create hota hai
* User DB se load hota hai

❌ Agar username galat → `UsernameNotFoundException`

---

### 🔸 STEP 4.2: Password Verification

```java
passwordEncoder.matches(rawPassword, userDetails.getPassword());
```

❌ Agar password galat → `BadCredentialsException`

👉 Flow yahin break ho jata hai

---

## 🔹 STEP 5 🔥 AUTHENTICATED Authentication Object BANTA HAI

📍 Still inside:

```
DaoAuthenticationProvider
```

Password match hone ke baad Spring **NAYA Authentication object** banata hai:

```java
UsernamePasswordAuthenticationToken authenticatedToken =
        new UsernamePasswordAuthenticationToken(
            userDetails,              // principal
            null,                     // credentials removed
            userDetails.getAuthorities() // roles
        );
```

📌 Is object ki state:

| Property      | Value             |
| ------------- | ----------------- |
| authenticated | ✅ true            |
| principal     | CustomUserDetails |
| credentials   | ❌ null            |
| authorities   | ✅ present         |

👉 **Yahin request AUTHENTICATED banti hai**

---

## 🔹 STEP 6: SecurityContext Me Store

📍 Class:

```
SecurityContextHolder
```

```java
SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
```

📌 Ab Spring bolta hai:

> "Is request ke liye user authenticated hai"

---

## 🔹 STEP 7: Request Continues to Controller

Ab controller ke andar:

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
```

* `auth.isAuthenticated()` → ✅ true
* `auth.getPrincipal()` → `CustomUserDetails`

---

## ❌ FAILURE FLOW (VERY IMPORTANT)

### ❌ Case 1: Username Galat

* Exception: `UsernameNotFoundException`
* Authenticated object ❌ Nahi banta
* SecurityContext ❌ EMPTY

### ❌ Case 2: Password Galat

* Exception: `BadCredentialsException`
* Authenticated object ❌ Nahi banta
* SecurityContext ❌ EMPTY

📌 Result:

```
REQUEST REMAINS UNAUTHENTICATED
```

---

## 🧠 AUTHENTICATION OBJECT LIFECYCLE (ONE LOOK)

```
Request aayi
   ↓
Authentication (authenticated = false)
   ↓
Password verify
   ↓
Authentication (authenticated = true)
   ↓
Stored in SecurityContext
```

---

## 🎯 FINAL GOLDEN RULES

1️⃣ Har request pehle UNAUTHENTICATED hoti hai
2️⃣ `UsernamePasswordAuthenticationFilter` pehla Authentication banata hai (false)
3️⃣ `DaoAuthenticationProvider` final Authentication banata hai (true)
4️⃣ Sirf SUCCESS par SecurityContext fill hota hai
5️⃣ Failure par context EMPTY rehta hai

---

## 📌 Interview-Ready streams.One Liner

> "Spring Security first creates an unauthenticated Authentication token from username and password, and only after successful verification replaces it with an authenticated token stored in the SecurityContext."

---

✅ This document is intentionally **internal-focused**
➡️ Next logical doc: **Authorization & Role Check Flow (`@PreAuthorize`)**
