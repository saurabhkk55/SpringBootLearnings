# pom.xml

```java
<!-- Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-aop -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>4.0.0-M2</version>
    <scope>compile</scope>
</dependency>
```

# Loggable.java (Custom Annotation)

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
}
```

# LoggingAspect.java

```java
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
class LoggingAspect {

    @Around("@annotation(Loggable)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("======================= Before method =======================");

        Object result = joinPoint.proceed();

        System.out.println("======================= After method ========================");

        return result;
    }
}
```


# UserController.java

- Use Custom decorator annotation on method of any class.

```java
@RestController
@RequestMapping("/core")
public class UserController {

    @Loggable
    public void processOrder() {
        System.out.println("Processing...");
    }
}
```

# MainClass.java

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ValidationApplication implements CommandLineRunner {

    @Autowired
    UserController userController;

	public static void main(String[] args) {
		SpringApplication.run(ValidationApplication.class, args);
        System.out.println("ValidationApplication started successfully ✅✅✅");
	}

    @Override
    public void run(String... args) throws Exception {
        userController.processOrder();
    }
}
```

# Output (SpringBoot Console)

```
======================= Before method =======================
Processing...
======================= After method =======================
ValidationApplication started successfully ✅✅✅
```
