**Heap** aur **Stack** dono **RAM (main memory)** ka part hote hain. Jab Java application run hoti hai, JVM operating system se memory allocate karti hai aur us memory ko alag-alag areas mein divide karti hai, jisme Stack aur Heap bhi shamil hain.

### Memory Layout (Simplified)

```
RAM
┌──────────────────────┐
│ Operating System     │
├──────────────────────┤
│ JVM Process          │
│                      │
│  ┌───────────────┐   │
│  │ Heap Memory   │   │
│  └───────────────┘   │
│                      │
│  ┌───────────────┐   │
│  │ Stack Thread1 │   │
│  └───────────────┘   │
│                      │
│  ┌───────────────┐   │
│  │ Stack Thread2 │   │
│  └───────────────┘   │
│                      │
└──────────────────────┘
```

### Stack Memory

* Har thread ka **apna stack** hota hai.
* Method calls, local variables, method parameters yahan store hote hain.
* Method complete hone par uska stack frame automatically remove ho jata hai.

Example:

```java
public void test() {
    int age = 25;
    String name = "Saurabh";
}
```

Stack:

```
test()
 ├── age = 25
 └── name (reference)
```

### Heap Memory

* Heap poori application ke liye shared hoti hai.
* Objects aur arrays heap mein bante hain.
* Garbage Collector unused objects ko remove karta hai.

Example:

```java
Student s = new Student();
```

Stack:

```
s ---> 0x100
```

Heap:

```
0x100 : Student Object
```

### Real-life Analogy

Maan lo tum ek office mein kaam kar rahe ho:

* **Stack** = Tumhari desk.

    * Sirf tum use karte ho.
    * Kaam khatam to papers hata diye jaate hain.

* **Heap** = Office store room.

    * Sab employees use kar sakte hain.
    * Jo samaan kisi ko nahi chahiye, office staff (Garbage Collector) hata deta hai.

### Interview Point

**Heap aur Stack physical locations nahi hain.** Dono JVM process ko mili hui RAM ke logical regions hain:

* **Stack** → Thread-specific memory for method execution.
* **Heap** → Shared memory for objects and instance data.
