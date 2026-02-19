package Impetus;

public class Parent {
    static void main() {
        Parent c = new Child();
        c.m1();
    }

    public void m1() {
        System.out.println("Hello m1");
        this.m2();
    }

    public void m2() {
        System.out.println("Hello m2 from parent");
    }
}

class Child extends Parent{
    public void m2() {
        System.out.println("Hello m2 from child");
    }
}

// OUTPUT
// -------------
// Hello m1
// Hello m2 from child