package railroad.entities;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Connection {
    private static int uniqueId = 0;
    private int id;
    private Station station2;
    private Station station1;
    private double distance;
    private Semaphore semaphore;
    private ConcurrentLinkedQueue<Trainset> waitingQueue;
    private ArrayList<Trainset> trainsetsOnThisConnection;

    public Connection(Station station1, Station station2, double distance) {
        this.id = uniqueId++;
        this.station1 = station1;
        this.station2 = station2;
        this.distance = distance;
        this.semaphore = new Semaphore(1);
        this.waitingQueue = new ConcurrentLinkedQueue<>();
        this.trainsetsOnThisConnection = new ArrayList<>();
    }

    public Station getStation1() {
        return station1;
    }
    public Station getStation2() {
        return station2;
    }
    public double getDistance(){
        return distance;
    }

    @Override
    public String toString() { return "Connection(" + id + ") between " + station1 + " and " + station2 + ". Distance of connection: " + distance; }

    public void acquireConnection(Trainset trainset) throws InterruptedException {
        waitingQueue.add(trainset);
        semaphore.acquire();
        trainsetsOnThisConnection.add(trainset);
        waitingQueue.poll();
    }

    public void releaseConnection(Trainset trainset) {
        semaphore.release();
        trainsetsOnThisConnection.remove(trainset);
    }

    public ArrayList<Trainset> getTrainsetsOnThisConnection(){
        return trainsetsOnThisConnection;
    }

    public int getId(){
        return id;
    }

}