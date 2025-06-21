package models;

public class Refrigerator extends Appliance {
private int capacity;
    
	public Refrigerator(int id, String name, double price, String brand,
        String imagePath, int stock, String type, int capacity) {
		super(id, name, price, brand, imagePath, stock, type);
		this.capacity = capacity;
}
}