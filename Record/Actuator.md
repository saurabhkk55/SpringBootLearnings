# Spring Boot Actuator

Spring Boot Actuator is used for:

* Monitoring application
* Checking health/status
* Viewing metrics
* Debugging
* Observability
* Production monitoring

It provides ready-made endpoints like:

* health
* metrics
* CPU usage
* memory usage
* beans
* env
* mappings
* logs
* thread dump
* heap dump

---

# 1. Add Actuator Dependency

## Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## Gradle

```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

---

# 2. Default Actuator URL

If app runs on:

```text
http://localhost:8080
```

Then actuator base URL becomes:

```text
http://localhost:8080/actuator
```

---

# 3. Expose All Endpoints

By default Spring Boot exposes only a few endpoints.

To expose all:

## application.properties

```properties
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

---

## application.yml

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"

  endpoint:
    health:
      show-details: always
```

---

# 4. Expose Selected Endpoints

Instead of exposing everything:

```properties
management.endpoints.web.exposure.include=health,info,metrics,beans,env
```

Only these endpoints become accessible.

This is recommended in production.

---

# 5. Main Actuator Endpoint

```http
GET /actuator
```

Provides list of all exposed endpoints.

Example:

```json
{
  "_links": {
    "health": {
      "href": "http://localhost:8080/actuator/health"
    },
    "metrics": {
      "href": "http://localhost:8080/actuator/metrics"
    }
  }
}
```

---

# 6. Health Endpoint

## URL

```http
GET /actuator/health
```

## Purpose

Checks whether application is UP or DOWN.

Used by:

* Kubernetes
* Load balancers
* Monitoring systems

---

## Example Response

```json
{
  "status": "UP"
}
```

Detailed response:

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

## What Information It Provides

| Component | Meaning                        |
| --------- | ------------------------------ |
| db        | Database connectivity status   |
| diskSpace | Server disk space availability |
| ping      | Basic app responsiveness       |
| redis     | Redis connection health        |
| mongo     | MongoDB status                 |
| kafka     | Kafka connectivity             |

---

# 7. Metrics Endpoint

## URL

```http
GET /actuator/metrics
```

## Purpose

Shows all available application metrics.

Example:

```json
{
  "names": [
    "system.cpu.usage",
    "process.cpu.usage",
    "jvm.memory.used",
    "http.server.requests"
  ]
}
```

---

# 8. CPU Metrics

## System CPU Usage

```http
GET /actuator/metrics/system.cpu.usage
```

## Provides

Overall machine/server CPU usage.

Example:

```json
{
  "name": "system.cpu.usage",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 0.18
    }
  ]
}
```

`0.18` means 18% CPU usage.

---

## Process CPU Usage

```http
GET /actuator/metrics/process.cpu.usage
```

## Provides

CPU consumed only by your Spring Boot application process.

Difference:

| Metric            | Meaning                                 |
| ----------------- | --------------------------------------- |
| system.cpu.usage  | Entire machine CPU usage                |
| process.cpu.usage | Only current Java application CPU usage |

---

# 9. JVM Memory Metrics

## URL

```http
GET /actuator/metrics/jvm.memory.used
```

## Provides

How much JVM memory is currently used.

Can show:

* heap memory
* non-heap memory
* eden space
* survivor space

Useful for:

* memory leak analysis
* JVM tuning
* GC monitoring

---

# 10. Environment Endpoint

## URL

```http
GET /actuator/env
```

## Purpose

Shows all application environment properties.

---

## Provides

* application.properties values
* system environment variables
* JVM properties
* OS variables
* active profiles

Example:

```json
{
  "activeProfiles": [
    "dev"
  ]
}
```

---

## Useful For

* Debugging wrong configurations
* Checking profile values
* Checking property overrides

---

# 11. Beans Endpoint

## URL

```http
GET /actuator/beans
```

## Purpose

Shows all Spring beans loaded in application context.

---

## Provides

* Bean names
* Bean types
* Dependencies
* Scope

Example:

```json
{
  "contexts": {
    "application": {
      "beans": {
        "userService": {
          "type": "com.demo.UserService"
        }
      }
    }
  }
}
```

---

## Useful For

* Debugging dependency injection
* Verifying bean creation
* Understanding Spring context

---

# 12. Mappings Endpoint

## URL

```http
GET /actuator/mappings
```

## Purpose

Shows all API mappings.

---

## Provides

* REST endpoints
* HTTP methods
* controller methods
* URL mappings

Example:

```text
GET /users
POST /users
DELETE /users/{id}
```

---

## Useful For

* API debugging
* Verifying routes
* Finding duplicate mappings

---

# 13. Loggers Endpoint

## URL

```http
GET /actuator/loggers
```

## Purpose

Shows and changes log levels dynamically.

---

## Useful For

Change logging without restarting app.

Example:

```text
DEBUG
INFO
ERROR
TRACE
```

---

# 14. Thread Dump Endpoint

## URL

```http
GET /actuator/threaddump
```

## Purpose

Shows all running JVM threads.

---

## Provides

* thread names
* states
* stack traces
* deadlocks

---

## Useful For

* performance analysis
* deadlock debugging
* stuck thread investigation

---

# 15. Heap Dump Endpoint

## URL

```http
GET /actuator/heapdump
```

## Purpose

Downloads JVM heap dump.

---

## Useful For

* memory leak analysis
* OutOfMemory debugging

Can be analyzed using:

* Eclipse MAT
* VisualVM

---

# 16. Config Props Endpoint

## URL

```http
GET /actuator/configprops
```

## Purpose

Shows all `@ConfigurationProperties`.

---

## Useful For

Understanding loaded configurations.

---

# 17. Scheduled Tasks Endpoint

## URL

```http
GET /actuator/scheduledtasks
```

## Purpose

Shows all scheduled jobs.

---

## Useful For

Debugging:

* `@Scheduled`
* cron jobs
* periodic tasks

---

# 18. Caches Endpoint

## URL

```http
GET /actuator/caches
```

## Purpose

Shows cache information.

---

## Useful For

* cache debugging
* cache names
* cache managers

---

# 19. Info Endpoint

## application.properties

```properties
info.app.name=UserService
info.app.version=1.0
info.app.owner=Saurabh
```

---

## URL

```http
GET /actuator/info
```

## Response

```json
{
  "app": {
    "name": "UserService",
    "version": "1.0",
    "owner": "Saurabh"
  }
}
```

---

# 20. Change Actuator Port

```properties
management.server.port=9090
```

Actuator becomes:

```text
http://localhost:9090/actuator
```

---

# 21. Change Base Path

```properties
management.endpoints.web.base-path=/manage
```

URL becomes:

```text
http://localhost:8080/manage
```

---

# 22. Secure Actuator in Production

Never expose everything publicly in production.

Bad:

```properties
management.endpoints.web.exposure.include=*
```

Better:

```properties
management.endpoints.web.exposure.include=health,info,metrics
```

Because endpoints like:

* env
* beans
* heapdump
* threaddump

may expose sensitive information.

---

# 25. Most Common Production Endpoints

Usually exposed:

```properties
management.endpoints.web.exposure.include=health,info,prometheus
```

Sometimes:

```properties
management.endpoints.web.exposure.include=health,metrics
```
