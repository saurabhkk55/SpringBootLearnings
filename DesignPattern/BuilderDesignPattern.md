Bilkul 👍
**Builder Design Pattern** ko simple Hinglish + real-life example ke saath samjhaata hoon.

---

## Builder Design Pattern kya hota hai?

**Builder Pattern** tab use hota hai jab:

* Object **bahut complex** ho
* **Bahut saare optional fields** ho
* Constructor me 5–10 parameters bhejna **confusing** ho jata ho

👉 Builder pattern object ko **step-by-step** build karta hai.

---

## Real-life example 🧱 (Burger banwana)

Socho tum burger order kar rahe ho 🍔

Options:

* Bun type (regular / wheat)
* Patty (veg / chicken)
* Cheese (yes / no)
* Extra toppings (yes / no)

Agar constructor hota:

```java
new Burger("wheat", "veg", true, true, false, true);
```

🤯 → samajh nahi aa raha kaunsa `true` kis cheez ka hai

---

## Builder Pattern solution ✅

Tum bolte ho:

> Bhaiya, wheat bun do
> Veg patty do
> Cheese add karo
> Extra mayo bhi

Exactly yehi Builder karta hai 👍

---

## Code Example (Simple & Clean)

### 1️⃣ Product class (Object jo banega)

```java
class Burger {
    private String bun;
    private String patty;
    private boolean cheese;
    private boolean mayo;

    // private constructor
    private Burger(BurgerBuilder builder) {
        this.bun = builder.bun;
        this.patty = builder.patty;
        this.cheese = builder.cheese;
        this.mayo = builder.mayo;
    }

    public void showBurger() {
        System.out.println(
            "Burger with " + bun + " bun, " + patty +
            ", cheese: " + cheese +
            ", mayo: " + mayo
        );
    }

    // 🧱 Builder class
    static class BurgerBuilder {
        private String bun;
        private String patty;
        private boolean cheese;
        private boolean mayo;

        public BurgerBuilder setBun(String bun) {
            this.bun = bun;
            return this;
        }

        public BurgerBuilder setPatty(String patty) {
            this.patty = patty;
            return this;
        }

        public BurgerBuilder addCheese(boolean cheese) {
            this.cheese = cheese;
            return this;
        }

        public BurgerBuilder addMayo(boolean mayo) {
            this.mayo = mayo;
            return this;
        }

        public Burger build() {
            return new Burger(this);
        }
    }
}
```

---

### 2️⃣ Client code (Use kaise kare)

```java
public class BuilderPatternDemo {
    public static void main(String[] args) {
        Burger burger = new Burger.BurgerBuilder()
                .setBun("Wheat")
                .setPatty("Veg")
                .addCheese(true)
                .addMayo(false)
                .build();

        burger.showBurger();
    }
}
```

---

## Output

```
Burger with Wheat bun, Veg, cheese: true, mayo: false
```

---

## Builder Pattern kab use kare? 🤔

Use karo jab:

* Constructor me **too many parameters** ho
* **Immutable object** banana ho
* Readability important ho
* Optional fields zyada ho

❌ Mat use karo jab:

* Sirf 2–3 fields ho
* Object simple ho

---

## Builder vs Constructor

| Constructor             | Builder                 |
| ----------------------- | ----------------------- |
| Hard to read            | Easy to read            |
| Order yaad rakhna padta | Order matter nahi karta |
| Optional fields messy   | Clean & flexible        |
