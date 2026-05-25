# Soft Delete & Hard Delete in JPA

In JPA/Hibernate:

* **Soft Delete** → record remains in DB, but marked deleted
* **Hard Delete** → row physically removed from DB

---

# 1. Soft Delete using `@SQLDelete` + `@Where`

This is the most common approach in Hibernate.

### How it works

When:

```java
userRepository.deleteById(id);
```

Instead of running:

```sql
DELETE FROM users WHERE id = ?
```

Hibernate runs:

```sql
UPDATE users
SET is_deleted = true
WHERE id = ?
```

And while fetching data, deleted records are automatically filtered.

---

## Entity Example

```java
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "users")
@Getter
@Setter
@SQLDelete(sql = """
        UPDATE users
        SET is_deleted = true
        WHERE id = ?
        """)
@Where(clause = "is_deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "is_deleted")
    private boolean deleted = false;
}
```

---

## What happens internally?

### Delete operation

Code:

```java
userRepository.deleteById(1L);
```

Generated SQL:

```sql
UPDATE users
SET is_deleted = true
WHERE id = 1;
```

---

### Fetch operation

Code:

```java
userRepository.findAll();
```

Hibernate automatically adds:

```sql
SELECT *
FROM users
WHERE is_deleted = false;
```

You do not need to manually write:

```sql
WHERE is_deleted = false
```

because `@Where` handles it.

---

## DB Table

| id |    name | is_deleted |
| -- | ------: | ---------: |
| 1  | Saurabh |       true |
| 2  |    Amit |      false |

Only Amit will be returned.

---

## Limitation of `@Where`

Suppose admin wants to see deleted users.

Problem:

```java
userRepository.findAll()
```

still filters deleted rows.

You must write custom native query:

```java
@Query(value = "SELECT * FROM users", nativeQuery = true)
List<User> findAllIncludingDeleted();
```

---

# 2. Soft Delete using `@SoftDelete` (Hibernate 6+)

Newer Hibernate versions provide built-in support.

Much cleaner than `@SQLDelete`.

Instead of manually writing update query.

---

## Entity Example

```java
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Table(name = "users")
@Getter
@Setter
@SoftDelete
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
```

Hibernate automatically creates internal deleted indicator.

When:

```java
userRepository.deleteById(1L);
```

Hibernate behaves like soft delete.

---

## Custom column name

By default Hibernate creates a delete indicator.

You can customize it.

```java
@Entity
@SoftDelete(columnName = "is_deleted")
public class User {

    @Id
    private Long id;

    private String name;
}
```

Table:

| id | name    | is_deleted |
| -- | ------- | ---------- |
| 1  | Saurabh | true       |

---

## Benefits of `@SoftDelete`

✅ Less boilerplate code
✅ Cleaner than `@SQLDelete`
✅ Hibernate-managed implementation
✅ No custom SQL needed

---

## Drawback

❌ Requires newer Hibernate version (Hibernate 6+)

In many enterprise projects people still use:

```java
@SQLDelete + @Where
```

because of backward compatibility.

---

# 3. Hard Delete in JPA

Hard delete means physically removing row.

Default JPA behavior already does hard delete.

Example:

```java
userRepository.deleteById(1L);
```

Generated SQL:

```sql
DELETE FROM users WHERE id = 1;
```

or

```java
userRepository.delete(user);
```

Generated SQL:

```sql
DELETE FROM users WHERE id = ?;
```

No annotation needed.

---

## Example Entity

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;

    private String name;
}
```

Delete:

```java
userRepository.deleteById(1L);
```

Result:

Before:

| id | name    |
| -- | ------- |
| 1  | Saurabh |
| 2  | Amit    |

After:

| id | name |
| -- | ---- |
| 2  | Amit |

Record permanently deleted.

---

# Important Interview Question

### Can we do hard delete in soft delete setup?

Yes.

Example:

```java
@Modifying
@Transactional
@Query("""
        DELETE FROM User u
        WHERE u.id = :id
        """)
void hardDelete(Long id);
```

This bypasses soft delete and permanently removes row.

---

# Comparison

| Feature             | `@SQLDelete + @Where` | `@SoftDelete` | Hard Delete |
| ------------------- | --------------------: | ------------: | ----------: |
| Keeps data          |                   Yes |           Yes |          No |
| Physical delete     |                    No |            No |         Yes |
| Recovery possible   |                   Yes |           Yes |          No |
| Requires custom SQL |                   Yes |            No |          No |
| Hibernate version   |                   Any |  Hibernate 6+ |         Any |

---

## Interview Answer (Short)

> In JPA, hard delete is default behavior where `deleteById()` executes SQL `DELETE` and removes row permanently. For soft delete, we can use Hibernate `@SQLDelete` to replace delete with update and `@Where` to filter deleted records automatically. In Hibernate 6+, `@SoftDelete` provides cleaner built-in soft delete support without custom SQL.
