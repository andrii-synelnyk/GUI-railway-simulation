package railroad.entities.cars;

public class PostOfficeCar extends Car {
    protected int mailCapacity;
    protected String securityLevel;
    private boolean refrigerationAvailable;

    public PostOfficeCar(int weight, int mailCapacity, String securityLevel, boolean refrigerationAvailable) {
        super(weight);
        this.mailCapacity = mailCapacity;
        this.securityLevel = securityLevel;
        this.refrigerationAvailable = refrigerationAvailable;
        this.requiresElectricalConnection = true;
    }

    @Override
    public String description() {
        return "Mail Capacity: " + mailCapacity + ", Security Level: " + securityLevel + ", Refrigeration available: " + (refrigerationAvailable ? "Yes" : "No");
    }

    @Override
    public void load(int mailAmount) throws Exception {
        if (mailAmount > mailCapacity)
            throw new Exception("Cannot load this much mail. Available mail capacity is: " + mailCapacity);
        double grossWeight = weight + mailAmount;
        if (locomotive != null && locomotive.getAvailableWeight() < grossWeight) throw new Exception("Maximum weight of the locomotive to which this car is attached will be exceeded if this much mail is loaded");
        else weight += mailAmount;
    }
}