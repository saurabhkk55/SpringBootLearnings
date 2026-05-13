In Spring Boot, annotating a class with both:

```java id="jlwm148"
@Controller
@RestController
```

is redundant but usually does NOT cause an error.

---

# Why?

Because:

```java id="jlwm149"
@RestController
```

already internally includes:

```java id="’wini0b"
@Controller
@ResponseBody
```

So:

```java id="’wini0c"
@RestController
```

is equivalent to:

```java id="’wini0d"
@Controller
@ResponseBody
```

---

# Example

```java id="’wini0e"
@Controller
@RestController
public class UserController {
}
```

Application starts successfully.

But:

```java id="’wini0f"
@Controller
```

becomes unnecessary/redundant.

---

# Actual Effect

Class behaves like:

```text id="’wini0g"
@RestController
```

meaning:

* return values converted to JSON/XML
* view resolver not used
* methods treated as REST APIs

---

# Example

```java id="’wini0h"
@GetMapping("/hello")
public String hello() {
    return "home";
}
```

Response becomes:

```text id="’wini0i"
home
```

NOT:

* JSP page
* Thymeleaf template

because `@ResponseBody` is active via `@RestController`.

---

# Important Clarification

There are NOT:

* 2 controllers created
* 2 `@ResponseBody` annotations applied separately

Spring processes annotations once through merged metadata.

---

# Best Practice

Use only one:

## For REST APIs

```java id="’wini0j"
@RestController
```

---

## For MVC/View Rendering

```java id="’wini0k"
@Controller
```

---

# Interview-Level Summary

> `@RestController` already includes `@Controller` and `@ResponseBody`.
>
> So annotating a class with both `@Controller` and `@RestController` is redundant but does not cause an error.
>
> The class behaves like a REST controller, meaning method return values are written directly to the HTTP response body instead of resolving views.
