# pom.xml

```java
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>4.0.0-M1</version>
    <scope>compile</scope>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

# GlobalExceptionHandler.java

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ObjectError fieldName;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Map<String, String> allErrorsMap = new HashMap<>();

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        for (ObjectError error : allErrors) {
            String fieldName = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            allErrorsMap.put(fieldName, defaultMessage);
        }
        return allErrorsMap;
    }
}
```

# User.java

```java
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotNull(message = "Name cannot be null")
    private String name;
    @NotEmpty(message = "UserName cannot be null")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password length should be >= 6 but <= 20")
    private String password;
    @Min(value = 18, message = "Minimum age should be 6")
    @Max(value = 60, message = "Maximum age should be 60")
    private int age;
    @Positive(message = "Salary must be positive")
    private Double salary;
    @Digits(integer = 6, fraction = 2, message = "Account balance must not be grater than 6 figures")
    private Double accountBalance;
    @Email(message = "Email should be in proper format")
    private String email;
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@theCuriousCoder\\.com$",
            message = "Email must be a valid corporate email ending with @theCuriousCoder.com"
    )
    private String corporateEmail;
    @Past(message = "Date of birth must be some past date")
    private LocalDate dob;
    @Future(message = "Expiry must be some future date")
    private LocalDate userExpiry;
    @Size(min = 1, message = "User must have at least 1 hobby")
    @NotNull(message = "Hobbies must not be null")
    private List<String> hobbies;

    @PhoneNumber(message = "Phone number is not in the required format")
    private String phoneNumber;
}

//@NotNull works: value is null, key is absent in the payload.

//@NotEmpty works: value is empty, value is null, key is absent.

//@NotBlank: value is balnk(" "), value is null, key is absent.
```


# UserController.java

```java
@RestController
@RequestMapping("/core")
public class UserController {

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody @Valid User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("New user is successfully created");
    }
}
```

# PhoneNumber.java: Custom Annotation

```java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "Invalid Phone Number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

# PhoneNumberValidator.java: Validator class

```java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final String PHONE_NUMBER = "^(\\+91[\\-\\s]?)?[6-9][0-9]{9}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if(phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }

        return phoneNumber.matches(PHONE_NUMBER);
    }
}
```
