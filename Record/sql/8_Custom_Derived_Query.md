Spring Data JPA mein **data fetch karne ke 3 main tareeke hote hain**:

1. **Derived Query (method name se)**
2. **Derived Query using @Query (custom JPQL/SQL likh ke)**
3. **Custom Implementation (khud logic likhna)**

Main teeno ko simple + real-life examples ke saath explain karta hoon 👇

---

# 🔷 1. Derived Query (Method Name se Query)

👉 Spring khud method name dekh ke query bana deta hai

### ✅ Example:

```java
List<User> findByName(String name);
```

👉 Ye internally banega:

```sql
SELECT * FROM user WHERE name = ?
```

---

### 🔥 Complex Example:

```java
List<User> findByNameAndAgeGreaterThan(String name, int age);
```

👉 Query:

```sql
SELECT * FROM user 
WHERE name = ? AND age > ?
```

---

### 📌 Common Keywords:

| Keyword     | Meaning     |
| ----------- | ----------- |
| findBy      | basic fetch |
| And / Or    | conditions  |
| GreaterThan | >           |
| LessThan    | <           |
| Like        | pattern     |
| In          | list        |
| Between     | range       |

---

### ❗ Limitation:

👉 Method name **bahut lamba aur complex ho jata hai**

```java
findByNameAndAgeGreaterThanAndStatusAndCity...
```

---

# 🔷 2. @Query (Custom Query)

👉 Jab derived query complex ho jaye → use `@Query`

---

## ✅ JPQL Example:

```java
@Query("SELECT u FROM User u WHERE u.name = :name")
List<User> getUsersByName(@Param("name") String name);
```

👉 Yahan:

* Table nahi → Entity name use hota hai
* Column nahi → field name use hota hai

---

## ✅ Native SQL Example:

```java
@Query(value = "SELECT * FROM user WHERE age > :age", nativeQuery = true)
List<User> getUsersAboveAge(@Param("age") int age);
```

---

## 🔥 Update Query (Important)

```java
@Modifying
@Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
int updateUserName(@Param("id") Long id, @Param("name") String name);
```

👉 Required:

* `@Modifying`
* `@Transactional`

---

## 🔥 Join Example:

```java
@Query("SELECT u FROM User u JOIN u.address a WHERE a.city = :city")
List<User> findUsersByCity(@Param("city") String city);
```

---

# 🔷 3. Custom Query (Manual Implementation)

👉 Jab:

* Bahut complex logic ho
* Dynamic filtering ho
* Multiple joins / conditions runtime pe decide ho

---

### Step 1: Custom Interface

```java
public interface UserRepositoryCustom {
    List<User> findCustomUsers(String name);
}
```

---

### Step 2: Implementation

```java
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findCustomUsers(String name) {
        String jpql = "SELECT u FROM User u WHERE u.name = :name";
        return em.createQuery(jpql, User.class)
                 .setParameter("name", name)
                 .getResultList();
    }
}
```

---

### Step 3: Use with Repository

```java
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
```

---

# 🔷 🧠 Interview Insight

👉 90% cases:

```
JpaRepository + Derived Query + @Query
```

👉 Custom implementation:

```
Only when really needed
```
