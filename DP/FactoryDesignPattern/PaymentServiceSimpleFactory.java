//package DP.FactoryDesignPattern;
//
//enum PaymentMode {
//    CREDIT_CARD, UPI, NET_BANKING;
//}
//
//interface Payment {
//    void pay(int amount);
//}
//
//class CreditCardPayment implements Payment {
//    @Override
//    public void pay(int amount) {
//        System.out.println("amount " + amount + " is paid via CreditCard");
//    }
//}
//
//class UPIPayment implements Payment {
//    @Override
//    public void pay(int amount) {
//        System.out.println("amount " + amount + " is paid via UPI");
//    }
//}
//
//class NetBankingPayment implements Payment {
//    @Override
//    public void pay(int amount) {
//        System.out.println("amount " + amount + " is paid via NetBanking");
//    }
//}
//
//// it is example of simple factory which is not a design pattern because to voilates 'O' of 'SOLID'
//// 'O' -> class should be open for extension and closed for edit
//class PaymentFactory {
//    public static Payment createPayment(PaymentMode paymentMode) {
//        if (paymentMode == null) {
//            throw new IllegalArgumentException("Payment mode cannot be null");
//        }
//
//        return switch(paymentMode) {
//            case UPI -> new UPIPayment();
//            case CREDIT_CARD -> new CreditCardPayment();
//            case NET_BANKING -> new NetBankingPayment();
//        };
//    }
//}
//
//public class PaymentServiceSimpleFactory {
//    static void main() {
//        Payment payment = PaymentFactory.createPayment(PaymentMode.UPI);
//        payment.pay(500);
//
//        Payment payment1 = PaymentFactory.createPayment(PaymentMode.NET_BANKING);
//        payment1.pay(500);
//
//        Payment payment2 = PaymentFactory.createPayment(PaymentMode.CREDIT_CARD);
//        payment2.pay(500);
//
//        Payment payment4 = PaymentFactory.createPayment(null);
//        payment4.pay(500);
//    }
//}
//
//// Payment Factory (Real-life)
//// Payments: CreditCardPayment, UPIPayment, NetBankingPayment
//// Method: pay(amount)
////	Client ko sirf Payment interface dikhe
//
//// =============== OUTPUT ===============
////amount 500 is paid via UPI
////amount 500 is paid via NetBanking
////amount 500 is paid via CreditCard
////Exception in thread "main" java.lang.IllegalArgumentException: Payment mode cannot be null
////at DP.FactoryDesignPattern.PaymentFactory.createPayment(PaymentService.java:36)
////at DP.FactoryDesignPattern.PaymentService.main(PaymentService.java:58)
