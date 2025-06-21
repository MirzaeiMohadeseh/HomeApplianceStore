package models;

public class Refrigerator extends Appliance {
private int capacity;
    
    public Refrigerator(int id, String name, double price, String brand, int capacity) {
        super(id, name, price, brand,"refrigerator");
        this.capacity = capacity;
    }
}