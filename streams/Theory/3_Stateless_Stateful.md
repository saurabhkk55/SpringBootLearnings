In Java's stream API, intermediate operations are divided into two categories: **stateless** and **stateful**. Let’s explore each type, along with examples and a list of commonly used methods for each.

### 1. **Stateless Operations**
Stateless operations process each element in a stream independently. They do not need to keep track of or depend on previous elements in the stream. Each element is processed in isolation, so the result for one element doesn't affect the processing of others. This makes stateless operations suitable for parallel processing because they do not maintain any shared state.

#### Common Stateless Operations
- `map`: Transforms each element in the stream.
- `filter`: Selects elements based on a condition.
- `flatMap`: Maps each element to a stream and flattens it into a single stream.
- `peek`: Applies a specified action to each element, useful for debugging.
- `limit`: Limits the stream to a specified number of elements.

### 2. **Stateful Operations**
Stateful operations need to process the entire data stream (or at least a large portion) before producing a result. This means they must maintain some internal state across multiple elements to determine the output. Stateful operations can introduce delays in the stream pipeline because they need to collect more data before they can pass any result downstream. This dependency on previous elements makes them generally less efficient for parallel processing.

#### Common Stateful Operations
- `sorted`: Sorts the elements in the stream.
- `distinct`: Removes duplicates from the stream.
- `limit`: Limits the stream to a specified number of elements (can be both stateful and stateless depending on use).
- `skip`: Skips the first n elements of the stream.
- `reduce`: Aggregates elements to produce a single result.