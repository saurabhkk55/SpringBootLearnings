It’s impossible to list **literally every** Kubernetes command because Kubernetes has hundreds of subcommands and flags, and new ones are added over time. But below is a **complete practical command reference** covering all major `kubectl` command groups you actually use in real projects.

---

# ✅ Kubernetes Commands Cheat Sheet (Complete Practical List)

> NOTE:
> - add -n <namespace_name> if you want to execute command in a particular
> - When we don't define -n <namespace_name> manually then it means commands will be executed in the default namespace (-n default)

---

## 🔹 1. Cluster Info & Context

```
kubectl cluster-info
kubectl version
kubectl config view
kubectl config get-contexts
kubectl config use-context <context-name>
kubectl config current-context
```

---

## 🔹 2. Get Resources (Most Used)

```
kubectl get pods
kubectl get nodes
kubectl get services
kubectl get deployments
kubectl get replicasets
kubectl get namespaces
kubectl get configmaps
kubectl get secrets
kubectl get ingress
kubectl get all
kubectl get <resource> -o wide
kubectl get <resource> -o yaml
kubectl get <resource> -o json
kubectl get pods -n <namespace>
```

---

## 🔹 3. Describe Resources (Detailed Info)

```
kubectl describe pod <pod-name>
kubectl describe node <node-name>
kubectl describe service <svc-name>
kubectl describe deployment <deployment-name>
```

---

## 🔹 4. Create Resources

```
kubectl create -f file.yaml
kubectl create deployment <name> --image=<image>
kubectl create namespace <name>
kubectl create configmap <name> --from-file=file.txt
kubectl create secret generic <name> --from-literal=key=value
```

---

## 🔹 5. Apply / Update Resources

```
kubectl apply -f file.yaml
kubectl apply -k <kustomize-dir>
```

---

## 🔹 6. Edit Resources (Live Edit)

```
kubectl edit deployment <name>
kubectl edit service <name>
kubectl edit pod <name>
```

---

## 🔹 7. Delete Resources

```
kubectl delete pod <name>
kubectl delete service <name>
kubectl delete deployment <name>
kubectl delete namespace <name>
kubectl delete -f file.yaml
kubectl delete all --all
```

---

## 🔹 8. Logs & Debugging

```yaml
kubectl logs <pod> # If pod has only 1 container no need to specify container name explicitly
kubectl logs <pod> -c <container> # Only if pod has multiple (more than 1) containers
kubectl logs -f <pod>       # stream logs
kubectl logs --previous <pod>
```

---

## 🔹 9. Execute Commands Inside Pod

```
kubectl exec <pod> -- ls
kubectl exec -it <pod> -- bash
kubectl exec -it <pod> -- sh
```

---

## 🔹 10. Port Forwarding

```
kubectl port-forward <pod> 8080:80
kubectl port-forward svc/<service> 8080:80
```

---

## 🔹 11. Scaling Applications

```
kubectl scale deployment <name> --replicas=3
kubectl autoscale deployment <name> --min=2 --max=5 --cpu-percent=70
```

---

## 🔹 12. Rolling Updates & Rollback

```
kubectl rollout status deployment <name>
kubectl rollout history deployment <name>
kubectl rollout undo deployment <name>
kubectl rollout restart deployment <name>
```

---

## 🔹 13. Copy Files To/From Pod

```
kubectl cp file.txt <pod>:/path
kubectl cp <pod>:/path/file.txt .
```

---

## 🔹 14. Top / Metrics

```
kubectl top nodes
kubectl top pods
```

---

## 🔹 15. Label & Annotate

```
kubectl label pod <name> env=prod
kubectl annotate pod <name> description="test pod"
```

---

## 🔹 16. Namespace Operations

```
kubectl get ns
kubectl create ns test
kubectl config set-context --current --namespace=test
```

---

## 🔹 17. Explain Resource Specs (Very Useful)

```
kubectl explain pod
kubectl explain deployment.spec
kubectl explain service.spec.ports
```

---

## 🔹 18. API Resources & Versions

```
kubectl api-resources
kubectl api-versions
```

---

## 🔹 19. Dry Run (Test Without Applying)

```
kubectl apply -f file.yaml --dry-run=client
kubectl create deployment nginx --image=nginx --dry-run=client -o yaml
```

---

## 🔹 20. Authentication & Permissions

```
kubectl auth can-i create pods
kubectl auth can-i delete nodes
```

---

## 🎯 Pro Tips (Real DevOps Usage)

Most commonly used commands in real production:

```
kubectl get pods
kubectl describe pod
kubectl logs
kubectl exec -it
kubectl apply -f
kubectl delete -f
kubectl rollout restart deployment
```

Master these first → you can handle 80% of Kubernetes tasks.

---

# ⭐ Shortcut Aliases (Recommended)

Add these to `.bashrc`:

```
alias k=kubectl
alias kgp='kubectl get pods'
alias kgs='kubectl get svc'
alias kdp='kubectl describe pod'
```
