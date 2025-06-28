package HospitalManagement.model;

public class Pharmacy {
    private String itemId;
    private String name;
    private String category; // e.g., Medicine, Equipment, FirstAid
    private String manufacturer;
    private int quantity;
    private double price;

    public Pharmacy(String itemId, String name, String category, String manufacturer, int quantity, double price) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return itemId + "," + name + "," + category + "," + manufacturer + "," + quantity + "," + price;
    }
}