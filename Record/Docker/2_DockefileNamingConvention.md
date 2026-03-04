## 1️⃣ Do we always have to name it `Dockerfile`?

### ❌ NO — it is **not mandatory**

### ✔️ Default & recommended name

```text
Dockerfile
```

Docker **automatically looks for a file named `Dockerfile`**.

### ✔️ You *can* use a different name

Examples:

```text
Dockerfile.dev
Dockerfile.prod
Dockerfile.test
```

📌 But if you change the name, you **must explicitly tell Docker** which file to use.

---

## 2️⃣ How to build an image using **only `Dockerfile`**?

If your file name is exactly **`Dockerfile`** and you are in the same directory:

```bash
docker build -t user-service .
```

### Explanation

| Part         | Meaning                           |
| ------------ | --------------------------------- |
| docker build | Build image                       |
| -t           | Tag (image name)                  |
| user-service | Image name                        |
| .            | Build context (current directory) |

📌 Docker automatically:

* Picks `Dockerfile`
* Uses current directory as build context

---

## 3️⃣ What is “build context”? (Very Important)

```bash
docker build -t app .
```

* `.` = current directory
* Docker can **only access files inside build context**
* `COPY target/app.jar` works **only if `target/` is inside `.`**

⚠️ Dockerfile **cannot access parent directories** (`../`)

---

## 4️⃣ If we have **multiple Dockerfiles**, how to build a specific one?

### ✔️ Use `-f` (file flag)

```bash
docker build -f Dockerfile.dev -t user-service-dev .
```

### Another example

```bash
docker build -f docker/Dockerfile.prod -t user-service-prod .
```

📌 You can place Dockerfile **anywhere**, but build context must include required files.

---

## 5️⃣ Typical Real-World Setup (Recommended)

```text
project-root/
│
├── Dockerfile            # default
├── Dockerfile.dev
├── Dockerfile.prod
├── target/
│   └── app.jar
```

### Commands

```bash
docker build -t app .
docker build -f Dockerfile.dev -t app-dev .
docker build -f Dockerfile.prod -t app-prod .
```

---

## 6️⃣ Very Common Interview Traps 🚨

### ❓ Can Dockerfile name be lowercase?

❌ No
Docker expects **exact case**: `Dockerfile`

---

### ❓ Can Dockerfile be named `Dockerfile.txt`?

❌ No (unless you use `-f`)

---

### ❓ Is Dockerfile extension mandatory?

❌ No extension required

---

### ❓ Can we build without Dockerfile?

❌ No
Docker image **must be built from Dockerfile**

---

## 7️⃣ streams.One-Line Summary (Memorize This)

```text
Default name → Dockerfile
Custom name → use -f
Build context → mandatory
Docker looks only inside build context
```
