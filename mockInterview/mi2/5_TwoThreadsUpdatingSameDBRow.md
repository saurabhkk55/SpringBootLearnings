# 2 Threads Updating Same DB Row

Example:

```text
Thread-1 -> updating account balance
Thread-2 -> updating same account balance
```

Without proper locking:

* one update may overwrite another
* data inconsistency occurs
* lost update problem happens

This is called:

```text
Race Condition
```

---

# Two Main DB Locking Approaches

1. Optimistic Locking
2. Pessimistic Locking

---

# 1. Optimistic Locking

Assumption:

```text
Conflicts are rare
```

So:

* multiple transactions can read same row
* no lock initially
* conflict checked only during update

---

# How It Works

A special version field is maintained.

Example table:

| id | name    | balance | version |
| -- | ------- | ------- | ------- |
| 1  | Saurabh | 1000    | 1       |

---

## Flow

### Step 1

Thread-1 reads:

```text
balance = 1000
version = 1
```

Thread-2 also reads:

```text
balance = 1000
version = 1
```

---

### Step 2

Thread-1 updates successfully:

```sql
UPDATE account
SET balance = 900,
    version = 2
WHERE id = 1
AND version = 1;
```

1 row updated.

---

### Step 3

Thread-2 tries update:

```sql
UPDATE account
SET balance = 800,
    version = 2
WHERE id = 1
AND version = 1;
```

But DB version already became `2`.

So:

```text
0 rows updated
```

meaning conflict detected.

---

# In JPA / Hibernate

Use:

```java
@Version
```

Example:

```java
@Entity
class Account {

    @Id
    private Long id;

    private int balance;

    @Version
    private Long version;
}
```

Hibernate automatically handles version checking.

---

# If Conflict Happens

Hibernate throws:

```java
OptimisticLockException
```

Then application can:

* retry
* show error
* reload latest data

---

# Advantages of Optimistic Locking

* better performance
* high concurrency
* no row blocking
* scalable

---

# Disadvantages

If conflicts happen frequently:

* many retries
* transaction failures

---

# Best Use Cases

Use when:

* reads are high
* writes are less frequent
* conflicts are rare

Example:

* e-commerce product view
* profile update
* booking systems with moderate concurrency

---

# 2. Pessimistic Locking

Assumption:

```text
Conflicts are likely
```

So row is locked immediately.

Other transactions must wait.

---

# How It Works

Thread-1 locks row:

```sql
SELECT * FROM account
WHERE id = 1
FOR UPDATE;
```

Now:

```text
Thread-2 cannot update same row
```

until Thread-1 commits/rolls back.

---

# In JPA

Use:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

Example:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT a FROM Account a WHERE a.id = :id")
Account findByIdForUpdate(Long id);
```

---

# Behavior

```text
Thread-1 -> acquires lock
Thread-2 -> waits
```

After Thread-1 completes:

```text
Thread-2 proceeds
```

---

# Advantages of Pessimistic Locking

* strong consistency
* prevents concurrent modification completely
* no retry logic needed

---

# Disadvantages

* thread waiting/blocking
* lower concurrency
* deadlock possibility
* poor scalability under heavy load

---

# Best Use Cases

Use when:

* conflicts are frequent
* critical financial transactions
* inventory deduction
* banking systems

---

# Optimistic vs Pessimistic Locking

| Feature           | Optimistic         | Pessimistic                  |
| ----------------- | ------------------ | ---------------------------- |
| Lock Initially    | No                 | Yes                          |
| Performance       | Better             | Lower                        |
| Scalability       | High               | Lower                        |
| Conflict Handling | Retry              | Wait                         |
| Blocking          | No                 | Yes                          |
| Deadlock Risk     | Very Low           | Possible                     |
| Best For          | Read-heavy systems | Write-heavy critical systems |

---

# Important Interview Point

## Optimistic Locking

```text
Assume conflicts are rare
Detect conflict during update
```

using:

```java
@Version
```

---

## Pessimistic Locking

```text
Assume conflicts are common
Lock row immediately
```

using:

```sql
FOR UPDATE
```

or:

```java
LockModeType.PESSIMISTIC_WRITE
```
