package HospitalManagement.model;

public class CanteenItem {
    private String name;
    private double price;

    public CanteenItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    public String toString() {
        return "CanteenItem{name='" + name + "', price=" + price + "}";
    }
}
