module HomeApplianceStore {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // از دیتابیس استفاده میشود
    requires java.base;
	requires java.desktop;
	requires javafx.graphics;

    opens controllers to javafx.fxml;
    exports controllers;
    exports models;
    exports app to javafx.graphics; //  `Main` را در دسترس JavaFX قرار می‌دهد.
}
