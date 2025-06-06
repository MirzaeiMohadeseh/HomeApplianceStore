package models;

public class TV extends Appliance {
    private int screenSize; // اندازه صفحه نمایش

    public TV(String name, double price, String brand, int screenSize) {
        super(name, price, brand);
        this.screenSize = screenSize;
    }

    @Override
    public void showDetails() {
        System.out.println(name + " - " + brand + " - $" + price + " - " + screenSize + " اینچ");
    }
}
