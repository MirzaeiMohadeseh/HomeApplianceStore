package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import database.Database;
import database.ProductDAO;
import models.Appliance;
import models.CartItem;
import models.User;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class MainController {
    @FXML private ListView<Appliance> productList;
    @FXML private VBox sideMenu;
    @FXML private Button toggleMenuButton;
    @FXML private Button closeMenuButton;
    @FXML private Button cartButton;
    @FXML private Label balanceLabel;
    @FXML private Button logoutButton;
    private User currentUser;
    
    private boolean isMenuOpen = true;
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private double balance = 5000.0;

    @FXML
    public void initialize() {
        try {
            setupProductList();
            setupMenuAnimation();
            updateBalanceDisplay();
            
            if (cartButton != null) {
                cartButton.setOnAction(this::showCart);
            }
            
            if (logoutButton != null) {
                logoutButton.setOnAction(this::handleLogout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupProductList() {
        productList.setCellFactory(lv -> new javafx.scene.control.ListCell<Appliance>() {
            private final ImageView imageView = new ImageView();
            private final Label label = new Label();
            private final HBox hbox = new HBox(10, imageView, label);

            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Appliance item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        InputStream is = getClass().getResourceAsStream(item.getImagePath());
                        if (is != null) {
                            imageView.setImage(new Image(is));
                        } else {
                            // نمایش تصویر پیش‌فرض اگر عکس محصول موجود نبود
                            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default_product.png")));
                        }
                    } catch (Exception e) {
                        imageView.setImage(null);
                    }
                    label.setText(String.format("%s - %s - $%.2f\nموجودی: %d", 
                            item.getName(), item.getBrand(), item.getPrice(), item.getStock()));
                    setGraphic(hbox);
                }
            }
        });
        
        loadProductsFromDatabase();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.balance = user.getBalance();
        updateBalanceDisplay();
    }
    
    private void updateBalanceDisplay() {
        if (balanceLabel != null) {
            balanceLabel.setText(String.format("موجودی: $%.2f", balance));
        }
    }
    
    private void loadProductsFromDatabase() {
        try {
            List<Appliance> products = ProductDAO.getAllProductsWithStock();
            productList.getItems().setAll(products);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "خطا", "خطا در بارگذاری محصولات", 
                     "خطایی در دریافت اطلاعات محصولات رخ داده است.");
        }
    }
    
    @FXML
    private void addToCart(ActionEvent event) {
        Appliance selectedProduct = productList.getSelectionModel().getSelectedItem();
        
        if (selectedProduct != null) {
            if (selectedProduct.getStock() > 0) {
                // کاهش موجودی در حافظه
                selectedProduct.setStock(selectedProduct.getStock() - 1);
                
                // به‌روزرسانی فوری در دیتابیس
                updateSingleProductStock(selectedProduct.getId(), selectedProduct.getStock());
                
                // اضافه کردن به سبد خرید
                cartItems.stream()
                    .filter(item -> item.getProduct().getId() == selectedProduct.getId())
                    .findFirst()
                    .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> cartItems.add(new CartItem(selectedProduct, 1))
                    );
                
                productList.refresh();
                
                showAlert(AlertType.INFORMATION, "موفقیت", 
                        "محصول به سبد خرید اضافه شد", 
                        selectedProduct.getName() + " به سبد خرید شما اضافه شد.");
            } else {
                showAlert(AlertType.WARNING, "خطا", 
                        "موجودی کافی نیست", 
                        "این محصول در حال حاضر موجود نیست.");
            }
        } else {
            showAlert(AlertType.WARNING, "خطا", 
                    "محصولی انتخاب نشده", 
                    "لطفاً یک محصول از لیست انتخاب کنید.");
        }
    }

    private void updateSingleProductStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newStock);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "خطا", "خطا در به‌روزرسانی موجودی", 
                     "خطایی در به‌روزرسانی موجودی محصول رخ داده است.");
        }
    }
    @FXML
    private void showCart(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("سبد خرید شما");
        alert.setHeaderText("محصولات انتخاب شده:");
        
        StringBuilder content = new StringBuilder();
        double total = cartItems.stream()
                           .mapToDouble(CartItem::getTotalPrice)
                           .sum();

        if (cartItems.isEmpty()) {
            content.append("سبد خرید شما خالی است.");
        } else {
            cartItems.forEach(item -> 
                content.append(String.format("%s - تعداد: %d - قیمت: $%.2f%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()))
            );
            content.append(String.format("%nجمع کل: $%.2f", total));
            
            ButtonType payButton = new ButtonType("پرداخت");
            alert.getButtonTypes().add(payButton);
            
            alert.setContentText(content.toString());
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == payButton) {
                    processPayment(total);
                }
            });
        }
    }
    
    private void processPayment(double amount) {
        if (balance >= amount) {
            // کاهش موجودی کاربر
            balance -= amount;
            currentUser.setBalance(balance);
            
            // کاهش موجودی کالاها در دیتابیس
            updateProductStocksInDatabase();
            
            cartItems.clear();
            updateBalanceDisplay();
            updateUserBalanceInDatabase();
            
            showAlert(AlertType.INFORMATION, "پرداخت موفق", "پرداخت با موفقیت انجام شد", 
                     String.format("مبلغ $%.2f از حساب شما کسر شد.%nموجودی جدید: $%.2f", amount, balance));
        } else {
            showAlert(AlertType.ERROR, "خطای پرداخت", "موجودی کافی نیست", 
                     String.format("موجودی حساب شما کافی نیست.%nموجودی فعلی: $%.2f%nمبلغ پرداخت: $%.2f", 
                             balance, amount));
        }
    }

    private void updateProductStocksInDatabase() {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection()) {
            // استفاده از تراکنش برای اطمینان از تمامیت داده‌ها
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (CartItem item : cartItems) {
                    stmt.setInt(1, item.getProduct().getStock());
                    stmt.setInt(2, item.getProduct().getId());
                    stmt.addBatch();
                }
                
                stmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "خطا", "خطا در به‌روزرسانی موجودی کالاها", 
                     "خطایی در به‌روزرسانی موجودی کالاها رخ داده است.");
        }
    }
    private void updateUserBalanceInDatabase() {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, balance);
            stmt.setInt(2, currentUser.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "خطا", "خطا در به‌روزرسانی موجودی", 
                     "خطایی در به‌روزرسانی موجودی حساب رخ داده است.");
        }
    }
    
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void setupMenuAnimation() {
        sideMenu.setTranslateX(0);
    }
    
    @FXML
    private void toggleMenu() {
        if (isMenuOpen) closeMenu();
        else openMenu();
    }
    
    @FXML
    private void openMenu() {
        animateMenu(0);
        isMenuOpen = true;
    }
    
    @FXML
    private void closeMenu() {
        animateMenu(sideMenu.getWidth());
        isMenuOpen = false;
    }
    
    private void animateMenu(double targetX) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), sideMenu);
        tt.setToX(targetX);
        tt.play();
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("تأیید خروج");
        confirmAlert.setHeaderText("آیا مطمئن هستید می‌خواهید از حساب خود خارج شوید؟");
        confirmAlert.setContentText("در صورت خروج، سبد خرید شما پاک خواهد شد.");
        
        confirmAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    cartItems.clear();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
                    Parent root = loader.load();
                    
                    Stage stage = (Stage) logoutButton.getScene().getWindow();
                    stage.setScene(new Scene(root, 400, 300));
                    stage.setTitle("ورود به فروشگاه");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(AlertType.ERROR, "خطا", "خطا در خروج از حساب", 
                            "خطایی در بازگشت به صفحه ورود رخ داده است.");
                }
            }
        });
    }
}