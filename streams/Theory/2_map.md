# Map

- stream's map method is another `intermediate operation` (intermediate operation are those operations that return new stream) like `filter`, `sorted`, `peek`.
- stream's map method return new stream.
- stream's map method transform an object of one type into another type.
- The map method accepts a functional interface `Function<T, R>`, where T is the input type, and R is the output type.
- Function Functional Interface has an abstract method named `apply`.
- `apply` abstract method takes input of any type T and return output of any type R.
- stream are `lazy` in nature and their execution will not start even if we use any number of `intermediate operations`, to execute stream pipeline we have to mandatory use only 1 `terminal operation` like forEach, count, collect.
- `Terminal operation` triggers the execution of stream pipeline and also end the stream pipeline meaning stream pipeline is no more usable after the terminal operation.
