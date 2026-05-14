# Java Flight Recorder (JFR)

Java Flight Recorder is a JVM profiling and monitoring tool used to analyze:

* application startup time
* CPU usage
* memory usage
* GC activity
* thread behavior
* lock contention
* slow methods
* performance bottlenecks

Very useful for:

```text
Why application is starting slowly?
```

---

# Common Startup Slowness Causes

* too many beans
* heavy bean initialization
* DB connection delay
* external API calls during startup
* expensive constructors
* large component scanning
* eager initialization
* circular dependencies
* slow Hibernate/JPA initialization

---

# How JFR Helps

JFR records JVM events with very low overhead.

You can analyze:

* which bean took most startup time
* thread waiting
* class loading
* method execution time
* GC pauses

---

# Enable JFR

## Run application with:

```bash
java -XX:StartFlightRecording=filename=app.jfr,duration=60s -jar app.jar
```

This creates:

```text
app.jfr
```

---

# Analyze Recording

Use:

```text
JDK Mission Control (JMC)
```

to open `.jfr` file.

You can inspect:

* CPU hotspots
* startup timeline
* thread states
* allocations
* blocking operations

---

# Spring Boot Startup Profiling

Spring Boot also provides:

```properties
spring.main.log-startup-info=true
```

and:

```properties
logging.level.org.springframework=DEBUG
```

to inspect bean creation.

---

# Use `ApplicationStartup`

Spring Boot 2.4+ provides startup profiling.

Example:

```java
SpringApplication app = new SpringApplication(MyApplication.class);

app.setApplicationStartup(new BufferingApplicationStartup(2048));
```

Can identify slow bean initialization.

---

# Use `@Lazy` to Improve Startup Time

By default:

```text
Singleton beans are eagerly initialized
```

during application startup.

Meaning:

```text
All singleton beans created at startup
```

which may slow startup.

---

# `@Lazy` Solution

Bean gets created only when actually needed.

Example:

```java
@Component
@Lazy
class HeavyService {

    public HeavyService() {
        System.out.println("Heavy bean created");
    }
}
```

Now bean is NOT created during startup.

Created only when first used.

---

# Benefits of `@Lazy`

* faster startup
* lower initial memory usage
* avoids unnecessary bean creation

Useful for:

* rarely used services
* heavy initialization logic
* expensive external connections

---

# But Be Careful

`@Lazy` does NOT remove cost.

It only:

```text
Shifts bean creation from startup time to runtime
```

So first request may become slow.

---

# Global Lazy Initialization

Spring Boot:

```properties
spring.main.lazy-initialization=true
```

Makes all beans lazy.

Useful for:

* development
* debugging startup issues

Not always ideal for production.

---

# Better Production Approach

Instead of blindly using `@Lazy`:

## Find actual bottleneck first

using:

* Java Flight Recorder
* JDK Mission Control
* Spring Startup profiling

Then optimize:

* remove unnecessary beans
* optimize DB initialization
* avoid network calls during startup
* reduce component scanning
* lazy load only expensive beans

---

# Important Interview Point

## JFR

Used for:

```text
Low-overhead JVM performance profiling
```

including:

* startup analysis
* CPU/memory profiling
* thread analysis
* GC analysis

---

## `@Lazy`

Used to:

```text
Delay bean initialization until first usage
```

to improve application startup time.
