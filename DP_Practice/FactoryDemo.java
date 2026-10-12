package DP_Practice;

interface Payment {
    void pay();
}

class UPIPayment implements Payment {
    @Override
    public void pay() {
        System.out.println("Paid using UPI");
    }
}

class DebitCardPayment implements Payment {
    @Override
    public void pay() {
        System.out.println("Paid using DebitCardPayment");
    }
}

interface PaymentFactory {
    Payment createPayment();
}

class UPIPaymentFactory implements PaymentFactory {
    @Override
    public Payment createPayment() {
        return new UPIPayment();
    }
}

class DebitCardPaymentFactory implements PaymentFactory {
    @Override
    public Payment createPayment() {
        return new DebitCardPayment();
    }
}

public class FactoryDemo {
    static void main() {
        PaymentFactory paymentFactory = new UPIPaymentFactory();
        Payment payment = paymentFactory.createPayment();
        payment.pay();

        PaymentFactory paymentFactory1 = new DebitCardPaymentFactory();
        Payment payment1 = paymentFactory1.createPayment();
        payment1.pay();
    }
}

//Output
//Paid using UPI
//Paid using DebitCardPayment
