# Problem Statement

Suppose one thread is iterating collection:

```java id="t7m2qf"
for(String name : list) {
}
```

and meanwhile collection gets modified:

```java id="x4k9pd"
list.add("New");
```

Question:

```text id="m1r8zc"
What should iterator do?
```

Two possible behaviors:

* Fail immediately
* Continue safely

Hence:

* Fail-Fast
* Fail-Safe

---

# 1. Fail-Fast Iterator

Fail immediately if collection is modified during iteration.

Throws:

```text id="u3n7wb"
ConcurrentModificationException
```

---

# Example

```java id="p6d1ya"
List<String> list = new ArrayList<>();

list.add("A");
list.add("B");

Iterator<String> itr = list.iterator();

while(itr.hasNext()) {

    String value = itr.next();

    if(value.equals("A")) {
        list.add("C");
    }
}
```

---

# Output

```text id="q9v2ke"
ConcurrentModificationException
```

---

# Why?

Because collection structure changed while iterator was traversing.

---

# Internal Working

Most fail-fast collections maintain:

```text id="j5x8lm"
modCount
```

(modification count)

Whenever collection structure changes:

```text id="n2c7sr"
modCount++
```

Iterator stores:

```text id="f8w1zd"
expectedModCount
```

During iteration:

```text id="z6p4bv"
if(modCount != expectedModCount)
    throw ConcurrentModificationException
```

---

# Fail-Fast Collections

Examples:

* `ArrayList`
* `HashMap`
* `HashSet`
* `Vector`
* `LinkedList`

Their iterators are fail-fast.

---

# Important Point

Fail-fast is:

```text id="d3y7qn"
Best effort detection
```

NOT guaranteed in all multithreaded scenarios.

---

# Safe Modification Using Iterator

Modification should happen through iterator itself.

✅ Correct:

```java id="r4c9mk"
Iterator<String> itr = list.iterator();

while(itr.hasNext()) {

    String value = itr.next();

    if(value.equals("A")) {
        itr.remove();
    }
}
```

This does NOT throw exception.

Because iterator updates expected state properly.

---

# 2. Fail-Safe Iterator

Fail-safe iterator works on:

```text id="k8v3pt"
Clone/Snapshot copy
```

of collection.

So modification does NOT affect iterator.

No exception thrown.

---

# Example

```java id="m7x1qw"
CopyOnWriteArrayList<String> list =
        new CopyOnWriteArrayList<>();

list.add("A");
list.add("B");

Iterator<String> itr = list.iterator();

while(itr.hasNext()) {

    String value = itr.next();

    System.out.println(value);

    list.add("C");
}
```

---

# Output

```text id="v5n8ye"
A
B
```

No exception.

---

# Why "C" Not Printed?

Iterator is traversing:

```text id="w2r6hc"
Old snapshot
```

taken before modification.

---

# Fail-Safe Collections

Examples:

* `CopyOnWriteArrayList`
* `ConcurrentHashMap`

---

# Internal Working of CopyOnWriteArrayList

Whenever modification happens:

```text id="y7m1zf"
Entire new copy created
```

Very safe for reads.

But costly for writes.

---

# Difference Table

| Fail-Fast                        | Fail-Safe                     |
| -------------------------------- | ----------------------------- |
| Throws exception on modification | No exception                  |
| Uses original collection         | Uses snapshot/copy            |
| Faster                           | More memory usage             |
| Not thread-safe                  | Thread-safe                   |
| Example: ArrayList               | Example: CopyOnWriteArrayList |

---

# Important Interview Question

## Does Fail-Fast Mean Thread-Safe?

❌ No.

Fail-fast only detects modification.

It does NOT provide thread safety.

---

# Simple One-Line Interview Answer

> Fail-fast iterators throw `ConcurrentModificationException` if collection is structurally modified during iteration, whereas fail-safe iterators iterate over a snapshot/copy of collection and do not throw exceptions during concurrent modifications.
