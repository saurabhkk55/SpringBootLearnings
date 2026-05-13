# Spring Bean Scopes & Initialization

## 1. Singleton Scope (Default Scope)

* **Only one bean instance** is created per Spring IOC container.
* The same bean instance is shared everywhere in the application.

### Bean Creation

* By default, Singleton beans use **EAGER initialization**.
* Bean is created during application startup when the IOC container is initialized.

### Lifecycle

* Bean remains alive until the application context (IOC container) is destroyed.
* Lifecycle exists for the entire application runtime.

### Dependency Injection Behavior

* No matter how many times dependency injection happens, the same object reference is injected.

```java
@Component
@Scope("singleton")
public class MyService {
}
```

---

## 2. Prototype Scope

* A **new bean instance** is created every time the bean is requested from the container.

### Bean Creation

* Prototype beans use **LAZY initialization** by nature.
* Bean is created only when requested/injected.

### Lifecycle

* Spring creates the bean and hands it over to the client.
* After creation, Spring does NOT manage its complete lifecycle.
* Garbage collector removes it when no references exist.

### Dependency Injection Behavior

* Every request/injection gets a new object.

```java
@Component
@Scope("prototype")
public class MyService {
}
```

### Important Clarification

❌ Not exactly “for each HTTP call”.

✔ Prototype means:

* New bean per retrieval/injection/request from IOC container.
* It is unrelated to HTTP unless used inside web flow manually.

Example:

```java
context.getBean(MyService.class);
```

Every call above creates a new object.

---

## 3. Request Scope

* One bean instance per HTTP request.

### Bean Creation

* Bean is lazily created when the first time it is needed during an HTTP request.

### Lifecycle

* Bean lives only for the duration of a single HTTP request.
* Destroyed once response is sent.

### Dependency Injection Behavior

* Within the same request:

    * Same bean instance is reused.
* New HTTP request:

    * New bean instance is created.

```java
@Component
@RequestScope
public class MyService {
}
```

---

## 4. Session Scope

* One bean instance per HTTP session.

### Bean Creation

* Bean is lazily created when first needed in a session.

### Lifecycle

* Bean lives until the HTTP session expires/invalidate.
* Multiple HTTP requests in the same session share the same bean.

### Dependency Injection Behavior

* Same session → same bean is reused regardless of number of dependency injections.
* New session → new bean is used for all dependency injections..

```java
@Component
@SessionScope
public class MyService {
}
```

---

# EAGER vs LAZY Initialization

## EAGER Initialization

* Bean is created during application startup.
* Default for Singleton beans.

```java
@Lazy(false)
```

### Advantages

* Errors detected early during startup.
* Faster runtime access.

### Disadvantages

* Increased startup time.
* Memory used even if bean is never needed.

---

## LAZY Initialization

* Bean is created only when first requested.

```java
@Lazy
```

### Advantages

* Faster application startup.
* Saves memory initially.

### Disadvantages

* First request may be slower.
* Errors discovered later at runtime.

---

# Quick Summary Table

| Scope     | Number of Beans                  | Creation Time | Lifecycle                        |
| --------- | -------------------------------- | ------------- | -------------------------------- |
| Singleton | One per IOC container            | Usually EAGER | Until application stops          |
| Prototype | New every request from container | LAZY          | Until object becomes unreachable |
| Request   | One per HTTP request             | LAZY          | Until HTTP request completes     |
| Session   | One per HTTP session             | LAZY          | Until session expires            |

---

# Important Interview Point

### Singleton + Lazy

Singleton scope does NOT always mean eager initialization.

You can make Singleton lazy:

```java
@Lazy
@Component
public class MyService {
}
```

Then bean will be created only when first needed.
