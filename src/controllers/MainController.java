package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;
import javafx.event.ActionEvent;
import database.ProductDAO;
import models.Appliance;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class MainController {
    @FXML private ListView<String> productList;
    @FXML private VBox sideMenu;
    @FXML private Button toggleMenuButton;
    @FXML private Button closeMenuButton;
    
    private boolean isMenuOpen = true;

    @FXML
    private void initialize() {
        loadProductsFromDatabase();
        setupMenuAnimation();
    }
    
    private void loadProductsFromDatabase() {
        List<Appliance> products = ProductDAO.getProducts();
        for (Appliance product : products) {
            productList.getItems().add(product.getName() + " - " + product.getBrand() + " - $" + product.getPrice());
        }
    }
    @FXML
    private void addToCart(ActionEvent event) {
        System.out.println("افزودن به سبد خرید کلیک شد");
        // منطق افزودن به سبد خرید اینجا پیاده‌سازی شود
    }

    
    private void setupMenuAnimation() {
        // تنظیم موقعیت اولیه منو
        sideMenu.setTranslateX(0);
    }
    
    @FXML
    private void toggleMenu() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
    
    @FXML
    private void openMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), sideMenu);
        tt.setToX(0);
        tt.play();
        isMenuOpen = true;
    }
    
    @FXML
    private void closeMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), sideMenu);
        tt.setToX(sideMenu.getWidth());
        tt.play();
        isMenuOpen = false;
    }
}