## 1️⃣ Traditional `try-catch-finally`

### Example

```java
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("data.txt"));
    System.out.println(br.readLine());
} catch (IOException e) {
    e.printStackTrace();
} finally {
    try {
        if (br != null) {
            br.close();   // manual cleanup
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### Problems with this approach

❌ You **must remember** to close the resource
❌ `finally` becomes **messy & repetitive**
❌ If an exception occurs **before `close()`**, resource may leak
❌ If `close()` itself throws an exception, handling becomes tricky

---

## 2️⃣ `try-with-resources` (Java 7+)

### Example

```java
try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    System.out.println(br.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
```

### What Java does internally

✔ Automatically calls `br.close()`
✔ Even if an exception occurs in `try`
✔ No `finally` block needed

---

## 3️⃣ Key Difference (Side-by-Side)

| Feature             | Traditional try-catch | Try-with-resources |
| ------------------- | --------------------- | ------------------ |
| Resource closing    | Manual (`finally`)    | Automatic          |
| Boilerplate code    | More                  | Less               |
| Risk of memory leak | Higher                | Very low           |
| Readability         | Average               | Clean & readable   |
| Introduced in       | Java 1.0              | Java 7             |

---

## 4️⃣ Why try-with-resources works

Any class that implements:

```java
java.lang.AutoCloseable
```

(or `Closeable`) can be used.

Example:

```java
public class MyResource implements AutoCloseable {
    @Override
    public void close() {
        System.out.println("Resource closed");
    }
}
```

---

## 5️⃣ Can we use **multiple statements/resources** in try-with-resources?

👉 **YES ✅**

### Example

```java
try (
    FileInputStream fis = new FileInputStream("input.txt");
    FileOutputStream fos = new FileOutputStream("output.txt")
) {
    fos.write(fis.read());
} catch (IOException e) {
    e.printStackTrace();
}
```

### Important points

✔ Multiple resources are **allowed**
✔ Separated by **semicolon (`;`)**
✔ They are closed in **reverse order**

📌 Closing order:

```
fos.close()
fis.close()
```

---

## 6️⃣ Can we write multiple normal statements inside `try` block?

👉 **Obviously YES**

```java
try (Scanner sc = new Scanner(System.in)) {
    int a = sc.nextInt();
    int b = sc.nextInt();
    int sum = a + b;
    System.out.println(sum);
}
```

---

## 7️⃣ Java 9+ Enhancement (Pro tip 💡)

You can use **already created variables**:

```java
BufferedReader br = new BufferedReader(new FileReader("data.txt"));

try (br) {
    System.out.println(br.readLine());
}
```

✔ Variable must be **effectively final**

---

## 8️⃣ Interview-ready one-liner 🧠

> **Try-with-resources automatically closes resources implementing AutoCloseable, reducing boilerplate code and preventing resource leaks compared to traditional try-catch-finally.**

---

If you want next:

* ❓ **Exception suppression in try-with-resources**
* ❓ **Custom AutoCloseable example**
* ❓ **Real Spring Boot use case**

Just tell me 👍
