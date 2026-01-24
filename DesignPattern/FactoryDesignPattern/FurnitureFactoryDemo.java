//package DesignPattern.FactoryDesignPattern;
//
////Furniture Factory
////Products:
//    //Chair
//    //Sofa
////Families:
//    //ModernChair, ModernSofa
//    //VictorianChair, VictorianSofa
//
//interface Chair {
//    String constructChair();
//}
//
//interface Sofa {
//    String constructSofa();
//}
//
//class ModernChair implements Chair {
//    @Override
//    public String constructChair() {
//        return "Creating a modern chair";
//    }
//}
//
//class ModernSofa implements Sofa {
//    @Override
//    public String constructSofa() {
//        return "Creating a modern sofa";
//    }
//}
//
//class VictorianChair implements Chair {
//    @Override
//    public String constructChair() {
//        return "Creating a victorian chair";
//    }
//}
//
//class VictorianSofa implements Sofa {
//    @Override
//    public String constructSofa() {
//        return "Creating a victorian sofa";
//    }
//}
//
//interface FurnitureFactory {
//    Chair createChair();
//    Sofa createSofa();
//}
//
//class ModernFurnitureFactory implements FurnitureFactory {
//    @Override
//    public Chair createChair() {
//        return new ModernChair();
//    }
//
//    @Override
//    public Sofa createSofa() {
//        return new ModernSofa();
//    }
//}
//
//class VictorianFurnitureFactory implements FurnitureFactory {
//    @Override
//    public Chair createChair() {
//        return new VictorianChair();
//    }
//
//    @Override
//    public Sofa createSofa() {
//        return new VictorianSofa();
//    }
//}
//
//class Application {
//    Chair chair;
//    Sofa sofa;
//
//
//    public Application(FurnitureFactory furnitureFactory) {
//        this.chair = furnitureFactory.createChair();
//        this.sofa = furnitureFactory.createSofa();
//    }
//
//    public String buildFurniture() {
//        String chairMsg = chair.constructChair();
//        String sofaMsg = sofa.constructSofa();
//
//        return (chairMsg + " + " + sofaMsg);
//    }
//}
//
//public class FurnitureFactoryDemo {
//    static void main() {
//        FurnitureFactory furnitureFactory = new ModernFurnitureFactory();
//
//        Application app = new Application(furnitureFactory);
//
//        System.out.println(app.buildFurniture());
//
//        System.out.println("------------------");
//
//        FurnitureFactory furnitureFactory1 = new VictorianFurnitureFactory();
//
//        Application app1 = new Application(furnitureFactory1);
//
//        System.out.println(app1.buildFurniture());
//    }
//}
