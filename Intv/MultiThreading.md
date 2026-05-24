### 1. Is a thread lighter than a process and can exist without it?

- Actually, a process is an instance of a program that is being executed. When a program runs, the operating system creates a process to manage its execution.
- For example, When we open Microsoft Word, it becomes a process in the operating system.
- Whereas, a thread is the smallest unit of execution within a process. A process can have multiple threads, which share the same resources but can run independently.
- A web browser like Google Chrome might use multiple threads for different tabs, with each tab running as a separate thread.
- So the answer is, Yes, thread is lighter then process and cannot exist without a process because it lives inside the process, for example, process is like a house and thread is like a room.

---

### 2. What are the different ways to create thread?

In older java, there were mainly two ways to create a thread:

1. Extending the Thread class

2. Implementing the Runnable interface

From Java 5 onwards, threads can also be created using the Executor Service framework along with Callable.

From Java 8 onwards, threads can be created using lambda expressions, which make the code more concise and readable.

---

### 3. Difference between extending thread and implementing runnable and when to use each?

In Extending Thread

- When we extend Thread, our class becomes a thread.
- We put our code in the run() method and start it using start().
- But the limitation is that our class cannot extend any other class because
- Java allows only one class to be extended.

When to use: Simple demos or learning purposes. Very small programs where inheritance is not needed.

In Implementing runnable:

- When we implement Runnable, our class represents a task, not the thread itself.
- We put logic in run() and pass it to a Thread object. Here our class can still extend another

When to use: In Real-world applications. When our class already extends another class.

---

### 4. Can a class extend Thread and implement Runnable together?

Yes, a class can extend Thread and also implement Runnable at the same time, but it makes no real sense.

Why?

Thread already implements Runnable. So if your class extends Thread, it automatically becomes a Runnable. Implementing Runnable again adds nothing.

---

### 5. Why is Runnable preferred in real-world applications?

Runnable is preferred in real-world applications because it gives better design and flexibility.

When we use Runnable, our class only defines what work to do, not how to run the thread. This keeps things clean and reusable. Our class can still extend another class, which is very common in real projects.

---

### 6. What happens internally when we call run() instead of start()?

When we call `run()` directly, no new thread is created.

What actually happens:
- The `run()` method runs like a normal method call.
- It runs on the same thread that called it (usually the main thread).
- Java does not create a separate thread or do any scheduling.

When we call `start()`:
- Java creates a new thread.
- That new thread then calls `run()` internally.

---

### 7. Can one Runnable instance be used by multiple threads?

Yes, one Runnable instance can be used by multiple threads. Meaning we can create one object that has the `run()` code, and then give that same object to many Thread objects.

---

### 8. What happens if start() is called twice on the same thread?

If we call start() twice on the same thread, Java throws a runtime error.

What exactly happens is:
- The first `start()` works and creates a new thread.
- The second `start()` causes an `IllegalThreadStateException`.
- Why this happens: A thread in Java can be started only once. After it is started (or finished), it cannot be restarted.

---

### 9. Explain the lifecycle of thread?

A thread in Java goes through different states from start to end.

This is called the thread lifecycle.

1) `New`: The thread is created, but not started yet. Example: Thread += new Thread():

2) `Runnable`: When we call start(), the thread becomes runnable. It is ready to run and waiting for CPU time.

3) `Running`: The thread is actually executing its code (means run() method). (This state is managed by the CPU scheduler.)

4) `Blocked / Waiting`: The thread is temporarily not running.

This happens when:
- It is waiting for a lock
- It calls sleep() or wait()
- It is waiting for another thread to
- finish (join())

5) `Terminated` (Dead): The thread finishes its work or stops due to an error. It cannot be restarted.

---

### 10. Can a thread re-enter RUNNABLE after TERMINATED?

- No, a thread cannot re-enter RUNNABLE after it is TERMINATED.
- Once a thread finishes its run() method or ends due to an error, it goes into the TERMINATED (dead) state. At this point, the thread's life is completely over.
- Java does not allow restarting a dead thread. If we try to call start() again, Java throws an exception.

---

### 11. If two threads are trying to update a counter variable in a getCounter method so what will happen and how could we solve that?

If two threads try to update the same counter at the same time, a race condition can happen.

What will happen:
- Both threads may read the same old value of the counter, increase it, and write it back. Because this happens at the same time, one update can overwrite the other. So the counter value becomes wrong or inconsistent.

How to solve it:
- We could use synchronized so only one thread can update the counter at a time.
- Or use AtomicInteger, which safely updates the value without locking.

Or use locks (Reenterent Lock, etc)

---

### 12. How can we use synchronized keyword?

1st is `synchronized method`: We can add synchronized keyword before method name and lock the entire method

```java
synchronized void increment() {
    count++;
}
```

2nd in `synchronized block`: we can create a synchronized block by curly braces and lock will be there in that particular block

```java
synchronized (this) (
    count++;
}
```

3rd is `static synchronized`: here we lock the class not the object. We add synchronized keyword after static keyword and before method name

```java
static synchronized void update() { 
    // Locked on Class object 
}
```

### 13. Is synchronized applied to code or to objects?

- synchronized is applied to objects, not directly to code.
- When you mark a method or a block as synchronized, the code gets a lock on an object before it runs. Only one thread can hold that object's lock at a time.
- For an instance synchronized method, the lock is on the current object.
- For a static synchronized method, the lock is on the Class object.

---

### 14. What happens if an exception occurs inside a synchronized block?

If an exception occurs inside a synchronized block, the lock is automatically released. Let's understand step by step:
- A thread enters the synchronized block and gets the lock.
- An exception occurs inside the block.
- The thread exits the block because of the exception.
- Java releases the lock automatically.

---

### 15. Can synchronization guarantee thread ordering?

No, synchronization does NOT guarantee thread ordering.

What synchronization does:
- It makes sure only one thread at a time enters the critical section. (A critical section is a part of code that accesses shared data and must not be executed by more than one thread at the same time like counter)
- It protects shared data from corruption.

What it does not do:
- It does not decide which thread goes first.
- It does not control the execution order of threads.

Thread scheduling is decided by the CPU and JVM, not by synchronized.

---

### 16. What are the limitations of synchronization?

synchronized helps keep data safe, but it also has some limitations. Main limitations are:

1. Performance issue: Only one thread can enter at a time, so other threads wait. This can slow down the application when many threads are used.
2. No fairness guarantee: synchronized does not guarantee which waiting thread will get the lock first. Some threads may wait longer.
3. Risk of deadlock: If locks are taken in the wrong order, threads can wait forever for each other.
4. Blocking nature: Waiting threads are blocked and cannot do any other work.
5. Hard to control: You cannot try to get a lock, set timeouts, or interrupt a thread waiting for a synchronized lock.

Because of the limitations of synchronized, ReentrantLock was introduced in Java 5.

---

### 17. What is reeantrant lock and why do we need it?

A ReentrantLock is a more powerful locking mechanism in Java that controls access to shared code or data.

What "reentrant" means: The same thread can lock the same lock again and again without getting stuck. It can also unlock the same number of times.

Why we need it: ReentrantLock was introduced to solve problems of synchronized.

It allows:

- More control over locking and unlocking
- Ability to try to get a lock without waiting forever (By tryLock())
- Setting fairness so threads get the lock in order
- Waiting with time limits
- Using multiple condition variables

---

### 18. How reeantract lock works?

- We can use the `lock()` method to lock the critical section and use `unlock()` to release the lock once our work is done.
- We could also use `tryLock()`, it does not block forever. If the lock is already held by another thread, it immediately returns `false`, so the thread can do something else instead of waiting.

---

### 19. How does tryLock() prevent deadlocks?

`tryLock()` helps prevent deadlocks by not forcing a thread to wait forever for a lock.

What normally causes deadlock: Two threads hold different locks and wait for each other's lock. Both wait forever.

How tryLock() helps:
- A thread tries to get the lock.
- If the lock is not available, it fails immediately (or after a short time).
- The thread can then release other locks, retry later, or do something else.
- Because the thread does not block forever, circular waiting is avoided.

---

### 20. What happens if you forget to unlock?

If we forget to call `unlock()`, the lock is never released.

What happens then:
- Other threads will wait forever to get the lock.
- The program may appear stuck or frozen.
- This can lead to deadlock or starvation.
- The only thread that can release the lock has already finished or moved on

---

### 21. When should you prefer ReentrantLock over synchronized?

We should prefer ReentrantLock over synchronized when we need more control over locking.

Use ReentrantLock when:
- We don't want threads to wait forever (use tryLock() or timeout).
- We need fairness, so threads get the lock in order.
- We want to lock and unlock at different places in code,
- We want better handling in complex multithreading scenarios.

Use synchronized when:
- The logic is simple.
- We want automatic lock handling.
- Readability and safety matter more than flexibility.

---

### 22. What is volatile keyword ?

`volatile` is used to make sure that when one thread changes a variable, all other threads see the latest value immediately. It avoids the problem where threads use old or cached values.

Each thread keep its own local copy of variables in CPU cache or registers but volatile read values directly from main memory

Lets take an example:
- Imagine we have music player app with a Play Thread that keeps playing songs in a loop. There is also a Stop button handled by the main thread.
- Both threads share a variable called stopPlaying.
- Play Thread keeps checking stopPlaying.
- When the user clicks Stop, the main thread sets stopPlaying = true.
- If stopPlaying is not volatile, Play Thread may keep using an old cached value and continue playing forever.
- If stopPlaying is volatile, Play Thread immediately sees the updated value and stops playing.

---

### 23. Why doesn't volatile guarantee atomicity?

Atomicity means an operation happens in one single step.

Many operations (like count++) are actually multiple steps: read value increase → write back.

volatile only guarantees visibility, not that these steps happen together. So two threads can still read the same value and update it at the same time, causing wrong results.

---

### 24. Can volatile fix race conditions?

No, volatile cannot fix race conditions.

What volatile does:
- Makes sure all threads see the latest value (that's called visibility).
- What a race condition needs:

Atomicity (means only one thread updates at a time).

---

### 25. What is AtomicClass and How do Atomic classes work internally (CAS)?

Atomic classes (like `AtomicInteger`, `AtomicLong`) are special Java classes that let multiple threads update shared values safely without using locks.

They are used when many threads need to change a value at the same time, like counters.

How Atomic classes work internally (CAS)

Atomic classes use a technique called CAS (Compare-And-Swap).

Simple idea of CAS:
1. Read the current value
2. Check if it is still the same as expected
3. If yes update it
4. If no try again

If another thread changes the value in between, the update fails and CAS retries.

---

### 26. Why are Atomics faster than synchronized?

Atomic classes are faster than synchronized because they avoid blocking threads.

With synchronized:
- Threads wait and block if the lock is taken.
- Blocking and waking threads is slow and costly.

With Atomic classes:
- Threads do not block.
- They use CAS (Compare-And-Swap) to retry instead of waiting.
- If an update fails, the thread just tries again.
- So less waiting, no lock management, and fewer context switching.

---

### 27. What is executer framework and why do we need it?

The Executor Framework is a Java feature that manages threads for us. Instead of creating and starting threads manually, we submit tasks, and Java decides which thread runs them and when.

Why we need it: Creating threads again and again is slow and hard to manage. We also have to handle how many threads to create, when to reuse them, and when to stop them.

The Executor Framework:
- Reuses threads (thread pool)
- Controls how many threads run at once
- Makes code cleaner and safer
- Improves performance and scalability.

---

### 28. Explain the concept of threadpool?

A thread pool is a group of ready-made threads that are reused to do multiple tasks.

Instead of creating a new thread every time a task comes, the thread pool gives the task to an existing idle thread. When the task is done, the thread goes back to the pool and waits for the next task.

Lets take an example of restaurant kitchen.
- The threads are the chefs.
- The thread pool is the set of chefs already hired.
- Orders are the tasks.
- When a customer places an order:
- The restaurant does not hire a new chef every time.
- An available chef takes the order and cooks it.
- After finishing, the chef becomes free and takes the next order.
- If all chefs are busy: New orders wait in a queue until a chef is free.

---

### 29. Who manages the thread lifecycle in executors?

The Executor Framework manages the entire thread lifecycle. It creates threads, assigns tasks to them, reuses them, and shuts them down when needed. You don't manage threads manually here.

---

### 30. Can executors reuse the same thread for different tasks?

Yes. Executors reuse the same thread to run many different tasks. After a task finishes, the thread goes back to the pool and waits for the next task.

---

### 31. What happens if the executor queue is full?

If both the thread pool is busy and the task queue is full, the executor rejects new tasks. This usually throws a `RejectedExecutionException` or follows a rejection policy (like running the task in the caller thread or discarding it).

---

### 32. Difference between submit() and execute()?

- `execute()` is used when we just want to run a task and don't care about any result. It takes a Runnable and returns nothing. If something goes wrong inside the task (an exception), it is thrown directly on the worker thread and usually ends up in logs via the thread's exception handler.
- Whereas whether `submit()` is used when we want more control over the task. It returns a Future, which lets us check whether the task is finished, cancel it, or get a result (if it's a Callable). If the task throws an exception, it doesn't crash the thread immediately-instead, the exception is stored and is thrown when we call future.get(). Because of this, submit() is safer and more flexible, and is generally preferred in real applications.

---

### 33. Difference between Runnable and Callable in Executors

In Java, when we use the Executor Framework, we don't directly tell a thread how to run. Instead, we tell it what work to do.

That "work" is represented using Runnable or Callable. So:

Executors manage threads.

`Runnable` and `Callable` represent the task that a thread will execute.

Both are used to define the code that runs inside a thread, especially when using ExecutorService and thread pools.

Taking about `Runnable`
- It does not return a result
- It cannot throw checked exceptions
- It is used for tasks that just perform work

In `Callable`
- It returns a result
- It can throw checked exceptions
- It is used when you need an outcome from the task.

---

### 34. Explain the types of thread executer?

There are multiple types of executers:


1. Fixed Thread Executor

    Here we define how many threads should work. For example, if we create executor like: `ExecutorService es = Executors.newFixedThreadPool(4);`
    
    - Here we have fixed that 4 threads will be in use.
    - It means at most 4 threads can run in parallel.
    - If more than 4 tasks are submitted, the extra tasks will wait in a queue until a thread becomes free.
    - Basically, it is a pool with a fixed number of threads.


2) Single Thread Executor

    Here we define that only one thread should work. For example, if we create executor like: `ExecutorService es = Executors.newSingleThreadExecutor();`
    
   - Here only one thread will be created.
   - It means only one task can run at a time.
   - All other tasks will wait in a queue and execute one by one.
   - Basically, it is a pool with exactly one thread, and execution is sequential.


3) Cached Thread Executor

    Here we do not define any fixed number of threads. For example, if we create executor like: `ExecutorService es = Executors.newCachedThreadPool();`

    In this executor:
     - New threads are created when needed
     - Idle threads are reused
     - Threads that stay idle for some time are destroyed
     - It means the number of threads can increase or decrease automatically based on load.
     - Basically, it is a dynamic thread pool.
     - If too many tasks come, it may create too many threads, which can cause performance or memory issues.


4) Scheduled Thread Executor

    This executor is used when we want to run tasks after a delay or repeatedly. For example, if we create executor like: `ScheduledExecutorService es = Executors.newScheduledThreadPool(2);`

    Here 2 threads are available for scheduled tasks. We can:
     - Run a task after some delay
     - Run a task at fixed time intervals
     - Basically, it is used for time-based tasks, like background jobs or periodic checks.


5) Fork Join Pool Executor

    This executor is used for parallel processing. For example, if we create executor like: `ForkJoinPool pool = new ForkJoinPool();`
    
    In this executor:
     - A big task is divided into smaller tasks
     - These tasks run in parallel
     - Idle threads can steal work from busy threads
     - Basically, it is useful for CPU-intensive tasks and divide-and-conquer problems.

---

### 35. What is CompletableFuture and how does it work?

Completable Future is used to run tasks asynchronously and chain multiple steps without blocking. How it works:

We start a task.
- While it runs, our main thread is free.
- When the task finishes, we can say "then do this", "then do that".

Simple example in words:
- Order food gets prepared → then deliver → then notify customer
- Each step runs automatically after the previous one finishes.

---

### 36. How do you handle exceptions in CompletableFuture?

Completable Future has built-in exception handling methods:
- `exceptionally()` → give a fallback value
- `handle()` → handle success or failure together
- `whenComplete()` → just log or observe errors

Simple idea: If something fails, we decide what to do next instead of crashing the program.

---

### 37. Can Completable Future cause thread starvation?

Yes, if used incorrectly. By default, Completable Future uses the Fork JoinPool common pool.

If: Tasks are blocking (sleep, I/O, DB calls)
- Too many long tasks run

Then: Threads get stuck. New tasks wait forever → starvation

Solution: Use a custom Executor for blocking tasks.

---

### 38. Why is CompletableFuture better than Future?

Problems with Future:
- We must call get() (blocks thread)
- No chaining
- Poor exception handling

Completable Future advantages:
- Non-blocking
- Task chaining (thenApply(), thenRun())
- Better exception handling
- Combine multiple tasks easily.

---

### 39. What are the common methods in CompletableFuture?

- Completable Future provides many methods to run and manage asynchronous tasks. 
- We can create async tasks using methods like `supplyAsync()` (when you need a result) and `runAsync()` (when no result is needed). 
- After a task completes, we can chain next actions using `thenApply()` to transform the result, `thenAccept()` to use the result, or `thenRun()` to run another task. 
- To work with multiple async tasks, methods like `thenCombine(), thenCompose(), allOf(), and anyOf()` are used. 
- Completable Future also offers strong exception handling through `exceptionally(), handle(), and whenComplete()`. 
- If needed, we can block and get the result using `get() or join()`. 
- It also provides control methods like `complete(), cancel(), and is Done()` to manually manage or check the task's state.

---

### 40. What is ForkJoinPool and its real example?

ForkJoinPool is a special thread pool designed for divide-and-conquer tasks.

How it works:
- Big task is split into smaller tasks (fork)
- Results are combined (join)
- Idle threads can steal work from busy threads

Real example:
- Searching a big array
- Parallel sorting
- Processing large data in chunks.
