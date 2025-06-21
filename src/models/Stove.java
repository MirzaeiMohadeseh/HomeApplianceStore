package models;

public class Stove extends Appliance {
    private int burners;
    
    public Stove(int id, String name, double price, String brand, 
            String imagePath, int stock, String type, int burners) {
    super(id, name, price, brand, imagePath, stock, type);
    this.burners = burners;
}
}
