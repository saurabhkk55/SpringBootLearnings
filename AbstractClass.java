public abstract class AbstractClass {
    void show() {
        System.out.println("Hello");
    }

    abstract void hi();
}

class s extends AbstractClass {

    @Override
    void hi() {
        System.out.println("hi");
    }
}

class Main {
    static void main() {
        AbstractClass ac = new s();
        ac.hi();
        ac.show();
    }
}