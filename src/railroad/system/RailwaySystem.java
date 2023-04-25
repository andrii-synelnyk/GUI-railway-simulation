package railroad.system;

import railroad.entities.Connection;
import railroad.entities.Locomotive;
import railroad.entities.Station;
import railroad.entities.Trainset;
import railroad.entities.cars.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

public class RailwaySystem {

    private HashMap<Integer, Connection> connections;
    private HashMap<Integer, Station> stations;
    private HashMap<Integer, Locomotive> locomotives;
    private HashMap<Integer, Trainset> trainsets;
    private HashMap<Integer, Car> railroadCars;

    public enum RailroadCarType {
        FREIGHT_BASIC, PASSENGER, POST_OFFICE, BAGGAGE_MAIL, RESTAURANT, HEAVY_FREIGHT, REFRIGERATED, LIQUID_MATERIALS,
        GASEOUS_MATERIALS, EXPLOSIVES, TOXIC_MATERIALS, LIQUID_TOXIC_MATERIALS
    }

    public RailwaySystem(){
        stations = new HashMap<>();
        connections = new HashMap<>();
        locomotives = new HashMap<>();
        trainsets = new HashMap<>();
        railroadCars = new HashMap<>();
    }

    public Locomotive createLocomotive(String name, Station homeStation, Station sourceStation, Station destinationStation, int maxRailroadCars, int maxWeight,
                                       int maxElectricalConnections, double speed) throws Exception {
        if (!sourceStation.equals(destinationStation)) {
            Locomotive locomotive = new Locomotive(name, homeStation, sourceStation, destinationStation, maxRailroadCars, maxWeight, maxElectricalConnections, speed);
            locomotives.put(locomotive.getId(), locomotive);
            return locomotive;
        } else throw new Exception("Source and destination stations cannot be the same. Try again.");
    }

    public Car createRailroadCar(RailroadCarType type, int weight, int passengerCapacity, int capacity) {
        // Default values:
        String cargoType = "solid";
        String securityLevel = "medium";
        boolean wifiAvailable = true;
        String menuType = "continental";
        double temperatureRange = -20.0;
        String liquidType = "crude oil";
        String gaseousType = "natural gas";
        String explosivesClass = "1.1";
        String toxicType = "toxic gas";
        int maxAxleLoad = 25000;
        boolean isShockAbsorbent = true;
        boolean corrosionResistantCoating = false;
        double maxPressure = 20.0;
        boolean refrigerationAvailable = true;
        int baggageCapacity = 1000;
        int mailCapacity = 5000;
        String baggageCompartmentType = "separate";
        boolean hasBar = false;
        boolean isHumidityControlled = false;
        boolean isPressurized = true;
        double pressureLimit = 30.0;
        boolean isHandlingCertified = true;
        int containmentLevel = 3;

        Car railroadCar = switch (type) {
            case FREIGHT_BASIC -> new FreightBasicCar(weight, capacity, cargoType);
            case PASSENGER -> new PassengerCar(weight, passengerCapacity, wifiAvailable);
            case POST_OFFICE -> new PostOfficeCar(weight, capacity, securityLevel, refrigerationAvailable);
            case BAGGAGE_MAIL -> new BaggageAndMailCar(weight, baggageCapacity, mailCapacity, securityLevel, refrigerationAvailable, baggageCompartmentType);
            case RESTAURANT -> new RestaurantCar(weight, passengerCapacity, wifiAvailable, menuType, hasBar);
            case HEAVY_FREIGHT -> new HeavyFreightCar(weight, capacity, cargoType, maxAxleLoad, isShockAbsorbent);
            case REFRIGERATED -> new RefrigeratedCar(weight, capacity, cargoType, temperatureRange, isHumidityControlled);
            case LIQUID_MATERIALS -> new LiquidMaterialsCar(weight, capacity, cargoType, liquidType, isPressurized);
            case GASEOUS_MATERIALS -> new GaseousMaterialsCar(weight, capacity, cargoType, gaseousType, pressureLimit);
            case EXPLOSIVES -> new ExplosivesCar(weight, capacity, cargoType, maxAxleLoad, isShockAbsorbent, explosivesClass, isHandlingCertified);
            case TOXIC_MATERIALS -> new ToxicMaterialsCar(weight, capacity, cargoType, maxAxleLoad, isShockAbsorbent, toxicType, containmentLevel);
            case LIQUID_TOXIC_MATERIALS -> new LiquidToxicMaterialsCar(weight, capacity, cargoType, maxAxleLoad, isShockAbsorbent, liquidType, toxicType, corrosionResistantCoating, maxPressure);
        };
        railroadCars.put(railroadCar.getId(), railroadCar);
        return railroadCar;
    }

    public Station createRailwayStation() {
        Station station = new Station();
        stations.put(station.getId(), station);
        return station;
    }

    public Connection createConnection(Station station1, Station station2, double distance) throws Exception {
        // Check if a connection between startStation and endStation already exists
        for (Connection connection : connections.values()) {
            if ((connection.getStation1().equals(station1) && connection.getStation2().equals(station2)) ||
                    (connection.getStation1().equals(station2) && connection.getStation2().equals(station1))) {
                throw new Exception("A connection between required stations already exists.");
            }
        }


        // If no connection exists, create a new connection
        Connection connection1 = new Connection(station1, station2, distance);
        station1.addConnection(connection1);
        station2.addConnection(connection1);

        connections.put(connection1.getId(), connection1);

        return connection1;
    }

    public void attachRailroadCarToLocomotive(Locomotive locomotive, Car car) throws Exception {

        for (Locomotive l : locomotives.values()){
            if (l.getRailroadCars().contains(car)) throw new Exception("Either this or other locomotive already has this railroad car attached");
        }

        int currentTotalWeight = locomotive.getTotalWeight();
        int currentElectricalConnections = locomotive.getElectricalConnectionsCount();

        if (locomotive.getRailroadCars().size() >= locomotive.getMaxRailroadCars()) {
            throw new Exception("Cannot add more railroad cars. Maximum limit reached.");
        }

        if (currentTotalWeight + car.getWeight() > locomotive.getMaxWeight()) {
            throw new Exception("Cannot add the railroad car. Weight limit exceeded.");
        }

        if (car.requiresElectricalConnection() && currentElectricalConnections >= locomotive.getMaxElectricalConnections()) {
            throw new Exception("Cannot add the railroad car. Maximum electrical connections limit reached.");
        }

        locomotive.addRailroadCar(car);
        car.assignLocomotive(locomotive);

        //Creating new Trainset or updating existing Trainset
        if (trainsets.isEmpty()) {
            Trainset newTrainset = new Trainset(locomotive, this);
            trainsets.put(newTrainset.getId(), newTrainset);
        }
        else {
            for (Trainset trainset : trainsets.values()){
                if (trainset.getLocomotive().equals(locomotive)) {
                    trainset.setRailroadCars(locomotive.getRailroadCars());
                    return;
                }
            }
            Trainset newTrainset = new Trainset(locomotive, this);
            trainsets.put(newTrainset.getId(), newTrainset); // Create new trainset even if trainsets is not empty, but there's no trainsets with this locomotive
        }
    }

    public Connection getConnectionBetweenStations(Station station1, Station station2) throws Exception {
        for (Connection connection : connections.values()) {
            if ((connection.getStation1().equals(station1) && connection.getStation2().equals(station2)) ||
                    (connection.getStation1().equals(station2) && connection.getStation2().equals(station1))) {
                return connection;
            }
        }
        throw new Exception("No connection found between the specified stations.");
    }

    public ArrayList<Station> findRoute(Station start, Station end, List<Station> visited) throws Exception {

        ArrayList<Station> route;

        if (start.equals(end)) {
            route = new ArrayList<>();
            route.add(start);
            return route;
        }
        visited.add(start);
        for (Connection connection : start.getConnections()) {
            Station station1 = connection.getStation1();
            Station station2 = connection.getStation2();
            Station nextStation = (start.equals(station1)) ? station2 : station1;

            if (!visited.contains(nextStation)) {
                route = findRoute(nextStation, end, visited);
                if (route != null) {
                    route.add(0, start);
                    return route;
                }
            }
        }
        throw new Exception("Could not find the route between specified stations (most likely there are not enough connections to do so)");
    }

    public void removeStation(Station station) throws Exception {
        // Remove the station from the list of stations (in RailwaySystem class)
        stations.remove(station.getId());

        // Remove all connections that include the station (in RailwaySystem class)
        HashSet<Connection> connectionsToRemove = new HashSet<>();
        for (Connection connection : connections.values()) {
            if (connection.getStation1().equals(station) || connection.getStation2().equals(station)) {
                connectionsToRemove.add(connection);
            }
        }

        connectionsToRemove.forEach(connection -> {
            try {
                removeConnection(connection);
            } catch (Exception e) {
                System.err.println("Error occurred while trying to remove connection associated with deleted station: " + e.getMessage());
            }
        });

        // Remove trainsets that are associated with the removed station
        List<Trainset> trainsetsToRemove = new ArrayList<>();
        for (Trainset trainset : trainsets.values()) {
            if (trainset.getLocomotive().getSourceStation().equals(station) ||
                    trainset.getLocomotive().getDestinationStation().equals(station) ||
                    trainset.getLocomotive().getHomeStation().equals(station)) {
                trainsetsToRemove.add(trainset);
            }
        }
        for (Trainset trainset : trainsetsToRemove) {
            removeTrainset(trainset, "dontKeepCars");
        }

        System.out.println(station + " has been successfully removed.");
    }

    public void removeConnection(Connection connection) throws Exception {

        for (Trainset trainset : trainsets.values()){
            if (trainset.getRouteConnections().contains(connection)){
                trainset.changeRoute();
            }
        }

        // Remove connection in instances of Station class (stations)
        for (Station station : stations.values()){
            if (station.getConnections().contains(connection)){
                station.removeSpecificConnection(connection);
            }
        }

        // Remove all objects which are currently on this connection
        removeAllObjectsOnConnection(connection);

        System.out.println(connection + " has been successfully removed.");

    }

    public void removeLocomotive(Locomotive locomotive) throws InterruptedException {
        // Remove the locomotive from the list of locomotives
        locomotives.remove(locomotive.getId());

        // Detach railroad cars from the locomotive
        for (Car car : locomotive.getRailroadCars()) {
            car.removeLocomotive();
        }

        // Remove trainsets associated with deleted locomotive but keep cars
        trainsets.values().stream()
                .filter(trainset -> trainset.getLocomotive().equals(locomotive))
                .forEach(trainset -> {
                    try {
                        removeTrainset(trainset, "keepCars");
                    } catch (InterruptedException e) {
                        System.err.println("Error occurred while trying to remove trainset associated with deleted locomotive: " + e.getMessage());
                    }
                });

        System.out.println(locomotive + " has been successfully removed.");
    }

    public void removeCar(Car car) {
        // Remove the railroad car from its locomotive
        if (car.getLocomotive() != null) {
            car.getLocomotive().removeCar(car);
        }
        // Remove the railroad car from the cars set (in RailwaySystem class)
        railroadCars.remove(car.getId());

        // Detach the car from this locomotive (in Car class)
        car.assignLocomotive(null);

        // Remove this car from associated Trainset class
        trainsets.values().stream()
                .filter(trainset -> trainset.getCars().contains(car))
                .forEach(Trainset::updateCars);

        System.out.println(car + " has been successfully removed.");
    }

    public void removeTrainset(Trainset trainset, String ifKeepCarsAlive) throws InterruptedException {
        // Stop the trainset's thread
        trainset.stop();

        // Remove the trainset from the list
        trainsets.remove(trainset.getId());

        // Release connection this trainset was on
        trainset.getConnectionOn().releaseConnection(trainset);

        // Check if it's needed to remove Cars associated with this trainset
        if (!ifKeepCarsAlive.equals("keepCars")) {
            // Remove cars associated with this trainset
            ArrayList<Car> carsToRemove = new ArrayList<>(trainset.getCars());
            carsToRemove.forEach(this::removeCar);

            // Remove locomotive associated with this trainset
            removeLocomotive(trainset.getLocomotive());
        }

        System.out.println("Trainset has been successfully removed.");
    }

    public void removeAllObjectsOnConnection(Connection connection){
        ArrayList<Trainset> trainsetsToRemove = new ArrayList<>(connection.getTrainsetsOnThisConnection());
        trainsetsToRemove.forEach(trainset -> {
            try {
                removeTrainset(trainset, "dontKeepCars");
            } catch (InterruptedException e) {
                System.err.println("Error occurred while trying to remove trainset on connection to be removed: " + e.getMessage());
            }
        });
    }

    public void startSimulation(int mode) throws Exception {
        if (mode == 1) {
            // Generate stations
            for (int i = 0; i < 100; i++) {
                createRailwayStation();
            }

            // Generate connections
            int numberOfNeighbors = 2;

            // Connect each station to its nearest neighbors
            for (int i = 0; i < stations.size(); i++) {
                Station currentStation = stations.get(i);
                for (int j = 1; j <= numberOfNeighbors; j++) {
                    Station neighborStation = stations.get((i + j) % stations.size()); // % station.size() is needed to ensure that stations.get() doesn't go further than
                    // stations.size(). If it goes further it will be connected to first stations, forming a loop
                    double distance = ThreadLocalRandom.current().nextDouble(200, 1000);
                    try {
                        createConnection(currentStation, neighborStation, distance);
                    } catch (Exception e) {
                        System.err.println("Error occurred while trying to create connection: " + e.getMessage());
                    }
                }
            }

            // Add random connections
            for (int i = 0; i < 100; i++) {
                Station firstStation = stations.get(i);
                Station secondStation = stations.get(ThreadLocalRandom.current().nextInt(0, stations.size()));
                while (firstStation.equals(secondStation) || firstStation.hasConnectionTo(secondStation)) {
                    secondStation = stations.get(ThreadLocalRandom.current().nextInt(0, stations.size()));
                }
                double distance = ThreadLocalRandom.current().nextDouble(200, 1000);
                try {
                    createConnection(firstStation, secondStation, distance);
                } catch (Exception e) {
                    System.err.println("Error occurred while trying to create connection: " + e.getMessage());
                }
            }

            // Generate locomotives
            for (int i = 0; i < 25; i++) {
                Station sourceStation = stations.get(ThreadLocalRandom.current().nextInt(0, stations.size()));
                Station destinationStation = stations.get(ThreadLocalRandom.current().nextInt(0, stations.size()));
                while (sourceStation.equals(destinationStation))
                    destinationStation = stations.get(ThreadLocalRandom.current().nextInt(0, stations.size()));
                int maxRailroadCars = ThreadLocalRandom.current().nextInt(10, 15);
                int maxWeight = ThreadLocalRandom.current().nextInt(2000, 4000);
                int maxElectricalConnections = ThreadLocalRandom.current().nextInt(5, 10);
                double speed = ThreadLocalRandom.current().nextDouble(50, 150);
                createLocomotive(String.valueOf(i), sourceStation, sourceStation, destinationStation, maxRailroadCars, maxWeight, maxElectricalConnections, speed);
            }

            // Generating and attaching cars (trainsets get generated when a car is connected to locomotive)
            for (Locomotive locomotive : locomotives.values()) {
                int numberOfCarsToGenerate = ThreadLocalRandom.current().nextInt(5, 10 + 1);
                for (int i = 0; i < numberOfCarsToGenerate; i++) {
                    RailroadCarType type = RailroadCarType.values()[ThreadLocalRandom.current().nextInt(0, RailroadCarType.values().length)];
                    int weight = ThreadLocalRandom.current().nextInt(100, 200);
                    int passengerCapacity = ThreadLocalRandom.current().nextInt(50, 100);
                    int capacity = ThreadLocalRandom.current().nextInt(100, 300);
                    Car carToAttach = createRailroadCar(type, weight, passengerCapacity, capacity);
                    try {
                        attachRailroadCarToLocomotive(locomotive, carToAttach);
                    } catch (Exception e) {
                        System.err.println("Error occurred while trying to attach railroad car to locomotive: " + e.getMessage());
                    }
                }
            }
        }else if (mode == 2) {

            Station station11 = createRailwayStation();
            Station station22 = createRailwayStation();
            Station station33 = createRailwayStation();

            createConnection(station11, station22, 300);
            createConnection(station22, station33, 300);

            Locomotive locomotive1 = createLocomotive("loco1", station11, station11, station33, 10, 2000, 10, 50);
            Locomotive locomotive2 = createLocomotive("loco2", station11, station11, station33, 10, 1500, 10, 50);

            Car car1 = createRailroadCar(RailwaySystem.RailroadCarType.PASSENGER, 10, 100, 100);
            Car car2 = createRailroadCar(RailroadCarType.FREIGHT_BASIC, 50, 200, 100);

            try {
                attachRailroadCarToLocomotive(locomotive1, car1);
                attachRailroadCarToLocomotive(locomotive2, car2);
            } catch (Exception e) {
                System.err.println("Error occurred while trying to attach railroad car to locomotive: " + e.getMessage());
            }
        }
    }

    public String trainsetReport(Trainset trainset) {
        Locomotive locomotive = trainset.getLocomotive();
        ArrayList<Station> originalRoute = trainset.getRoute();
        int currentPosition = trainset.getCurrentPosition();
        HashSet<Car> cars = trainset.getCars();
        Connection connectionOn = trainset.getConnectionOn();

        // Basic information about trainset
        StringBuilder report = new StringBuilder("Trainset Report:\n");
        report.append("Trainset ID: ").append(trainset.getId()).append("\n");
        report.append("Locomotive ID: ").append(locomotive.getId()).append("\n");
        report.append("Source station: ").append(locomotive.getSourceStation()).append("\n");
        report.append("Destination station: ").append(locomotive.getDestinationStation()).append("\n");

        // % of the distance completed between the starting and destination railway stations
        try {
            double totalDistance = calculateTotalDistance(originalRoute);
            double completedToNearest = trainset.getDistanceTravelled();
            double completedDistanceToEnd = calculateTotalDistance(originalRoute.subList(0, currentPosition)) + completedToNearest;
            double percentageCompletedToEnd = (completedDistanceToEnd / totalDistance) * 100;
            double percentageCompletedToNearest = (completedToNearest / connectionOn.getDistance()) * 100;
            percentageCompletedToEnd = (percentageCompletedToEnd > 100) ? 100 : percentageCompletedToEnd;
            percentageCompletedToNearest = (percentageCompletedToNearest > 100) ? 100 : percentageCompletedToNearest;

            report.append("Percentage of distance completed between the starting and destination railway stations: ").append(String.format("%.2f%%", percentageCompletedToEnd)).append("\n");
            report.append("Percentage of distance completed between the nearest railway stations on the route: ").append(String.format("%.2f%%", percentageCompletedToNearest)).append("\n");
        } catch (Exception e) {
            System.err.println("Error occurred while trying to calculate distance of route for trainset: " + e.getMessage());
        }

        // Summary of information about railroad cars, the number of people based on the goods transported
        int numberOfCars = trainset.getCars().size();
        report.append("Number of cars in trainset: ").append(numberOfCars).append("\n");

        int totalWeight = 0;
        for (Car car : cars) {
            totalWeight += car.getWeight();
        }
        int totalPeople = totalWeight/80;
        report.append("Total number of people based on the goods transported: ").append(totalPeople).append("\n");

        return report.toString();
    }

    public double calculateTotalDistance(List<Station> route) throws Exception {
        double totalDistance = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            Station currentStation = route.get(i);
            Station nextStation = route.get(i + 1);
            Connection connection = getConnectionBetweenStations(currentStation, nextStation);
            totalDistance += connection.getDistance();
        }
        return totalDistance;
    }

    public HashMap<Integer, Station> getStations(){ return stations; }
    public HashMap<Integer, Locomotive> getLocomotives(){ return locomotives; }

    public HashMap<Integer, Car> getCars(){
        return railroadCars;
    }
    public HashMap<Integer, Trainset> getTrainsets(){
        return trainsets;
    }

    public void showLogs(){
        for (Trainset trainset : trainsets.values()){
            trainset.showLogs();
        }
    }
}