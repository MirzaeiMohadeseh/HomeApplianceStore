package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/home_appliance_store?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // اگر رمز عبور MySQL در XAMPP تنظیم نشده، مقدار را خالی بگذار

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 🚀 بارگذاری دستی درایور
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("درایور MySQL پیدا نشد!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("خطا در اتصال به MySQL!");
            e.printStackTrace();
            return null;
        }
    }
}
