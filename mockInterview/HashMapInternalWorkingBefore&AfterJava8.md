# How `HashMap` Works Internally in Java

## Core Idea

Java `HashMap` stores data in:

```text id="’wini10"
key → value
```

format using:

* array
* hashing
* buckets

Average complexity:

```text id="’wini11"
O(1)
```

for:

* put
* get
* remove

---

# Internal Structure

Internally:

```text id="’wini12"
Array of buckets
```

Each bucket stores nodes/entries.

---

# Step-by-Step Working

# 1. Hash Calculation

When inserting:

```java id="’wini13"
map.put("A", 100);
```

HashMap calculates:

```text id="’wini14"
hash(key)
```

using:

```java id="’wini15"
hashCode()
```

---

# 2. Bucket Index Calculation

Index calculated using:

```text id="’wini16"
hash % arraySize
```

(or bitwise optimization internally).

Determines where data should go.

---

# 3. Store Entry in Bucket

Stored as:

```text id="’wini17"
Node(key, value, hash, next)
```

inside bucket.

---

# Collision

Different keys may produce same bucket index.

Example:

```text id="’wini18"
"A" → bucket 5
"B" → bucket 5
```

This is called:

```text id="’wini19"
Hash Collision
```

---

# Before Java 8

Collision handled using:

```text id="’wini1a"
Linked List
```

Structure:

```text id="’wini1b"
Bucket
  ↓
Node → Node → Node
```

---

# Problem Before Java 8

If many collisions occur:

```text id="’wini1c"
Linked list becomes long
```

Performance degrades:

```text id="’wini1d"
O(1) → O(n)
```

Worst case:

* all keys in same bucket

---

# Search Before Java 8

HashMap traverses linked list:

```text id="’wini1e"
compare key.equals()
```

until matching key found.

---

# After Java 8 Improvement

Java 8 introduced:

```text id="’wini1f"
Balanced Binary Tree (Red-Black Tree)
```

when collisions become large.

---

# Condition for Tree Conversion

If:

* bucket size > 8
  AND
* total array size >= 64

then linked list converts into:

```text id="’wini1g"
Red-Black Tree
```

---

# Structure After Java 8

```text id="’wini1h"
Bucket
   ↓
Red-Black Tree
```

instead of linked list.

---

# Benefit

Search complexity improves:

```text id="’wini1i"
O(n) → O(log n)
```

for heavy collisions.

---

# Why Threshold = 8?

Because:

* linked list faster for small data
* tree creation has overhead

So tree used only when collisions become significant.

---

# Resize/Rehashing

Default capacity:

```text id="’wini1j"
16
```

Default load factor:

```text id="’wini1k"
0.75
```

When:

```text id="’wini1l"
size > capacity × loadFactor
```

HashMap:

* doubles array size
* recalculates bucket positions
* rehashes entries

Example:

```text id="’wini1m"
16 × 0.75 = 12
```

After 12 entries:

* resize occurs
* capacity becomes 32

---

# Why HashMap is Not Thread Safe?

Multiple threads modifying simultaneously may cause:

* inconsistent data
* race conditions
* infinite loop (older Java versions during resize)

Use:

* `ConcurrentHashMap`
* synchronization

for multithreading.

---

# Internal Flow Summary

```text id="’wini1n"
put(key, value)
      ↓
hashCode()
      ↓
bucket index calculation
      ↓
store in bucket
      ↓
collision?
      ↓
Before Java 8 → Linked List
After Java 8 → Linked List → Red-Black Tree (if > 8 nodes)
```

---

# Complexity Comparison

| Operation           | Before Java 8 | After Java 8       |
| ------------------- | ------------- | ------------------ |
| Average Search      | O(1)          | O(1)               |
| Worst Case Search   | O(n)          | O(log n)           |
| Collision Structure | Linked List   | Linked List + Tree |

---

# Interview-Level Summary

> `HashMap` internally uses an array of buckets.
>
> Keys are stored based on hash value and bucket index calculation.
>
> Before Java 8, collisions were handled using linked lists, causing worst-case complexity of `O(n)`.
>
> After Java 8, if collisions in a bucket exceed threshold 8, the linked list converts into a Red-Black Tree, improving worst-case complexity to `O(log n)`.
>
> HashMap also performs resizing and rehashing when load factor threshold is exceeded.
