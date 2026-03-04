## 1️⃣ Sabse pehle: Problem kahan hoti hai?

DB read slow hota hai kyunki:

* Table **bahut bada** hai
* **Index nahi** laga
* **Zyada data** fetch ho raha hai (unwanted columns)
* **N+1 query** problem
* **Network / serialization** overhead
* **Repeated same query** (no cache)

---

## 2️⃣ Indexing – 80% performance yahin se aati hai

### Rule:

👉 **WHERE, JOIN, ORDER BY, GROUP BY** ke columns par index

### Example:

```sql
SELECT * FROM orders WHERE user_id = 101;
```

Agar `user_id` pe index nahi → **full table scan ❌**

```sql
CREATE INDEX idx_orders_user_id ON orders(user_id);
```

✔️ Result:

* Disk I/O kam
* Lookup fast (B-Tree)

📌 Interview line:

> "Index reduces table scan and improves read performance drastically."

---

## 3️⃣ Sirf wahi columns lao jo chahiye

### ❌ Galat:

```sql
SELECT * FROM users;
```

### ✅ Sahi:

```sql
SELECT id, name, email FROM users;
```

Spring Boot (JPA Projection):

```java
interface UserView {
    Long getId();
    String getName();
}
```

✔️ Kam:

* Memory usage
* Network data
* Serialization time

---

## 4️⃣ Pagination – ek baar mein sab mat uthao

### ❌ Galat:

```sql
SELECT * FROM orders;
```

### ✅ Sahi:

```sql
SELECT * FROM orders LIMIT 20 OFFSET 0;
```

Spring Data:

```java
Page<User> users = userRepo.findAll(PageRequest.of(0, 20));
```

✔️ UI fast
✔️ DB load kam

---

## 5️⃣ N+1 Query Problem avoid karo (bahut common)

### ❌ Problem:

```java
List<Order> orders = orderRepo.findAll();
for(Order o : orders){
    o.getUser().getName(); // Har order ke liye new query 😡
}
```

### ✅ Solution:

```java
@Query("SELECT o FROM Order o JOIN FETCH o.user")
List<Order> findAllWithUser();
```

OR

```java
@EntityGraph(attributePaths = "user")
List<Order> findAll();
```

✔️ Single query
✔️ Fast read

---

## 6️⃣ Cache lagao – repeat data ke liye

### Scenario:

* Same user profile
* Same product list

### Spring Cache:

```java
@Cacheable("users")
public User getUser(Long id) {
    return userRepo.findById(id).orElseThrow();
}
```

✔️ DB hit = 0
✔️ Millisecond response

Popular caches:

* Redis
* Ehcache
* Caffeine

---

## 7️⃣ Read Replicas (High load system)

### Architecture:

```
Write → Master DB
Read  → Read Replica
```

✔️ Read load distribute
✔️ Scaling easy

Used in:

* Amazon
* Flipkart
* Zomato type systems

---

## 8️⃣ Connection Pooling (must-have)

Spring Boot default: **HikariCP**

Tune it:

```yaml
spring.datasource.hikari.maximum-pool-size: 20
spring.datasource.hikari.minimum-idle: 5
```

✔️ New connection banane ka time bachta
✔️ Throughput improve

---

## 9️⃣ Batch reads (IN query)

### ❌ Galat:

```sql
SELECT * FROM users WHERE id = 1;
SELECT * FROM users WHERE id = 2;
```

### ✅ Sahi:

```sql
SELECT * FROM users WHERE id IN (1,2,3);
```

✔️ Less DB round-trips

---

## 🔥 Real-world Priority Order (Yaad rakh)

1. **Proper Index**
2. **Avoid SELECT ***
3. **Pagination**
4. **Fix N+1**
5. **Cache**
6. **Connection Pool**
7. **Read Replica**

---

## 🧠 streams.One-liner interview answer:

> “Fast DB read ke liye indexing, limited column fetch, pagination, N+1 avoidance, caching aur connection pooling use karte hain.”
