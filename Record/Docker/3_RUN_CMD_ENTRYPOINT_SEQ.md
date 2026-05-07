### 🔥 Actual Execution Flow in Docker

There are **two phases**:

---

## 🏗️ 1. Build Time (Image Creation)

👉 Only **`RUN`** executes here

```
Dockerfile → docker build → Image
```

* `RUN` commands are executed **while building the image**
* Each `RUN` creates a new layer

✅ Example:

```dockerfile
RUN apt-get update
RUN apt-get install -y nginx
```

👉 These run **during build**, NOT when container starts

---

## 🚀 2. Container Start Time

👉 **`CMD` and `ENTRYPOINT`** execute here

```
docker run → container starts
```

---

## ⚡ Final Execution Order

### Case 1: Only CMD

```dockerfile
CMD ["echo", "Hello"]
```

➡️ Runs:

```
echo Hello
```

---

### Case 2: Only ENTRYPOINT

```dockerfile
ENTRYPOINT ["echo", "Hello"]
```

➡️ Runs:

```
echo Hello
```

---

### Case 3: Both ENTRYPOINT + CMD (IMPORTANT ⭐)

```dockerfile
ENTRYPOINT ["echo"]
CMD ["Hello"]
```

➡️ Final execution:

```
echo Hello
```

👉 **ENTRYPOINT runs first**
👉 **CMD acts as arguments to ENTRYPOINT**

---

## 🎯 Final Sequence Summary

```
1. RUN        → during docker build
2. ENTRYPOINT → when container starts
3. CMD        → passed as arguments to ENTRYPOINT (or runs alone if no ENTRYPOINT)
```

---

## ⚠️ Important Notes

* Only **one CMD** is allowed → last one wins
* Only **one ENTRYPOINT** is used → last one wins
* `docker run` can override:
    * CMD easily
    * ENTRYPOINT (with `--entrypoint`)
