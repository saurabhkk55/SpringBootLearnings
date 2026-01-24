Short answer: **Share kar rahe hain ✅ — copy nahi hai ❌**

Ab bilkul **clear + to-the-point** samjhte hain 👇

---

## Tumhare code mein object creation

```java
HelloWorld helloWorld = new HelloWorld(10);
```

👉 **Sirf ek hi `HelloWorld` object bana hai**

---

## Threads kaise kaam kar rahe hain?

```java
executorService.execute(() -> helloWorld.printHelloAfter1Second());
executorService.execute(() -> helloWorld.printWorldAfter2Second());
```

👉 **Dono threads same object reference (`helloWorld`) use kar rahe hain**

---

## Variables ka truth 💡

```java
int maxCounter;
int counter = 1;
int helloCounter = 0;
int worldCounter = 0;
```

Ye sab **instance variables** hain (static nahi).

### 👉 Iska matlab:

* Object ke andar **ek hi copy hoti hai**
* **Dono threads wahi same values read/write karte hain**

✔️ `counter++` agar Hello thread ne kiya
→ World thread ko updated value hi milegi

---

## Agar copy hoti toh kab hoti? ❌

### Case 1: Local variables

```java
void method() {
    int x = 10;
}
```

* Har thread ke paas **apni copy**

---

### Case 2: Alag object banaya hota

```java
HelloWorld h1 = new HelloWorld(10);
HelloWorld h2 = new HelloWorld(10);
```

* Thread-1 → `h1`
* Thread-2 → `h2`

👉 Tab **copy hoti**

---

## `static` ka special case ⚠️

```java
static boolean isHelloTurn = true;
```

* Ye **class variable** hai
* Ye bhi **shared** hota hai
* Chahe 10 object bana lo, value ek hi rahegi

---

## Tumhare code ka exact memory picture 🧠

```
Heap:
┌─────────────────────┐
│ HelloWorld object   │
│ ─ maxCounter        │
│ ─ counter           │
│ ─ helloCounter      │
│ ─ worldCounter      │
│ ─ static isHelloTurn│ (class level)
└─────────────────────┘
        ▲
        │
 Thread-1        Thread-2
 (HELLO)         (WORLD)
```

---

## Interview-ready one-liner 🚀

> **Instance variables are shared between threads if threads are operating on the same object.
> Local variables are thread-confined and each thread gets its own copy.**

Agar chaho next:

* race condition kya hota
* volatile vs synchronized
* static + multithreading pitfalls

ye sab bhi solid example ke saath samjha doon 🔥
