```java
// Stream.generate creates an infinite stream, while the lambda in forEach consumes each element
// Note: Adding limit(n) is recommended here to control the output and avoid infinite streaming
Stream.generate(() -> "Hello interview")
.limit(5) // Limit to the first 5 elements to avoid infinite stream processing
.forEach((str) -> System.out.println("Consumed the generated data: " + str));

// Stream.of() generates a finite / definite stream, limited to the elements provided as arguments
Stream.of(1, 2, 3, 4, 5).forEach(i -> System.out.println("Consumed: " + i));
```

- **Intermediate operations**, like `filter`, `map`, and `sorted`, do not produce a result or terminate the stream. Instead, they build a processing pipeline between the source (stream<Object>) and the destination, where a terminal operation (like forEach, collect, etc.) is applied to produce a result or perform an action.
- **Lazy Evaluation** for Efficiency: stream operations like filter are intermediate operations and won’t actually process any data until a terminal operation (such as forEach, collect, etc.) is called. This approach, known as lazy evaluation, means that only necessary steps are executed when required, making streams more efficient with resources.

```java
import java.util.Arrays;
import java.util.stream.stream;

public class StreamFilter2 {
  public static void main(String[] args) {
    Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    stream<Integer> stream_of_integer = Arrays.stream(numbers);
    stream<Integer> filtered_stream_of_integer = stream_of_integer.filter(i -> i > 5);
    stream_of_integer.forEach(i -> System.out.println("Consumed from stream_of_integer: "+i));
    filtered_stream_of_integer.forEach(i -> System .out.println("Consumed from filtered_stream_of_integer: "+i));
  }
}
```

**OUTPUT**

```java
Exception in thread "main" java.lang.IllegalStateException: stream has already been operated upon or closed
	at java.base/java.util.stream.AbstractPipeline.sourceStageSpliterator(AbstractPipeline.java:279)
	at java.base/java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:762)
	at second.StreamFilter2.main(StreamFilter2.java:12)
```
