# Resilience4j Circuit Breaker – Complete Documentation

## 1. Problem Statement (Real‑world Scenario)

Humare paas **do microservices** hain:

* **Source Microservice** – jo API call karti hai
* **Target Microservice** – jo request serve karti hai

Production me aksar hota hai:

* Target service **temporarily ya permanently down** ho jaati hai
* Baar‑baar failed calls hone lagti hain
* Source microservice ke threads waste hone lagte hain
* Latency badhti hai aur **cascading failure** ho jaata hai

Agar source microservice fir bhi calls bhejti rahe:

* Koi fayda nahi hota
* System aur zyada unstable ho jaata hai

Is problem ko solve karne ke liye hum **Circuit Breaker Pattern** use karte hain.

---

## 2. Circuit Breaker Pattern – Core Concept

**Circuit Breaker ka matlab**:

* Jab kisi dependency me failures zyada ho jaayein
* Toh us dependency ke calls **temporarily block** kar do
* Aur direct **fallback response** return karo

Simple words me:

> "Agar service kaam nahi kar rahi, toh baar‑baar try mat karo."

---

## 3. Circuit Breaker Kahan Implement Karte Hain?

✅ **Circuit Breaker hamesha SOURCE (caller) microservice pe lagta hai**

Reason:

* Resilience4j client‑side library hai
* Caller hi decide karta hai ki call bhejni hai ya nahi

Flow:

```
Source Microservice
   ↓ Circuit Breaker
Target Microservice
```

Interview line:

> "Circuit Breaker is applied at the caller side to stop calls to unhealthy dependencies."

---

## 4. Circuit Breaker States (Most Important)

Circuit Breaker ke **3 main states** hote hain:

### 4.1 CLOSED State

* Normal state
* Saari calls target service tak jaati hain
* Failures track ki jaati hain

Transition:

* Jab failure rate threshold cross hota hai → **OPEN**

---

### 4.2 OPEN State

* Calls **block ho jaati hain**
* Target service ko koi call nahi jaati
* Direct fallback execute hota hai

Transition:

* `waitDurationInOpenState` ke baad → **HALF_OPEN**

---

### 4.3 HALF_OPEN State

* Limited number of test calls allow hoti hain
* Ye calls service ki health check karti hain

Result:

* Agar calls successful → **CLOSED**
* Agar fail → **OPEN**

---

## 5. Circuit Breaker Configuration (Example)

```yaml
resilience4j.circuitbreaker:
  instances:
    testCircuitBreaker:
      registerHealthIndicator: true
      slidingWindowType: COUNT_BASED
      slidingWindowSize: 10
      minimumNumberOfCalls: 5
      failureRateThreshold: 50
      waitDurationInOpenState: 5s
      permittedNumberOfCallsInHalfOpenState: 3
      automaticTransitionFromOpenToHalfOpenEnabled: true
      eventConsumerBufferSize: 10
```

---

## 6. Configuration Keys – Detailed Explanation

### 6.1 `registerHealthIndicator`

```yaml
registerHealthIndicator: true
```

* Actuator ke through circuit breaker ka health expose karta hai
* `/actuator/health` me circuit breaker status dikhta hai

---

### 6.2 `slidingWindowType`

```yaml
slidingWindowType: COUNT_BASED
```

* Failure calculation kis basis par hogi

Types:

* `COUNT_BASED` → last N calls
* `TIME_BASED` → last N time window

---

### 6.3 `slidingWindowSize`

```yaml
slidingWindowSize: 10
```

* Kitni calls ko consider karke failure rate calculate hoga

---

### 6.4 `minimumNumberOfCalls`

```yaml
minimumNumberOfCalls: 5
```

* Itni calls hone ke baad hi circuit breaker failure rate calculate karega
* Isse early open hone se bacha jaata hai

---

### 6.5 `failureRateThreshold`

```yaml
failureRateThreshold: 50
```

* Failure percentage jiske baad circuit OPEN ho jaata hai
* 50 ka matlab → 50% ya usse zyada failures

---

### 6.6 `waitDurationInOpenState`

```yaml
waitDurationInOpenState: 5s
```

* Circuit kitni der OPEN state me rahega
* Is time ke dauraan calls block rahengi

---

### 6.7 `permittedNumberOfCallsInHalfOpenState`

```yaml
permittedNumberOfCallsInHalfOpenState: 3
```

* HALF_OPEN state me kitni test calls allow hongi

---

### 6.8 `automaticTransitionFromOpenToHalfOpenEnabled`

```yaml
automaticTransitionFromOpenToHalfOpenEnabled: true
```

* OPEN → HALF_OPEN automatic transition enable karta hai
* Agar false ho toh manual trigger chahiye

---

### 6.9 `eventConsumerBufferSize`

```yaml
eventConsumerBufferSize: 10
```

* Circuit breaker events ka buffer size
* Metrics aur monitoring ke kaam aata hai

---

## 7. Circuit Breaker – Step‑by‑Step Execution Flow

Assume:

* slidingWindowSize = 10
* minimumNumberOfCalls = 5
* failureRateThreshold = 50%

Flow:

```
First 5 calls → metrics collect
Next 5 calls → failure rate calculate

Agar failures ≥ 50%
→ Circuit OPEN
→ Calls blocked

After 5s
→ HALF_OPEN
→ 3 test calls

If success → CLOSED
If failure → OPEN
```

---

## 8. Circuit Breaker vs Retry (Important Difference)

| Retry                               | Circuit Breaker                     |
| ----------------------------------- | ----------------------------------- |
| Temporary failures handle karta hai | Permanent failures handle karta hai |
| Limited attempts                    | Calls completely block karta hai    |
| Short‑term solution                 | System protection mechanism         |

---

## 9. Circuit Breaker Kab Use Karna Chahiye?

Use karo jab:

* Service frequently fail ho rahi ho
* Dependency unreliable ho
* Retry useless ho chuka ho

---

## 10. Circuit Breaker Kab Use Nahi Karna Chahiye?

Avoid karo jab:

* Failure rare ho
* Very fast retries possible ho
* Stateless lightweight calls ho

---

## 11. Circuit Breaker with Other Patterns (Best Practice)

Production‑ready order:

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

* Circuit breaker caller side pe lagta hai
* OPEN state me calls block hoti hain
* HALF_OPEN health check ke liye hota hai
* Failure rate ke basis par OPEN hota hai

---

## 13. One‑Line Summary

> **Resilience4j Circuit Breaker stops calls to failing dependencies to prevent cascading failures and stabilize the system.**

---

### Sliding Window Types (Very Important)

Circuit Breaker failure rate calculate karne ke liye **sliding window** use karta hai. Ye window decide karti hai kaun‑si recent calls ko consider kiya jayega.

Resilience4j me **2 types ke sliding window** hote hain:

#### 1️⃣ COUNT_BASED Sliding Window

```yaml
slidingWindowType: COUNT_BASED
slidingWindowSize: 10
````

**Meaning:**

* Circuit breaker last **N API calls** ko observe karta hai
* Window ka size **fixed** hota hai
* Window continuously **slide** hoti rehti hai

---

#### Important Clarification (Very Common Doubt)

❓ **Agar slidingWindowSize = 10 hai aur 10 ki 10 calls SUCCESS ho jaati hain, toh kya counting phir 1 se start hoti hai?**

✅ **Answer: Nahi. Counting reset nahi hoti. Window slide hoti hai.**

---

#### Step-by-Step Example

Initial 10 calls:

```
[S, S, S, S, S, S, S, S, S, S]
Failure Rate = 0%
Circuit = CLOSED
```

11th call aayi:

* Oldest call window se remove ho jaati hai
* New call window me add hoti hai

```
[S, S, S, S, S, S, S, S, S, S]
(Call 2 → Call 11)
```

📌 **Window ka size 10 hi rehta hai, reset nahi hota.**

---

#### Failure Case Example

Agar 11th call FAIL ho jaaye:

```
[S, S, S, S, S, S, S, S, S, F]
Failure Rate = 10%
```

* Failure rate threshold agar 50% hai → circuit CLOSED hi rahega

---

#### Key Takeaway

> **COUNT_BASED sliding window reset nahi hoti; wo oldest call drop karke new call add karti rehti hai.**

---

#### 2️⃣ TIME_BASED Sliding Window

```yaml
slidingWindowType: TIME_BASED
slidingWindowSize: 10s
```

**Meaning:**

* Last **N time window** ke andar aayi calls ko consider karta hai

**Example:**

* slidingWindowSize = 10 seconds
* Last 10 seconds me:

    * 8 calls aaye
    * 5 failed
    * 3 success

Failure Rate = 62.5%

Agar threshold 50% hai → circuit **OPEN** ho jayega.

📌 Isme calls ki count fixed nahi hoti, **time fixed hota hai**.

---

### COUNT_BASED vs TIME_BASED (Clear Difference)

| Feature              | COUNT_BASED     | TIME_BASED             |
| -------------------- | --------------- | ---------------------- |
| Based on             | Number of calls | Time window            |
| Predictability       | High            | Medium                 |
| Best for             | Stable traffic  | Burst / uneven traffic |
| Interview preference | ⭐ Most common   | ⭐⭐ Advanced            |

---

### Which One to Use?

✔️ Use **COUNT_BASED** when:

* Traffic steady hai
* Predictable API load

✔️ Use **TIME_BASED** when:

* Traffic bursty hai
* Peak hours me sudden spike aata hai

---