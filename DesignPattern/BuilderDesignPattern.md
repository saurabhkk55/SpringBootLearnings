## Builder Design Pattern in Java (with Mandatory & Optional Fields)

Builder pattern is used when:

* Object has **many fields**
* Some fields are **mandatory**
* Some fields are **optional**
* You want clean and readable object creation

---

## 🔹 Real-Life Example

Let’s create a `User` object.

### Requirements:

* **Mandatory fields** → `name`, `email`
* **Optional fields** → `age`, `phone`, `address`

---

## ✅ Implementation

```java
public class User {

    // Mandatory fields
    private final String name;
    private final String email;

    // Optional fields
    private final int age;
    private final String phone;
    private final String address;

    // Private constructor (only Builder can create object)
    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    // Static Builder class
    public static class Builder {

        // Mandatory fields
        private final String name;
        private final String email;

        // Optional fields (default values allowed)
        private int age;
        private String phone;
        private String address;

        // Constructor with mandatory fields
        public Builder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Optional setters (return Builder for chaining)
        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        // Build method
        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', email='" + email + 
               "', age=" + age + ", phone='" + phone + 
               "', address='" + address + "'}";
    }
}
```

---

## ✅ How to Use

```java
public class Main {
    public static void main(String[] args) {

        User user1 = new User.Builder("Saurabh", "saurabh@gmail.com")
                .age(25)
                .phone("9876543210")
                .build();

        User user2 = new User.Builder("Rahul", "rahul@gmail.com")
                .build();  // only mandatory fields
    }
}
```

---

## 🔹 Why This Is Good?

Without Builder:

```java
new User("Saurabh", "email", 25, null, null);
```

👉 Confusing
👉 Order mistakes possible
👉 Hard to read

With Builder:

```java
new User.Builder("Saurabh", "email")
        .age(25)
        .build();
```

✔ Readable
✔ Flexible
✔ Immutable object
✔ Mandatory fields enforced

---

## 🔹 Key Points (Interview Important)

* Make constructor **private**
* Mandatory fields in **Builder constructor**
* Optional fields via **method chaining**
* `build()` returns final object
* Usually used in **immutable classes**
