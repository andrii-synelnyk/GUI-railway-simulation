package railroad.entities.cars;

public class PassengerCar extends Car {

    protected int numberOfSeats;
    protected boolean wifiAvailable;

    public PassengerCar(int weight, int numberOfSeats, boolean wifiAvailable) {
        super(weight);
        this.numberOfSeats = numberOfSeats;
        this.wifiAvailable = wifiAvailable;
        this.requiresElectricalConnection = true;
    }

    @Override
    public String description() {
        return "Number of seats: " + numberOfSeats + ", Wi-Fi available: " + (wifiAvailable ? "Yes" : "No");
    }

    @Override
    public void load(int numberOfPeople) throws Exception {
        if (numberOfPeople > numberOfSeats)
            throw new Exception("Cannot load these many people. Available number of seats in this railroad car is: " + numberOfSeats);
        double grossWeight = weight + 80 * numberOfPeople;
        if (locomotive != null && locomotive.getAvailableWeight() < grossWeight)
            throw new Exception("Maximum weight of the locomotive to which this car is attached will be exceeded if these many people are loaded");
        else weight += 80 * numberOfPeople;
    }
}