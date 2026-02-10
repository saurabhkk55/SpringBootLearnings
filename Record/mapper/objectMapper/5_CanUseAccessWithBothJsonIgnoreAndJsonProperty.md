# **❌ No — you cannot (and should not) use `access` with `@JsonIgnore`.**

---

# ❓ Why `access` Works Only with `@JsonProperty`

### `@JsonIgnore`

```java
@JsonIgnore
private String password;
```

📌 Meaning:

* ❌ Ignored during **serialization** (response)
* ❌ Ignored during **deserialization** (request)
* ❌ **No configuration possible**
* ❌ **No access control**

➡️ It is a **hard ignore**, always.

---

### `@JsonProperty(access = …)`

```java
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private String password;
```

📌 Meaning:

* ✅ Accepted in **request**
* ❌ Not sent in **response**

➡️ **Fine-grained control**

---

# ❌ Can We Combine Them?

```java
@JsonIgnore
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private String password;
```

🚫 **Wrong & useless**

Why?

* `@JsonIgnore` **wins**
* Field is ignored completely
* `@JsonProperty` becomes meaningless

📌 Jackson stops processing once it sees `@JsonIgnore`

---

# ✅ Correct Ways (Depending on Requirement)

---

## ✔ Case 1: Accept in Request, Hide in Response (MOST COMMON)

```java
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private String password;
```

---

## ✔ Case 2: Send in Response, Ignore from Request

```java
@JsonProperty(access = JsonProperty.Access.READ_ONLY)
private Long id;
```

---

## ✔ Case 3: Ignore Completely (Never Accept, Never Send)

```java
@JsonIgnore
private String internalToken;
```

---

## ✔ Case 4: Getter-Level Control (Advanced): Accept password, Hide in Response ✅

```java
@JsonIgnore
public String getPassword() {
    return password;
}
```

✔ Accepted via setter
❌ Not returned via getter

📌 Useful when you want **method-level control**
