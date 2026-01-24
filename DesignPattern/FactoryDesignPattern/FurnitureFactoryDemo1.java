package DesignPattern.FactoryDesignPattern;

enum ChairType {
    OFFICE, DINNING, RECLINER;
}

interface Chair {
    void createChair();
}

interface Sofa {
    void createSofa();
}

class ModernOfficeChair implements Chair {
    @Override
    public void createChair() {
        System.out.println("Modern office chair");
    }
}

class ModernDinningChair implements Chair {
    @Override
    public void createChair() {
        System.out.println("Modern dinning chair");
    }
}

class ModernReclinerChair implements Chair {
    @Override
    public void createChair() {
        System.out.println("Modern recliner chair");
    }
}

class VictorianOfficeChair implements Chair {
    @Override
    public void createChair() {
        System.out.println("Victorian office chair");
    }
}

class VictorianDinningChair implements Chair {
    @Override
    public void createChair() {
        System.out.println("Victorian dinning chair");
    }
}

class VictorianReclinerChair implements Chair {
    @Override
    public void createChair() {
        System.out.println("Victorian recliner chair");
    }
}

class ModernSofa implements Sofa {
    @Override
    public void createSofa() {
        System.out.println("Modern Safa");
    }
}

class VictorianSofa implements Sofa {
    @Override
    public void createSofa() {
        System.out.println("Victorian Safa");
    }
}

interface FurnitureFactory {
    Chair createChair(ChairType type);

    Sofa createSofa();
}

class ModernFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair(ChairType type) {
        return switch (type) {
            case ChairType.OFFICE -> new ModernOfficeChair();
            case ChairType.DINNING -> new ModernDinningChair();
            case ChairType.RECLINER -> new ModernReclinerChair();
            default -> null;
        };
    }

    @Override
    public Sofa createSofa() {
        return new ModernSofa();
    }
}

class VictorianFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair(ChairType type) {
        return switch (type) {
            case ChairType.OFFICE -> new VictorianOfficeChair();
            case ChairType.DINNING -> new VictorianDinningChair();
            case ChairType.RECLINER -> new VictorianReclinerChair();
            default -> null;
        };
    }

    @Override
    public Sofa createSofa() {
        return new VictorianSofa();
    }
}

class Application {
    private final Chair chair;
    private final Sofa sofa;

    public Application(FurnitureFactory furnitureFactory, ChairType type) {
        this.chair = furnitureFactory.createChair(type);
        this.sofa = furnitureFactory.createSofa();
    }

    void buildChair() {
        chair.createChair();
    }

    void buildSofa() {
        sofa.createSofa();
    }
}

public class FurnitureFactoryDemo1 {
    static void main() {
        FurnitureFactory furnitureFactory = new ModernFurnitureFactory();

        Application app = new Application(furnitureFactory, ChairType.OFFICE);

        app.buildChair();
        app.buildSofa();

        FurnitureFactory furnitureFactory1 = new VictorianFurnitureFactory();

        Application app1 = new Application(furnitureFactory1, ChairType.RECLINER);

        app1.buildChair();
        app1.buildSofa();
    }
}
