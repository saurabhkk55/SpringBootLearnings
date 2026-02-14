# 🔑 Primary Key vs Unique Column

| Feature         | Primary Key              | Unique Column                        |
| --------------- | ------------------------ | ------------------------------------ |
| Uniqueness      | Must be unique           | Must be unique                       |
| Null allowed    | ❌ Not allowed            | ✅ Allowed (usually 1 null)           |
| Count per table | Only **one primary key** | Can have **multiple unique columns** |
| Purpose         | Main identifier of row   | Prevent duplicate values             |
| Index           | Automatically indexed    | Automatically indexed                |
| Use case        | ID column                | Email, phone, username               |

---

## 1️⃣ Primary Key — Main Identifier

Primary key is the **main identity** of a table row.

Example:

```sql
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(50)
);
```

Rules:

* Must be unique
* Cannot be null
* Only one per table

👉 Think of it as:

> Aadhaar number of a person — must exist and must be unique.

---

## 2️⃣ Unique Column — Duplicate Preventer

Unique constraint ensures **no two rows have same value**, but it is not the main identifier.

Example:

```sql
CREATE TABLE users (
    id INT PRIMARY KEY,
    email VARCHAR(100) UNIQUE
);
```

Rules:

* Must be unique
* Can be null
* Multiple unique columns allowed

👉 Think:

> Email must be unique, but it’s not the system’s main ID.

---

## 3️⃣ Multiple Unique vs Single Primary

Allowed:

```sql
email UNIQUE
phone UNIQUE
username UNIQUE
```

Not allowed:

```sql
PRIMARY KEY (id)
PRIMARY KEY (another_id) ❌
```

Only **one primary key constraint per table**.

---

## 4️⃣ Why Primary Key Cannot Be Null?

Because it identifies a row.

If null were allowed:

* row identity unknown
* duplicates possible
* relationships fail (foreign key references break)

---

## 5️⃣ Real-world Example (User Table)

| id | email                             | phone |
| -- | --------------------------------- | ----- |
| 1  | [a@gmail.com](mailto:a@gmail.com) | 999   |
| 2  | [b@gmail.com](mailto:b@gmail.com) | 888   |

* `id` → Primary key
* `email` → Unique
* `phone` → Unique

---

## 6️⃣ In JPA / Spring Boot

Primary key:

```java
@Id
private Long id;
```

Unique column:

```java
@Column(unique = true)
private String email;
```

---

## 🎯 Interview One-Line Difference

> Primary key uniquely identifies each row and cannot be null, while a unique column only prevents duplicate values and can allow null.
