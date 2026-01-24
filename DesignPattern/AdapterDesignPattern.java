package DesignPattern;

// target
interface PaymentService {
    String pay(int money);
}

// adaptee
class PaypalGateway {
    String sendMoney(int money) {
        int moneyInDollar = money / 90;
        return "$" + moneyInDollar + " is sent successfully via PAYPAL.";
    }
}

// adaptee
class RazorpayGateway {
    String payMoney(int money) {
        return "Rs. " + money + " is sent successfully via RAZORPAY.";
    }
}

// adaptor for paypal
class PaypalGatewayAdapter implements PaymentService {
    PaypalGateway paypalGateway;

    public PaypalGatewayAdapter(PaypalGateway paypalGateway) {
        this.paypalGateway = paypalGateway;
    }

    @Override
    public String pay(int money) {
        return paypalGateway.sendMoney(money);
    }
}

// adaptor for razorpay
class RazorpayGatewayAdapter implements PaymentService {
    RazorpayGateway razorpayGateway;

    public RazorpayGatewayAdapter(RazorpayGateway razorpayGateway) {
        this.razorpayGateway = razorpayGateway;
    }

    @Override
    public String pay(int money) {
        return razorpayGateway.payMoney(money);
    }
}

// client
public class AdapterDesignPattern {
    static void main() {
        PaymentService paymentService = new PaypalGatewayAdapter(new PaypalGateway());

        String pay = paymentService.pay(100);

        System.out.println(pay);

        PaymentService razorpayGatewayPaymentService = new RazorpayGatewayAdapter(new RazorpayGateway());

        String pay1 = razorpayGatewayPaymentService.pay(100);

        System.out.println(pay1);
    }
}
