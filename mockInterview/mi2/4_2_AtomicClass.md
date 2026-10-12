# Java Atomic Classes – Quick Revision Notes

### 1. Definition

- Atomic classes are thread-safe classes available in the `java.util.concurrent.atomic` package.
- They provide atomic operations on shared variables in a multithreaded environment.
- They ensure visibility and atomicity for their supported operations.
- Atomic operations execute as a single indivisible operation, preventing race conditions such as lost updates.
- They generally use CAS (Compare-And-Set) or other hardware-supported atomic operations instead of explicit locking.

### 2. Common Atomic Classes

| Atomic Class         | Purpose                                                |
| -------------------- | ------------------------------------------------------ |
| `AtomicInteger`      | Thread-safe integer operations                         |
| `AtomicLong`         | Thread-safe long operations                            |
| `AtomicBoolean`      | Thread-safe boolean operations                         |
| `AtomicReference<T>` | Thread-safe reference updates                          |
| `AtomicIntegerArray` | Atomic operations on individual integer array elements |

### 3. Syntax

```
import java.util.concurrent.atomic.AtomicInteger;

AtomicInteger count = new AtomicInteger(0);

// Get current value
count.get();

// Set a new value
count.set(10);

// Increment and return updated value
count.incrementAndGet();

// Return current value, then increment
count.getAndIncrement();

// Decrement and return updated value
count.decrementAndGet();

// Add 5 and return updated value
count.addAndGet(5);

// Compare expected value and update
count.compareAndSet(10, 20);
```

### 4. Why do we use Atomic Classes?

- To safely update shared variables accessed by multiple threads.
- To prevent race conditions and lost updates.
- To perform compound operations like increment and decrement atomically.
- To avoid explicit `synchronized` blocks for supported operations.
- To implement thread-safe counters, flags, and state transitions.

### 5. Real-Life Example – Counting API Requests

Suppose multiple threads are processing API requests in a Spring Boot application. We want to maintain an accurate request count.

```
import java.util.concurrent.atomic.AtomicInteger;

public class RequestCounter {

    private final AtomicInteger count =
            new AtomicInteger(0);

    public void processRequest() {

        int total = count.incrementAndGet();

        System.out.println(
            "Total requests: " + total
        );
    }

    public int getTotalRequests() {
        return count.get();
    }
}
```

How it works:

1. Multiple threads call `processRequest()` concurrently.
2. Each thread executes `incrementAndGet()`.
3. The counter is incremented atomically.
4. No increments are lost, even when multiple threads update the counter simultaneously.
5. `get()` returns the current counter value with the appropriate visibility guarantees.

Example: If 10 threads each increment the counter 100 times, the final count will be 1,000, assuming all operations complete successfully.

### 6. How do Atomic Classes work internally? – CAS

Atomic classes commonly use CAS (Compare-And-Set).

CAS performs three logical steps as one atomic operation:

1. Read the current value.
2. Compare it with the expected value.
3. Update to the new value only if both values match.

Syntax:

```
AtomicInteger count = new AtomicInteger(10);

boolean updated = count.compareAndSet(10, 20);

System.out.println(updated);    // true
System.out.println(count.get()); // 20
```

Here:

- Current value = `10`
- Expected value = `10`
- New value = `20`
- Since current and expected values match, CAS updates the value to `20` and returns `true`.

If another thread has already changed the value to `15`, the same CAS operation fails and returns `false`.

Important: CAS is atomic. If multiple threads attempt the same CAS operation simultaneously, only a thread whose expected value matches the current value can successfully update it.

### 7. Important Properties

- Atomicity: Supported operations such as `incrementAndGet()` execute atomically.
- Visibility: Atomic variables provide memory visibility guarantees similar to volatile variables for their standard read/write operations.
- Thread safety: Individual atomic operations are thread-safe.
- Non-blocking: Standard atomic operations generally use lock-free algorithms rather than monitor locks.
- CAS support: Methods such as `compareAndSet()` allow conditional updates without explicit synchronization.
- Limitations: Multiple separate atomic operations are not automatically atomic as a group.

### 8. Why use `AtomicInteger` instead of `volatile int`?

Consider:

```
private volatile int count = 0;

public void increment() {
    count++; // Not atomic
}
```

`count++` involves:

```
int temp = count;  // Read
temp = temp + 1;   // Modify
count = temp;      // Write
```

Two threads can read the same value and overwrite each other's updates.

Solution:

```
private final AtomicInteger count =
        new AtomicInteger(0);

public void increment() {
    count.incrementAndGet();
}
```

Here, the increment operation is atomic, preventing lost updates.

### 9. Real-Life Example – `AtomicBoolean`

Suppose only one thread should process a particular task at a time.

```
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskProcessor {

    private final AtomicBoolean processing =
            new AtomicBoolean(false);

    public void processTask() {

        if (!processing.compareAndSet(false, true)) {
            System.out.println("Task already running");
            return;
        }

        try {
            System.out.println("Processing task...");
            // Perform task
        } finally {
            processing.set(false);
        }
    }
}
```

How it works:

1. Initially, `processing = false`.
2. Multiple threads attempt `compareAndSet(false, true)`.
3. Only one thread successfully changes the value to `true`.
4. Other threads fail the CAS check and skip processing.
5. After processing, the flag is reset to `false`.

This prevents concurrent execution of the same task within that Java object.

### 10. When to Use and Avoid Atomic Classes

| Use Atomic Classes            | Avoid relying only on Atomic Classes               |
| ----------------------------- | -------------------------------------------------- |
| Thread-safe counters          | Multi-variable transactions                        |
| Atomic increment/decrement    | Complex critical sections                          |
| Task execution flags          | Operations requiring several atomic steps together |
| CAS-based state transitions   | Distributed locking across microservices           |
| Thread-safe reference updates | Database transactions                              |

### 11. Atomic Classes vs `volatile` vs `synchronized`

| Feature                             | Atomic Classes         | `volatile`       | `synchronized`              |
| ----------------------------------- | ---------------------- | ---------------- | --------------------------- |
| Visibility                          | Yes                    | Yes              | Yes                         |
| Atomic increment                    | Yes                    | No               | Yes                         |
| CAS support                         | Yes                    | No               | Not directly                |
| Explicit locking                    | No                     | No               | Yes                         |
| Multiple operations atomic together | No, not automatically  | No               | Yes, if correctly protected |
| Best suited for                     | Counters, flags, state | Visibility flags | Complex critical sections   |

### 12. Interview-Ready Definition

Atomic classes are thread-safe classes provided by the `java.util.concurrent.atomic` package that support atomic operations on shared variables without requiring explicit synchronization. They ensure visibility and atomicity for supported operations and commonly use CAS (Compare-And-Set) internally to prevent race conditions.

Remember:

- `volatile` = Visibility + Ordering
- Atomic classes = Visibility + Atomicity for supported operations
- `synchronized` = Visibility + Mutual Exclusion
- CAS = Compare current value with expected value and update atomically if they match

Key interview point: Atomic classes make individual supported operations thread-safe, but a sequence of multiple atomic operations is not automatically thread-safe as one transaction.