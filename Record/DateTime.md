# Java Date & Time API

## Before Java 8 vs After Java 8

---

# Before Java 8

Java mainly used:

* `java.util.Date`
* `java.util.Calendar`
* `java.text.SimpleDateFormat`

These APIs had many design problems.

---

# Problems Before Java 8

---

## 1. Mutable Objects

Old classes were mutable.

```java
Date d = new Date();
d.setYear(125);
```

The object state changes directly.

### Problems

* Unsafe in multithreading
* Accidental modifications
* Difficult debugging

---

## 2. Not Thread Safe

`SimpleDateFormat` was not thread safe.

```java
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
```

If multiple threads use the same object:

* incorrect parsing
* random exceptions
* corrupted data

---

## 3. Confusing API

```java
new Date(124, 4, 15);
```

Problems:

* Year starts from 1900
* Month starts from 0

Very confusing and error-prone.

---

## 4. Poor Timezone Handling

Timezone handling required lots of boilerplate code.

```java
Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
```

---

## 5. Date & Time Mixed Together

Old `Date` stored:

* date
* time

No proper separation.

---

## 6. Difficult Date Calculations

```java
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DATE, 5);
```

Complex and less readable.

---

# After Java 8

Java 8 introduced the modern:

```java
java.time
```

API inspired by Joda-Time.

---

# Major Classes Introduced

| Class               | Purpose                |
| ------------------- | ---------------------- |
| `LocalDate`         | Only date              |
| `LocalTime`         | Only time              |
| `LocalDateTime`     | Date + Time            |
| `ZonedDateTime`     | Date + Time + Timezone |
| `Instant`           | Machine timestamp      |
| `Period`            | Date difference        |
| `Duration`          | Time difference        |
| `DateTimeFormatter` | Formatting & Parsing   |

---

# Major Improvements After Java 8

| Feature          | Before Java 8 | After Java 8 |
| ---------------- | ------------- | ------------ |
| Mutable          | Yes           | No           |
| Thread Safe      | No            | Yes          |
| Readability      | Poor          | Excellent    |
| Timezone Support | Difficult     | Easy         |
| Date Calculation | Complex       | Simple       |
| API Design       | Poor          | Clean        |

---

# Why Java 8 API is Thread Safe?

All major classes are immutable.

Example:

```java
LocalDate today = LocalDate.now();

LocalDate tomorrow = today.plusDays(1);
```

`today` does not change.

A new object gets created.

Hence:

* thread safe
* no synchronization needed
* no accidental modification

---

# 1. LocalDate (Only Date)

Used when only date is required.

Example:

* DOB
* Joining date
* Expiry date

---

## Create LocalDate

```java
import java.time.LocalDate;

public class Demo {

    public static void main(String[] args) {

        LocalDate today = LocalDate.now();

        System.out.println(today);

        LocalDate customDate = LocalDate.of(2026, 5, 15);

        System.out.println(customDate);
    }
}
```

---

## Common LocalDate Operations

```java
LocalDate today = LocalDate.now();

System.out.println(today.plusDays(5));

System.out.println(today.minusMonths(1));

System.out.println(today.plusYears(2));

System.out.println(today.getDayOfWeek());

System.out.println(today.isLeapYear());
```

---

# 2. LocalTime (Only Time)

Used when only time is needed.

Example:

* Office timing
* Alarm timing
* Store opening hours

---

## Create LocalTime

```java
import java.time.LocalTime;

public class Demo {

    public static void main(String[] args) {

        LocalTime currentTime = LocalTime.now();

        System.out.println(currentTime);

        LocalTime customTime = LocalTime.of(10, 30, 45);

        System.out.println(customTime);
    }
}
```

---

## Common LocalTime Operations

```java
LocalTime time = LocalTime.now();

System.out.println(time.plusHours(2));

System.out.println(time.minusMinutes(15));

System.out.println(time.getHour());

System.out.println(time.getMinute());
```

---

# 3. LocalDateTime (Date + Time)

Used when both date and time are required.

Example:

* Order timestamp
* Login timestamp
* Event timing

---

## Create LocalDateTime

```java
import java.time.LocalDateTime;

public class Demo {

    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now();

        System.out.println(now);

        LocalDateTime custom = LocalDateTime.of(
                        2026,
                        5,
                        15,
                        10,
                        30
                );

        System.out.println(custom);
    }
}
```

---

## Common LocalDateTime Operations

```java
LocalDateTime now =
        LocalDateTime.now();

System.out.println(now.plusDays(2));

System.out.println(now.minusHours(5));

System.out.println(now.getMonth());

System.out.println(now.getYear());
```

---

# 4. Formatting & Parsing

Using:

```java
DateTimeFormatter
```

---

# Common Formatter Patterns

| Pattern | Meaning | Example |
| ------- | ------- | ------- |
| `dd`    | Day     | 15      |
| `MM`    | Month   | 05      |
| `yyyy`  | Year    | 2026    |
| `HH`    | 24-hour | 18      |
| `hh`    | 12-hour | 06      |
| `mm`    | Minute  | 30      |
| `ss`    | Second  | 45      |
| `a`     | AM/PM   | PM      |

---

## Formatting Example

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Demo {

    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formatted = now.format(formatter);

        System.out.println(formatted);
    }
}
```

---

## Parsing Example

Convert String → Date

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Demo {

    public static void main(String[] args) {

        String date = "15-05-2026";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate parsedDate = LocalDate.parse(date, formatter);

        System.out.println(parsedDate);
    }
}
```

---

# 5. ZonedDateTime (Timezone Handling)

Used when timezone matters.

Example:

* International applications
* Flight systems
* Banking systems

---

## Example

```java
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Demo {

    public static void main(String[] args) {

        ZonedDateTime indiaTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        ZonedDateTime usTime = ZonedDateTime.now(ZoneId.of("America/New_York"));

        System.out.println(indiaTime);

        System.out.println(usTime);
    }
}
```

---

# 6. Period (Date Difference)

Used for:

* years
* months
* days

Human calendar difference.

---

## Example

```java
import java.time.LocalDate;
import java.time.Period;

public class Demo {

    public static void main(String[] args) {

        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        LocalDate today = LocalDate.now();

        Period age = Period.between(birthDate, today);

        System.out.println(age.getYears());
    }
}
```

---

## Real-Life Uses

* Age calculation
* Membership validity
* Subscription expiry

---

# 7. Duration (Time Difference)

Used for:

* hours
* minutes
* seconds

Exact time difference.

---

## Example

```java
import java.time.Duration;
import java.time.LocalTime;

public class Demo {

    public static void main(String[] args) {

        LocalTime start = LocalTime.of(10, 0);

        LocalTime end = LocalTime.of(12, 30);

        Duration duration = Duration.between(start, end);

        System.out.println(duration.toMinutes());
    }
}
```

---

## Real-Life Uses

* API execution time
* Video duration
* Timeout calculation
* Job execution time

---

# Period vs Duration

| Feature        | Period            | Duration                |
| -------------- | ----------------- | ----------------------- |
| Measures       | Years/Months/Days | Hours/Minutes/Seconds   |
| Calendar Aware | Yes               | No                      |
| Used With      | LocalDate         | LocalTime/LocalDateTime |
| Example        | Age calculation   | API execution time      |

---

# Important Interview Question

## Why is `DateTimeFormatter` thread safe but `SimpleDateFormat` is not?

---

## `SimpleDateFormat`

* Mutable internal state
* Changes during parsing/formatting
* Unsafe in shared multithreading

---

## `DateTimeFormatter`

* Immutable
* No internal state modification
* Completely thread safe

---

# Before Java 8

```java
private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
```

Needed:

* synchronization
* ThreadLocal
* repeated object creation

---

# After Java 8

```java
private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
```

Safe across all threads.

---

# Best Practices

| Recommendation                        | Reason                    |
| ------------------------------------- | ------------------------- |
| Use `LocalDate` for only date         | Clean separation          |
| Use `LocalTime` for only time         | Better readability        |
| Use `LocalDateTime` for timestamp     | Standard usage            |
| Use `ZonedDateTime` for timezone apps | Correct timezone handling |
| Use `DateTimeFormatter`               | Thread safe               |
| Avoid old `Date` & `Calendar` APIs    | Legacy APIs               |

---

# Final Interview Summary

Java 8 solved old Date API problems by introducing:

* immutable classes
* thread-safe design
* cleaner APIs
* better readability
* proper timezone support
* easy formatting/parsing
* clear separation of date & time concepts
