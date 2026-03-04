Bilkul bhai 😄
**Bridge Design Pattern** ko simple Hinglish + real-life example ke saath samjhte hain.

---

## 🔑 Problem kya hoti hai?

Kabhi-kabhi **ek cheez ke 2 dimensions** hote hain:

* *WHAT* (kya kaam karegi)
* *HOW* (kaise karegi)

Agar dono ko ek hi class hierarchy mein daal do, to:

* classes explode ho jaati hain 💥
* chhota change = bahut saari classes change

---

## 🎯 Bridge Pattern ka Idea

> **Abstraction ko Implementation se alag kar do**
> taaki dono independently change ho sakein.

Isiliye naam hai **Bridge** → dono ke beech bridge banata hai 🌉

---

## 🧠 Simple Real-Life Example

### 📺 Remote & TV

* Remote = **Abstraction**
* TV (Sony, Samsung) = **Implementation**

Remote ko ye nahi pata hona chahiye ki:

> "Ye Sony TV hai ya Samsung TV"

Bas usko bolna hai:

> ON / OFF / Volume++

---

## ❌ Without Bridge (Problem)

```text
SonyBasicRemote
SonyAdvancedRemote
SamsungBasicRemote
SamsungAdvancedRemote
```

👉 Har new TV brand × Har new remote type = classes ki baarish 🌧️

---

## ✅ With Bridge (Solution)

### Step 1️⃣ Implementation Interface

```java
interface TV {
    void on();
    void off();
}
```

### Step 2️⃣ Concrete Implementations

```java
class SonyTV implements TV {
    public void on() {
        System.out.println("Sony TV ON");
    }
    public void off() {
        System.out.println("Sony TV OFF");
    }
}

class SamsungTV implements TV {
    public void on() {
        System.out.println("Samsung TV ON");
    }
    public void off() {
        System.out.println("Samsung TV OFF");
    }
}
```

---

### Step 3️⃣ Abstraction (Bridge yahin hai)

```java
abstract class Remote {
    protected TV tv;   // Bridge

    Remote(TV tv) {
        this.tv = tv;
    }

    abstract void powerOn();
    abstract void powerOff();
}
```

---

### Step 4️⃣ Refined Abstraction

```java
class BasicRemote extends Remote {

    BasicRemote(TV tv) {
        super(tv);
    }

    @Override
    void powerOn() {
        tv.on();
    }

    @Override
    void powerOff() {
        tv.off();
    }
}
```

---

### Step 5️⃣ Client Code

```java
public class BridgePatternDemo {
    public static void main(String[] args) {

        TV sonyTV = new SonyTV();
        Remote remote = new BasicRemote(sonyTV);

        remote.powerOn();   // Sony TV ON
        remote.powerOff();  // Sony TV OFF
    }
}
```

---

## 🔥 Ab kya fayda mila?

✔ New TV add karo → Remote classes change nahi
✔ New Remote type add karo → TV classes change nahi
✔ Clean code
✔ Open-Closed Principle follow hota hai

---

## 🧩 Bridge Pattern kab use kare?

* Jab **abstraction + implementation dono grow ho sakte ho**
* Jab inheritance se class explosion ho raha ho
* Jab runtime pe implementation change karni ho

---

## 🧠 streams.One-line yaad rakhne ka rule

> **"Inheritance ki jagah composition use karo, jab 2 dimensions ho"**
