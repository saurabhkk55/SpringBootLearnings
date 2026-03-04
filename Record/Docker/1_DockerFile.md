# 📘 Dockerfile – Complete Final Guide

## 🔹 What is a Dockerfile?

A **Dockerfile** is a text file containing **instructions** that Docker uses to **build an image**.

* Each instruction creates a **layer**
* Layers are **cached**
* Order of instructions matters

---

# 🔹 Dockerfile Execution Flow (Very Important)

1. Docker reads Dockerfile **top to bottom**
2. Executes instructions **one by one**
3. Creates image layers
4. Final image is used to run containers

---

# 🔹 Core Dockerfile Instructions (Detailed)

---

## 1️⃣ `FROM` – Base Image

Defines the **base image** for your Docker image.

```dockerfile
FROM openjdk:17-jdk-slim
```

### Key Points

* **Mandatory**
* Must be the **first instruction**
* Can have multiple `FROM` (multi-stage build)

### Examples

| App Type    | Base Image                |
| ----------- | ------------------------- |
| Spring Boot | openjdk / eclipse-temurin |
| Node        | node                      |
| Python      | python                    |
| Minimal OS  | alpine                    |

---

## 2️⃣ `WORKDIR` – Working Directory

Sets the **default directory inside the container**.

```dockerfile
WORKDIR /app
```

### Behavior

* All subsequent commands (`RUN`, `COPY`, `ADD`, `CMD`, `ENTRYPOINT`)
  run **inside this directory**
* If directory doesn’t exist → Docker **creates it**

### Example

```dockerfile
WORKDIR /app
RUN touch myFile.txt
```

📌 Result:

```text
/app/myFile.txt
```

✅ **Best practice**: Always use `WORKDIR`

---

## 3️⃣ `COPY` – Copy Files (Recommended)

Copies files **from local machine → image**

```dockerfile
COPY target/user-service.jar app.jar
```

### How paths work

* **Source** → relative to **build context (local machine)**
* **Destination** → relative to `WORKDIR` (if not absolute)

With:

```dockerfile
WORKDIR /app
```

This becomes:

```text
/app/app.jar
```

### Limitations

* ❌ Cannot download from internet
* ❌ Cannot extract archives

✅ **Preferred over ADD**

---

## 4️⃣ `ADD` – Copy with Extra Features (Use Carefully)

```dockerfile
ADD file.txt /app/
```

### ADD can do everything COPY can, plus:

| Feature                | Supported |
| ---------------------- | --------- |
| Copy local files       | ✅         |
| Download from URL      | ✅         |
| Auto-extract tar files | ✅         |

### URL Example

```dockerfile
ADD https://example.com/app.jar /app/app.jar
```

⚠️ **Not recommended**
Better:

```dockerfile
RUN curl -o app.jar https://example.com/app.jar
```

### Archive Extraction

```dockerfile
ADD myfiles.tar.gz /app/
```

Automatically extracts into `/app`

### Recommendation

> **Use COPY unless ADD’s extra behavior is explicitly required**

---

## 5️⃣ `RUN` – Build-Time Command

Executes commands **during image build**.

```dockerfile
RUN apt-get update && apt-get install -y curl
```

### Purpose

* Install dependencies
* Create directories
* Compile code
* OS setup

### Best Practices

❌ Bad (multiple layers)

```dockerfile
RUN apt-get update
RUN apt-get install -y curl
```

✅ Good

```dockerfile
RUN apt-get update && apt-get install -y curl
```

🧠 Mental model:

> **RUN prepares the image**

---

## 6️⃣ `CMD` – Default Runtime Command

Defines the **default command** executed when container starts.

```dockerfile
CMD ["java", "-jar", "app.jar"]
```

### Behavior

* Runs during `docker run`
* Can be **easily overridden**

```bash
docker run myimage bash
```

### Rules

* Only **one CMD** is effective (last one wins)
* Used for **flexibility**

---

## 7️⃣ `ENTRYPOINT` – Fixed Runtime Command

Defines the **main executable** of the container.

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Behavior

* Always runs
* Harder to override

```bash
docker run myimage
# java -jar app.jar
```

Override only via:

```bash
docker run --entrypoint bash myimage
```

### Use case

* Microservices
* Batch jobs
* streams.One-purpose containers

---

## 8️⃣ ENTRYPOINT + CMD (Best Practice ✅)

### Recommended Pattern

```dockerfile
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]
```

### Runtime behavior

```bash
docker run myimage
# java -jar app.jar
```

```bash
docker run myimage other.jar
# java -jar other.jar
```

🧠 Rule:

* `ENTRYPOINT` → fixed command
* `CMD` → default arguments

---

## 9️⃣ `EXPOSE` – Document Port

Documents which port the app listens on.

```dockerfile
EXPOSE 8080
```

⚠️ Does **not** open the port
Still need:

```bash
docker run -p 8080:8080 myimage
```

---

## 🔟 `ENV` – Runtime Environment Variables

```dockerfile
ENV SPRING_PROFILES_ACTIVE=prod
```

Available inside container:

```java
System.getenv("SPRING_PROFILES_ACTIVE");
```

Used for:

* Profiles
* Configs
* Feature flags

---

## 1️⃣1️⃣ `ARG` – Build-Time Variables

```dockerfile
ARG JAR_FILE=target/app.jar
COPY ${JAR_FILE} app.jar
```

### ARG vs ENV

| ARG                  | ENV                        |
| -------------------- | -------------------------- |
| Build time           | Runtime                    |
| Not inside container | Available inside container |

---

## 1️⃣2️⃣ `VOLUME` – Persistent Storage

```dockerfile
VOLUME /data
```

Used for:

* Logs
* DB files
* Uploads

---

## 1️⃣3️⃣ `USER` – Security

```dockerfile
USER appuser
```

📌 Avoid running containers as `root` in production.

---

## 1️⃣4️⃣ `LABEL` – Metadata

```dockerfile
LABEL maintainer="saurabh@example.com"
```

---

# 🔹 Shell Form vs Exec Form (Very Important)

❌ Shell form (avoid)

```dockerfile
CMD java -jar app.jar
```

Problems:

* Signal handling issues
* Bad for Kubernetes

✅ Exec form (recommended)

```dockerfile
CMD ["java", "-jar", "app.jar"]
```

---

# 🔹 Sample Production-Ready Spring Boot Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/user-service.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

# 🔹 streams.One-Line Mental Model

| Instruction | Meaning           |
| ----------- | ----------------- |
| FROM        | Base OS / runtime |
| WORKDIR     | Default folder    |
| COPY        | Safe file copy    |
| ADD         | Copy + magic      |
| RUN         | Build-time setup  |
| CMD         | Default command   |
| ENTRYPOINT  | Main executable   |
| EXPOSE      | Document port     |
| ENV         | Runtime config    |
| ARG         | Build config      |

---

# 🔹 Interview Gold Summary 🏆

```text
RUN        → Build time
CMD        → Default runtime command (overridable)
ENTRYPOINT → Fixed runtime executable
COPY       → Preferred over ADD
WORKDIR    → Always use
```
