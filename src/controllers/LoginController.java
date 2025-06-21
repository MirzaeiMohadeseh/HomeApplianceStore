package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import database.AuthDAO;
import models.User;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("خطا", "لطفاً نام کاربری و رمز عبور را وارد کنید.");
            return;
        }

        User user = AuthDAO.loginUser(username, password);
        if (user != null) {
            openMainWindow(user);
        } else {
            showAlert("خطا", "نام کاربری یا رمز عبور اشتباه است.");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("خطا", "لطفاً نام کاربری و رمز عبور را وارد کنید.");
            return;
        }

        if (AuthDAO.registerUser(username, password)) {
            showAlert("موفقیت", "ثبت نام با موفقیت انجام شد. اکنون می‌توانید وارد شوید.");
        } else {
            showAlert("خطا", "ثبت نام انجام نشد. ممکن است نام کاربری تکراری باشد.");
        }
    }

    private void openMainWindow(User user) {
        try {
            FXMLLoader loader;
            if (user.isAdmin()) {
                loader = new FXMLLoader(getClass().getResource("/views/AdminPanel.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
            }
            
            Parent root = loader.load();
            
            if (user.isAdmin()) {
                AdminController adminController = loader.getController();
                // مقداردهی اولیه کنترلر ادمین اگر نیاز باشد
            } else {
                MainController mainController = loader.getController();
                mainController.setCurrentUser(user);
            }
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(user.isAdmin() ? "پنل مدیریت" : "فروشگاه آنلاین لوازم خانگی");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("خطا", "خطا در بارگذاری صفحه: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}