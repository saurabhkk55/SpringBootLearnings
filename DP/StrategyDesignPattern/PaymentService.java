package DP.StrategyDesignPattern;

interface PaymentStrategy {
    void pay(double amount);
}

class UPIPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class NetBankingPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Net Banking");
    }
}

class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void makePayment(double amount) {
        if (paymentStrategy == null) {
            System.out.println("Please select a payment method first");
            return;
        }
        paymentStrategy.pay(amount);
    }
}

public class PaymentService {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        context.setPaymentStrategy(new UPIPayment());
        context.makePayment(500);

        context.setPaymentStrategy(new CreditCardPayment());
        context.makePayment(1200);

        context.setPaymentStrategy(new NetBankingPayment());
        context.makePayment(2000);
    }
}


//Payment System
    //Context: PaymentService
    //Strategies:
    //  CreditCardPayment
    //  UPIPayment
    //  NetBankingPayment
    //Task:
    //  Runtime par payment method change karo
    //  pay(amount) method implement karo
