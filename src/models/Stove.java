package models;

public class Stove extends Appliance {
    private int burners; // تعداد شعله‌ها

    public Stove(String name, double price, String brand, int burners) {
        super(name, price, brand);
        this.burners = burners;
    }

}
