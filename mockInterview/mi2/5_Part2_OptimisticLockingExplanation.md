That is exactly the core idea of Optimistic Locking.

Both threads CAN read the same version simultaneously.

The conflict is detected during UPDATE time, not READ time.

---

Example:

Initial DB row:

| id | balance | version |
| -- | ------- | ------- |
| 1  | 1000    | 1       |

---

# Step 1 — Both Threads Read Same Version

Thread-1 reads:

```text id="r9mxp0"
balance = 1000
version = 1
```

Thread-2 also reads:

```text id="q5k7a9"
balance = 1000
version = 1
```

This is completely allowed.

No lock is applied.

---

# Step 2 — Both Try Updating "At Same Time"

Thread-1 executes:

```sql id="u4vk6r"
UPDATE account
SET balance = 900,
    version = 2
WHERE id = 1
AND version = 1;
```

---

Thread-2 executes:

```sql id="5dglf0"
UPDATE account
SET balance = 800,
    version = 2
WHERE id = 1
AND version = 1;
```

---

# What Actually Happens Internally?

DB processes updates atomically.

Only ONE update succeeds first.

Suppose Thread-1 wins.

DB row becomes:

| id | balance | version |
| -- | ------- | ------- |
| 1  | 900     | 2       |

---

Now Thread-2 query becomes:

```sql id="g5d2j5"
WHERE version = 1
```

But current DB version is already:

```text id="a5z4xj"
2
```

So:

```text id="3ayf4y"
0 rows updated
```

Meaning:

```text id="lhlsut"
Conflict detected
```

---

# Important Understanding

Optimistic Locking does NOT prevent simultaneous reads.

It prevents:

```text id="25pypm"
Lost updates
```

by validating version during update.

---

# In Hibernate/JPA

Hibernate checks:

```text id="iuh3xg"
Rows affected count
```

If:

```text id="n7w7xm"
0 rows updated
```

Hibernate throws:

```java id="x2b8kl"
OptimisticLockException
```

---

# Then What Should Application Do?

Usually:

## Retry

```text id="i9t7e6"
1. Read latest data again
2. Apply changes
3. Retry update
```

OR

## Show Conflict Error

Example:

```text id="zfjrv2"
Record modified by another user
Please refresh and try again
```

---

# Important Interview Point

In Optimistic Locking:

```text id="h71g7u"
Multiple threads may read same version simultaneously
```

but during update:

```text id="pl4k9n"
version check ensures only one update succeeds
```

thus preventing race conditions and lost updates.
