package railroad.entities.cars;

public class GaseousMaterialsCar extends FreightBasicCar {
    private String gaseousType;
    private double pressureLimit;

    public GaseousMaterialsCar(int weight, int capacity, String cargoType, String gaseousType, double pressureLimit) {
        super(weight, capacity, cargoType);
        this.gaseousType = gaseousType;
        this.pressureLimit = pressureLimit;
    }

    @Override
    public String description() {
        return super.description() + ", Gaseous Type: " + gaseousType + ", Pressure Limit: " + pressureLimit;
    }

    @Override
    public void load(int cargo) throws Exception {
        // Add additional logic for gaseous materials car if needed
        super.load(cargo);
    }

    public String getGaseousType() {
        return gaseousType;
    }

    public void setGaseousType(String gaseousType) {
        this.gaseousType = gaseousType;
    }
}