Yes, you can use **multiple `catch` blocks**, including one for a custom exception and another for `RuntimeException`.

### Example

```java
public class InvalidUserException extends RuntimeException {

    public InvalidUserException(String message) {
        super(message);
    }
}
```

Now catching it:

```java
public class Main {

    public static void main(String[] args) {

        try {

            throw new InvalidUserException("User not found");

        } catch (InvalidUserException e) {

            System.out.println("Custom exception caught: " + e.getMessage());

        } catch (RuntimeException e) {

            System.out.println("Runtime exception caught: " + e.getMessage());
        }
    }
}
```

### Output

```text
Custom exception caught: User not found
```

### Important Rule: Order matters

Since `InvalidUserException` extends `RuntimeException`, the **child exception catch must come first**.

✅ Correct:

```java
try {

} catch (InvalidUserException e) {

} catch (RuntimeException e) {

}
```

❌ Wrong:

```java
try {

} catch (RuntimeException e) {

} catch (InvalidUserException e) {

}
```

Why wrong?

Because `RuntimeException` already catches everything that is a subclass of it, including `InvalidUserException`.

So Java gives a compile-time error:

> Unreachable catch block for InvalidUserException
