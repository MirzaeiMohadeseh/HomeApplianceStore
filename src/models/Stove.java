package models;

public class Stove extends Appliance {
    private int burners;
    
    public Stove(int id, String name, double price, String brand, int burners) {
        super(id, name, price, brand, "stove"); // نوع ثابت برای اجاق گاز
        this.burners = burners;
    }
}
