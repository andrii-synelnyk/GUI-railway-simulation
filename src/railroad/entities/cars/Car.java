package railroad.entities.cars;

import railroad.entities.Locomotive;

public abstract class Car {

    private static int uniqueId = 0;
    protected int id;
    protected double weight;
    protected boolean requiresElectricalConnection;
    protected Locomotive locomotive;
    protected String type;

    public Car(double weight) {
        this.id = uniqueId++;
        this.weight = weight;
    }

    public boolean requiresElectricalConnection() {
        return requiresElectricalConnection;
    }

    public double getWeight(){
        return weight;
    }

    @Override
    public String toString(){
        return "Car("+ id + ") of type " + getType();
    }
    public abstract void load(int load) throws Exception;
    public abstract String description();

    // Getters and setters
    public void assignLocomotive(Locomotive locomotive){
        this.locomotive = locomotive;
    }
    public void removeLocomotive() {
        this.locomotive = null;
    }

    public Locomotive getLocomotive(){
        return locomotive;
    }
    public int getId(){
        return id;
    }

    public String getType(){
        return this.getClass().getSimpleName();
    }
}