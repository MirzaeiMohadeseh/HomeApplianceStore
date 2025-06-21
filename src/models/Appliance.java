package models;

public abstract class Appliance {
	protected int id;
    protected String name;
    protected double price;
    protected String brand;
    protected  String imagePath;
    protected int stock;
    protected String type;

    public Appliance(int id, String name, double price, String brand,String imagePath,int stock, String type) {
    	this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.imagePath = imagePath;
        this.stock = stock;
        this.type = type;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getBrand() { return brand; }
    public String getImagePath() {return imagePath;}
    public int getStock() { return stock; }
    public String getType() {return type;}

}
