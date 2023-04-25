package railroad.entities;

import railroad.entities.cars.Car;
import railroad.system.RailroadHazard;
import railroad.system.RailwaySystem;

import java.util.ArrayList;
import java.util.HashSet;

public class Trainset implements Runnable{
    private static int uniqueId = 0;
    private int id;
    private Locomotive locomotive;
    private HashSet<Car> railroadCars;
    private ArrayList<Station> route;
    private RailwaySystem railwaySystem;
    private volatile boolean running = true;
    private volatile boolean changeRoute = false;
    private Connection connection;

    private double distanceTraveled;
    private volatile int currentPosition;

    Thread thisThread;
    String threadName;
    private volatile boolean showLogs;

    public Trainset(Locomotive locomotive, RailwaySystem railwaySystem) throws Exception {
        this.id = uniqueId++;
        this.locomotive = locomotive;
        this.railroadCars = locomotive.getRailroadCars();
        this.railwaySystem = railwaySystem;
        this.route = railwaySystem.findRoute(locomotive.getSourceStation(), locomotive.getDestinationStation(), new ArrayList<>());
        thisThread = new Thread(this);
        thisThread.start();
        threadName = thisThread.getName();
        showLogs = false;
    }

    @Override
    public void run() {
        if (running) {
            try {
                moveAlongRoute();
            } catch (Exception e) {
                System.err.println("Error occurred while trying to move trainset: " + e.getMessage());
            }
        }
    }

    public void moveAlongRoute() throws Exception {
        currentPosition = 0;

        while (currentPosition < route.size() - 1 && running) {
            Station currentStation = route.get(currentPosition);
            Station nextStation = route.get(currentPosition + 1);
            connection = railwaySystem.getConnectionBetweenStations(currentStation, nextStation);

            connection.acquireConnection(this);

            Thread moveThread = new Thread(() -> {
                try {
                    updateDistanceTraveled(connection.getDistance());
                    if(showLogs) System.out.println("Trainset(" + id + ") with " + threadName + " moved from " + currentStation + " to " + nextStation + " by " + connection);
                } catch (InterruptedException | RailroadHazard e) {
                    System.err.println("Error occurred while trying to update travelled distance for trainset: " + e.getMessage());
                }
            });

            Thread waitThread = new Thread(() -> {
                try {
                    if (!running) return;
                    if(showLogs) System.out.println("Trainset(" + id + ") with " + threadName + " waits for 2 seconds at the station");
                    Thread.sleep(2000); // Waiting for 2 sec at the station
                } catch (InterruptedException e) {
                    System.err.println("Error occurred while waiting on station: " + e.getMessage());
                }
            });

            ArrayList<Station> finalRoute = route;
            Thread changeRouteThread = new Thread(() -> {
                if (changeRoute && currentPosition + 1 < finalRoute.size()) {
                    Station saveCurrentStation = finalRoute.get(currentPosition);
                    finalRoute.subList(currentPosition, finalRoute.size()).clear();

                    try {
                        ArrayList<Station> newRoute = railwaySystem.findRoute(saveCurrentStation, locomotive.getDestinationStation(), new ArrayList<>());
                        finalRoute.addAll(newRoute);
                    } catch (Exception e) {
                        System.err.println("It is impossible to find the route to destination station for Trainset(" + id + ") (most likely because destination station became isolated" +
                                " as a result of object removal)");
                    }

                    System.out.println(threadName + " changed route");
                    changeRoute = false;
                }
            });

            moveThread.start();
            moveThread.join(); // Main thread waits till moveThread finishes

            connection.releaseConnection(this);

            currentPosition++;
            if (currentPosition != route.size() - 1) {
                waitThread.start();
                waitThread.join(); // Main thread waits till waitThread finishes

                changeRouteThread.start();
                changeRouteThread.join(); // Main thread waits till changeRouteThread finishes
            } else {
                if(showLogs) System.out.println("Trainset(" + id + ") with " + threadName + " waits for 30 seconds at the destination station");
                Thread.sleep(30000);

                route = railwaySystem.findRoute(route.get(route.size() - 1), route.get(0), new ArrayList<>());
                moveAlongRoute();
            }
        }
    }

    public void updateDistanceTraveled(double distanceToNextStation) throws InterruptedException, RailroadHazard {
        double currentSpeed;
        distanceTraveled = 0;

        while (distanceTraveled < distanceToNextStation && running) {
            currentSpeed = locomotive.calculateNewSpeed();
            distanceTraveled += currentSpeed * 1;

            //System.out.println(threadName + " " + "distance travelled: " + distanceTraveled);
            Thread.sleep(1000);
        }
    }

    public Locomotive getLocomotive() { return locomotive; }

    public void setRailroadCars(HashSet<Car> cars){ railroadCars = cars; }

    public void updateCars(){
        railroadCars = locomotive.getRailroadCars();
    }

    public HashSet<Car> getCars(){
        return railroadCars;
    }

    public void stop() throws InterruptedException {
        running = false;
        thisThread.join();
    }

    public void changeRoute(){
        changeRoute = true;
    }

    public ArrayList<Station> getRoute(){
        return route;
    }

    public ArrayList<Connection> getRouteConnections() throws Exception {
        ArrayList<Connection> connectionsInRoute = new ArrayList<>();

        for (int i = 0; i < route.size() - 1; i++) {
            Station currentStation = route.get(i);
            Station nextStation = route.get(i + 1);

            Connection connection = railwaySystem.getConnectionBetweenStations(currentStation, nextStation);
            connectionsInRoute.add(connection);
        }
        return connectionsInRoute;
    }

    public Connection getConnectionOn(){
        return connection;
    }

    public int getId(){
        return id;
    }

    public double getDistanceTravelled() {
        return distanceTraveled;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    @Override
    public String toString(){
        return "Trainset(" + id + ")";
    }

    public void showLogs(){
        showLogs = true;
    }
}