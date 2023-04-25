package railroad.system;

import railroad.entities.Trainset;
import railroad.entities.cars.Car;

import java.io.FileOutputStream;
import java.util.*;

public class Logger implements Runnable {
    private RailwaySystem railwaySystem;
    private HashMap<Integer, Trainset> trainsets;
    private boolean running = true;
    private List<Trainset> sortedTrainsets;

    public Logger(RailwaySystem railwaySystem) {
        this.railwaySystem = railwaySystem;
        this.trainsets = railwaySystem.getTrainsets();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);

                Thread sortThread = new Thread(() -> {
                    sortedTrainsets = sortTrainsets();
                });
                Thread writeThread = new Thread(() -> {
                    writeAppStateToFile(sortedTrainsets);
                });

                sortThread.start();
                sortThread.join();

                writeThread.start();
                writeThread.join();

                Thread.sleep(4000);

            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }

    private List<Trainset> sortTrainsets() {
        // Create a copy of the trainsets list
        List<Trainset> trainsetsCopy = new ArrayList<>(trainsets.values());
        //System.out.println("Logger: " + trainsetsCopy);

        // Sort the copied trainsets using the custom comparator
        trainsetsCopy.sort((t1, t2) -> {
            try {
                double distance1 = railwaySystem.calculateTotalDistance(t1.getRoute());
                double distance2 = railwaySystem.calculateTotalDistance(t2.getRoute());
                return Double.compare(distance2, distance1); // Descending order
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return trainsetsCopy;
    }

    private void writeAppStateToFile(List<Trainset> sortedTrainsets) {
        try (FileOutputStream fos = new FileOutputStream("AppState.txt")) {
            for (Trainset trainset : sortedTrainsets) {
                fos.write(("Trainset: " + trainset.getId() + "\n").getBytes());
                fos.write(("Route Length: " + railwaySystem.calculateTotalDistance(trainset.getRoute()) + "\n").getBytes());
                fos.write("Railroad Cars:\n".getBytes());

                ArrayList<Car> sortedCars = new ArrayList<>(trainset.getCars());
                sortedCars.sort((car1, car2) -> {
                    double weight1 = car1.getWeight();
                    double weight2 = car2.getWeight();
                    return Double.compare(weight1, weight2);
                });

                for (Car car : sortedCars) {
                    fos.write(("Car ID: " + car.getId() + ", Weight: " + car.getWeight() + ", Type: " + car.getType() + "\n").getBytes());
                }
                fos.write("\n".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}