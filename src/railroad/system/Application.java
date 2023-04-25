package railroad.system;

import railroad.entities.Connection;
import railroad.entities.Locomotive;
import railroad.entities.Station;
import railroad.entities.Trainset;
import railroad.entities.cars.Car;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        RailwaySystem railwaySystem = new RailwaySystem();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Enter your choice:");
            System.out.println("1: Start simulation"); // 9
            System.out.println("2: Create new railway station");
            System.out.println("3: Create new connection between stations");
            System.out.println("4: Create new locomotive");
            System.out.println("5: Create new railroad car");
            System.out.println("6: Attach railroad car to a locomotive");
            System.out.println("7: Load people/cargo onto railroad cars");
            System.out.println("8: Remove an object (station, locomotive, railroad car or connection)");
            System.out.println("9: Generate trainset report");
            System.out.println("10: Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.println("Choose in which mode you want to start simulation:");
                        System.out.println("1: Normal simulation (25 trainsets, 100 stations)");
                        System.out.println("2: Tiny simulation (2 trainsets, 3 stations) - better for testing object removal or queue");
                        int modeChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (modeChoice) {
                            case 1 -> railwaySystem.startSimulation(1);
                            case 2 -> railwaySystem.startSimulation(2);
                        }
                        Logger logger = new Logger(railwaySystem);
                        Thread loggerThread = new Thread(logger);
                        loggerThread.start();

                        System.out.println("Do you wish to see logs (how trainsets move)?:");
                        System.out.println("1: Yes");
                        System.out.println("2: No (silent mode)");
                        int logChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (logChoice) {
                            case 1 -> railwaySystem.showLogs();
                            case 2 -> {}
                            default -> throw new IllegalArgumentException("Invalid choice: " + logChoice);
                        }
                    }
                    case 2 -> {
                        // Create a new railway station
                        Station station = railwaySystem.createRailwayStation();
                        System.out.println("Railway station created: " + station);
                    }
                    case 3 -> {
                        // Create a new connection between stations
                        System.out.print("Enter ID of the first station: ");
                        int station1ID = scanner.nextInt();
                        Station station1 = railwaySystem.getStations().get(station1ID);
                        System.out.print("Enter ID of the second station: ");
                        int station2ID = scanner.nextInt();
                        Station station2 = railwaySystem.getStations().get(station2ID);
                        System.out.print("Enter the distance between the stations (in km): ");
                        double distance = scanner.nextDouble();
                        scanner.nextLine();
                        try {
                            Connection connection = railwaySystem.createConnection(station1, station2, distance);
                            if (connection != null)
                                System.out.println("Connection created: " + connection);
                        } catch (Exception e) {
                            System.err.println("Error occurred while trying to create connection: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        // Create a new locomotive
                        System.out.print("Enter the name for the locomotive: ");
                        String locoName = scanner.nextLine();
                        System.out.print("Enter the source station ID for the locomotive: ");
                        int station1ID = scanner.nextInt();
                        Station station1 = railwaySystem.getStations().get(station1ID);
                        System.out.print("Enter the destination station ID for the locomotive: ");
                        int station2ID = scanner.nextInt();
                        scanner.nextLine();
                        Station station2 = railwaySystem.getStations().get(station2ID);
                        // Add user input to get the required parameters for the locomotive
                        Locomotive locomotive = railwaySystem.createLocomotive(locoName, station1, station1, station2, 10, 500, 3, 140);
                        System.out.println("Locomotive created: " + locomotive);
                    }
                    case 5 -> {
                        // Create a new railroad car
                        System.out.println("Choose which type of railroad car you want to create: ");
                        System.out.println("1. Freight Basic ");
                        System.out.println("2. Passenger ");
                        System.out.println("3. Post Office ");
                        System.out.println("4. Baggage Mail ");
                        System.out.println("5. Restaurant ");
                        System.out.println("6. Heavy Freight ");
                        System.out.println("7. Refrigerated ");
                        System.out.println("8. Liquid Materials ");
                        System.out.println("9. Gaseous Materials ");
                        System.out.println("10. Explosives ");
                        System.out.println("11. Toxic Materials ");
                        System.out.println("12. Liquid Toxic Materials");

                        int typeChoice = scanner.nextInt();
                        scanner.nextLine();

                        RailwaySystem.RailroadCarType type = switch (typeChoice) {
                            case 1 -> RailwaySystem.RailroadCarType.FREIGHT_BASIC;
                            case 2 -> RailwaySystem.RailroadCarType.PASSENGER;
                            case 3 -> RailwaySystem.RailroadCarType.POST_OFFICE;
                            case 4 -> RailwaySystem.RailroadCarType.BAGGAGE_MAIL;
                            case 5 -> RailwaySystem.RailroadCarType.RESTAURANT;
                            case 6 -> RailwaySystem.RailroadCarType.HEAVY_FREIGHT;
                            case 7 -> RailwaySystem.RailroadCarType.REFRIGERATED;
                            case 8 -> RailwaySystem.RailroadCarType.LIQUID_MATERIALS;
                            case 9 -> RailwaySystem.RailroadCarType.GASEOUS_MATERIALS;
                            case 10 -> RailwaySystem.RailroadCarType.EXPLOSIVES;
                            case 11 -> RailwaySystem.RailroadCarType.TOXIC_MATERIALS;
                            case 12 -> RailwaySystem.RailroadCarType.LIQUID_TOXIC_MATERIALS;
                            default -> throw new IllegalArgumentException("Invalid choice: " + typeChoice);
                        };

                        Car railroadCar = railwaySystem.createRailroadCar(type, 50, 100, 100);
                        System.out.println("Railroad car created: " + railroadCar);
                    }
                    case 6 -> {
                        // Attach a railroad car to a locomotive
                        System.out.print("Enter ID of the locomotive: ");
                        int locoID = scanner.nextInt();
                        Locomotive loco = railwaySystem.getLocomotives().get(locoID);
                        System.out.print("Enter the ID of the railroad car: ");
                        int carID = scanner.nextInt();
                        scanner.nextLine();
                        Car car = railwaySystem.getCars().get(carID);
                        railwaySystem.attachRailroadCarToLocomotive(loco, car);
                        System.out.println("Railroad car: " + car + " attached to locomotive: " + loco);
                    }
                    case 7 -> {
                        // Load people/cargo onto railroad cars
                        System.out.print("Enter the ID of the railroad car: ");
                        int railroadCarId = scanner.nextInt();
                        Car carToLoad = railwaySystem.getCars().get(railroadCarId);
                        System.out.print("Enter the number of people/cargo kgs to load (remember each person weights 80kg): ");
                        int loadAmount = scanner.nextInt();
                        scanner.nextLine();
                        carToLoad.load(loadAmount);
                        System.out.println("People/cargo loaded onto railroad car. Current car weight is " + carToLoad.getWeight());
                    }
                    case 8 -> {
                        // Remove an object
                        System.out.println("Which type of object do you want to remove?");
                        System.out.println("1: Station");
                        System.out.println("2: Connection");
                        System.out.println("3: Locomotive");
                        System.out.println("4: Railroad car");
                        System.out.println("5: Trainset");
                        int innerChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (innerChoice) {
                            case 1 -> {
                                System.out.print("Enter ID of the station you want to remove: ");
                                int stationRemoveID = scanner.nextInt();
                                Station stationRemove = railwaySystem.getStations().get(stationRemoveID);
                                railwaySystem.removeStation(stationRemove);
                                scanner.nextLine();
                            }
                            case 2 -> {
                                System.out.print("Enter ID of the first station: ");
                                int station1Id = scanner.nextInt();
                                System.out.print("Enter ID of the second station: ");
                                int station2Id = scanner.nextInt();
                                try {
                                    Connection connectionToRemove = railwaySystem.getConnectionBetweenStations(railwaySystem.getStations().get(station1Id),
                                            railwaySystem.getStations().get(station2Id));
                                    railwaySystem.removeConnection(connectionToRemove);
                                } catch (Exception e) {
                                    System.err.println("Error occurred while trying to remove connection: " + e.getMessage());
                                }
                                scanner.nextLine();
                            }
                            case 3 -> {
                                System.out.print("Enter ID of the locomotive: ");
                                int locoId = scanner.nextInt();
                                Locomotive locoToRemove = railwaySystem.getLocomotives().get(locoId);
                                railwaySystem.removeLocomotive(locoToRemove);
                                scanner.nextLine();
                            }
                            case 4 -> {
                                System.out.print("Enter the ID of the railroad car: ");
                                int carId = scanner.nextInt();
                                Car carToRemove = railwaySystem.getCars().get(carId);
                                railwaySystem.removeCar(carToRemove);
                                scanner.nextLine();
                            }
                            case 5 -> {
                                System.out.print("Enter the ID of the trainset: ");
                                int trainsetId = scanner.nextInt();
                                Trainset trainsetToRemove = railwaySystem.getTrainsets().get(trainsetId);
                                railwaySystem.removeTrainset(trainsetToRemove, "dontKeepCars");
                                scanner.nextLine();
                            }
                            default -> throw new IllegalArgumentException("Invalid choice: " + innerChoice);
                        }
                    }
                    case 9 -> {
                        // Generate trainset report
                        System.out.print("Enter the ID of the trainset: ");
                        int trainId = scanner.nextInt();
                        Trainset trainsetToReport = railwaySystem.getTrainsets().get(trainId);
                        String report = railwaySystem.trainsetReport(trainsetToReport);
                        System.out.println(report);
                        scanner.nextLine();
                    }
                    case 10 -> {
                        exit = true;
                        System.exit(0);
                    }
                    default -> throw new IllegalArgumentException("Invalid choice: " + choice);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        scanner.close();
    }

}