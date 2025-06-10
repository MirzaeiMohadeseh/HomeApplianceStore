package models;

public class Refrigerator extends Appliance {
    private int capacity; // ظرفیت (لیتر)

    public Refrigerator(String name, double price, String brand, int capacity) {
        super(name, price, brand);
        this.capacity = capacity;
    }

}
