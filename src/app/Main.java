package app;

import database.AuthDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AuthDAO.initializeAdmin();
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setTitle("ورود به فروشگاه");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}