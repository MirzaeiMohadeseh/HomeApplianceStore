package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import database.ProductDAO;
import models.Appliance;
import java.util.List;

public class MainController {
    @FXML private ListView<String> productList;
    @FXML private ImageView productImage;

    @FXML
    private void initialize() {
        loadProductsFromDatabase();
        productList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateProductImage(newValue);
        });
    }

    private void loadProductsFromDatabase() {
        List<Appliance> products = ProductDAO.getProducts();
        for (Appliance product : products) {
            productList.getItems().add(product.getName() + " - " + product.getBrand() + " - $" + product.getPrice());
        }
    }

    private void updateProductImage(String productName) {
        String imagePath = "/images/" + productName.toLowerCase().replace(" ", "_") + ".png";
        productImage.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }
    
}
