package models;

public class TV extends Appliance {
    private int screenSize;
    
    public TV(int id, String name, double price, String brand, int screenSize) {
        super(id, name, price, brand, "tv"); // نوع ثابت برای تلویزیون
        this.screenSize = screenSize;
    }
}