Yes, in newer versions of Spring Boot / Spring Framework, if:

* 2 configuration classes create beans with same name
* and bean overriding is disabled (default behavior)

then application startup fails with:

```text
BeanDefinitionOverrideException
```

---

# Example

## Config 1

```java id="jlwm88"
@Configuration
public class ConfigA {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
```

---

## Config 2

```java id="jlwm89"
@Configuration
public class ConfigB {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
```

Both bean names become:

```text id="jlwm90"
objectMapper
```

Result:

```text id="jlwm91"
BeanDefinitionOverrideException
```

because Spring finds duplicate bean definition.

---

# Why This Happens?

By default, modern Spring Boot versions disable silent bean overriding to avoid:

* accidental overrides
* unpredictable behavior
* debugging difficulty

---

# Exception Example

```text id="jlwm92"
The bean 'objectMapper' could not be registered.
A bean with that name has already been defined.
```

---

# How to Fix?

# Option 1 — Rename Bean (Best Simple Fix)

```java id="jlwm93"
@Bean(name = "customObjectMapper")
```

---

# Option 2 — Use @Primary

If both beans exist but one should be preferred.

```java id="jlwm94"
@Bean
@Primary
```

---

# Option 3 — Use @Qualifier

Explicitly specify which bean to inject.

```java id="jlwm95"
@Qualifier("customObjectMapper")
```

---

# Option 4 — Allow Bean Overriding (Usually Not Recommended)

```properties id="jlwm96"
spring.main.allow-bean-definition-overriding=true
```

This allows second bean to replace first bean silently.

---

# Why Usually NOT Recommended?

Because:

* hides configuration problems
* bean loading order becomes important
* debugging becomes difficult

---

# Important Clarification

If bean names differ:

```java id="jlwm97"
@Bean
public ObjectMapper mapper1()
```

```java id="jlwm98"
@Bean
public ObjectMapper mapper2()
```

application starts successfully.

But injection by type may fail with:

```text id="jlwm99"
NoUniqueBeanDefinitionException
```

because now 2 beans of same type exist.

---

# Interview-Level Summary

> If 2 configuration classes define beans with the same bean name, modern Spring Boot versions throw `BeanDefinitionOverrideException` during startup because bean overriding is disabled by default.
>
> Fixes include:
>
> * renaming bean
> * using `@Primary`
> * using `@Qualifier`
> * enabling `spring.main.allow-bean-definition-overriding=true` (not preferred)
