## Understanding `proxyBeanMethod` in Spring `@Configuration`

In Spring, `@Configuration` classes are proxied by default using **CGLIB**. This proxying is what ensures the Singleton nature of your beans, even when you call the `@Bean` methods like regular Java methods.

---
```java
@Configuration(proxyBeanMethods = true)
public class SimpleConfig {

    @Bean
    public A getA() {
        return new A();
    }
}

class A { }
```

By default, **`proxyBeanMethods` is `true`** for the `@Configuration` annotation, even if you don't explicitly specify it.
---

### 1. When `proxyBeanMethod = true` (Default)

When this is active, Spring wraps your `SimpleConfig` class in a proxy.

* **The Behavior:** If you call `getA()` from another method within the same configuration class, the proxy intercepts the call.
* **The Logic:** It checks the ApplicationContext (IoC Container) first. If an instance of `A` already exists, it returns that instance instead of executing your `return new A();` logic again.
* **The Result:** Strict **Singleton** behavior is maintained. Every call returns the exact same memory address.

### 2. When `proxyBeanMethod = false` (Lite Mode)

Setting this to `false` tells Spring **not** to proxy the configuration class. This is often called "Lite" mode.

* **The Behavior:** The `@Bean` methods are treated like plain Java methods.
* **The Logic:** Every time you call `getA()` manually from within the class, the code `return new A();` executes literally.
* **The Result:** You get a **new object** every time you call the method internally.
* **The Benefit:** Faster startup time and lower memory overhead because Spring doesn't have to generate a CGLIB subclass.

---

### Key Comparison Table

| Feature | `proxyBeanMethod = true` (Full Mode) | `proxyBeanMethod = false` (Lite Mode) |
| --- | --- | --- |
| **Proxy Type** | CGLIB Proxy generated. | No Proxy (Direct subclass). |
| **Inter-bean Calls** | Returns the **same** instance (Singleton). | Returns a **new** instance. |
| **Performance** | Slightly slower (Proxy overhead). | Faster startup & less memory. |
| **Use Case** | When beans have dependencies on each other. | When you have independent beans. |

### Important Clarification

It is a common misconception that `proxyBeanMethod = false` makes the bean a "Prototype" scope.

> **Note:** Even with `proxyBeanMethod = false`, if the **Spring Container** injects Bean A into two different places, it will still provide the same instance. The "new object" behavior only happens when you call the method **directly** in your code (e.g., `this.getA()`).
