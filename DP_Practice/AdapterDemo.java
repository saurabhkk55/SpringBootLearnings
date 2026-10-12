package DP_Practice;

interface PaymentGateway {
    void pay();
}

class Paypal {
    public void dollarPay() {
        System.out.println("Paid in dollar using Paypal");
    }
}

class UPIPay {
    public void rupeesPay() {
        System.out.println("Paid in rupees using UPI");
    }
}

class PayPalPaymentGateway implements PaymentGateway {
    Paypal paypal;

    PayPalPaymentGateway(Paypal paypal) {
        this.paypal = paypal;
    }

    @Override
    public void pay() {
        paypal.dollarPay();
    }
}

class UPIPaymentGateway implements PaymentGateway {
    UPIPay uPIPay;

    UPIPaymentGateway(UPIPay uPIPay) {
        this.uPIPay = uPIPay;
    }

    @Override
    public void pay() {
        uPIPay.rupeesPay();
    }
}

public class AdapterDemo {
    static void main() {
        PaymentGateway pg = new PayPalPaymentGateway(new Paypal());
        pg.pay();

        PaymentGateway pg1 = new UPIPaymentGateway(new UPIPay());
        pg1.pay();
    }
}
