package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import database.ProductDAO;
import models.Appliance;

import java.util.List;

public class MainController {
    @FXML private ListView<String> productList;
    @FXML private Button homeButton; // دکمه صفحه اصلی
    @FXML private Button cartButton; // دکمه سبد خرید

    @FXML
    private void initialize() {
        loadProductsFromDatabase();
        
        // تنظیم رویدادهای منو
        if (homeButton != null) {
            homeButton.setOnAction(e -> loadProductsFromDatabase());
        }
        
        if (cartButton != null) {
            cartButton.setOnAction(e -> System.out.println("سبد خرید کلیک شد"));
        }
    }
    
    private void loadProductsFromDatabase() {
        productList.getItems().clear();
        List<Appliance> products = ProductDAO.getProducts();
        for (Appliance product : products) {
            productList.getItems().add(product.getName() + " - " + product.getBrand() + " - $" + product.getPrice());
        }
    }
}