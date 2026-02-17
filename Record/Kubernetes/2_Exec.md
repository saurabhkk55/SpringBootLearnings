# To **go inside a running container in Kubernetes**, you use:

```
kubectl exec -it <pod-name> -- /bin/bash
```

---

## ✅ Explanation (Simple)

- `exec` → runs a command inside container
- `-it` → interactive terminal
- `<pod-name>` → your pod name
- `-- /bin/bash` → shell inside container

---

## 🔹 If Bash is NOT available

Some containers only have `sh`, so use:

```
kubectl exec -it <pod-name> -- /bin/sh
```

---

## 🔹 If Pod Has Multiple Containers

Specify container name:

```
kubectl exec -it <pod-name> -c <container-name> -- /bin/bash
```

---

## 🔹 Example

```
kubectl exec -it nginx-pod -- /bin/bash
```

---

## 🔥 Quick Tip (Real DevOps Practice)

First get pod name:

```
kubectl get pods
```

Then exec into it.

---

✅ **One-line shortcut pros use**

```
kubectl exec -it $(kubectl get pod -l app=myapp -o jsonpath="{.items[0].metadata.name}") -- sh
```
