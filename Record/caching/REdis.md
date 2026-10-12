**Optional**: use this if we need to set/customize properties for RedisCacheManger bean

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().prefixCacheNameWith("my-redis-")
                .entryTtl(Duration.ofSeconds(60));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
```

```java
spring.cache.type=redis
spring.data.redis.host=your-redis-host # url
spring.data.redis.port=your-redis-port
spring.data.redis.password=your-redis-password
spring.data.redis.ssl.enabled=false
```

for redis we need to serialize class that we want to store into Redis

```java
class User implements Serialzable {
    String name;
    String email;
}
```
