package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showMainPage();
    }

    public static void showMainPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/Main.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.setTitle("فروشگاه آنلاین لوازم خانگی");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
