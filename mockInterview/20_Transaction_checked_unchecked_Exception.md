By default, Spring’s `@Transactional` only rolls back for:

* `RuntimeException`
* `Error`

It does **NOT** rollback for checked exceptions (`Exception`, `IOException`, etc.).

---

# Problem in Real Projects

Most real-world failures are:

* Insufficient balance
* Invalid order state
* Seat already booked
* Payment failed
* Stock unavailable
* Business validation failed

These are usually **business exceptions**, not `NullPointerException`.

---

# Example Problem

```java id="k9m21a"
@Transactional
public void transferMoney() throws Exception {

    withdraw();

    throw new Exception("Insufficient balance");
}
```

## Result

❌ Transaction will NOT rollback.

Why?

Because `Exception` is checked exception.

So DB changes may get committed.

---

# Solution 1 (Most Common) → `rollbackFor`

```java id="q2nxse"
@Transactional(rollbackFor = Exception.class)
public void transferMoney() throws Exception {

    withdraw();

    throw new Exception("Insufficient balance");
}
```

Now transaction will rollback.

---

# Better Real-World Approach

Create custom business exception.

---

# Step 1: Create Business Exception

## Option A → Extend RuntimeException (Most Preferred)

```java id="9gwzrn"
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
```

---

# Step 2: Throw Business Exception

```java id="n3p7g2"
@Transactional
public void bookTicket() {

    reserveSeat();

    if(seatAlreadyBooked()) {
        throw new BusinessException("Seat already booked");
    }
}
```

---

# Result

✅ Automatic rollback happens.

Why?

Because `BusinessException` extends `RuntimeException`.

---

# Why Industry Prefers RuntimeException

Most enterprise applications create:

```text id="hd1n0m"
BusinessException extends RuntimeException
ValidationException extends RuntimeException
ResourceNotFoundException extends RuntimeException
PaymentFailedException extends RuntimeException
```

Benefits:

* Cleaner code
* No mandatory `throws`
* Automatic rollback
* Less boilerplate

---

# Solution 2 → rollbackFor with Custom Checked Exception

If you intentionally want checked exception:

```java id="y7cb5m"
public class PaymentFailedException extends Exception {

    public PaymentFailedException(String msg) {
        super(msg);
    }
}
```

Then:

```java id="v5q2kg"
@Transactional(rollbackFor = PaymentFailedException.class)
public void processPayment() throws PaymentFailedException {

    debitMoney();

    throw new PaymentFailedException("Payment failed");
}
```
