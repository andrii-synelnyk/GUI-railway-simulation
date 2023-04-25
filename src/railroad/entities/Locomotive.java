package railroad.entities;

import railroad.entities.cars.Car;
import railroad.system.RailroadHazard;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class Locomotive {
    private static int uniqueId = 0;
    private int id;
    private String name;
    private Station homeStation;
    private Station sourceStation;
    private Station destinationStation;
    private int maxRailroadCars;
    private int maxWeight;
    private int maxElectricalConnections;
    private HashSet<Car> railroadCars;
    private double speed;

    public Locomotive(String name, Station homeStation, Station sourceStation, Station destinationStation, int maxRailroadCars, int maxWeight,
                      int maxElectricalConnections, double speed) {
        this.id = uniqueId++;
        this.name = name;
        this.homeStation = homeStation;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.maxRailroadCars = maxRailroadCars;
        this.maxWeight = maxWeight;
        this.maxElectricalConnections = maxElectricalConnections;
        this.speed = speed;

        this.railroadCars = new HashSet<>();
    }

    public int getTotalWeight() {
        int totalWeight = 0;
        for (Car car : railroadCars) {
            totalWeight += car.getWeight();
        }
        return totalWeight;
    }

    public int getAvailableWeight(){
        return maxWeight - getTotalWeight();
    }

    public int getElectricalConnectionsCount() {
        int count = 0;
        for (Car car : railroadCars) {
            if (car.requiresElectricalConnection()) {
                count++;
            }
        }
        return count;
    }

    public double calculateNewSpeed() throws RailroadHazard {

        int signOfChange = ThreadLocalRandom.current().nextInt(0, 2);
        double newSpeed;

        if (signOfChange == 0) newSpeed = speed - speed * 0.03;
        else newSpeed = speed + speed * 0.03;

        if (newSpeed > 200) throw new RailroadHazard("Trainset(" + id + ") is traveling at a dangerous speed!");
        speed = newSpeed;

        return speed;
    }
    // Getters and setters

    public int getMaxRailroadCars() { return maxRailroadCars; }

    public int getMaxWeight() { return maxWeight; }

    public int getMaxElectricalConnections() { return maxElectricalConnections; }

    public HashSet<Car> getRailroadCars() { return railroadCars; }

    public void addRailroadCar(Car car){ railroadCars.add(car); }

    public int getId(){ return id; }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public Station getHomeStation() {
        return homeStation;
    }

    public void removeCar(Car car) {
        railroadCars.remove(car);
    }

    @Override
    public String toString(){ return "Locomotive(" + id + ")"; }
}