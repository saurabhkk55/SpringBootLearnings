# Kubernetes Port-Forwarding

**Port-forwarding** lets you access a pod or service running inside Kubernetes **from your local machine** without exposing it publicly (no LoadBalancer / NodePort needed).

It creates a temporary tunnel:

```
Your Laptop:PORT  →  Kubernetes Pod:PORT
```

---

# ✅ Basic Syntax

```
kubectl port-forward <resource> <local-port>:<container-port>
```

---

# 🚀 Most Common Use Cases

---

## 🔹 1. Port-forward to a Pod

```
kubectl port-forward pod/<pod-name> 8080:80
```

Meaning:

```
localhost:8080 → pod:80
```

Now open in browser:

```
http://localhost:8080
```

---


## 🔹 2. Port-forward to a Service (Recommended in Real Projects)

```
kubectl port-forward svc/<service-name> 8080:80
```

Why service is better:

- Pod names change
- Service is stable
- Works even if pods restart

---


## 🔹 3. Run in Specific Namespace

```
kubectl port-forward pod/mypod 8080:80 -n dev
```

---

## 🔹 4. Multiple Ports Forward

```
kubectl port-forward pod/mypod 8080:80 9090:90
```

---

## 🔹 5. Bind to All Interfaces (Access From Other Machines)

By default:

```
only localhost can access
```

To allow others on network:

```
kubectl port-forward pod/mypod 8080:80 --address 0.0.0.0
```

---


# 🎯 Real DevOps Workflow Example

You deployed a Spring Boot app inside Kubernetes.

Steps:

```
kubectl get pods
kubectl port-forward pod/myapp-7d8f9 8080:8080
```

Now test API locally:

```
http://localhost:8080/api/users
```

---

# 🔥 Important Behavior Notes

- ✔ Runs only while terminal is open
- ✔ Stops when you press **CTRL + C**
- ✔ Secure tunnel via Kubernetes API server
- ✔ No firewall or ingress needed

---

# 🧠 When Should You Use Port Forwarding?

Use it when:

- Debugging app locally
- Testing APIs
- Accessing internal dashboards
- Connecting to DB inside cluster
- No ingress configured

---

# ⚡ Pro Tip (Advanced Shortcut)

Auto select first pod of deployment:

```
kubectl port-forward deploy/myapp 8080:80
```

---

# ⭐ Interview-Ready streams.One-Line Definition

> Port-forwarding in Kubernetes temporarily tunnels traffic from a local port to a pod or service port using the Kubernetes API server without exposing it externally.
