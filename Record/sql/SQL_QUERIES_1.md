1. Inner Join

Question:
Write a query to get all employees along with their department names.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

Select e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
INNER JOIN departments d
ON e.dept_id = d.dept_id
```

---

2. Left Join

👉 Question:
Get all employees and their department names. Include employees who are not assigned to any department.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

Select e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
LEFT JOIN departments d
ON e.dept_id = d.dept_id

👉 Follow-up question:
“Show ONLY employees without a department”

Then you add a filter: WHERE d.dept_id IS NULL;
```

---

3. Right Join

👉 Question:
Get all departments and employees working in them. Include departments with no employees.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

Select e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
RIGHT JOIN departments d
ON e.dept_id = d.dept_id

👉 Follow-up question:
“Show ONLY departments with NO employees”

Then you add a filter: WHERE e.emp_id IS NULL;
```

---

4. Full Outer Join

👉 Question:
Get all employees and departments, including unmatched records from both tables.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

Select e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
FULL JOIN departments d
ON e.dept_id = d.dept_id

```

⚠️ Important Real-World Note (Interview Favorite)
FULL JOIN (or FULL OUTER JOIN) is NOT supported in MySQL ❌
It works in:
PostgreSQL ✅
SQL Server ✅
Oracle ✅

👉 So if you're using MySQL, this query will fail ❌

✅ MySQL Alternative (Very Important 🔥)

You simulate FULL OUTER JOIN using UNION:

```sql
SELECT e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
LEFT JOIN departments d
ON e.dept_id = d.dept_id

UNION

SELECT e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
RIGHT JOIN departments d
ON e.dept_id = d.dept_id;
```

---

5. Employees without Department

👉 Question:
Find employees who are not assigned to any department.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

SELECT e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
LEFT JOIN departments d
ON e.dept_id = d.dept_id
WHERE d.dept_id IS NULL;
```

---

6. Departments without Employees

👉 Question:
Find departments that do not have any employees.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

SELECT e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
RIGHT JOIN departments d
ON e.dept_id = d.dept_id
WHERE e.emp_id IS NULL;
```

---

7. Self Join (Important 🔥)

👉 Question:
Find employees and their managers.

```sql
Table:

employees(emp_id, name, manager_id)

Answer:

SELECT
e.name AS employee_name,
m.name AS manager_name
FROM employees e
LEFT JOIN employees m
ON e.manager_id = m.emp_id;
```

💡 Explanation (Simple):
- e → employee
- m → manager (same table, different alias)
- e.manager_id = m.emp_id → link employee to their manager
📌 Why LEFT JOIN?
- Some employees may not have a manager (CEO)
- So we still want them in result → manager will be NULL
	
---

8. Multiple Joins

👉 Question:
Get employee name, department name, and project name.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)
projects(proj_id, proj_name, emp_id)

Answer:

SELECT
e.name AS employee_name,
d.dept_name,
p.proj_name
FROM employees e
INNER JOIN departments d
ON e.dept_id = d.dept_id
INNER JOIN projects p
ON e.emp_id = p.emp_id;
```

---

9. Join with Condition

👉 Question:
Find employees who are working in "IT" department.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

SELECT e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
Left JOIN departments d
ON e.dept_id = d.dept_id
WHERE d.dept_name = "IT";
```

⚠️ Key Insight (VERY IMPORTANT 🔥)
- Because of this condition: WHERE d.dept_name = "IT"

👉 Your LEFT JOIN behaves like INNER JOIN

💡 Why?
- LEFT JOIN keeps all employees
- But WHERE d.dept_name = 'IT' removes:
- NULL values
- non-IT departments

👉 So only matching rows remain → same as INNER JOIN

✅ Better / Cleaner Query:
```sql
SELECT e.emp_id, e.name, e.dept_id, d.dept_name
FROM employees e
INNER JOIN departments d
ON e.dept_id = d.dept_id
WHERE d.dept_name = 'IT';
```

---

10. Count using Join

👉 Question:
Count number of employees in each department.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

SELECT
d.dept_id,
d.dept_name,
COUNT(e.emp_id) AS employee_count
FROM departments d
LEFT JOIN employees e
ON e.dept_id = d.dept_id
GROUP BY d.dept_id, d.dept_name;
```

💡 Why this is correct:
- LEFT JOIN → includes all departments (even with 0 employees) ✅
- COUNT(e.emp_id) → counts only matching employees
- Shows department + count → meaningful result
	
---

11. Highest Salary per Department 👉 Question: Find employees who have the highest salary in each department.

```sql
Tables:

employees(emp_id, name, dept_id, salary)

Answer:

SELECT e.emp_id, e.name, e.dept_id, e.salary
FROM employees e
JOIN (
SELECT dept_id, MAX(salary) AS max_salary
FROM employees
GROUP BY dept_id
) m
ON e.dept_id = m.dept_id
AND e.salary = m.max_salary;
```

---

12. Duplicate Records using Join

👉 Question:
Find duplicate employees based on name.

```sql
Tables:

employees(emp_id, name, dept_id, salary)

Answer:

SELECT name, COUNT(*) AS cnt
FROM employees
GROUP BY name
HAVING COUNT(*) > 1;
```

---

13. Join + Aggregation + Having
👉 Question: Find departments having more than 5 employees.

```sql
Tables:

employees(emp_id, name, dept_id)
departments(dept_id, dept_name)

Answer:

SELECT
d.dept_id,
d.dept_name,
COUNT(e.emp_id) AS employee_count
FROM departments d
JOIN employees e
ON e.dept_id = d.dept_id
GROUP BY d.dept_id, d.dept_name
HAVING COUNT(e.emp_id) > 5;
```

---

14. Anti Join (Very Important 🔥)

👉 Question:
Find employees who are not assigned to any project.

Answer:

```sql
SELECT e.emp_id, e.name
FROM employees e
LEFT JOIN projects p
ON e.emp_id = p.emp_id
WHERE p.emp_id IS NULL;
```

---

15. Top N per Group

👉 Question:
Find top 2 highest paid employees in each department.

```sql
Tables:

employees(emp_id, name, dept_id, salary)

Answer:

✅ Best Approach: Window Function (Recommended)

SELECT emp_id, name, dept_id, salary
FROM (
SELECT
emp_id, name, dept_id, salary,
DENSE_RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rnk
FROM employees
) t
WHERE rnk <= 2;
```

💡 Explanation (Simple):
- PARTITION BY dept_id → divide data per department
- ORDER BY salary DESC → highest salary first
- DENSE_RANK() → assign ranks (1, 2, 2, 3...)
- rnk <= 2 → pick top 2 salaries per department

---

16. Join on Multiple Conditions

👉 Question:
Join two tables where:
emp.dept_id = dept.dept_id
AND emp.location = dept.location

Answer:

```sql
SELECT
e.emp_id,
e.name,
e.dept_id,
d.dept_name,
e.location
FROM employees e
JOIN departments d
ON e.dept_id = d.dept_id
AND e.location = d.location;
```

---

17. Cross Join

👉 Question:
Generate all possible combinations of employees and departments.

Answer:

```sql
SELECT
e.emp_id,
e.name,
d.dept_id,
d.dept_name
FROM employees e
CROSS JOIN departments d;
```

---

18. Join with Subquery

👉 Question:
Get employees whose salary is greater than average salary of their department.

Answer:

```sql
SELECT e.emp_id, e.name, e.dept_id, e.salary
FROM employees e
JOIN (
SELECT dept_id, AVG(salary) AS avg_salary
FROM employees
GROUP BY dept_id
) avg_table
ON e.dept_id = avg_table.dept_id
WHERE e.salary > avg_table.avg_salary;
```

---

19. E-commerce Example

👉 Question:
Get all orders with customer name and product name.

Tables:
orders(order_id, customer_id, product_id)
customers(customer_id, name)
products(product_id, product_name)

Answer:

```sql
SELECT
o.order_id,
c.name AS customer_name,
p.product_name
FROM orders o
JOIN customers c
ON o.customer_id = c.customer_id
JOIN products p
ON o.product_id = p.product_id;
```

---

20. Latest Record Join

👉 Question:
Get latest order for each customer.

Answer:

```sql
SELECT o.*
FROM orders o
JOIN (
SELECT customer_id, MAX(order_date) AS latest_date
FROM orders
GROUP BY customer_id
) t
ON o.customer_id = t.customer_id
AND o.order_date = t.latest_date;
```
