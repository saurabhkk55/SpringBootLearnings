# What Happens If We Define 2 JPA Repositories for the Same Entity?

In Spring Boot, defining 2 JPA repositories for the same entity is valid.

Example:

```java id="jlwm138"
@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {
}
```

```java id="jlwm139"
@Repository
public interface AuditUserRepository
        extends JpaRepository<User, Long> {
}
```

Both manage:

```java id="jlwm140"
User
```

Application usually starts successfully because:

* bean names are different
* repository interface types are different

---

# Important Clarification

If you inject using repository interface name:

```java id="jlwm141"
@Autowired
private UserRepository userRepository;
```

then Spring knows exactly which bean to inject.

No issue occurs.

---

# When Problem Happens

Problem happens when injecting:

* common parent type
* generic type
* interface shared by multiple beans

Example:

```java id="jlwm142"
@Autowired
private JpaRepository<User, Long> repository;
```

Now Spring finds multiple matching beans:

```text id="jlwm143"
UserRepository
AuditUserRepository
```

Result:

```text id="jlwm144"
NoUniqueBeanDefinitionException
```

because injection becomes ambiguous.

---

# Fix 1 — @Primary

Mark one repository as default/preferred.

```java id="’wini0a"
@Repository
@Primary
public interface UserRepository
        extends JpaRepository<User, Long> {
}
```

Now Spring injects this bean by default.

---

# Fix 2 — @Qualifier

Explicitly specify which bean to inject.

Example:

```java id="jlwm145"
@Autowired
@Qualifier("auditUserRepository")
private JpaRepository<User, Long> repository;
```

---

# When BeanDefinitionOverrideException Happens

`BeanDefinitionOverrideException` happens only when:

* same bean name is defined twice
* bean overriding is disabled (default behavior)

Example:

```java id="jlwm146"
@Bean(name = "userRepository")
```

defined multiple times.

Then startup fails with:

```text id="jlwm147"
BeanDefinitionOverrideException
```

---

# Summary

| Scenario                                       | Result                            |
| ---------------------------------------------- | --------------------------------- |
| Same entity + different repository interfaces  | Works fine                        |
| Injecting exact repository interface           | Works fine                        |
| Injecting common parent type (`JpaRepository`) | `NoUniqueBeanDefinitionException` |
| Same bean name defined twice                   | `BeanDefinitionOverrideException` |
