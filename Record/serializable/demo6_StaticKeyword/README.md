```java
private static String platformName;
```

* ❌ `static` fields are **NOT serialized**
* ✅ On deserialization, they **retain the class’s current value**
* ❌ They do **NOT** get default values (`null`, `0`, etc.) based on the data type.

This is **expected and by design**.

---

## Why does this happen? (Core reason)

### 🔑 Key rule of Java Serialization

> **Serialization is about object state, not class state**

* **Instance fields** → belong to an object → serialized
* **Static fields** → belong to the class → **NOT serialized**

---

## What happens internally (real flow)

### 1️⃣ Before serialization

```java
User.platformName = "YouTube";
User user = new User(...);
```

Memory:

```
Class User:
  static platformName = "YouTube"

Heap Object user:
  id = 101
  name = "Saurabh"
  password = "P@$$word"
```

---

### 2️⃣ During `writeObject(user)`

Java writes:

* class metadata
* serialVersionUID
* **instance fields only**

❌ `platformName` is skipped entirely

---

### 3️⃣ During `readObject()`

This is the **critical difference from transient** 👇

* Java does **NOT initialize static fields**
* JVM loads the class normally
* Static fields already have values via:
    * static initializer
    * previous assignments
    * class loading

So Java **does nothing** to `platformName`

---

## Result after deserialization

```java
System.out.println(User.platformName);
```

Output:

```text
YouTube
```

💡 Not because it was read from file —
👉 but because it was **never part of the file**

---

## Why transient behaves differently

| Aspect                      | transient            | static                |
| --------------------------- | -------------------- |-----------------------|
| Belongs to                  | Object               | Class                 |
| Serialized?                 | ❌ No                | ❌ No                 |
| Value after deserialization | Default value        | Existing class value  |
| Why                         | Object field skipped | Class state untouched |

---

## Important comparison example

```java
class User implements Serializable {
    private String name;
    private transient String password;
    private static String platformName = "YouTube";
}
```

After deserialization:

```java
name         → restored from file
password     → null
platformName → "YouTube"
```

---

## Final interview-ready explanation 🏆

> Static fields are not serialized because serialization captures object state, not class state.
> During deserialization, static fields retain their current class-level values since they are initialized during class loading and are not part of the serialized stream.
> This is why static fields do not revert to default values like transient instance fields.
