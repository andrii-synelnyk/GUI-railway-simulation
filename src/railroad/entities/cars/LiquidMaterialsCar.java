package railroad.entities.cars;

import railroad.entities.cars.interfaces.LiquidCarrier;

public class LiquidMaterialsCar extends FreightBasicCar implements LiquidCarrier {
    private String liquidType;
    private boolean isPressurized;

    public LiquidMaterialsCar(int weight, int capacity, String cargoType, String liquidType, boolean isPressurized) {
        super(weight, capacity, cargoType);
        this.liquidType = liquidType;
        this.isPressurized = isPressurized;
    }

    @Override
    public String description() {
        return super.description() + ", Liquid Type: " + liquidType + ", Pressurized: " + isPressurized;
    }

    @Override
    public void load(int cargo) throws Exception {
        // Add additional logic for liquid materials car if needed
        super.load(cargo);
    }

    @Override
    public String getLiquidType() {
        return liquidType;
    }

    @Override
    public void setLiquidType(String liquidType) {
        this.liquidType = liquidType;
    }
}