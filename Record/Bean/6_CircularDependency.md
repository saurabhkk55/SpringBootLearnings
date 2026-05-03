Circular dependency tab hoti hai jab **2 ya zyada beans ek-dusre pe depend ho jaate hain in a loop** — aur Spring unhe create karte waqt phas jaata hai.

---

# 🔥 Why circular dependency occurs?

### 👉 Example

```java
@Component
class A {
    private final B b;

    public A(B b) {
        this.b = b;
    }
}

@Component
class B {
    private final A a;

    public B(A a) {
        this.a = a;
    }
}
```

👉 Flow:

* Spring tries to create **A**
* A ko chahiye **B**
* Spring tries to create **B**
* B ko chahiye **A**

💥 Infinite loop → Spring confuse → bean create nahi kar pata

---

# ❌ Kaunsa error aata hai?

Most common:

👉 `BeanCurrentlyInCreationException`

Ya detailed message:

```
The dependencies of some of the beans in the application context form a cycle
```

👉 Spring Boot 2.6+ me by default circular dependency **allowed nahi hai**, so app startup fail ho jata hai

---

# 🛠️ Fix kaise kare?

## ✅ 1. Best Fix: Design change (Recommended)

👉 Circular dependency ek **design smell** hai

Instead of:

```
A ↔ B
```

Try:

```
A → C ← B
```

👉 ek **third service / common layer** bana do

---

## ✅ 2. @Lazy use karke (quick fix)

```java
@Component
class A {
    private final B b;

    public A(@Lazy B b) {
        this.b = b;
    }
}
```

👉 `@Lazy` kya karta hai?

* Bean ko immediately create nahi karta
* Jab use hota hai tab banata hai

👉 Result:

* Circular chain break ho jaati hai at startup

---

## ✅ 3. Setter Injection use karo

```java
@Component
class A {
    private B b;

    @Autowired
    public void setB(B b) {
        this.b = b;
    }
}
```

👉 Kyun kaam karta hai?

* Constructor injection me object creation time pe dependency chahiye
* Setter injection me:

    * Pehle object ban jata hai
    * Baad me dependency inject hoti hai

👉 Isse Spring ko breathing space milti hai 😄

---

## ✅ 4. Bean ko alag class me shift karna

👉 Sometimes dependency indirect hoti hai config ki wajah se

Example:

```java
@Configuration
class Config {
    @Bean
    public A a(B b) {
        return new A(b);
    }

    @Bean
    public B b(A a) {
        return new B(a);
    }
}
```

👉 Fix:

* A aur B ko alag-alag config ya layers me divide karo
* Ya ek dependency remove/refactor karo

---

## ⚠️ Important Note

👉 Constructor injection + circular dependency = ❌ fail
👉 Setter / Field injection = ⚠️ sometimes works but not best practice

---

# 🧠 Simple analogy

Socho:

* A bolta hai: "Mujhe B chahiye tab hi main exist karunga"
* B bolta hai: "Mujhe A chahiye tab hi main exist karunga"

👉 Dono ek dusre ka wait karte rahenge → deadlock 😄

---

# 🚀 Final Summary

| Approach              | Status                   |
| --------------------- | ------------------------ |
| Redesign (best)       | ✅ Recommended            |
| @Lazy                 | ✅ Quick fix              |
| Setter Injection      | ✅ Works                  |
| Constructor Injection | ❌ Fails in circular case |
