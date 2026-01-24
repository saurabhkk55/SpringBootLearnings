package DesignPattern.FactoryDesignPattern;

import java.util.Objects;

interface Burger {
    void prepareBurger();
}

class BasicBurger implements Burger {
    @Override
    public void prepareBurger() {
        System.out.println("BASIC BURGER is ready.");
    }
}

class StandardBurger implements Burger {
    @Override
    public void prepareBurger() {
        System.out.println("STANDARD BURGER is ready.");
    }
}

class PremiumBurger implements Burger {
    @Override
    public void prepareBurger() {
        System.out.println("PREMIUM BURGER is ready.");
    }
}

class BurgerFactory {
    Burger createBurger(String type) {
        Burger burger = switch (type) {
            case "basic" -> new BasicBurger();
            case "standard" -> new StandardBurger();
            case "premium" -> new PremiumBurger();
            default -> null;
        };
        return burger;
    }
}

public class SimpleFactory {
    static void main() {
        BurgerFactory burgerFactory = new BurgerFactory();
        Burger burger1 = burgerFactory.createBurger("standard");
        burger1.prepareBurger();

        Burger burger2 = burgerFactory.createBurger("premium");
        burger2.prepareBurger();

        Burger burger3 = burgerFactory.createBurger("null");
        if (Objects.isNull(burger3)) {
            System.out.println("NO SUCH BURGER is available.");
        } else {
            burger3.prepareBurger();
        }
    }
}
