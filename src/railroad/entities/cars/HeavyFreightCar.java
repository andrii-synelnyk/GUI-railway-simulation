package railroad.entities.cars;

public class HeavyFreightCar extends FreightBasicCar {
    private int maxAxleLoad;
    private boolean isShockAbsorbent;

    public HeavyFreightCar(int weight, int capacity, String cargoType, int maxAxleLoad, boolean isShockAbsorbent) {
        super(weight, capacity + (capacity * 2), cargoType); // 3 times basic freight car capacity
        this.maxAxleLoad = maxAxleLoad;
        this.isShockAbsorbent = isShockAbsorbent;
        this.requiresElectricalConnection = false;
    }

    @Override
    public String description() {
        return super.description() + ", Max Axle Load: " + maxAxleLoad + ", Shock Absorbent: " + isShockAbsorbent;
    }

    @Override
    public void load(int cargo) throws Exception {
        super.load(cargo);
    }
}