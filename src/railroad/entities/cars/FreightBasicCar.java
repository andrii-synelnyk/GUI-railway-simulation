package railroad.entities.cars;

public class FreightBasicCar extends Car {
    protected int capacity;
    protected String cargoType;

    public FreightBasicCar(int weight, int capacity, String cargoType){
        super(weight);
        this.capacity = capacity;
        this.cargoType = cargoType;
        this.requiresElectricalConnection = false;
    }

    @Override
    public String description() {
        return "Capacity: " + capacity + ", Cargo Type: " + cargoType;
    }

    @Override
    public void load(int cargo) throws Exception {
        if (cargo > capacity) throw new Exception("Cannot load these much cargo. Available capacity for cargo is: " + capacity);
        double grossWeight = weight + cargo;
        if (locomotive != null && locomotive.getAvailableWeight() < grossWeight) throw new Exception("Maximum weight of the locomotive to which this car is attached will be exceeded if this much cargo is loaded");
        else weight += cargo;
    }
}