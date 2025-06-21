package models;

public abstract class Appliance {
	protected int id;
    protected String name;
    protected double price;
    protected String brand;
    private int stock;
    protected String type;

    public Appliance(int id, String name, double price, String brand, String type) {
    	this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.type = type;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getBrand() { return brand; }
    public String getType() {return type;}
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
