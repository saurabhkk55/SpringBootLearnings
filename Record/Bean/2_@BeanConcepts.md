# 🌱 `@Bean` Annotation – Complete & Correct Explanation (Beginner → Pro)

---

## 1️⃣ Spring Bean ka matlab kya hai?

👉 **Spring Bean = object jo Spring IOC container ke andar manage hota hai**

Spring ye kaam karta hai:

* object create karta hai
* lifecycle manage karta hai
* dependency inject karta hai

❗ **Har object bean nahi hota**
✔ Sirf wahi object bean hota hai jo **Spring ke control mein ho**

---

## 2️⃣ Bean kaise banti hai? (2 main ways)

### ✅ Way 1: Stereotype annotations

```java
@Component
@Service
@Repository
@Controller
```

➡ Jab class **tumhari apni ho**
➡ Jab class editable ho

---

### ❌ Problem: 3rd-party classes

### ❓ 3rd-party class kya hoti hai?

* Jo tumhare project mein likhi hui nahi hoti
* Dependency se aati hai (Add dependency in the pom.xml, it will make the 3rd party class to be accessible in your project but still we need to create an object in the class marked with @configuration annotation)
* Editable nahi hoti

### Examples:

* `ModelMapper`
* `ObjectMapper`
* `PasswordEncoder`
* `RestTemplate`

Tum **in classes pe `@Component` nahi laga sakte** ❌

---

## 3️⃣ Sirf dependency add karne se bean nahi banti ❌

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.1.1</version>
</dependency>
```

✔ Class available ho gayi
❌ Bean automatically create **nahi** hui

👉 **Spring ko explicitly bolna padega: “is class ka bean banao”**

---

## 4️⃣ `@Bean` ka real purpose 💡

> **`@Bean` is used to explicitly create and register an object into the Spring IOC container, mostly for 3rd-party classes.**

---

## 5️⃣ `@Configuration` + `@Bean` (MOST IMPORTANT)

```java
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```

### 🔍 Internally Spring kya karta hai?

1. `@Configuration` class ko scan karta hai
2. `@Bean` method ko call karta hai
3. Jo object return hota hai usko:

    * IOC container mein store karta hai
    * Bean bana deta hai

✔ Ab `ModelMapper` **Spring-managed bean** hai

---

## 6️⃣ Bean use kaise hoti hai? (Autowiring)

```java
@Service
public class UserService {

    @Autowired
    private ModelMapper modelMapper;

    public void test() {
        System.out.println(modelMapper);
    }
}
```

### 🖨 Output:

```
org.modelmapper.ModelMapper@5a07e868
```

✔ Matlab Spring ne dependency inject kar di

---

## 7️⃣ Custom example (MaheshBabu 🎬)

### Simple class (normal POJO)

```java
public class MaheshBabu {

    public void act() {
        System.out.println("Mahesh Babu is acting 🔥");
    }
}
```

---

### Bean creation using `@Bean`

```java
@Configuration
public class ActorConfig {

    @Bean
    public MaheshBabu maheshBabu() {
        return new MaheshBabu();
    }
}
```

---

### Bean usage

```java
@Component
public class Movie {

    @Autowired
    private MaheshBabu maheshBabu;

    @PostConstruct
    public void start() {
        maheshBabu.act();
    }
}
```

### 🖨 Output:

```
Mahesh Babu is acting 🔥
```

---

## 8️⃣ Bean name kaise decide hota hai?

### ✅ Case 1: Method name = Bean name (DEFAULT)

```java
@Bean
public MaheshBabu maheshBabu() { }
```

🟢 Bean name → `maheshBabu`

---

### ✅ Case 2: Explicit name

```java
@Bean(name = "maheshBabu")
public MaheshBabu actor() { }
```

🟢 Bean name → `maheshBabu`

---

### ✅ Case 3: Multiple names (aliases)

```java
@Bean(name = {"mahesh", "babu"})
public MaheshBabu actor() { }
```

✔ Same bean
✔ 2 aliases

```java
@Autowired
@Qualifier("mahesh")
private MaheshBabu mb;
```

---

### ✅ Case 4: `value` attribute

```java
@Bean(value = "myBabu")
public MaheshBabu actor() { }
```

🟢 Bean name / alias → `myBabu`

---

## 9️⃣ `autowireCandidate` – PROPER & CORRECT EXPLANATION ⚠️

```java
@Bean(autowireCandidate = false)
public MaheshBabu maheshBabu() {
    return new MaheshBabu();
}
```

### ❗ IMPORTANT TRUTH:

> **`autowireCandidate = false` bean ko delete nahi karta**

✔ Bean IOC container mein hoti hai
❌ But Spring **automatic injection ke liye consider nahi karta**

---

### ❌ Ye FAIL karega

```java
@Autowired
private MaheshBabu maheshBabu;
```

🛑 Error:

```
No qualifying bean of type 'MaheshBabu'
```

---

### ✅ Ye WORK karega

```java
@Autowired
@Qualifier("maheshBabu")
private MaheshBabu maheshBabu;
```

OR

```java
ApplicationContext context = ...
MaheshBabu mb = context.getBean("maheshBabu", MaheshBabu.class);
```

---

### 📊 Summary Table

| autowireCandidate | Bean present | @Autowired           |
| ----------------- | ------------ | -------------------- |
| true (default)    | ✅ Yes        | ✅ Works              |
| false             | ✅ Yes        | ❌ Fails (by default) |

---

## 🔟 Default behavior (yaad rakhna)

```java
@Bean
public MaheshBabu maheshBabu() { }
```

✔ `autowireCandidate = true` by default
✔ Likhnay ki zarurat nahi

---

## 🔥 Interview-Ready Final Line

> **`@Bean` is used to manually register objects into the Spring IOC container, especially when the class is external or cannot be annotated directly.**

---

> Note

Excellent question 👍
Yahin pe **maximum log confuse hote hain**, so ab main **100% precise, internals-level explanation** de raha hoon — **no hand-waving**.

---

# 🔥 autowireCandidate = false — **ACTUAL INTERNAL BEHAVIOR**

## Sabse pehle ek line yaad rakho 👇

> **`autowireCandidate = false` sirf *automatic type-based autowiring* se bean ko exclude karta hai,
> bean IOC container se remove / unavailable nahi karta.**

---

## Step 1️⃣ Bean actually kahan hoti hai?

```java
@Bean(autowireCandidate = false)
public MaheshBabu maheshBabu() {
    return new MaheshBabu();
}
```

Spring startup pe:

* Ye method **execute hota hai**
* `MaheshBabu` ka object create hota hai
* Bean **IOC container ke bean registry mein register hoti hai**

✔ Bean **exist karti hai**
✔ Bean ka **name = "maheshBabu"**

👉 `autowireCandidate=false` ka **bean creation se koi relation nahi**

---

## Step 2️⃣ Ab `@Autowired` ka decision process samjho 🧠

### Case A ❌ — Only `@Autowired` (by type)

```java
@Autowired
private MaheshBabu maheshBabu;
```

### Spring internally kya karta hai?

1. Type dekhta hai → `MaheshBabu`
2. Container se poochta hai:

   > “Koi bean hai jo:
   >
   > * type = MaheshBabu
   > * **autowireCandidate = true** ho?”

❌ Is case mein:

* Bean type match karti hai
* BUT `autowireCandidate = false`

👉 Spring ke liye ye bean **“eligible hi nahi”** hai

🛑 Result:

```
No qualifying bean of type 'MaheshBabu'
```

---

## Step 3️⃣ 🔑 `@Qualifier` game changer kaise banta hai?

```java
@Autowired
@Qualifier("maheshBabu")
private MaheshBabu maheshBabu;
```

### Ab Spring ka flow change ho jata hai 👇

1. Spring dekhta hai:

    * `@Qualifier("maheshBabu")` present hai

2. Ab Spring **type-based search nahi karta**

3. Spring directly bolta hai:

   > “Mujhe bean ka **exact name** diya gaya hai”

4. Spring IOC container se lookup karta hai:

   ```
   getBean("maheshBabu")
   ```

✔ Bean registry mein bean present hoti hai
✔ Name exact match karta hai

👉 **`autowireCandidate` yahan check hi nahi hota**

---

## 🔥 VERY IMPORTANT LINE (Interview Gold)

> **`@Qualifier` name-based lookup karta hai,
> aur name-based lookup `autowireCandidate` ko bypass kar deta hai.**

---

## Step 4️⃣ Visual flow (easy to remember)

### ❌ Without `@Qualifier`

```
@Autowired
  ↓
By TYPE lookup
  ↓
Check autowireCandidate
  ↓
false ❌
  ↓
Injection fails
```

---

### ✅ With `@Qualifier`

```
@Autowired + @Qualifier
  ↓
By NAME lookup
  ↓
Direct bean registry access
  ↓
autowireCandidate ignored
  ↓
Injection succeeds ✅
```

---

## Step 5️⃣ Direct proof using ApplicationContext

```java
@Autowired
ApplicationContext context;

@PostConstruct
public void test() {
    MaheshBabu mb = context.getBean("maheshBabu", MaheshBabu.class);
    mb.act();
}
```

🖨 Output:

```
Mahesh Babu is acting 🔥
```

👉 Ye proof hai ki:

* Bean exist karti hai
* Sirf autowiring ke rules alag hain

---

## Step 6️⃣ Real-life analogy 🧠

### autowireCandidate = false =

🚫 “HR ke through hire nahi kar sakte”

### @Qualifier =

📞 “Direct referral / direct call”

✔ Employee company mein already hai
✔ HR bypass ho gaya

---

## Step 7️⃣ streams.One-line interview answer 🎯

> **`autowireCandidate = false` excludes a bean from automatic type-based autowiring,
> but the bean is still available in the IOC container and can be injected using name-based lookup with `@Qualifier`.**
