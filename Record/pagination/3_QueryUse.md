`query` ka use advanced query customization ke liye hota hai.

```java
(root, query, criteriaBuilder)
```

Yahaan:

* `root` → table/entity ko represent karta hai
* `criteriaBuilder` → conditions banane ke liye
* `query` → final SQL query ko customize karne ke liye

Abhi tumhare example mein `query` use nahi ho raha because simple filtering ho rahi hai.

But real projects mein iska use hota hai for:

---

# 1. DISTINCT Results

Duplicate records avoid karne ke liye:

```java
query.distinct(true);
```

Example:

```java
return (root, query, cb) -> {
    query.distinct(true);

    return cb.equal(root.get("name"), "Saurabh");
};
```

---

# 2. Sorting (ORDER BY)

```java
query.orderBy(criteriaBuilder.desc(root.get("age")));
```

Example:

```java
query.orderBy(cb.asc(root.get("name")));
```

SQL:

```sql
ORDER BY name ASC
```

---

# 3. Join Fetch (Avoid N+1 Problem)

Suppose:

```java
User -> Department
```

You can fetch join:

```java
root.fetch("department");
```

or

```java
query.distinct(true);
```

with joins.

---

# 4. Group By

```java
query.groupBy(root.get("age"));
```

---

# 5. Having Clause

```java
query.having(cb.gt(cb.count(root), 1));
```

---

# 6. Select Specific Columns

Normally JPA full entity laata hai.

But query se projection kar sakte ho.

---

# Why query mostly unused?

Most simple Specifications only need:

* root
* criteriaBuilder

because sirf WHERE clause banana hota hai.

---

# Real Understanding

Think:

```java
root             -> FROM user
predicates       -> WHERE condition
query            -> Full SQL customization
criteriaBuilder  -> SQL operators generator
```

---

# Your Current Example Generates Something Like

```sql
SELECT *
FROM user
WHERE name = ?
AND age = ?
AND mobile = ?
```

Yahaan `query` ki zarurat nahi padi.

---

# Interview Line

You can say:

> `query` object is used to customize the overall SQL query such as distinct, sorting, joins, group by, having, and projections in JPA Criteria API Specifications.
