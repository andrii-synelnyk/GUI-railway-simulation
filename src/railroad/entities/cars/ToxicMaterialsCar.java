package railroad.entities.cars;

import railroad.entities.cars.interfaces.ToxicCarrier;

public class ToxicMaterialsCar extends HeavyFreightCar implements ToxicCarrier {
    private String toxicType;
    private int containmentLevel;

    public ToxicMaterialsCar(int weight, int capacity, String type, int maxAxleLoad, boolean isShockAbsorbent, String toxicType, int containmentLevel) {
        super(weight, capacity, type, maxAxleLoad, isShockAbsorbent);
        this.toxicType = toxicType;
        this.containmentLevel = containmentLevel;
    }

    @Override
    public String description() {
        return super.description() + ", Toxic Type: " + toxicType + ", Containment Level: " + containmentLevel;
    }

    @Override
    public void load(int cargo) throws Exception {
        // Add additional logic for toxic materials car if needed
        super.load(cargo);
    }

    @Override
    public String getToxicType() {
        return toxicType;
    }

    @Override
    public void setToxicType(String toxicType) {
        this.toxicType = toxicType;
    }

    public int getContainmentLevel() {
        return containmentLevel;
    }

    public void setContainmentLevel(int containmentLevel) {
        this.containmentLevel = containmentLevel;
    }
}