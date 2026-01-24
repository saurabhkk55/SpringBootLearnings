package DesignPattern;

interface Pizza {
    String getDescription();
    int getCost();
}

class PlainPizza implements Pizza {

    @Override
    public String getDescription() {
        return "PLAIN PIZZA";
    }

    @Override
    public int getCost() {
        return 100;
    }
}

abstract class ToppingDecorator implements Pizza {
    Pizza pizza;

    public ToppingDecorator(Pizza pizza) {
        this.pizza = pizza;
    }
}

class ExtraCheese extends ToppingDecorator {

    public ExtraCheese(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        pizza.getDescription();
        return pizza.getDescription() + ", EXTRA CHEESE";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 50;
    }
}

class ExtraMushroom extends ToppingDecorator {

    public ExtraMushroom(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", EXTRA MUSHROOM";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 30;
    }
}

public class DecoratorDesignPattern {
    static void main() {
        Pizza pizza = new ExtraMushroom(new ExtraCheese(new ExtraMushroom(new PlainPizza())));

        String description = pizza.getDescription();
        int cost = pizza.getCost();

        System.out.println(description);
        System.out.println(cost);
    }
}
