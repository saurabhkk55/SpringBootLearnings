package DP.FactoryDesignPattern;

import java.util.Objects;

enum VehicleType {
    BIKE, CAR;
}

interface Vehicle {
    void start();
}

class PetrolBike implements Vehicle {
    @Override
    public void start() {
        System.out.println("Started petrol bike");
    }
}

class PetrolCar implements Vehicle {
    @Override
    public void start() {
        System.out.println("Started petrol car");
    }
}

class ElectricBike implements Vehicle {
    @Override
    public void start() {
        System.out.println("Started electric bike");
    }
}

class ElectricCar implements Vehicle {
    @Override
    public void start() {
        System.out.println("Started electric car");
    }
}

interface VehicleFactory{
    Vehicle createVehicle(VehicleType vehicleType);
}

class PetrolVehicleFactory implements VehicleFactory {
    @Override
    public Vehicle createVehicle(VehicleType vehicleType) {
        if(Objects.isNull(vehicleType)) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        return switch(vehicleType) {
            case BIKE -> new PetrolBike();
            case CAR -> new PetrolCar();
        };
    }
}

class ElectricVehicleFactory implements VehicleFactory {
    @Override
    public Vehicle createVehicle(VehicleType vehicleType) {
        if(Objects.isNull(vehicleType)) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        return switch(vehicleType) {
            case BIKE -> new ElectricBike();
            case CAR -> new ElectricCar();
        };
    }
}

public class VehicleService {
    static void main() {
        VehicleFactory vehicleFactory = new PetrolVehicleFactory();
        Vehicle vehicle = vehicleFactory.createVehicle(VehicleType.CAR);
        vehicle.start();

        VehicleFactory vehicleFactory2 = new ElectricVehicleFactory();
        Vehicle vehicle2 = vehicleFactory2.createVehicle(VehicleType.BIKE);
        vehicle2.start();

        vehicleFactory2.createVehicle(VehicleType.CAR).start();
        vehicleFactory2.createVehicle(null).start();
    }
}

//Vehicle Factory
//Vehicles:
    //Car
    //Bike
//Fuel Types:
    //PetrolCar, PetrolBike
    //ElectricCar, ElectricBike

// =============== OUTPUT ===============
//Started petrol car
//Started electric bike
//Started electric car
//Exception in thread "main" java.lang.IllegalArgumentException: Vehicle type cannot be null
//at DP.FactoryDesignPattern.ElectricVehicleFactory.createVehicle(VehicleService.java:63)
//at DP.FactoryDesignPattern.VehicleService.main(VehicleService.java:84)
