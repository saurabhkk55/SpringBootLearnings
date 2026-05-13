# Update Selected Fields in JPA using `@Query`

```java id="lrq5ck"
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.status = :status
        WHERE u.id = :id
    """)
    int updateStatus(Long id, String status);
}
```

---

# Default Behavior of `@Query`

By default, `@Query` is used for **SELECT operations**.

Example:

```java id="szwtok"
@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> findByEmail(String email);
```

Without `@Modifying`:

* JPA assumes query is a `SELECT`
* Query returns data from DB
* Hibernate performs read operation

So this works normally:

```java id="qavwjo"
@Query("SELECT u FROM User u")
List<User> findAllUsers();
```

---

# What Happens for UPDATE/DELETE?

If you write:

```java id="gkqscw"
@Query("""
    UPDATE User u
    SET u.status = :status
    WHERE u.id = :id
""")
```

without `@Modifying`,
JPA still treats it like a SELECT query and throws exception like:

```text
QueryExecutionRequestException:
Not supported for DML operations
```

Because:

* UPDATE/DELETE are DML operations
* JPA needs explicit instruction using `@Modifying`

---

# Why `@Modifying` is Required?

`@Modifying` tells Spring Data JPA:

* This query modifies DB data
* Execute as UPDATE/DELETE
* Not a SELECT query

Example:

```java id="g3slq9"
@Modifying
@Query("""
    UPDATE User u
    SET u.status = :status
    WHERE u.id = :id
""")
```

---

# Important Annotations

| Annotation       | Purpose                                   |
| ---------------- | ----------------------------------------- |
| `@Query`         | Custom JPQL/SQL query                     |
| `@Modifying`     | Required for UPDATE/DELETE queries        |
| `@Transactional` | Executes query inside transaction         |
| `@Param`         | Binds method parameter to query parameter |

---

# Updating Multiple Selected Fields

```java id="uh7t4r"
@Modifying
@Transactional
@Query("""
    UPDATE User u
    SET u.name = :name,
        u.email = :email
    WHERE u.id = :id
""")
int updateUser(Long id, String name, String email);
```

---

# Native SQL Query

```java id="1bflh1"
@Modifying
@Transactional
@Query(
   value = """
       UPDATE users
       SET status = :status
       WHERE id = :id
   """,
   nativeQuery = true
)
int updateStatusNative(Long id, String status);
```

---

# Why Use This Instead of `save()`?

Using `save()`:

```java id="mh5s6r"
User user = repo.findById(id).get();

user.setStatus("ACTIVE");

repo.save(user);
```

Problem:

* Fetches complete entity first
* Hibernate dirty checking occurs
* More DB interaction

Using `@Query UPDATE`:

* Direct DB update
* Better performance
* Useful for partial updates

---

# Important Interview Point

`@Modifying` queries bypass Hibernate dirty checking because entity is updated directly in DB.

So:

* Persistence context may contain stale data
* Hibernate cache may not reflect latest DB value immediately

Use:

```java id="w4r4rk"
@Modifying(clearAutomatically = true)
```

Example:

```java id="sfl7m7"
@Modifying(clearAutomatically = true)
@Transactional
@Query("""
    UPDATE User u
    SET u.status = :status
    WHERE u.id = :id
""")
int updateStatus(Long id, String status);
```

This automatically clears persistence context after update.
