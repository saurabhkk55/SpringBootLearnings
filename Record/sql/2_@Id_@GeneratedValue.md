# 1️⃣ What is `@Id`?

`@Id` is a **JPA annotation** that marks a field as the **primary key** of the entity.

👉 Primary key means:

- Uniquely identifies each record in a table
- Cannot be null
- Must be unique

### Example

```java
@Entity
public class User {

    @Id
    private Long id;

    private String name;
}
```

Here:

* `id` is the primary key column in DB.

If you don’t mark any field with `@Id` → **JPA throws error** because it doesn't know which field is the identifier.

---

# 2️⃣ Why we use `@GeneratedValue` with `@Id`?

If you use only `@Id`, **you must manually set the id value** before saving.

Example:

```java
User user = new User();
user.setId(101L); // must set manually
repo.save(user);
```

But most applications want **database to generate IDs automatically** (auto-increment).

So we use:

```java
@Id
@GeneratedValue
private Long id;
```

Now:

- You don’t set ID manually
- DB or JPA provider generates it automatically

---

# 3️⃣ What happens internally?

When you save entity:

```java
repo.save(user);
```

Hibernate:

1. Sees `@GeneratedValue`
2. Uses configured strategy
3. Generates ID
4. Inserts row

---

# 4️⃣ Types of `@GeneratedValue` strategies

```java
@GeneratedValue(strategy = GenerationType.X)
```

### Common Strategies

| Strategy | Meaning                 | Used When          |
| -------- | ----------------------- | ------------------ |
| AUTO     | Provider decides        | Default            |
| IDENTITY | DB auto-increment       | MySQL, PostgreSQL  |
| SEQUENCE | DB sequence             | Oracle, PostgreSQL |
| TABLE    | Uses table to store IDs | Rare               |

Example:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

---

# 5️⃣ Why use both together?

Because they solve **two different problems**:

| Annotation      | Purpose                      |
| --------------- | ---------------------------- |
| @Id             | Declares primary key         |
| @GeneratedValue | Tells how value is generated |

👉 Think like:

* `@Id` = **"This is the ID column"**
* `@GeneratedValue` = **"Generate it automatically"**

---

# 6️⃣ What if you don’t use `@GeneratedValue`?

Then:

* JPA expects ID from you
* If null → error on save

---

# 7️⃣ Real-world Best Practice

Most projects use:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

Why?

* Simple
* DB handles increment
* Works well with most relational DBs

---

✅ **Simple Summary**

> `@Id` defines the primary key.
> `@GeneratedValue` tells JPA to auto-generate it.
> Together they ensure every row has a unique automatically generated ID.

---

✔️ **Interview Tip Answer**
If asked:

> Why do we use @GeneratedValue with @Id?

Say:

> Because @Id marks the primary key, and @GeneratedValue automatically generates unique values for it, so we don’t need to set IDs manually and avoid duplication errors.
