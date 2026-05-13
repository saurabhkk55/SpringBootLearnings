# Custom Bean Stopped Working After Spring Boot Upgrade — Because Default Bean Started Overriding It

# Common Symptoms

* custom configuration ignored
* wrong bean injected
* `NoUniqueBeanDefinitionException`
* `BeanDefinitionOverrideException`
* application uses framework default behavior
* serialization/deserialization changed
* security config stopped working

---

# Solution 1 — Use @Primary (Most Common Fix)

Mark your custom bean as primary.

Example:

```java id="jlwm47"
@Bean
@Primary
public ObjectMapper customObjectMapper() {
    return new ObjectMapper();
}
```

Meaning:

```text id="jlwm48"
Prefer this bean when multiple candidates exist
```

---

# When to Use

Use when:

* you intentionally want your bean to override default behavior
* multiple beans of same type exist

---

# Solution 2 — Use @Qualifier

If multiple beans should coexist.

Example:

```java id="jlwm49"
@Bean
public ObjectMapper customObjectMapper() {
    return new ObjectMapper();
}
```

Inject specifically:

```java id="jlwm50"
@Autowired
@Qualifier("customObjectMapper")
private ObjectMapper objectMapper;
```

---

# When to Use

Use when:

* both beans are needed
* explicit bean selection required

---

# Solution 3 — Exclude Auto Configuration

Sometimes framework auto-config itself must be disabled.

Example:

```java id="jlwm51"
@SpringBootApplication(
    exclude = JacksonAutoConfiguration.class
)
```

or:

```properties id="jlwm52"
spring.autoconfigure.exclude=\
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
```

---

# When to Use

Use when:

* framework auto-config completely conflicts
* you want full control

---

# Solution 4 — Use Conditional Bean Loading

Best practice for reusable/shared libraries.

Example:

```java id="jlwm53"
@Bean
@ConditionalOnMissingBean
public ObjectMapper objectMapper() {
    return new ObjectMapper();
}
```

Meaning:

```text id="jlwm54"
Create bean only if another bean does not already exist
```

---

# Important

Spring Boot itself heavily uses:

```text id="jlwm55"
@ConditionalOnMissingBean
```

inside auto-configurations.

After upgrade:
framework conditions may behave differently.

---

# Solution 5 — Enable Bean Definition Overriding (Usually NOT Recommended)

Older Spring versions allowed overriding silently.

Newer versions are stricter.

Error example:

```text id="jlwm56"
BeanDefinitionOverrideException
```

Temporary fix:

```properties id="jlwm57"
spring.main.allow-bean-definition-overriding=true
```

---

# Why Usually NOT Recommended?

Because:

* hides configuration problems
* creates unpredictable behavior
* harder debugging
* bean loading order issues

Better:

* use `@Primary`
* use `@Qualifier`
* use explicit config

---

# Interview-Level Summary

> After a Spring Boot upgrade, custom beans may stop working because new framework auto-configured beans get created and take precedence.
>
> Common fixes include:
>
> * Mark custom bean with `@Primary`
> * Use `@Qualifier` for explicit injection
> * Exclude conflicting auto-configuration
> * Use `@ConditionalOnMissingBean`
>
> Spring Boot upgrades often change auto-configuration behavior, so checking startup condition evaluation logs is important for debugging bean conflicts.
