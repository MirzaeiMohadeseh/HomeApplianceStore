package models;

public class TV extends Appliance {
    private int screenSize;
    
    public TV(int id, String name, double price, String brand,
            String imagePath, int stock, String type, int screenSize) {
       super(id, name, price, brand, imagePath, stock, type);
       this.screenSize = screenSize;
   }
}