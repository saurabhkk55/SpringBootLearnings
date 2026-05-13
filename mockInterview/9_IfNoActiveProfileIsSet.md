In Spring Boot, if no active profile is set:

```properties
spring.profiles.active
```

then Spring Boot uses:

```text
default profile
```

---

# What Happens?

Spring Boot loads:

```text
application.properties
```

or

```text
application.yml
```

only.

It does NOT load:

```text
application-dev.properties
application-prod.properties
application-test.properties
```

unless profile is explicitly activated.

---

# Example

Files:

```text
application.properties
application-dev.properties
application-prod.properties
```

No active profile:

```properties
# spring.profiles.active not set
```

Then only:

```text
application.properties
```

gets loaded.

---

# If Active Profile is Set

Example:

```properties
spring.profiles.active=dev
```

Then Spring loads:

```text id="jlwm104"
application.properties
+
application-dev.properties
```

Profile-specific properties override common properties.

---

# Default Profile Name

Internally:

```text
default
```

is considered active when no profile specified.

---

# You Can Create Default Profile Config Too

Example:

```properties id="jlwm106"
application-default.properties
```

Loaded automatically when no active profile exists.

---

# How Active Profile Can Be Set?

## application.properties

```properties id="jlwm107"
spring.profiles.active=dev
```

---

## Environment Variable

```java
SPRING_PROFILES_ACTIVE=prod
```

---

## JVM Argument

```java
-Dspring.profiles.active=prod
```

---

## Command Line

```java
--spring.profiles.active=prod
```

---

# Important Interview Point

If no active profile is set:

* Spring Boot does NOT fail
* it runs using default configuration
* only base configuration gets loaded

---

# Interview-Level Summary

> If no active profile is configured in Spring Boot, the application runs with the default profile.
>
> In this case, only `application.properties` (or `application.yml`) and optionally `application-default.properties` are loaded.
>
> Profile-specific files like `application-dev.properties` or `application-prod.properties` are ignored unless explicitly activated using `spring.profiles.active`.
