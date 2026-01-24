package DesignPattern.FactoryDesignPattern;

enum BurgerType2 {
    BASIC,
    STANDARD,
    PREMIUM;
}

enum GarlicBreadType {
    BASIC_GARLIC_BREAD,
    BASIC_CHEESE_GARLIC_BREAD,
    WHEAT_GARLIC_BREAD,
    WHEAT_CHEESE_GARLIC_BREAD;
}

interface Burger2 {
    void prepareBurger();
}

class BasicBurger2 implements Burger2 {
    @Override
    public void prepareBurger() {
        System.out.println("BASIC BURGER is ready.");
    }
}

class StandardBurger2 implements Burger2 {
    @Override
    public void prepareBurger() {
        System.out.println("STANDARD BURGER is ready.");
    }
}

class PremiumBurger2 implements Burger2 {
    @Override
    public void prepareBurger() {
        System.out.println("PREMIUM BURGER is ready.");
    }
}

class WheatBasicBurger2 implements Burger2 {
    @Override
    public void prepareBurger() {
        System.out.println("BASIC WHEAT BURGER is ready.");
    }
}

class WheatStandardBurger2 implements Burger2 {
    @Override
    public void prepareBurger() {
        System.out.println("STANDARD WHEAT BURGER is ready.");
    }
}

class WheatPremiumBurger2 implements Burger2 {
    @Override
    public void prepareBurger() {
        System.out.println("PREMIUM WHEAT BURGER is ready.");
    }
}

interface GarlicBread {
    void prepare();
}

class BasicGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("BASIC GARLIC BREAD is ready.");
    }
}

class BasicCheeseGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("BASIC CHEESE GARLIC BREAD is ready.");
    }
}

class WheatGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("BASIC WHEAT GARLIC BREAD is ready.");
    }
}

class WheatCheeseGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("BASIC WHEAT CHEESE GARLIC BREAD is ready.");
    }
}

interface MealFactory {
    Burger2 createBurger(BurgerType burgerType);
    GarlicBread createGarlicBread(GarlicBreadType garlicBreadType);
}

class KingBurger2 implements MealFactory {
    @Override
    public Burger2 createBurger(BurgerType type) {
        return switch (type) {
            case BurgerType.BASIC -> new BasicBurger2();
            case BurgerType.STANDARD -> new StandardBurger2();
            case BurgerType.PREMIUM -> new PremiumBurger2();
            default -> null;
        };
    }

    @Override
    public GarlicBread createGarlicBread(GarlicBreadType type) {
        return switch (type) {
            case GarlicBreadType.BASIC_GARLIC_BREAD -> new BasicGarlicBread();
            case GarlicBreadType.BASIC_CHEESE_GARLIC_BREAD -> new BasicCheeseGarlicBread();
            default -> null;
        };
    }
}

class SinghWheatBurger2 implements MealFactory {
    @Override
    public Burger2 createBurger(BurgerType type) {
        return switch (type) {
            case BurgerType.BASIC -> new WheatBasicBurger2();
            case BurgerType.STANDARD -> new WheatStandardBurger2();
            case BurgerType.PREMIUM -> new WheatPremiumBurger2();
            default -> null;
        };
    }

    @Override
    public GarlicBread createGarlicBread(GarlicBreadType type) {
        return switch (type) {
            case GarlicBreadType.WHEAT_GARLIC_BREAD -> new WheatGarlicBread();
            case GarlicBreadType.WHEAT_CHEESE_GARLIC_BREAD -> new WheatCheeseGarlicBread();
            default -> null;
        };
    }
}

public class AbstractFactoryMethod {
    static void main() {
        MealFactory mealFactory = new KingBurger2();
        Burger2 burger1 = mealFactory.createBurger(BurgerType.PREMIUM);
        burger1.prepareBurger();
        GarlicBread garlicBread = mealFactory.createGarlicBread(GarlicBreadType.BASIC_CHEESE_GARLIC_BREAD);
        garlicBread.prepare();

        MealFactory mealFactory1 = new SinghWheatBurger2();
        Burger2 burger2 = mealFactory1.createBurger(BurgerType.BASIC);
        burger2.prepareBurger();
        GarlicBread garlicBread1 = mealFactory1.createGarlicBread(GarlicBreadType.WHEAT_GARLIC_BREAD);
        garlicBread1.prepare();
    }
}
