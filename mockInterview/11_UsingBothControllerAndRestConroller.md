### Redundancy in Spring Annotations

Using both `@Controller` and `@RestController` on a single class is **redundant but harmless**. Since `@RestController` is a **composed annotation** that already includes `@Controller`, Spring simply merges the metadata.

---

### The Anatomy of `@RestController`

The behavior is governed by this hierarchy:

* **`@RestController`** = `@Controller` + `@ResponseBody`

### Key Outcomes

* **No Conflicts:** Spring does not create two separate beans; it processes the class once.
* **REST Behavior Wins:** Because `@ResponseBody` is inherited from `@RestController`, the return values are written directly to the HTTP response (JSON/XML) rather than being resolved as HTML templates.
* **Clean Code:** You should use **only** `@RestController` for APIs and **only** `@Controller` for traditional web pages (JSP/Thymeleaf).

> **Bottom Line:** It's like putting a "Vehicle" sticker on a "Car"—true, but unnecessary. Stick to `@RestController` to keep your code clean and intentional.
