package railroad.system;

import railroad.entities.Connection;
import railroad.entities.Locomotive;
import railroad.entities.Station;
import railroad.entities.Trainset;
import railroad.entities.cars.*;

public class Presentation {
    public static void main(String[] args) throws Exception {
        RailwaySystem railwaySystem = new RailwaySystem();

        // Creating stations
        Station station1 = railwaySystem.createRailwayStation();
        Station station2 = railwaySystem.createRailwayStation();
        Station station3 = railwaySystem.createRailwayStation();
        System.out.println("Stations have been created");

        // Creating connection
        Connection connection1 = railwaySystem.createConnection(station1, station2, 300);
        Connection connection2 = railwaySystem.createConnection(station2, station3, 300);
        System.out.println("Connections have been created");

        // Creating locomotives
        Locomotive locomotive1 = railwaySystem.createLocomotive("loco1", station1, station1, station3, 10, 100, 10, 50);
        Locomotive locomotive2 = railwaySystem.createLocomotive("loco2", station1, station1, station3, 10, 100, 10, 50);
        Locomotive locomotive3 = railwaySystem.createLocomotive("loco3", station1, station1, station3, 10, 100, 10, 50);
        System.out.println("Locomotives have been created");

        // Creating railroad cars
        Car car1 = railwaySystem.createRailroadCar(RailwaySystem.RailroadCarType.PASSENGER, 10,  100, 100);
        Car car2 = railwaySystem.createRailroadCar(RailwaySystem.RailroadCarType.FREIGHT_BASIC, 10,  100, 100);
        Car car3 = railwaySystem.createRailroadCar(RailwaySystem.RailroadCarType.LIQUID_TOXIC_MATERIALS, 10,  100, 100);
        System.out.println("Railroad cars have been created");

        // Attaching railroad cars to locomotives/ Creating trainsets
        try {
            railwaySystem.attachRailroadCarToLocomotive(locomotive1, car1);
            railwaySystem.attachRailroadCarToLocomotive(locomotive2, car2);
            railwaySystem.attachRailroadCarToLocomotive(locomotive3, car3);
        } catch (Exception e){
            System.out.println("Error occurred while trying to attach railroad car to locomotive: " + e.getMessage());
        }
        System.out.println("Railroad cars have been attached");

        // Loading cars
        try {
            car1.load(1);
            car3.load(10);
        }catch (Exception e){
            System.err.println("Error occurred while trying to load car: " + e.getMessage());
        }

        System.out.println("Railroad cars have been loaded");

        // Showing report on trainset
        Trainset trainsetToReport = railwaySystem.getTrainsets().get(0);
        String report = railwaySystem.trainsetReport(trainsetToReport);
        System.out.println(report);

        // Removing objects
        railwaySystem.removeTrainset(railwaySystem.getTrainsets().get(0), "dontKeepCars");
        railwaySystem.removeConnection(connection2);
        railwaySystem.removeStation(station3);
        railwaySystem.removeCar(car3);
        railwaySystem.removeLocomotive(locomotive3);
        System.out.println("Objects have been deleted");

    }

}