package railroad.entities.cars;

public class BaggageAndMailCar extends PostOfficeCar {
    private int baggageCapacity;
    private String baggageCompartmentType;

    public BaggageAndMailCar(int weight, int baggageCapacity, int mailCapacity, String securityLevel, boolean refrigerationAvailable, String baggageCompartmentType) {
        super(weight, mailCapacity, securityLevel, refrigerationAvailable);
        this.baggageCapacity = baggageCapacity;
        this.baggageCompartmentType = baggageCompartmentType;
        this.requiresElectricalConnection = false;
    }

    @Override
    public String description() {
        return "Baggage Capacity: " + baggageCapacity + ", Mail Capacity: " + mailCapacity + ", Baggage Compartment Type: " + baggageCompartmentType;
    }

    @Override
    public void load(int combinedLoad) throws Exception {
        if (combinedLoad > (baggageCapacity + mailCapacity))
            throw new Exception("Cannot load this much baggage and mail. Available combined capacity is: " + (baggageCapacity + mailCapacity));
        double grossWeight = weight + combinedLoad;
        if (locomotive != null && locomotive.getAvailableWeight() < grossWeight) throw new Exception("Maximum weight of the locomotive to which this car is attached will be exceeded if this much baggage and mail is loaded");
        else weight += combinedLoad;
    }
}