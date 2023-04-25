package railroad.entities.cars;

public class RestaurantCar extends PassengerCar {

    private String menuType;
    private boolean hasBar;

    public RestaurantCar(int weight, int numberOfSeats, boolean wifiAvailable, String menuType, boolean hasBar) {
        super(weight, numberOfSeats, wifiAvailable);
        this.menuType = menuType;
        this.hasBar = hasBar;
        this.requiresElectricalConnection = true;
    }

    @Override
    public String description() {
        return "Number of Seats: " + numberOfSeats + ", Menu Type: " + menuType + ", Has Bar: " + (hasBar ? "Yes" : "No");
    }

    @Override
    public void load(int numberOfPeople) throws Exception {
        super.load(numberOfPeople);
    }
}