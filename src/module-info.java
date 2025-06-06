module HomeApplianceStore {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // اگر از دیتابیس استفاده می‌کنی
    requires java.base;

    opens controllers to javafx.fxml;
    exports controllers;
    exports models;
    exports app to javafx.graphics; // 🚀 این خط، `Main` را در دسترس JavaFX قرار می‌دهد.
}
