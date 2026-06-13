**Sharding** and **Partitioning** are techniques used in databases to split data into smaller pieces for better performance and scalability.

People often confuse them, so think of it like this:

> **Partitioning = split data inside one database/server**
> **Sharding = split data across multiple database servers**

---

# 1. Partitioning

Partitioning means dividing a table into smaller parts **within the same database**.

Imagine a huge table:

`orders`

| order_id | customer_id | order_date |
| -------- | ----------: | ---------- |
| 1        |         101 | 2024-01-01 |
| 2        |         102 | 2024-02-01 |
| 3        |         103 | 2025-01-01 |

Instead of storing everything in one giant table, DB internally splits it.

Example:

```text
orders_2023
orders_2024
orders_2025
```

But for the application:

```sql
SELECT * FROM orders;
```

You still query a single table.

Database internally knows where data exists.

### Real-life example

Think of a school record room.

Instead of putting all student files in one cupboard:

```text
Cupboard A → 2022 students
Cupboard B → 2023 students
Cupboard C → 2024 students
```

Still same office, same building.

---

## Types of Partitioning

### 1. Range Partitioning

Split data by range.

Example:

```text
2022 orders
2023 orders
2024 orders
```

Example SQL:

```sql
PARTITION BY RANGE(order_date)
```

Used when:

* Data is time-based
* Logs
* Orders
* Transactions

Example:

Large e-commerce order table.

---

### 2. Hash Partitioning

Database hashes a key and distributes data.

Example:

```text
customer_id % 4

0 → partition0
1 → partition1
2 → partition2
3 → partition3
```

Used when:

* Even distribution needed
* No specific range filtering

---

### 3. List Partitioning

Partition based on predefined values.

Example:

```text
India → partition1
USA → partition2
UK → partition3
```

---

## Why use partitioning?

### 1. Faster queries

Suppose:

```sql
SELECT * 
FROM orders
WHERE order_date = '2025-01-01';
```

Without partitioning:

```text
Scan 500 million rows
```

With range partitioning:

```text
Only check 2025 partition
```

Much faster.

---

### 2. Easier maintenance

Deleting old logs:

Without partition:

```sql
DELETE FROM logs WHERE year = 2022;
```

Slow.

With partition:

```text
Drop partition
```

Fast.

---

### When do we use partitioning?

Use when:

* Table becomes huge (millions/billions rows)
* Single DB server still handles traffic
* Queries filter on a key (date, region, customer)

Examples:

* Orders table
* Logs table
* Payment transactions

---

# 2. Sharding

Sharding means splitting data across **multiple database servers**.

Example:

Instead of one DB:

```text
DB1
```

You create:

```text
DB1
DB2
DB3
DB4
```

Each server stores partial data.

Example:

```text
customer_id 1–10000  → shard1
10001–20000 → shard2
20001–30000 → shard3
```

or

```text
customer_id % 3
```

distribution.

---

### Real-life example

Imagine a bank.

Instead of one branch storing all customer files:

```text
Delhi branch → customers A–F
Mumbai branch → G–M
Bangalore branch → N–Z
```

Different locations.

That is sharding.

---

## Why sharding?

### Problem: single DB limit

Suppose:

```text
500 million users
100K requests/sec
```

One server becomes bottleneck.

Problems:

* CPU high
* Memory high
* Disk IO issue
* Query slow

Then scale horizontally.

Instead of:

```text
1 powerful machine
```

Use:

```text
many machines
```

That is sharding.

---

## Example

Users table:

Before:

```text
Single DB
```

After sharding:

```text
Shard1 → user_id % 3 = 0
Shard2 → user_id % 3 = 1
Shard3 → user_id % 3 = 2
```

Example:

```text
user_id=5

5 % 3 = 2
```

Stored in shard2.

---

## Types of Sharding

### 1. Range-based sharding

```text
1–10000 → shard1
10001–20000 → shard2
```

Problem:

Hotspot risk.

Suppose recent users are more active:

```text
Shard3 overloaded
```

---

### 2. Hash-based sharding

Better distribution:

```text
userId % n
```

Example:

```text
13 % 4 = 1
```

Store in shard1.

Most common.

---

### 3. Geo sharding

By region:

```text
India users → India DB
Europe users → Europe DB
US users → US DB
```

Useful for latency and regulations.

---

## When do we use sharding?

Use when:

* Single DB cannot handle traffic
* Need horizontal scaling
* Massive data size
* High throughput system

Examples:

* Social media
* Banking
* E-commerce at huge scale
* Large SaaS systems

---

# Partitioning vs Sharding

| Feature       | Partitioning       | Sharding           |
| ------------- | ------------------ | ------------------ |
| Server count  | One server         | Multiple servers   |
| Scaling       | Vertical           | Horizontal         |
| Complexity    | Lower              | Higher             |
| Purpose       | Query optimization | Scale traffic/data |
| Data location | Same DB            | Different DBs      |

---

## Simple interview answer

> Partitioning splits a large table into smaller parts inside the same database to improve performance and maintenance.
> Sharding splits data across multiple database servers to scale horizontally when one server cannot handle load.

### Typical order in real systems

Usually:

```text
First → Partitioning
Then → Sharding
```

Why?

Because sharding adds complexity:

* Cross-shard joins
* Distributed transactions
* Data balancing
* Routing logic

So companies first optimize a single DB, then shard only when required.
