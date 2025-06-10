package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import database.ProductDAO;
import models.Appliance;
import models.CartItem;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class MainController {
    @FXML private ListView<Appliance> productList;
    @FXML private VBox sideMenu;
    @FXML private Button toggleMenuButton;
    @FXML private Button closeMenuButton;
    @FXML private Button cartButton;
    @FXML private Label balanceLabel;
    
    private boolean isMenuOpen = true;
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private double balance = 5000.0;

    @FXML
    public void initialize() {
        try {
            loadProductsFromDatabase();
            setupMenuAnimation();
            updateBalanceDisplay();
            
            // تنظیم رویداد کلیک برای دکمه سبد خرید
            if (cartButton != null) {
                cartButton.setOnAction(this::showCart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateBalanceDisplay() {
        if (balanceLabel != null) {
            balanceLabel.setText(String.format("موجودی: $%.2f", balance));
        }
    }
    
    private void loadProductsFromDatabase() {
        try {
            List<Appliance> products = ProductDAO.getProducts();
            productList.getItems().addAll(products);
            
            productList.setCellFactory(lv -> new javafx.scene.control.ListCell<Appliance>() {
                @Override
                protected void updateItem(Appliance item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " - " + item.getBrand() + " - $" + item.getPrice());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void addToCart(ActionEvent event) {
        Appliance selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getProduct().equals(selectedProduct)) {
                    item.setQuantity(item.getQuantity() + 1);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                cartItems.add(new CartItem(selectedProduct, 1));
            }
            
            showAlert(AlertType.INFORMATION, "موفقیت", "محصول به سبد خرید اضافه شد", 
                     selectedProduct.getName() + " به سبد خرید شما اضافه شد.");
        } else {
            showAlert(AlertType.WARNING, "خطا", "محصولی انتخاب نشده", 
                     "لطفاً یک محصول از لیست انتخاب کنید.");
        }
    }
    
    @FXML
    private void showCart(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("سبد خرید شما");
        alert.setHeaderText("محصولات انتخاب شده:");
        
        StringBuilder content = new StringBuilder();
        final double[] total = {0.0}; // استفاده از آرایه برای حل مشکل final
        
        if (cartItems.isEmpty()) {
            content.append("سبد خرید شما خالی است.");
        } else {
            for (CartItem item : cartItems) {
                content.append(item.getProduct().getName())
                      .append(" - تعداد: ").append(item.getQuantity())
                      .append(" - قیمت: $").append(item.getTotalPrice())
                      .append("\n");
                total[0] += item.getTotalPrice();
            }
            content.append("\nجمع کل: $").append(total[0]);
            
            ButtonType payButton = new ButtonType("پرداخت");
            alert.getButtonTypes().add(payButton);
            
            alert.setContentText(content.toString());
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == payButton) {
                    processPayment(total[0]);
                }
            });
        }
    }
    
    private void processPayment(double amount) {
        if (balance >= amount) {
            balance -= amount;
            cartItems.clear();
            updateBalanceDisplay();
            showAlert(AlertType.INFORMATION, "پرداخت موفق", "پرداخت با موفقیت انجام شد", 
                     "مبلغ $" + amount + " از حساب شما کسر شد.\nموجودی جدید: $" + balance);
        } else {
            showAlert(AlertType.ERROR, "خطای پرداخت", "موجودی کافی نیست", 
                     "موجودی حساب شما کافی نیست.\nموجودی فعلی: $" + balance + "\nمبلغ پرداخت: $" + amount);
        }
    }
    
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
     // تغییر ظاهر پنجره هشدار
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #e74c3c; "
                          + "-fx-border-radius: 10; -fx-text-fill: white; -fx-font-size: 14;");
        
        // تغییر رنگ دکمه‌ها
        dialogPane.lookupButton(ButtonType.OK).setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        // نمایش هشدار
        alert.showAndWait();
    }
    
    private void setupMenuAnimation() {
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