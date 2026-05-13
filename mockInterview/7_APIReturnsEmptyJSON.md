# API Returns Empty JSON Even Though DB Has Data — Possible Reasons

If:

* database contains data
* service layer fetches entity correctly
* but API response becomes:

```json
{}
```

or partially empty JSON,

then one very common reason is:

```text
Jackson serialization issue
```

during object-to-JSON conversion.

---

# Request Flow

```text id="jlwm63"
DB
 ↓
JPA Entity
 ↓
Service Layer
 ↓
Controller
 ↓
Jackson Serialization
 ↓
JSON Response
```

Data may exist in entity,
but Jackson fails to serialize fields properly.

---

# 1. Missing Getters (Very Common)

Jackson serializes fields mainly using:

* getters
* public fields

Example problem:

```java id="jlwm64"
public class User {

    private String name;
}
```

No getter:

```java id="jlwm65"
getName()
```

Result:

```json
{}
```

because Jackson cannot access field.

---

# Fix

```java id="jlwm66"
public class User {

    private String name;

    public String getName() {
        return name;
    }
}
```

---

# Lombok Issue

If using Lombok:

```java id="jlwm67"
@Data
@Getter
@Setter
```

must compile properly.

Sometimes:

* Lombok plugin missing
* annotation processing disabled

causes getters not generated.

---

# 2. Fields Marked with @JsonIgnore

Example:

```java id="jlwm68"
@JsonIgnore
private String password;
```

Ignored fields not serialized.

If all fields ignored:

```json
{}
```

---

# 3. Lazy Loading Issue (Hibernate + Jackson)

Very common with JPA.

Example:

```java id="jlwm69"
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```

After transaction closes:

* lazy proxy cannot load data
* serialization may fail silently
* empty JSON returned
* or exception occurs

---

# Symptoms

* empty nested objects
* empty collections
* `LazyInitializationException`
* partial response

---

# Fixes

## Use DTOs (Best Practice)

Instead of returning entity directly:

```java id="jlwm70"
return userEntity;
```

map to DTO:

```java id="jlwm71"
return userResponseDto;
```

Best enterprise approach.

---

## Or Fetch Data Properly

Use:

* JOIN FETCH
* EntityGraph
* eager fetch carefully

---


# 8. Null Values Excluded

If configured:

```java id="jlwm81"
@JsonInclude(JsonInclude.Include.NON_NULL)
```

and all fields are null:

```json
{}
```

---

# Most Common Real Root Causes

Usually one of these:

```text id="jlwm87"
1. Missing getters
2. Lombok not working
3. Lazy loading issue
4. @JsonIgnore usage
5. Bidirectional recursion
6. DTO mapping issue
7. Jackson custom config
```

---

# Interview-Level Summary

> If API returns empty JSON despite DB having data, a common reason is Jackson serialization failure.
>
> Typical causes include:
>
> * Missing getters
> * Lazy-loaded Hibernate entities
> * `@JsonIgnore`
> * Bidirectional relationship recursion
> * Incorrect ObjectMapper configuration
> * DTO mapping issues
>
> Enterprise applications typically use DTOs instead of directly returning JPA entities to avoid serialization and lazy loading problems.
