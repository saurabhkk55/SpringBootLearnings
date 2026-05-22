What happens if we modify the source collection while the stream is being processed?

- If we modify the source collection while the stream is running, it can cause problems like `ConcurrentModificationException` or unexpected results. 
- Streams expect the source data to remain unchanged during processing, so modifying it during execution is not recommend

---

Can a Stream pipeline run without a terminal operation?

- No, a stream pipeline cannot run without a `terminal` operation. 
- Intermediate operations like `filter()` or `map()` only build the pipeline, but they do not process the data. 
- The stream actually starts processing only when a terminal operation like `forEach()`, `collect()`, or `count()` is called.

---

What is Parallel Stream and How does Java decide how many threads to use in a parallel stream?

- A Parallel Stream is a stream that processes data using multiple threads at the same time to improve performance. 
- We can create it using `parallelStream()` or `stream().parallel()`. 
- Java usually decides the number of threads based on the number of CPU cores available in the system.

---

Why can using Parallel Streams sometimes make performance worse instead of better?

- Parallel streams can be slower when the task is very small, because creating and managing multiple threads adds extra overhead. 
- They can also be slower when there is too much synchronization, shared data, or complex operations, which reduces the benefit of parallel processing.

---

What kind of operations are not suitable for Parallel Streams?

- Operations that modify shared variables, depend on order, are not suitable for parallel streams. 
- Also, very small or lightweight tasks are not good candidates because the overhead of parallelism can reduce performance.

---

Parallel Streams internally use which thread pool? Can it affect other tasks?

- Parallel streams internally use the Fork Join `Pool.commonPool()`. 
- Since this pool is shared across the application, heavy parallel stream tasks can occupy the threads and affect other tasks that also depend on the same pool.

---

If a stream operation depends on previous results, should we use Parallel Streams? Why?

- No, we should avoid Parallel Streams in this case. 
- Parallel streams process elements simultaneously in different threads, so the order of execution is not guaranteed. 
- If an operation depends on the result of the previous element, parallel processing can give incorrect or unpredictable results.

---

When should we prefer sequential streams over parallel streams?

- We should prefer sequential streams when the data size is small, when the operations are simple, or when the logic depends on order or shared data. 
- In these cases, sequential streams are simpler, safer, and sometimes even faster than parallel streams.

---

Why does Java allow multiple default methods in a functional interface but only one abstract method?

- Java allows multiple default methods because they already have an implementation, so they do not affect the main purpose of the interface. 
- But only one abstract method is allowed because lambda expressions need one clear method to implement.

---

Can a Functional Interface extend another interface? What happens if both have abstract methods?

- Yes, a Functional Interface can extend another interface.
- But if both interfaces together result in more than one abstract method, then it will no longer be a functional interface and cannot be used with lambda expressions.

---

Give some examples of Functional Interface ?

Some common examples of functional interfaces in Java are `Runnable`, `Callable`, `Comparator`, and `interfaces from java.util.function` like `Predicate`, `Function`, `Consumer`, and `Supplier`.

---

Why does Comparator qualify as a functional interface even though it has many methods?

- Comparator is still a functional interface because it has only one abstract method, which is `compare()`.
- The other methods like `reversed()` or then `Comparing()` are `default` or `static` methods, so they already have implementations and do not break the functional interface rule.

---

What is the difference between Predicate, Function, Consumer, and Supplier conceptually?

- Conceptually, these interfaces represent different types of operations. 
- `Predicate` takes a value and returns true or false (used for conditions). 
- `Function` takes a value and returns another value after transformation. 
- `Consumer` takes a value and performs some action but returns nothing. 
- `Supplier` does not take any input but returns a value.

---

Why must variables use inside lambda expressions be final or effectively final?

- Variables used inside a lambda must be `final` or `effectively final` so their value does not change while the lambda is using them. 
- This helps avoid problems with unexpected value changes and thread safety.

---

What does "effectively final" mean?

- `Effectively final` means the variable is not declared with the `final keyword`, but its value is assigned only once and never changed later. 
- In such cases, Java treats it like a final variable.

---

Can lambda expressions access instance variables and static variables?

Yes, lambda expressions can access both `instance variables` and `static variables` of the class. These variables do not need to be final, unlike local variables.

---

What is a method reference and why do we need it?

- A method reference is a shorter way to call an existing method using lambda expressions. 
- Instead of writing a full lambda, we can directly refer to the method using `:: (colon colon symbol)` It helps make the code cleaner, shorter, and easier to read.

---

Can you please explain local variable 'var' introduced in java 10?

- var was introduced in Java 10 to make code shorter and cleaner. 
- It allows the compiler to automatically detect the variable type based on the value assigned. 
- For example, instead of writing `List<String> list = new ArrayList<>();`, we can write `var list = new ArrayList<String>();`. The type is still decided at compile time, it is just inferred automatically