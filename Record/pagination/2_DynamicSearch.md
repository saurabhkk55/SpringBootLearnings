# Part 1: `Dynamic search`

If user can search dynamically using any combination of fields:

* name
* age
* mobile
* email
* city

then creating repository methods like below becomes impossible:

```java
findByName()
findByNameAndAge()
findByNameAndMobile()
findByAgeAndMobile()
findByNameAndAgeAndMobile()
```

Because combinations grow exponentially.

So instead of creating multiple methods, we use:

* `Specification`
* Criteria API
* `toPredicate()` method

---

## Why Specification?

Specification helps us create **dynamic queries at runtime**.

Meaning:

* if user sends `name`
  → add name condition

* if user sends `mobile`
  → add mobile condition

* if user sends both
  → add both conditions

---

## Flow

### 1. Repository

Repository should extend:

```java
JpaSpecificationExecutor
```

Example:

```java
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
```

---

# 2. Create Search DTO

```java
public class UserSearchRequest {

    private String name;
    private Integer age;
    private String mobile;

    // getters setters
}
```

---

# 3. Create Specification

```java
public class UserSpecification {

    public static Specification<User> search(UserSearchRequest request) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), request.getName()));
            }

            if (request.getAge() != null) {
                predicates.add(criteriaBuilder.equal(root.get("age"), request.getAge()));
            }

            if (request.getMobile() != null) {
                predicates.add(criteriaBuilder.equal(root.get("mobile"), request.getMobile()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0])); // AND Operator (we can OR as well)
        };
    }
}
```

---

# 4. Use in Service

```java
public List<User> search(UserSearchRequest request) {

    Specification<User> specification = UserSpecification.search(request);
    return userRepository.findAll(specification);
}
```

---

# What happens internally?

Suppose request is:

```json
{
   "name": "Saurabh",
   "mobile": "9999999999"
}
```

Then generated query becomes:

```sql
select * from user where name = 'Saurabh' and mobile = '9999999999';
```

If only age comes:

```json
{
   "age": 25
}
```

Then query becomes:

```sql
select * from user where age = 25;
```

---

# What is `toPredicate()`?

`toPredicate()` is the method we override inside Specification.

```java
(root, query, criteriaBuilder) -> {}
```

Internally equivalent to:

```java
@Override
public Predicate toPredicate(
        Root<User> root,
        CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
}
```

## 2. CriteriaBuilder

Used to create conditions.

Examples:

```java
criteriaBuilder.equal()
criteriaBuilder.like()
criteriaBuilder.greaterThan()
criteriaBuilder.lessThan()
```

---

## 3. Predicate

Represents a condition.

Example:

```java
name = 'Saurabh'
```

is one Predicate.

---

# Interview one-line answer

> We use JPA Specification when search filters are dynamic and user can send different combinations of fields. It helps build queries dynamically using Criteria API and `toPredicate()` method instead of creating multiple repository methods.

---

# Part 2: `CriteriaBuilder methods`

```java
public class UserSpecification {

    public static Specification<User> search(UserSearchRequest request) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();


            // exact match
            // SQL -> where name = 'Saurabh'
            if (request.getName() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("name"),
                                request.getName()
                        )
                );
            }


            // partial match
            // SQL -> where name like '%sau%'
            if (request.getName() != null) {

                predicates.add(
                        criteriaBuilder.like(
                                root.get("name"),
                                "%" + request.getName() + "%"
                        )
                );
            }


            // greater than
            // SQL -> where age > 25
            if (request.getAge() != null) {

                predicates.add(
                        criteriaBuilder.greaterThan(
                                root.get("age"),
                                request.getAge()
                        )
                );
            }


            // less than
            // SQL -> where age < 60
            if (request.getAge() != null) {

                predicates.add(
                        criteriaBuilder.lessThan(
                                root.get("age"),
                                request.getAge()
                        )
                );
            }


            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
```

# Common CriteriaBuilder methods

| Method                   | Purpose          | SQL Equivalent |
| ------------------------ | ---------------- | -------------- |
| `equal()`                | exact match      | `=`            |
| `like()`                 | partial search   | `LIKE`         |
| `greaterThan()`          | greater value    | `>`            |
| `lessThan()`             | smaller value    | `<`            |
| `greaterThanOrEqualTo()` | greater or equal | `>=`           |
| `lessThanOrEqualTo()`    | less or equal    | `<=`           |
| `notEqual()`             | not equal        | `!=`           |
| `in()`                   | match from list  | `IN`           |
| `between()`              | range search     | `BETWEEN`      |
| `isNull()`               | null check       | `IS NULL`      |
| `isNotNull()`            | not null check   | `IS NOT NULL`  |

Example:

```java
criteriaBuilder.between(
        root.get("salary"),
        20000,
        50000
);
```

SQL:

```sql
salary between 20000 and 50000
```
