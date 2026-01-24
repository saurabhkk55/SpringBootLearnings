package DesignPattern.FactoryDesignPattern;

enum BurgerType {
    BASIC,
    STANDARD,
    PREMIUM;
}

interface Burger1 {
    void prepareBurger();
}

class BasicBurger1 implements Burger1 {
    @Override
    public void prepareBurger() {
        System.out.println("BASIC BURGER is ready.");
    }
}

class StandardBurger1 implements Burger1 {
    @Override
    public void prepareBurger() {
        System.out.println("STANDARD BURGER is ready.");
    }
}

class PremiumBurger1 implements Burger1 {
    @Override
    public void prepareBurger() {
        System.out.println("PREMIUM BURGER is ready.");
    }
}

class WheatBasicBurger implements Burger1 {
    @Override
    public void prepareBurger() {
        System.out.println("BASIC WHEAT BURGER is ready.");
    }
}

class WheatStandardBurger implements Burger1 {
    @Override
    public void prepareBurger() {
        System.out.println("STANDARD WHEAT BURGER is ready.");
    }
}

class WheatPremiumBurger implements Burger1 {
    @Override
    public void prepareBurger() {
        System.out.println("PREMIUM WHEAT BURGER is ready.");
    }
}

interface BurgerFactory1 {
    Burger1 createBurger(BurgerType burgerType);
}

class KingBurger implements  BurgerFactory1 {
    @Override
    public Burger1 createBurger(BurgerType type) {
        Burger1 burger = switch (type) {
            case BurgerType.BASIC -> new BasicBurger1();
            case BurgerType.STANDARD -> new StandardBurger1();
            case BurgerType.PREMIUM -> new PremiumBurger1();
            default -> null;
        };
        return burger;
    }
}

class SinghWheatBurger implements  BurgerFactory1 {
    @Override
    public Burger1 createBurger(BurgerType type) {
        Burger1 burger = switch (type) {
            case BurgerType.BASIC -> new WheatBasicBurger();
            case BurgerType.STANDARD -> new WheatStandardBurger();
            case BurgerType.PREMIUM -> new WheatPremiumBurger();
            default -> null;
        };
        return burger;
    }
}

public class FactoryMethod {
    static void main() {
        BurgerFactory1 burgerFactory1 = new KingBurger();
        Burger1 burger1 = burgerFactory1.createBurger(BurgerType.PREMIUM);
        burger1.prepareBurger();

        BurgerFactory1 burgerFactory2 = new SinghWheatBurger();
        Burger1 burger2 = burgerFactory2.createBurger(BurgerType.BASIC);
        burger2.prepareBurger();
    }
}
