# Java `volatile` Keyword – Quick Revision Notes

### 1. Definition

- `volatile` is a Java keyword used in multithreading.
- It ensures that changes made to a shared variable by one thread are visible to other threads.
- It provides visibility and ordering guarantees, but not atomicity for compound operations.

### 2. Syntax

```
private volatile boolean running = true;
private volatile int count = 0;
```

- Applicable to instance and static variables.
- Cannot be applied to local variables.
- Cannot be applied to methods or classes.

### 3. Why do we use `volatile`?

- To ensure threads observe updated values of shared variables.
- To prevent threads from indefinitely using stale values.
- To avoid explicit synchronization when only visibility and appropriate ordering are required.
- Commonly used for status flags, shutdown flags, and configuration flags.

### 4. Real-Life Example – Stop a Background Thread

```
class Worker {

    private volatile boolean running = true;

    public void start() {
        while (running) {
            // Perform background task
        }
    }

    public void stop() {
        running = false;
    }
}
```

How it works:

1. Thread 1 executes `start()` and continuously checks `running`.
2. Thread 2 calls `stop()` and changes `running` to `false`.
3. Because `running` is volatile, Thread 1 observes the updated value.
4. Thread 1 exits the loop.

Without `volatile` or another synchronization mechanism, Thread 1 is not guaranteed to observe the change.

### 5. Important Properties

- Visibility: Changes to a volatile variable become visible to other threads.
- Atomicity: Individual volatile reads and writes are atomic, including `long` and `double`, but compound operations are not.
- Ordering: A volatile write happens-before a subsequent volatile read of the same variable in the synchronization order.
- Thread safety: `volatile` alone does not make an entire class or compound operation thread-safe.
- Lock-free: Reading or writing a volatile variable does not require acquiring a Java monitor lock.

### 6. Why is `volatile` not suitable for compound operations?

Compound operations involve multiple steps: Read → Modify → Write.

```
private volatile int count = 0;

public void increment() {
    count++;  // Not atomic
}
```

Internally:

```
int temp = count;
temp = temp + 1;
count = temp;
```

Two threads may read the same value and overwrite each other's updates, causing a race condition.

Solution: Use `AtomicInteger` or `synchronized`.

```
private final AtomicInteger count = new AtomicInteger(0);

public void increment() {
    count.incrementAndGet();
}
```

### 7. When to use and avoid `volatile`

| Use `volatile`                                | Avoid relying only on `volatile`  |
| --------------------------------------------- | --------------------------------- |
| Shutdown flags                                | `count++` or `count--`            |
| Running/stopped status                        | Check-then-update operations      |
| Single-writer configuration flags             | Multiple related variable updates |
| Publishing immutable configuration references | Banking transactions              |

### 8. `volatile` vs `synchronized`

| `volatile`                               | `synchronized`                                               |
| ---------------------------------------- | ------------------------------------------------------------ |
| Keyword for variables                    | Keyword for methods or blocks                                |
| Guarantees visibility                    | Guarantees visibility and mutual exclusion                   |
| No locking                               | Uses monitor locking                                         |
| Does not make compound operations atomic | Can make compound operations atomic when correctly protected |
| Suitable for simple flags                | Suitable for critical sections                               |

### 9. Interview-Ready Definition

`volatile` is a Java keyword that ensures visibility of shared-variable updates across threads and provides ordering guarantees. It is suitable for simple reads and writes but does not guarantee atomicity for compound operations such as `count++`. For compound operations, we should use Atomic classes or synchronization.

Remember: `volatile` = Visibility + Ordering, not compound-operation atomicity.
