package models;

import java.util.Objects;

public class CartItem {
    private Appliance product;
    private int quantity;
    
    public CartItem(Appliance product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    public Appliance getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return product.getId() == cartItem.product.getId();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(product.getId());
    }
}