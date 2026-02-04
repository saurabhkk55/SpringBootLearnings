## What does `transient` actually mean?

```java
private transient String password;
```

👉 `transient` tells Java Serialization:

> **“Do NOT persist this field when serializing the object.”**

That’s it. Nothing mystical.

---

## What happens during `saveData(user)`?

### Before serialization

```java
User user = new User(101, "Saurabh Kardam", "P@$$word");
```

Object in memory:

```
id = 101
name = "Saurabh Kardam"
password = "P@$$word"
platformName = "YouTube"
```

---

### During serialization (`writeObject`)

Java writes:

* class metadata
* serialVersionUID
* **ONLY non-transient fields**

So the file contains:

```
id = 101
name = "Saurabh Kardam"
platformName = "YouTube"
```

❌ `password` is **completely skipped**

---

## What happens during `loadData()`?

### Step 1: Object creation

Java creates a `User` object **without calling the constructor**.

### Step 2: Field population

* Serialized fields → restored from file
* Transient fields → **NOT restored**

### Result:

```java
password = null
```

📌 Because:

* `String` default value → `null`
* `int` default value → `0`
* `boolean` default value → `false`

---

## Output you will see

```text
User [id=101, name=Saurabh Kardam, password=null, platformName=YouTube]
```

✅ Exactly as you said.

---

## Why was `transient` introduced? (Very important)

### 🔐 Security

You should **never serialize sensitive data**:

* passwords
* tokens
* private keys
* session data

```java
private transient String password;
private transient String authToken;
```

---

## Final crystal-clear summary (Interview gold 🏆)

> A `transient` field is excluded from Java serialization.
> Its value is not written to the stream, and during deserialization it is initialized with its default value (null, 0, false).
> `transient` is commonly used for sensitive, derived, or non-serializable fields and only affects Java’s built-in serialization mechanism.
