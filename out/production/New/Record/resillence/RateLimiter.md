# Resilience4j RateLimiter – Complete Documentation

## 1. Problem Statement (Real‑world Scenario)

Humare paas **do microservices** hain:

* **Source Microservice** – jo API calls bhejti hai
* **Target Microservice** – jo requests process karti hai

Production systems me common problems:

* Bahut zyada requests **short time me** aa jaati hain
* Target microservice overload ho jaati hai
* CPU / DB / downstream services pe pressure badhta hai
* Eventually service **temporarily down** ho sakti hai

Is problem ko solve karne ke liye hum **Resilience4j RateLimiter Pattern** use karte hain.

---

## 2. Rate Limiter Pattern – Core Concept

**Rate Limiter ka matlab**:

* Ek fixed **time window** ke andar
* Sirf **limited number of requests** allow karna

Simple language me:

> "Har time interval me sirf itni hi calls process hongi, baaki reject ya wait karengi."

---

## 3. RateLimiter vs Bulkhead (High‑level Difference)

* **RateLimiter** → time ke basis par requests control karta hai
* **Bulkhead** → concurrency (parallel calls) ke basis par control karta hai

Important difference:

* RateLimiter logically **one‑by‑one permit** deta hai
* Bulkhead **parallel execution** allow karta hai (limited threads)

---

## 4. RateLimiter Kahan Implement Karte Hain?

✅ **RateLimiter hamesha SOURCE microservice pe lagta hai**

Reason:

* Resilience4j client‑side library hai
* Caller apni outgoing request rate control karta hai

Flow:

```
Source Microservice
   ↓ RateLimiter (limit request rate)
Target Microservice
```

Interview line:

> "RateLimiter is applied at the caller side to control request rate over time."

---

## 5. RateLimiter Configuration (Example)

```yaml
resilience4j.ratelimiter:
  instances:
    testRateLimiter:
      limitForPeriod: 2
      limitRefreshPeriod: 5s
      timeoutDuration: 0
```

---

## 6. RateLimiter Configuration Keys – Detailed Explanation

### 6.1 `limitForPeriod`

```yaml
limitForPeriod: 2
```

* Ek time window me **maximum kitni calls allowed** hongi
* Ye number strict hota hai

Example:

* limitForPeriod = 2
  → Sirf 2 requests allowed per interval

---

### 6.2 `limitRefreshPeriod`

```yaml
limitRefreshPeriod: 5s
```

* Time interval jiske baad rate limiter **reset** hota hai
* Har interval me fresh permits milte hain

Example:

* Har 5 seconds me counter reset
* Next 5 seconds me fir se 2 calls allowed

---

### 6.3 `timeoutDuration`

```yaml
timeoutDuration: 0
```

* Extra request **kitni der wait karegi** permit milne ke liye
* Ye waiting next refresh period tak ho sakti hai

Values ka behaviour:

#### Case 1: `timeoutDuration = 0`

* Extra calls **immediately reject** ho jaati hain
* Direct fallback method execute hota hai

#### Case 2: `timeoutDuration > 0` (e.g. 5s)

* Extra calls wait karti hain
* Agar next refresh period me permit mil gaya → request execute
* Nahi mila → request reject + fallback

---

## 7. RateLimiter – Step‑by‑Step Execution Flow

Assume:

* limitForPeriod = 2
* limitRefreshPeriod = 5s
* timeoutDuration = 0

### Scenario: 5 requests ek saath aayi

```
Request 1 → allowed
Request 2 → allowed
Request 3 → rejected (fallback)
Request 4 → rejected (fallback)
Request 5 → rejected (fallback)
```

---

### Same Scenario with `timeoutDuration = 5s`

```
Request 1 → allowed
Request 2 → allowed
Request 3 → waits (up to 5s)
Request 4 → waits (up to 5s)
Request 5 → waits (up to 5s)
```

Next interval start hota hai:

* Agar permits mil jaate hain → next 2 requests execute
* Baaki timeout hone par reject + fallback

---

## 8. Blocking vs Non‑Blocking Behaviour

* **Spring MVC / Feign / RestTemplate**:

    * RateLimiter **blocking** hota hai
    * Thread wait karta hai `timeoutDuration` tak

* **WebClient / WebFlux**:

    * RateLimiter **non‑blocking** hota hai
    * Event‑loop block nahi hota

---

## 9. RateLimiter Kab Use Karna Chahiye?

RateLimiter best hai jab:

* Traffic spikes predictable ho
* Fair usage enforce karni ho
* Downstream service sensitive ho

Examples:

* Public APIs
* Login / OTP endpoints
* Third‑party integrations

---

## 10. RateLimiter Kab Use Nahi Karna Chahiye?

Avoid RateLimiter jab:

* Pure concurrency issue ho (Bulkhead better)
* Long‑running calls ho
* Already strong back‑pressure implemented ho

---

## 11. RateLimiter + Bulkhead + Retry (Best Practice)

Production‑ready flow:

```
RateLimiter
   ↓
Bulkhead
   ↓
Retry
   ↓
Circuit Breaker
   ↓
Fallback
```

---

## 12. Key Interview Points (Must Remember)

* RateLimiter time‑based limiting karta hai
* Bulkhead concurrency‑based limiting karta hai
* `limitForPeriod` strict hota hai
* `timeoutDuration = 0` → instant reject
* Caller side pe implement hota hai

---

## 13. One‑Line Summary

> **Resilience4j RateLimiter controls how many requests are allowed in a given time window to protect downstream services from traffic spikes.**
