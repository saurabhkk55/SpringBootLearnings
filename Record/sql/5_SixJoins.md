Let’s assume these two tables:

**Employee**

| id | name | designation |
| -- | ---- | ----------- |
| 1  | A    | Dev         |
| 2  | B    | Tester      |
| 3  | C    | Manager     |

**Department**

| id | name | employeeId |
| -- | ---- | ---------- |
| 10 | IT   | 1          |
| 11 | QA   | 2          |
| 12 | HR   | 4          |

---

# 1️⃣ INNER JOIN

### Query

```sql
SELECT e.id, e.name, d.name
FROM Employee e
INNER JOIN Department d
ON e.id = d.employeeId;
```

### Output Includes

✔ Only matching rows from both tables.

| id | name | department |
| -- | ---- | ---------- |
| 1  | A    | IT         |
| 2  | B    | QA         |

- 👉 Employee 3 excluded (no department)
- 👉 Department HR excluded (no employee)

---

# 2️⃣ LEFT JOIN

### Query

```sql
SELECT e.id, e.name, d.name
FROM Employee e
LEFT JOIN Department d
ON e.id = d.employeeId;
```

### Output Includes

✔ All employees
✔ Matching departments
✔ NULL if no match

| id | name | department |
| -- | ---- | ---------- |
| 1  | A    | IT         |
| 2  | B    | QA         |
| 3  | C    | NULL       |

👉 Keeps all left table rows.

---

# 3️⃣ RIGHT JOIN

### Query

```sql
SELECT e.id, e.name, d.name
FROM Employee e
RIGHT JOIN Department d
ON e.id = d.employeeId;
```

### Output Includes

✔ All departments
✔ Matching employees
✔ NULL if no employee

| id   | name | department |
| ---- | ---- | ---------- |
| 1    | A    | IT         |
| 2    | B    | QA         |
| NULL | NULL | HR         |

👉 Keeps all right table rows.

---

# 4️⃣ FULL JOIN (FULL OUTER JOIN)

### Query

```sql
SELECT e.id, e.name, d.name
FROM Employee e
FULL JOIN Department d
ON e.id = d.employeeId;
```

### Output Includes

- ✔ All rows from both tables
- ✔ Matching where possible
- ✔ NULL where no match

| id   | name | department |
| ---- | ---- | ---------- |
| 1    | A    | IT         |
| 2    | B    | QA         |
| 3    | C    | NULL       |
| NULL | NULL | HR         |

---

# 5️⃣ NATURAL JOIN

Natural join automatically joins columns with **same name**.

But here:

* Employee → id
* Department → employeeId

No same column name → ❌ won’t work as expected.

To use NATURAL JOIN, column names must match.

Example if Department had column **id instead of employeeId**

```sql
SELECT *
FROM Employee
NATURAL JOIN Department;
```

It would automatically do:

```
Employee.id = Department.id
```

✔ Only matching rows returned.

---

# 6️⃣ CROSS JOIN

### Query

```sql
SELECT e.name, d.name
FROM Employee e
CROSS JOIN Department d;
```

### Output Includes

✔ Cartesian product
👉 Every employee paired with every department.

If:

* 3 employees
* 3 departments

Result → **3 × 3 = 9 rows**

Example:

| employee | dept |
| -------- | ---- |
| A        | IT   |
| A        | QA   |
| A        | HR   |
| B        | IT   |
| B        | QA   |
| B        | HR   |
| C        | IT   |
| C        | QA   |
| C        | HR   |

---

# 🔥 Quick Visual Summary

| JOIN    | Keeps unmatched rows? | Which side            |
| ------- | --------------------- | --------------------- |
| INNER   | ❌                     | none                  |
| LEFT    | ✔                     | left                  |
| RIGHT   | ✔                     | right                 |
| FULL    | ✔                     | both                  |
| NATURAL | ❌                     | matching columns only |
| CROSS   | all combos            | both                  |

---

✅ **Simple memory trick**

- > INNER → Only matches
- > LEFT → Everything from left
- > RIGHT → Everything from right
- > FULL → Everything from both
- > CROSS → Everything × Everything
