package railroad.entities.cars;

public class ExplosivesCar extends HeavyFreightCar {
    private String explosivesClass;
    private boolean isHandlingCertified;

    public ExplosivesCar(int weight, int capacity, String type, int maxAxleLoad, boolean isShockAbsorbent, String explosivesClass, boolean isHandlingCertified) {
        super(weight, capacity, type, maxAxleLoad, isShockAbsorbent);
        this.explosivesClass = explosivesClass;
        this.isHandlingCertified = isHandlingCertified;
    }

    @Override
    public String description() {
        return super.description() + ", Explosives Class: " + explosivesClass + ", Hazardous Material Handling Certified: " + isHandlingCertified;
    }

    @Override
    public void load(int cargo) throws Exception {
        // Add additional logic for explosives car if needed
        super.load(cargo);
    }

    public String getExplosivesClass() {
        return explosivesClass;
    }

    public void setExplosivesClass(String explosivesClass) {
        this.explosivesClass = explosivesClass;
    }

    public boolean isHandlingCertified() {
        return isHandlingCertified;
    }

    public void setHandlingCertified(boolean isHandlingCertified) {
        this.isHandlingCertified = isHandlingCertified;
    }
}