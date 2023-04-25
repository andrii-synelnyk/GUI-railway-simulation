package railroad.entities.cars;

public class RefrigeratedCar extends FreightBasicCar {
    private double temperatureRange;
    private boolean isHumidityControlled;

    public RefrigeratedCar(int weight, int capacity, String cargoType, double temperatureRange, boolean isHumidityControlled) {
        super(weight, capacity, cargoType);
        this.temperatureRange = temperatureRange;
        this.isHumidityControlled = isHumidityControlled;
        this.requiresElectricalConnection = true;
    }

    @Override
    public String description() {
        return super.description() + ", Temperature Range: " + temperatureRange + ", Humidity Controlled: " + isHumidityControlled;
    }

    @Override
    public void load(int cargo) throws Exception {
        // Add additional logic for refrigerated car if needed
        super.load(cargo);
    }
}