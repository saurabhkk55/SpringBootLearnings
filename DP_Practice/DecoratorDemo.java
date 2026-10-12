package DP_Practice;



interface Coffee {
    String getDesc();
    int getPrice();
}

class SimpleCoffee implements Coffee {
    @Override
    public String getDesc() {
        return "Simple Coffee";
    }

    @Override
    public int getPrice() {
        return 10;
    }
}

abstract class CoffeeDecorator implements Coffee {
    Coffee coffee;

    CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class MilkDecorator extends CoffeeDecorator {
    MilkDecorator(Coffee coffee) {
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

class SugarDecorator extends CoffeeDecorator {
    SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDesc() {
        return coffee.getDesc() + " Sugar";
    }

    @Override
    public int getPrice() {
        return coffee.getPrice() + 5;
    }
}

public class DecoratorDemo {
    static void main() {
        Coffee coffee = new MilkDecorator(new SimpleCoffee());
        String desc = coffee.getDesc();
        System.out.println(desc);
        int price = coffee.getPrice();
        System.out.println(price);
    }
}
