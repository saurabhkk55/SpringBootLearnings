package DP.FactoryDesignPattern;

interface Payment {
    void pay(int amount);
}

class CreditCardPayment implements Payment {
    @Override
    public void pay(int amount) {
        System.out.println("amount " + amount + " is paid via CreditCard");
    }
}

class UPIPayment implements Payment {
    @Override
    public void pay(int amount) {
        System.out.println("amount " + amount + " is paid via UPI");
    }
}

class NetBankingPayment implements Payment {
    @Override
    public void pay(int amount) {
        System.out.println("amount " + amount + " is paid via NetBanking");
    }
}

interface PaymentFactory{
    Payment createPayment();
}

class UPIFactory implements PaymentFactory {
    @Override
    public Payment createPayment() {
        return new UPIPayment();
    }
}

class CreditCardFactory implements PaymentFactory {
    @Override
    public Payment createPayment() {
        return new CreditCardPayment();
    }
}

class NetBankingPaymentFactory implements PaymentFactory {
    @Override
    public Payment createPayment() {
        return new NetBankingPayment();
    }
}

public class PayementServiceFactoryMethod {
    static void main() {
        PaymentFactory UPI_PaymentFactory = new UPIFactory();
        Payment UPI_Payment = UPI_PaymentFactory.createPayment();
        UPI_Payment.pay(500);

        PaymentFactory creditCardPaymentFactory = new CreditCardFactory();
        Payment creditCardPayment = creditCardPaymentFactory.createPayment();
        creditCardPayment.pay(500);

        PaymentFactory netBankingPaymentFactory = new NetBankingPaymentFactory();
        Payment netBankingPayment = netBankingPaymentFactory.createPayment();
        netBankingPayment.pay(500);
    }
}

// ========= OUTPUT
//amount 500 is paid via UPI
//amount 500 is paid via CreditCard
//amount 500 is paid via NetBanking
