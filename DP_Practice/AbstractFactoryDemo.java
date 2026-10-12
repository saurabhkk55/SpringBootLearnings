package DP_Practice;

interface Vehicle {
    void drive();
}

class ElectricCar implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving Electric Car");
    }
}

class ElectricBike implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving Electric Bike");
    }
}

class PetrolCar implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving Petrol Car");
    }
}

class PetrolBike implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving Petrol Bike");
    }
}

interface VehicleFactory {
    Vehicle getCar();
    Vehicle getBike();
}

class PetrolVehicle implements VehicleFactory {

    @Override
    public Vehicle getCar() {
        return new PetrolCar();
    }

    @Override
    public Vehicle getBike() {
        return new PetrolBike();
    }
}

class ElectricVehicle implements VehicleFactory {

    @Override
    public Vehicle getCar() {
        return new ElectricCar();
    }

    @Override
    public Vehicle getBike() {
        return new ElectricBike();
    }
}

public class AbstractFactoryDemo {
    static void main() {
        VehicleFactory electricVehicle = new ElectricVehicle();
        Vehicle car = electricVehicle.getCar();
        car.drive();

        Vehicle bike = electricVehicle.getBike();
        bike.drive();

        VehicleFactory petrolVehicle = new PetrolVehicle();
        Vehicle petrolCar = petrolVehicle.getCar();
        petrolCar.drive();

        Vehicle petrolBike = petrolVehicle.getBike();
        petrolBike.drive();
    }
}
