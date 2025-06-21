package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import database.ProductDAO;
import models.Appliance;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Optional;

public class AdminController {
    @FXML private TableView<Appliance> productsTable;
    @FXML private TableColumn<Appliance, String> nameCol;
    @FXML private TableColumn<Appliance, String> brandCol;
    @FXML private TableColumn<Appliance, Double> priceCol;
    @FXML private TableColumn<Appliance, Integer> stockCol;

    
    @FXML
    public void initialize() {
        setupTable();
        loadProducts();
    }

    private void setupTable() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void loadProducts() {
        productsTable.getItems().setAll(ProductDAO.getAllProductsWithStock());
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void increaseStock() {
        Appliance selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ProductDAO.updateProductStock(selected.getId(), selected.getStock() + 1);
            loadProducts();
            showAlert("موفقیت", "موجودی محصول افزایش یافت");
        }
    }

    @FXML
    private void decreaseStock() {
        Appliance selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getStock() > 0) {
            ProductDAO.updateProductStock(selected.getId(), selected.getStock() - 1);
            loadProducts();
            showAlert("موفقیت", "موجودی محصول کاهش یافت");
        } else {
            showAlert("خطا", "موجودی محصول نمی‌تواند منفی باشد");
        }
    }
    @FXML
    private void orderFromWarehouse() {
        Appliance selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getStock() == 0) {
            showAlert("سفارش از انبار", "درخواست شما برای محصول " + selected.getName() + " به انبار ارسال شد.");
            // در واقعیت اینجا باید به سیستم انبار اطلاع داده شود
        }
    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("خروج از حساب");
        alert.setHeaderText("آیا مطمئن هستید می‌خواهید خارج شوید؟");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
                Stage stage = (Stage) productsTable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("ورود به سیستم");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("خطا", "خطا در هنگام خروج");
            }
        }
    }
}