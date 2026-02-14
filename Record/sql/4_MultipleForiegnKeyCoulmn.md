# ✅ What is a Foreign Key?

A **foreign key** is a column that references the **primary key (or unique key)** of another table.

👉 Purpose:

* Maintain **referential integrity**
* Ensure related data exists in parent table

---

# 📊 Example — Multiple Foreign Keys in One Table

### Parent Tables

```sql
Customer(id PK)
Product(id PK)
```

### Child Table

```sql
Orders(
    order_id PK,
    customer_id FK,
    product_id FK
)
```

Here:

* `customer_id` → references Customer table
* `product_id` → references Product table

👉 So **Orders table has 2 foreign keys**

---

# 🧠 Real-Life Analogy

Think of an **Order**:

An order belongs to:

* a customer
* a product
* a delivery agent

So order table may have:

* customer_id
* product_id
* agent_id

All foreign keys.

---

# 🧾 SQL Example

```sql
CREATE TABLE orders (
    id INT PRIMARY KEY,
    customer_id INT,
    product_id INT,

    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

---

# 🚀 In Spring Boot / JPA

Multiple foreign keys = multiple relationships

```java
@Entity
class Order {

    @Id
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Product product;
}
```

Hibernate automatically creates foreign keys for both.

---

# ⚠️ Important Rules

✔ Foreign key can reference:

* Primary key
* Unique column

✔ Multiple rows can have same foreign key value
(Unlike primary key)

✔ Foreign key can be null (unless specified NOT NULL)

---

# 🎯 Interview One-Line Answer

> Yes, a table can have multiple foreign keys because each foreign key represents a relationship with another table.
