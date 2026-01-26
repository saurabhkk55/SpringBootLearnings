# 🌱 Spring Bean Lifecycle

Spring Bean lifecycle ka **order** ye hota hai 👇

---

## ✅ 1️⃣ Constructor Call Hota Hai

* Sabse pehle **bean ka object create hota hai**
* Matlab **constructor execute hota hai**

```java
@Component
public class Teacher {

    public Teacher() {
        System.out.println("1. Constructor called");
    }
}
```

📌 Is stage pe:

* Dependency inject **nahi** hoti
* Instance variables default values pe hote hain

---

## ✅ 2️⃣ Dependency Injection Hota Hai

* `@Autowired`, constructor injection, setter injection
* Is stage ke baad **dependencies available hoti hain**

```java
@Autowired
private Subject subject;
```

📌 Ab object fully wired ho chuka hota hai

---

## ✅ 3️⃣ Initialization Phase (3 Tarike)

### 🔹 (A) `@PostConstruct` ✅ **Recommended**

* **Java standard annotation**
* Spring ke saath tightly coupled nahi
* Dependency injection ke **baad** call hota hai

```java
@PostConstruct
public void init() {
    System.out.println("3. @PostConstruct called");
}
```

✔ Best practice
✔ Clean & readable
✔ Most commonly used

---

### 🔹 (B) `InitializingBean` Interface (Spring specific)

```java
@Component
public class Teacher implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        System.out.println("afterPropertiesSet() called");
    }
}
```

📌 Downsides:

* Spring-specific → **tight coupling**
* Class level dependency aa jati hai

👉 Interview mein bolna:

> Prefer `@PostConstruct` over `InitializingBean`

---

### 🔹 (C) `@Bean(initMethod = "...")`

```java
@Configuration
public class AppConfig {

    @Bean(initMethod = "init")
    public Teacher teacher() {
        return new Teacher();
    }
}
```

```java
public void init() {
    System.out.println("init method called");
}
```

📌 Use case:

* Jab class third-party ho
* Ya annotation modify na kar sakte ho

---

## ✅ 4️⃣ Bean Ready to Use 🚀

Ab developer:

* Instance variables access kar sakta hai
* Methods call kar sakta hai
* Business logic run kar sakta hai

👉 Ye **actual usage phase** hai

---

## ✅ 5️⃣ Destroy Phase (`@PreDestroy`)

### 🔹 `@PreDestroy` Annotation

* **Java standard annotation**
* Bean destroy hone se **just pehle** call hota hai

```java
@PreDestroy
public void destroy() {
    System.out.println("@PreDestroy called");
}
```

📌 Typical use cases:

* DB connection close
* Thread pool shutdown
* Resource cleanup

---

## 🧨 Bean Kab Destroy Hota Hai?

👉 Jab **IOC Container destroy hota hai**

---

## 🏗 IOC Container Destroy Karne Ke Tarike

### ❌ `context.close()` (Manual – Not Safe)

```java
AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(AppConfig.class);

context.close();
```

❌ Problem:

* Agar exception aa gaya **close() se pehle**
* To `@PreDestroy` execute hi nahi hoga

---

### ✅ `registerShutdownHook()` (Recommended ✔)

```java
AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(AppConfig.class);

context.registerShutdownHook();
```

✔ JVM shutdown pe automatically call hota hai
✔ Java ka feature hai (Spring ka nahi)
✔ **Guaranteed cleanup**

👉 Interview golden line:

> `registerShutdownHook()` is safer than `close()`

---

## 🔁 Destroy Phase ke 3 Tarike (Same Pattern as Init)

### 🔹 (A) `@PreDestroy` ✅ Recommended

### 🔹 (B) `DisposableBean`

```java
public class Teacher implements DisposableBean {

    @Override
    public void destroy() {
        System.out.println("destroy() called");
    }
}
```

❌ Spring specific
❌ Tight coupling

---

### 🔹 (C) `@Bean(destroyMethod = "...")`

```java
@Bean(destroyMethod = "cleanup")
public Teacher teacher() {
    return new Teacher();
}
```

```java
public class Teacher {
    public void cleanup() {
        System.out.println("cleanup() destroy method called");
    }
}
```
---

## 🧠 Final Interview-Ready Lifecycle Order

```
1. Constructor
2. Dependency Injection
3. @PostConstruct / afterPropertiesSet / initMethod
4. Bean in Use
5. @PreDestroy / destroy
```

---

## ⭐ Best Practice Summary (Yaad Rakhne Layak)

| Phase              | Best Option              |
| ------------------ | ------------------------ |
| Init               | `@PostConstruct`         |
| Destroy            | `@PreDestroy`            |
| Container shutdown | `registerShutdownHook()` |

> Note:

Bilkul 💯 **tum sahi ho** — ye baat **Spring Boot specific** hai aur interview mein bolni hi chahiye.
Main isko **clean, corrected & final statement** ke form mein explain kar raha hoon.

---

## ✅ Spring Boot mein `close()` ya `registerShutdownHook()` ki zarurat kyun nahi hoti?

### ✔ Short Answer (Interview-ready):

**Spring Boot automatically IOC container ko shutdown karta hai**, isliye hume manually:

* `context.close()`
* `context.registerShutdownHook()`

likhne ki **koi zarurat nahi hoti**.

---

## 🔍 Reason (Internals samjho)

Spring Boot jab application start karta hai, wo:

```java
SpringApplication.run(Application.class, args);
```

✔ Ye internally:

* `ApplicationContext` create karta hai
* **JVM ke saath shutdown hook register kar deta hai**

---

## 🔄 JVM Shutdown ke Time kya hota hai?

Jab:

* App stop hoti hai
* Server shutdown hota hai
* Ctrl + C press karte ho
* Container (Docker / K8s pod) stop hota hai

👉 Spring Boot automatically:

* IOC container destroy karta hai
* Saare beans ke:

    * `@PreDestroy`
    * `destroyMethod`
    * `DisposableBean.destroy()`

execute karta hai

---

## 🧨 Comparison: Spring Core vs Spring Boot

| Feature                  | Spring Core  | Spring Boot  |
| ------------------------ | ------------ | ------------ |
| Context creation         | Manual       | Auto         |
| Shutdown hook            | Manual       | Auto         |
| `close()` required       | Yes (mostly) | ❌ No         |
| `registerShutdownHook()` | Recommended  | ❌ Not needed |
| `@PreDestroy` execution  | Depends      | ✅ Always     |

---

## ⚠️ Important Clarification (Common Confusion)

### ❌ Ye sirf **Spring Core** mein required hota hai:

```java
AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(AppConfig.class);

context.registerShutdownHook();
```

### ✅ Spring Boot mein **ye code likhna hi nahi chahiye**

---

## 🧠 Interview Golden Lines ⭐

> "In Spring Boot, the ApplicationContext lifecycle is managed by Spring itself, so we do not need to explicitly close the context or register a shutdown hook. Bean destruction callbacks are executed automatically."

---

## ✅ Final Conclusion

* ✔ Spring Boot → **Automatic lifecycle management**
* ✔ `@PreDestroy` always works
* ❌ Manual `close()` / `registerShutdownHook()` not required
* ❌ Writing them in Spring Boot = **bad practice**
