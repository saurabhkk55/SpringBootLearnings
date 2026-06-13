Yes. Even if the `try` block has a `return` statement, the `finally` block **still executes before the method actually returns**.

### Example

```java
public int test() {
    try {
        System.out.println("Try");
        return 10;
    } finally {
        System.out.println("Finally");
    }
}
```

**Output:**

```text
Try
Finally
```

**Returned value:** `10`

---

### Important: `finally` can change the return value

```java
public int test() {
    try {
        return 10;
    } finally {
        return 20;
    }
}
```

**Returned value:** `20`

The return in `finally` overrides the return from `try`.

---

### Interview Rule

Execution order:

```java
try {
    return value;
} catch (...) {
    return value;
} finally {
    // Always executes
}
```

The `finally` block runs:

* After `try`
* After `catch`
* Even when there is a `return` in `try` or `catch`

**Exception:** `finally` may not run if the JVM terminates abruptly, e.g.:

```java
System.exit(0);
```

or a JVM crash.

---

If the `catch` block has a `return`, the `finally` block **still executes before the method returns**.

### Example

```java
public int test() {
    try {
        int x = 10 / 0;
        return 10;
    } catch (Exception e) {
        System.out.println("Catch");
        return 20;
    } finally {
        System.out.println("Finally");
    }
}
```

**Output:**

```text
Catch
Finally
```

**Returned value:** `20`

---

### If `finally` also has a return

```java
public int test() {
    try {
        int x = 10 / 0;
        return 10;
    } catch (Exception e) {
        return 20;
    } finally {
        return 30;
    }
}
```

**Returned value:** `30`

The `return` in `finally` overrides both the `try` and `catch` returns.

---

### Quick Interview Summary

| Scenario                             | Return Value    |
| ------------------------------------ | --------------- |
| `try` returns, `finally` no return   | `try` value     |
| `catch` returns, `finally` no return | `catch` value   |
| `try` returns, `finally` returns     | `finally` value |
| `catch` returns, `finally` returns   | `finally` value |

**Best Practice:** Avoid `return` statements inside `finally`. They make code harder to understand and can suppress exceptions or override intended return values.

