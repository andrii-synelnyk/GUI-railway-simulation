package railroad.entities.cars;

import railroad.entities.cars.interfaces.LiquidCarrier;
import railroad.entities.cars.interfaces.ToxicCarrier;

public class LiquidToxicMaterialsCar extends HeavyFreightCar implements LiquidCarrier, ToxicCarrier {
    private String liquidType;
    private String toxicType;
    private boolean corrosionResistantCoating;
    private double maxPressure;

    public LiquidToxicMaterialsCar(int weight, int capacity, String type, int maxAxleLoad, boolean isShockAbsorbent, String liquidType, String toxicType, boolean corrosionResistantCoating, double maxPressure) {
        super(weight, capacity, type, maxAxleLoad, isShockAbsorbent);
        this.liquidType = liquidType;
        this.toxicType = toxicType;
        this.corrosionResistantCoating = corrosionResistantCoating;
        this.maxPressure = maxPressure;
    }

    @Override
    public String description() {
        return super.description() + ", Liquid Type: " + liquidType + ", Toxic Type: " + toxicType + ", Corrosion Resistant Coating: " + corrosionResistantCoating + ", Max Pressure: " + maxPressure;
    }

    @Override
    public void load(int cargo) throws Exception {
        // Add additional logic for liquid toxic materials car if needed
        super.load(cargo);
    }

    // LiquidCarrier methods
    @Override
    public String getLiquidType() {
        return liquidType;
    }

    @Override
    public void setLiquidType(String liquidType) {
        this.liquidType = liquidType;
    }

    // ToxicCarrier methods
    @Override
    public String getToxicType() {
        return toxicType;
    }

    @Override
    public void setToxicType(String toxicType) {
        this.toxicType = toxicType;
    }

    public boolean isCorrosionResistantCoating() {
        return corrosionResistantCoating;
    }

    public void setCorrosionResistantCoating(boolean corrosionResistantCoating) {
        this.corrosionResistantCoating = corrosionResistantCoating;
    }

    public double getMaxPressure() {
        return maxPressure;
    }

    public void setMaxPressure(double maxPressure) {
        this.maxPressure = maxPressure;
    }
}