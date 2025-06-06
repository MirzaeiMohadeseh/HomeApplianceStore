package models;

public abstract class Appliance {
    protected String name;
    protected double price;
    protected String brand;

    public Appliance(String name, double price, String brand) {
        this.name = name;
        this.price = price;
        this.brand = brand;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getBrand() { return brand; }
}
