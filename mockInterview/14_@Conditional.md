`@Conditional` in Spring Boot is used to **conditionally create or load beans/configurations** based on some condition.

It helps Spring decide:

> “Should I create this bean or not?”

---

# Why do we use `@Conditional`?

Without conditions, every bean inside `@Configuration` gets created.

But in real projects:

* Some beans should load only in specific environments
* Some beans depend on properties
* Some beans depend on classes/libraries
* Some beans should load only if another bean exists

So Spring provides conditional loading.

---

# Basic Syntax

```java
@Conditional(MyCondition.class)
@Bean
public PaymentService paymentService() {
    return new RazorpayService();
}
```

Spring will call `MyCondition` class.

If condition returns:

* `true` → bean created
* `false` → bean not created

---

# Custom Condition Example

## Step 1: Create Condition Class

```java
public class MyCondition implements Condition {

    @Override
    public boolean matches(
            ConditionContext context,
            AnnotatedTypeMetadata metadata) {

        String env = context.getEnvironment()
                            .getProperty("app.env");

        return "prod".equals(env);
    }
}
```

---

## Step 2: Use It

```java
@Configuration
public class AppConfig {

    @Bean
    @Conditional(MyCondition.class)
    public PaymentService paymentService() {
        return new RazorpayService();
    }
}
```

---

# Real-Life Example

Suppose:

* In DEV → use fake payment gateway
* In PROD → use real payment gateway

```java
@Bean
@Conditional(DevCondition.class)
public PaymentService fakeService() {
    return new FakePaymentService();
}

@Bean
@Conditional(ProdCondition.class)
public PaymentService realService() {
    return new RazorpayService();
}
```

---

# Important Built-in Conditional Annotations in Spring Boot

Spring Boot already provides many commonly used conditions.

---

# 1. `@ConditionalOnProperty`

Creates bean only if property exists or has a value.

## Example

```properties
payment.enabled=true
```

```java
@Bean
@ConditionalOnProperty(
    name = "payment.enabled",
    havingValue = "true"
)
public PaymentService paymentService() {
    return new RazorpayService();
}
```

## Usage

Feature toggle / enabling-disabling modules.

---

# 2. `@ConditionalOnMissingBean`

Create bean only if bean is NOT already present.

```java
@Bean
@ConditionalOnMissingBean
public ObjectMapper objectMapper() {
    return new ObjectMapper();
}
```

## Usage

Used heavily in:

* Spring Boot auto-configuration
* Custom default beans

Meaning:

> “If user has not created one, then create default bean.”

---

# 3. `@ConditionalOnBean`

Create bean only if another bean exists.

```java
@Bean
@ConditionalOnBean(DataSource.class)
public UserRepository repo() {
    return new UserRepository();
}
```

## Usage

Dependent bean creation.

---

# 4. `@ConditionalOnClass`

Create bean only if class/library exists in classpath.

```java
@Bean
@ConditionalOnClass(name = "com.mysql.cj.jdbc.Driver")
public MySQLService service() {
    return new MySQLService();
}
```

## Usage

Auto-configurations based on dependencies.

---

# 5. `@ConditionalOnMissingClass`

Opposite of above.

```java
@ConditionalOnMissingClass("redis.clients.jedis.Jedis")
```

---

# 6. `@ConditionalOnExpression`

Condition using SpEL expression.

```java
@ConditionalOnExpression("${app.cache.enabled:true}")
```

---

# 7. `@Profile`

Technically profile-based conditional loading.

```java
@Profile("dev")
@Bean
public EmailService fakeEmailService() {
    return new FakeEmailService();
}
```

## Usage

Environment-specific beans.

---

# Internal Working

When Spring starts:

1. Reads configuration classes
2. Finds `@Conditional`
3. Calls `Condition.matches()`
4. If true:

    * BeanDefinition registered
5. Else:

    * Bean ignored completely

So bean is never created.

---

# Major Usage in Spring Boot

Spring Boot auto-configuration is heavily based on conditional annotations.

Example:

```java
@ConditionalOnClass(DataSource.class)
@ConditionalOnMissingBean
```

Meaning:

> “If JDBC dependency exists and user has not created datasource bean, create default datasource.”

---

# Real Project Use Cases

| Use Case                     | Annotation                  |
| ---------------------------- | --------------------------- |
| Feature toggle               | `@ConditionalOnProperty`    |
| Environment-specific bean    | `@Profile`                  |
| Default bean creation        | `@ConditionalOnMissingBean` |
| Optional library integration | `@ConditionalOnClass`       |
| Dependent bean loading       | `@ConditionalOnBean`        |

---

# Difference: `@Conditional` vs `@Profile`

| `@Conditional`        | `@Profile`              |
| --------------------- | ----------------------- |
| Generic condition     | Only environment-based  |
| Custom logic possible | Simple profile matching |
| More powerful         | Easier to use           |

