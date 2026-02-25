package DP.DecoratorDesignPattern;

interface  Coffee{
    String getDesc();
    int getPrice();
}

class SimpleCoffee implements Coffee{
    @Override
    public String getDesc() {
        return "Simple coffee";
    }

    @Override
    public int getPrice() {
        return 10;
    }
}

abstract class CoffeeDecorator implements Coffee {
    Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class Milk extends CoffeeDecorator {

    public Milk(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDesc() {
        return coffee.getDesc() + " + Milk";
    }

    @Override
    public int getPrice() {
        return coffee.getPrice() + 20;
    }
}

class Sugar extends CoffeeDecorator {

    public Sugar(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDesc() {
        return coffee.getDesc() + " + Sugar";
    }

    @Override
    public int getPrice() {
        return coffee.getPrice() + 5;
    }
}

class WhippedCream extends CoffeeDecorator {

    public WhippedCream(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDesc() {
        return coffee.getDesc() + " + Whipped Cream";
    }

    @Override
    public int getPrice() {
        return coffee.getPrice() + 10;
    }
}

public class CoffeeService {
    static void main() {
        Coffee coffee1 = new Sugar(new Milk(new SimpleCoffee()));
        String desc1 = coffee1.getDesc();
        int price1 = coffee1.getPrice();
        System.out.println("Coffee: " + desc1);
        System.out.println("Price: " + price1);

        System.out.println("========================");

        Coffee coffee2 = new SimpleCoffee();
        coffee2 = new Milk(coffee2);
        coffee2 = new Sugar(coffee2);
        coffee2 = new WhippedCream(coffee2);
        String desc2 = coffee2.getDesc();
        int price2 = coffee2.getPrice();
        System.out.println("Coffee: " + desc2);
        System.out.println("Price: " + price2);
    }
}
