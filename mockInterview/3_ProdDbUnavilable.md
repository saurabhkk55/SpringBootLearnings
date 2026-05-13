This is one of the most common real-world production issues.

Scenario:

```text id="exth0p"
Works on local
Fails in production
Cannot connect to DB
```

Usually error looks like:

```text id="2q3mwl"
Connection refused
Timeout
Unknown host
Authentication failed
Too many connections
SSL error
```

---

# Step-by-Step Debugging Approach

A senior engineer usually checks in this order:

```text id="9c8epf"
1. Configuration
2. Network
3. Credentials
4. DB availability
5. Firewall/Security
6. Connection pool
7. SSL/TLS
8. Kubernetes/Docker networking
9. DNS issues
10. Logs & monitoring
```

---

# 1. Verify Environment Configuration

Most common issue.

Local config:

```yaml id="0q7ggs"
spring.datasource.url=jdbc:mysql://localhost:3306/testdb
```

Production should NOT use localhost.

Correct prod example:

```yaml id="tq6gh7"
spring.datasource.url=jdbc:mysql://prod-db.company.com:3306/proddb
```

---

# Common Mistake

```text id="t7wlro"
localhost in container/server means SAME machine itself
```

Inside Docker/Kubernetes:

```text id="v70qzu"
localhost != actual DB server
```

---

# Check

* DB host
* port
* DB name
* username
* password
* JDBC URL

---

# 2. Verify DB is Reachable

From production server:

```bash id="q0l7d6"
telnet db-host 3306
```

or:

```bash id="l5r91d"
nc -zv db-host 3306
```

If fails:

* network issue
* firewall
* security group
* DB not running

---

# 3. Check Database is Running

Example:

* MySQL
* PostgreSQL
* Oracle Database

---

## Check DB Health

MySQL:

```bash id="r7kpl6"
systemctl status mysql
```

Postgres:

```bash id="7g6j8r"
systemctl status postgresql
```

---

# 4. Verify Credentials

Very common in prod.

Problems:

* wrong username/password
* secret not mounted
* env variable missing
* expired password

---

# Kubernetes Example

Check secret:

```bash id="tlgr04"
kubectl get secret
```

Check env variables:

```bash id="2o5u6m"
kubectl describe pod pod-name
```

---

# 5. Check Firewall / Security Groups

Especially in:

* AWS
* Azure
* GCP

Example:

```text id="h4gq1x"
Application server cannot access DB port
```

---

# AWS Example

Need:

* EC2 security group
* RDS security group
* inbound rules

Port:

```text id="1z0vr5"
3306 -> MySQL
5432 -> PostgreSQL
```

---

# 6. Check Connection Pool Exhaustion

Works locally because:

```text id="vxks5z"
few users
```

Fails in prod because:

```text id="kmn3lj"
many connections
```

---

# Symptoms

```text id="vg7cl4"
HikariPool exhausted
Too many connections
Connection timeout
```

---

# Fix

Increase pool size carefully.

Spring Boot:

```yaml id="4nrksh"
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
```

---

# 7. Check DB Connection Limit

Database may reject connections.

MySQL:

```sql id="i3u2cq"
SHOW VARIABLES LIKE 'max_connections';
```

---

# 8. SSL/TLS Issue

Production DB often requires SSL.

Local may not.

---

## Example Error

```text id="9w3ffu"
SSLHandshakeException
```

---

# Fix

Add SSL params:

```text id="skqlsb"
jdbc:mysql://host:3306/db?useSSL=true
```

or provide certificates.

---

# 9. DNS Resolution Issue

Sometimes:

```text id="o2r3pp"
prod-db.company.com
```

cannot resolve.

---

# Check

```bash id="xavt20"
nslookup prod-db.company.com
```

or:

```bash id="11p0pc"
ping prod-db.company.com
```

---

# 10. Kubernetes Networking Issues

Very common.

---

# Wrong

```text id="c1fsk8"
localhost
```

---

# Correct

Use Kubernetes service name:

```text id="a3k9jt"
mysql-service.default.svc.cluster.local
```

---

# Check

```bash id="v6k0gx"
kubectl get svc
```

---

# 11. Docker Networking Issue

Containers cannot use:

```text id="7u7y4y"
localhost
```

Use:

* container name
* docker network

Example:

```text id="6v0n5p"
jdbc:mysql://mysql-container:3306/db
```

---

# 12. Check Logs Properly

Application logs usually tell exact issue.

Examples:

| Error                 | Meaning           |
| --------------------- | ----------------- |
| Connection refused    | DB unreachable    |
| Unknown host          | DNS issue         |
| Access denied         | Wrong credentials |
| Timeout               | Network/firewall  |
| SSLHandshakeException | SSL issue         |
| Too many connections  | Pool exhaustion   |

---

# 13. Check DB Driver Version

Sometimes:

* local JDK different
* prod JDK different
* driver incompatible

Example:

```text id="x4al7f"
MySQL 5 driver with MySQL 8 server
```

---

# 14. Production Checklist

Senior engineers usually verify:

| Check              | Why                      |
| ------------------ | ------------------------ |
| DB URL             | Correct endpoint         |
| Credentials        | Correct auth             |
| Network access     | Reachability             |
| Security group     | Firewall                 |
| DB running         | Availability             |
| Connection pool    | Resource limits          |
| SSL config         | Secure connectivity      |
| DNS                | Host resolution          |
| Kubernetes service | Correct internal routing |
| Secrets            | Proper mounting          |

---

# Real Production Example

Problem:

```text id="mvdv3s"
Application works locally
Fails in Kubernetes
```

Cause:

```text id="sbr5ux"
Used localhost in JDBC URL
```

Inside pod:

```text id="tcl1if"
localhost means pod itself
```

Fix:

```text id="6bzq8v"
Use MySQL Kubernetes service name
```

---

# Another Common Production Issue

Problem:

```text id="m1hrnq"
Works initially
Fails under load
```

Cause:

```text id="tviyck"
Connection leak
Connections not closed
```

Fix:

* use HikariCP
* close ResultSet/Connection properly
* use JPA repository correctly

---

# Golden Rule

```text id="y5t75w"
If it works locally but not in prod,
first suspect environment/configuration differences.
```

---

# Interview-Level Answer

If interviewer asks:

> "Application connects locally but fails in production. How do you debug?"

Good answer:

```text id="5drs2n"
I would verify:
- datasource configuration
- DB host/port reachability
- credentials and secrets
- firewall/security groups
- connection pool limits
- SSL configuration
- DNS resolution
- Kubernetes/Docker networking
- database availability
- application and DB logs

Then isolate whether the issue is configuration, networking, authentication, or resource exhaustion.
```
