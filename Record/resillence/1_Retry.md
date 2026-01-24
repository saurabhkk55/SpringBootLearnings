# Resilience4j Retry – Complete Documentation

## 1. Problem Statement (Real-world Scenario)

Humare paas **do microservices** hain:

* **Source Microservice** – jo API call karti hai
* **Target Microservice** – jo request receive karti hai

Real production systems me ye issues common hote hain:

* Target service temporarily **down** ho sakti hai
* Target service pe **too many API calls** aa sakti hain
* Network glitch / pod restart / short maintenance

Agar source service **baar-baar direct call** karti rahe:

* Target pe unnecessary load badhta hai
* Threads waste hote hain
* Overall system unstable ho jata hai

Is problem ko solve karne ke liye hum **Resilience4j Retry Pattern** use karte hain.

---

## 2. Retry Pattern – Concept

**Retry Pattern ka matlab**:

* Agar koi API call fail ho jaaye
* Toh usse **limited number of times** dobara try kiya jaata hai
* Har retry ke beech **wait time diya jaata hai**, jo usually **exponentially increase** hota hai
* Agar saare retry attempts fail ho jaayein → **fallback response return kiya jaata hai**

Retry ka use **sirf temporary / transient failures** ke liye hota hai (jaise network glitch, short downtime), **permanent failures** ke liye nahi.

---

## 3. Resilience4j Retry – Core Behaviour

Resilience4j Retry me:

* **Initial call bhi count hoti hai**
* Retry tabhi hoti hai jab configured exception aaye. **Agar `retryExceptions` specify nahi kiye gaye, toh default behaviour me almost saare runtime failures par retry ho sakti hai**
* Agar kisi retry me success mil jaaye → wahi response return ho jata hai
* Remaining retries cancel ho jaati hain

---

## 4. Retry Configuration (YAML)

```yaml
resilience4j.retry:
  instances:
    testRetry:
      maxAttempts: 4
      waitDuration: 10s
      enableExponentialBackoff: true
      exponentialBackoffMultiplier: 2
      retryExceptions:
        - org.springframework.web.client.HttpServerErrorException
      ignoreExceptions:
        - java.lang.IllegalArgumentException
```

---

## 5. Configuration Parameters – Deep Explanation

### 5.1 maxAttempts

```yaml
maxAttempts: 4
```

* Total attempts = **initial call + retries**
* Iska matlab:

    * 1 initial API call
    * 3 retry attempts

Total = **4 attempts**

---

### 5.2 waitDuration

```yaml
waitDuration: 10s
```

* Ye **first failure ke baad ka wait time** hai
* Retry immediately nahi hoti

---

### 5.3 Exponential Backoff

```yaml
enableExponentialBackoff: true
exponentialBackoffMultiplier: 2
```

Har retry ke baad wait time multiply hota hai:

```
next_wait = previous_wait × multiplier
```

---

## 6. Actual Retry Flow (Step-by-Step)

Assume:

* maxAttempts = 4
* initial wait = 10s
* multiplier = 2

### Execution Flow:

```
Attempt 1 (Initial Call)
Source → Target ❌ fail
Wait: 10s

Attempt 2 (Retry 1)
Source → Target ❌ fail
Wait: 20s

Attempt 3 (Retry 2)
Source → Target ❌ fail
Wait: 40s

Attempt 4 (Retry 3)
Source → Target ❌ fail
(No more retries)
→ Fallback method executed
```

---

## 7. Success Scenario (Important)

Agar kisi bhi retry me:

```
Source → Target ✅ success
```

Toh:

* Turant response return ho jaata hai
* Remaining retries execute nahi hoti
* Wait time apply nahi hota

---

## 8. Thread Behaviour (Very Important for Interviews)

### Blocking Nature

* Retry **blocking hoti hai** (Spring MVC, Feign, RestTemplate)
* Jab tak retries chal rahi hoti hain:

    * Request thread occupied rehta hai
    * Client ko response nahi milta

Isliye retry count aur wait time **carefully choose** karna chahiye.

### Non-Blocking (Async) Note

* Agar **Spring WebFlux / WebClient** use kar rahe ho
* Toh retry **non-blocking** hoti hai
* Event-loop block nahi hota, sirf reactive pipeline delay hoti hai

Interview me ye bolna strong point hota hai.

---

## 9. Fallback Method

Fallback tab execute hoti hai jab:

* Saare retry attempts fail ho jaate hain

Fallback ka purpose:

* Graceful response dena
* System crash hone se bachana
* User ko controlled message dena

Example use-cases:

* Cached data return karna
* Default response
* Meaningful error message

---

## 10. RetryExceptions vs IgnoreExceptions

### retryExceptions

* Sirf in exceptions pe retry hoga
* Mostly 5xx / transient failures

### ignoreExceptions

* In exceptions pe retry **nahi** hoga
* Example:

    * Validation error
    * IllegalArgumentException

---

## 11. Retry Kab Use Karna Chahiye?

Retry **best hai** jab failure:

* Temporary ho
* Short-lived ho
* Recoverable ho

Examples:

* Network timeout
* Pod restart
* Temporary overload

---

## 12. Retry Kab Use Nahi Karna Chahiye?

Retry **avoid karo** jab:

* Service permanently down ho
* Business validation failure ho
* 4xx client errors ho

Is case me:
👉 **Circuit Breaker** use karna chahiye

---

## 13. Retry vs Circuit Breaker (Interview Gold)

| Retry                              | Circuit Breaker                     |
| ---------------------------------- | ----------------------------------- |
| Temporary failure handle karta hai | Permanent failure handle karta hai  |
| Limited attempts                   | Calls ko completely block karta hai |
| Blocking hota hai                  | System ko protect karta hai         |

Production me usually:

```
Retry → Circuit Breaker → Fallback
```

---

## 14. Key Interview Points (Must Remember)

* maxAttempts me initial call included hoti hai
* Retry blocking hoti hai
* Exponential backoff load ko kam karta hai
* Retry permanent failure ka solution nahi hai
* Retry + Circuit Breaker = best practice

---

## 15. One-Line Summary

> **Resilience4j Retry temporary failures ko safely handle karta hai by limited retries with controlled wait time, aur complete failure ke case me fallback provide karta hai.**
