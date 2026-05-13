Stale or outdated cache data is one of the biggest problems in caching.
It happens when:

* DB value is updated
* but cache still contains old value

Example:

```text
DB -> User name = "Saurabh Kumar"
Cache -> User name = "Saurabh"
```

Your application keeps returning old data until cache is refreshed.

---

# Common Ways to Fix Outdated Cache

---

# 1. TTL (Time To Live)

TTL means:

> Automatically remove cache after some time.

Example:

```text
Cache entry valid for 5 minutes
```

After 5 minutes:

* cache expires
* next request fetches fresh data from DB
* cache is rebuilt

---

## Example in Spring Boot (Redis)

```yaml
spring:
  cache:
    redis:
      time-to-live: 600000   # 10 minutes
```

or programmatically:

```java
RedisCacheConfiguration.defaultCacheConfig()
    .entryTtl(Duration.ofMinutes(10));
```

---

## When to use TTL

Good for:

* product catalog
* news feed
* reports
* dashboards
* semi-static data

Not ideal for:

* bank balance
* payment status
* stock trading
* real-time systems

because stale data even for 10 seconds can be dangerous.

---

# 2. Use Proper Cache Key

Bad cache key design causes wrong or outdated data.

---

## Bad Example

```java
@Cacheable(value = "users", key = "'user'")
```

All users share same cache entry.

---

## Correct Example

```java
@Cacheable(value = "users", key = "#id")
public User getUser(Long id)
```

Now:

```text
users::1
users::2
users::3
```

Each user has separate cache.

---

# 3. Use `@CachePut`

`@CachePut`:

* updates DB
* ALSO updates cache immediately

Without this:

* DB updated
* cache still old

---

## Example

```java
@CachePut(value = "users", key = "#user.id")
public User updateUser(User user) {
    return repository.save(user);
}
```

Now:

```text
Update DB -> cache also refreshed
```

---

# Difference

| Annotation    | Behavior                               |
| ------------- | -------------------------------------- |
| `@Cacheable`  | Read from cache if exists              |
| `@CachePut`   | Always execute method and update cache |
| `@CacheEvict` | Remove cache                           |
| `@Caching`    | Combine multiple cache operations      |

---

# 4. Use `@CacheEvict`

When data changes:

* remove old cache
* next request gets fresh data from DB

---

## Example

```java
@CacheEvict(value = "users", key = "#id")
public void deleteUser(Long id) {
    repository.deleteById(id);
}
```

Flow:

```text
Delete DB record
↓
Remove cache
↓
Future requests won't get stale data
```

---

# 5. Use `allEntries=true`

When many cache entries become outdated together.

Example:

* Product prices updated in bulk
* Entire catalog changed

---

## Example

```java
@CacheEvict(value = "products", allEntries = true)
public void refreshCatalog() {
}
```

This clears:

```text
products::1
products::2
products::3
...
```

---

# 6. Write-Through Caching

Update:

* DB
* cache
  together in same flow.

Example:

```text
PUT /user/1
↓
Update DB
↓
Update Cache
```

This reduces stale data chances.

---

# 7. Cache Aside Pattern (Most Common)

Application controls cache manually.

Flow:

```text
Request
↓
Check Cache
↓
Miss?
↓
Read DB
↓
Store in Cache
↓
Return Response
```

When updating:

```text
Update DB
↓
Delete cache
```

Next request rebuilds cache.

This is the most common strategy in microservices.

---

# 8. Event-Based Cache Invalidation

Used in distributed microservices.

Example:

```text
User Service updates profile
↓
Publishes Kafka event
↓
Other services evict their caches
```

Very common in:

* microservices
* distributed systems
* high scale applications

---

# 9. Versioned Cache Key

Instead of:

```text
user:101
```

Use:

```text
user:101:v2
```

Whenever data changes:

```text
user:101:v3
```

Old cache automatically becomes unused.

Useful in:

* CDN
* API caching
* distributed systems

---

# 10. Cache Delete vs Cache Evict

They are conceptually same.

In Spring:

* `@CacheEvict` is used

Internally:

```text
Redis DEL key
```

is executed.

---

# Real Production Strategy

Usually companies combine:

| Technique         | Purpose                       |
| ----------------- | ----------------------------- |
| TTL               | Safety net                    |
| CacheEvict        | Remove stale data             |
| CachePut          | Immediate refresh             |
| Kafka events      | Distributed invalidation      |
| Proper key design | Avoid wrong cache             |
| Redis             | Centralized distributed cache |

---

# Typical Interview Answer

If interviewer asks:

> "How do you handle stale cache data?"

Good answer:

```text
We handle stale cache using:
- proper cache key design
- TTL expiration
- @CachePut for updates
- @CacheEvict for deletes/modifications
- event-driven cache invalidation in microservices
- Redis as centralized distributed cache
```

---

# Example End-to-End Flow

## Read API

```java
@Cacheable(value = "users", key = "#id")
public User getUser(Long id)
```

---

## Update API

```java
@CachePut(value = "users", key = "#user.id")
public User updateUser(User user)
```

---

## Delete API

```java
@CacheEvict(value = "users", key = "#id")
public void deleteUser(Long id)
```

---

# Important Problem in Distributed Systems

Suppose:

```text
Service A cache updated
Service B cache still old
```

This creates inconsistency.

Solution:

* Redis centralized cache
* Kafka invalidation events
* shorter TTL

---

# Golden Rule

```text
Cache is optimization, not source of truth.
Database is source of truth.
```

Always design assuming:

```text
Cache can become stale anytime.
```
