# Handling Sensitive Credentials in Spring Boot & Cloud-Native Applications

Sensitive information includes:

* Database username/password
* API keys
* JWT secret keys
* OAuth client secrets
* AWS access keys
* Encryption keys
* Third-party service credentials

These should NEVER be hardcoded inside:

```properties id="ylslvd"
application.properties
application.yml
source code
Git repository
Docker image
```

because it creates major security risks.

---

# Why Hardcoding Secrets is Dangerous?

Example:

```properties id="r7ic6x"
spring.datasource.password=mySecret123
```

Problems:

* Can accidentally get pushed to GitHub
* Anyone with repo access can see credentials
* Secrets become difficult to rotate
* Same credentials may get shared across environments
* JAR decompilation may expose secrets
* Violates enterprise security standards

---

# Recommended Secure Approaches

Security maturity generally evolves like this:

```text id="2mkc9q"
Local Dev
   ↓
Environment Variables
   ↓
Docker Secrets
   ↓
Kubernetes Secrets
   ↓
Cloud Secret Manager + IAM Roles
```

---

# 1. Environment Variables (Basic & Common)

Instead of hardcoding values:

```properties id="bbngd7"
spring.datasource.password=mySecret123
```

use:

```properties id="f8v4np"
spring.datasource.password=${DB_PASSWORD}
```

Spring Boot automatically reads environment variables.

---

# Local Machine Configuration

## Linux/Mac

```bash id="swhq7c"
export DB_PASSWORD=mySecret123
```

Run application:

```bash id="m8n4o4"
mvn spring-boot:run
```

or

```bash id="4n7qmw"
java -jar app.jar
```

---

## Windows CMD

```cmd id="0d4t5i"
set DB_PASSWORD=mySecret123
```

---

## Windows PowerShell

```powershell id="0hnwxw"
$env:DB_PASSWORD="mySecret123"
```

---

# Advantages

* Secret stays outside source code
* Safer Git repository
* Environment-specific configuration
* Easy local development

---

# Limitations

Still not enterprise-grade because:

* Visible to machine users
* Can leak through logs/process inspection
* Difficult centralized management

So production systems use stronger mechanisms.

---

# 2. Docker-Level Secret Handling

When app runs in Docker:

## Pass Environment Variable

```bash id="mdohln"
docker run -e DB_PASSWORD=mySecret123 myapp:latest
```

Spring Boot:

```properties id="7z0p1l"
spring.datasource.password=${DB_PASSWORD}
```

---

# Docker Compose Example

```yaml id="1jcv3o"
services:
  app:
    image: myapp
    environment:
      DB_PASSWORD: mySecret123
```

---

# Problem with Plain Docker Env Variables

Secrets can still be exposed using:

```bash id="10r44r"
docker inspect <container>
```

So better production approaches are:

* Docker Secrets
* Kubernetes Secrets
* AWS Secrets Manager

---

# 3. Kubernetes Secrets

Kubernetes stores secrets separately from deployment YAML.

---

# Create Secret

```bash id="y8og6p"
kubectl create secret generic db-secret \
--from-literal=password=mySecret123
```

---

# Verify Secret

```bash id="k7r0aj"
kubectl get secrets
```

---

# Inject Secret into Pod

```yaml id="r4qjlwm"
env:
  - name: DB_PASSWORD
    valueFrom:
      secretKeyRef:
        name: db-secret
        key: password
```

---

# Spring Boot Reads It

```properties id="6vlcku"
spring.datasource.password=${DB_PASSWORD}
```

Kubernetes injects it as environment variable.

---

# Alternative: Mount Secret as File

```yaml id="7aj66d"
volumes:
  - name: secret-volume
    secret:
      secretName: db-secret
```

Application can read file contents directly.

---

# Advantages of Kubernetes Secrets

* Separate from application code
* Easier secret rotation
* Namespace isolation
* RBAC-controlled access
* Better deployment management

---

# Important Reality About Kubernetes Secrets

By default:

```text id="t6l4jw"
Kubernetes secrets are Base64 encoded
NOT strongly encrypted automatically
```

So production systems usually enable:

* etcd encryption
* external secret management integration

---

# 4. AWS Secrets Manager (Enterprise Best Practice)

Instead of storing secrets in:

* application.properties
* Docker image
* Kubernetes YAML

store them securely in AWS Secrets Manager.

Example:

```json id="mj0xkz"
{
  "username": "admin",
  "password": "mySecret123"
}
```

---

# Why AWS Secrets Manager?

Benefits:

* Centralized secret management
* Automatic rotation
* KMS encryption
* IAM-based access control
* Audit logging
* Secure secret retrieval

---

# Access Secret Using AWS SDK

Example Java code:

```java id="n5j28y"
SecretsManagerClient client = SecretsManagerClient.create();

GetSecretValueRequest request =
    GetSecretValueRequest.builder()
        .secretId("prod/db-secret")
        .build();

GetSecretValueResponse response =
    client.getSecretValue(request);

String secret = response.secretString();
```

---

# Wrong Approach for AWS Authentication

Never do this:

```properties id="ez7jlwm"
aws.access-key=XXXX
aws.secret-key=YYYY
```

because:

* credentials may leak
* manual rotation needed
* security risk
* shared static credentials

---

# Correct Enterprise Approach

Use:

```text id="9bqarf"
IAM Roles + Kubernetes Service Accounts (IRSA)
```

No hardcoded AWS keys needed.

---

# 5. IAM Roles for Service Accounts (IRSA)

This is the recommended modern cloud-native authentication mechanism.

---

# Goal

Allow Kubernetes pod to securely access AWS services like:

* AWS Secrets Manager
* S3
* DynamoDB
* SQS

without storing AWS credentials anywhere.

---

# IRSA Flow

```text id="aqyjlwm"
Spring Boot App
      ↓
Kubernetes Pod
      ↓
Kubernetes Service Account
      ↓
Service Account Annotation
      ↓
IAM Role
      ↓
AWS STS
      ↓
Temporary AWS Credentials
      ↓
AWS Services
```

---

# Important Clarification

Many developers confuse:

```text id="c4ww7q"
RoleBinding / ClusterRoleBinding
```

with AWS IAM Role binding.

They are completely different things.

---

# Kubernetes RBAC vs AWS IAM

| Feature   | Kubernetes RBAC           | AWS IAM (IRSA)             |
| --------- | ------------------------- | -------------------------- |
| Purpose   | Kubernetes permissions    | AWS permissions            |
| Used For  | Pods, ConfigMaps, Secrets | S3, Secrets Manager        |
| Mechanism | Role/ClusterRole          | IAM Role                   |
| Binding   | RoleBinding               | Service Account Annotation |
| Scope     | Inside Kubernetes         | Inside AWS                 |

---

# Kubernetes RBAC Example

Used for Kubernetes API permissions.

Example:

* read pods
* create deployments
* access configmaps

Uses:

* `Role`
* `ClusterRole`
* `RoleBinding`
* `ClusterRoleBinding`

Example:

```yaml id="d2m32q"
kind: RoleBinding
```

This DOES NOT provide AWS access.

---

# How IAM Role is Actually Bound?

NOT using RoleBinding.

Instead:

```yaml id="7qbvkn"
annotations:
  eks.amazonaws.com/role-arn: arn:aws:iam::123456789:role/my-role
```

inside Kubernetes Service Account.

---

# Step-by-Step IRSA Setup

# Step 1: Create IAM Role

IAM policy example:

```json id="18xvbc"
{
  "Effect": "Allow",
  "Action": [
    "secretsmanager:GetSecretValue"
  ],
  "Resource": "*"
}
```

---

# Step 2: Configure Trust Relationship

IAM Role trust policy:

```json id="splypq"
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Federated": "arn:aws:iam::<ACCOUNT_ID>:oidc-provider/oidc.eks.region.amazonaws.com/id/XXXX"
      },
      "Action": "sts:AssumeRoleWithWebIdentity",
      "Condition": {
        "StringEquals": {
          "oidc.eks.region.amazonaws.com/id/XXXX:sub":
          "system:serviceaccount:default:myapp-sa"
        }
      }
    }
  ]
}
```

Meaning:

```text id="2ehsmu"
Only this Kubernetes service account can assume this IAM role
```

---

# Step 3: Create Kubernetes Service Account

```yaml id="lkdhj7"
apiVersion: v1
kind: ServiceAccount
metadata:
  name: myapp-sa
  namespace: default
  annotations:
    eks.amazonaws.com/role-arn: arn:aws:iam::123456789:role/my-role
```

This annotation is the actual IAM role binding.

---

# Step 4: Use Service Account in Pod

```yaml id="rqarfm"
spec:
  serviceAccountName: myapp-sa
```

Now pod automatically receives temporary AWS credentials.

---

# What Happens Internally?

1. Pod starts
2. Kubernetes injects service account token
3. AWS SDK reads token
4. AWS STS validates token using OIDC
5. Temporary credentials generated
6. Application securely accesses AWS

No static AWS keys required.

---

# Why IRSA is Best Practice?

Because it provides:

* No hardcoded AWS credentials
* Temporary credentials
* Automatic credential rotation
* Least privilege security
* Centralized auditability
* Enterprise-grade security

---

# Real Enterprise Architecture

```text id="t2d1jq"
Spring Boot Application
        ↓
Kubernetes Pod
        ↓
Service Account
        ↓
IAM Role (IRSA)
        ↓
AWS Secrets Manager
        ↓
Database/API Credentials
```

NOT:

```text id="dxfu5r"
application.properties → password=123
```

---

# Security Best Practices

## Never Commit Secrets

Add to `.gitignore`:

```text id="0zjlwm"
.env
application-prod.properties
```

---

# Use Spring Profiles

```text id="6vc1l8"
application-dev.properties
application-prod.properties
```

---

# Rotate Secrets Regularly

Especially:

* DB passwords
* API keys
* Tokens

---

# Follow Least Privilege Principle

Application should only access:

* required secrets
* required AWS resources

NOT full admin access.

---

# Interview Summary

> Sensitive credentials should never be hardcoded inside `application.properties` because it creates major security and operational risks.
>
> Recommended approaches:
>
> * Environment variables for local development
> * Docker/Kubernetes secrets for containerized environments
> * AWS Secrets Manager for enterprise production systems
> * IAM Roles for Service Accounts (IRSA) to avoid hardcoded AWS credentials
>
> Kubernetes `RoleBinding` is used for Kubernetes RBAC permissions and NOT for AWS IAM role binding.
>
> AWS IAM roles are mapped to Kubernetes Service Accounts using Service Account annotations and OIDC-based authentication.
