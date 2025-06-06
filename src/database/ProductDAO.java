package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Appliance;
import models.Stove;
import models.TV;
import models.Refrigerator;

public class ProductDAO {
    public static List<Appliance> getProducts() {
        List<Appliance> products = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Appliance product = null;
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String brand = rs.getString("brand");
                String imagePath = rs.getString("image_path");

                switch (name.toLowerCase()) {
                    case "اجاق گاز":
                        product = new Stove(name, price, brand, 4); // تعداد شعله را تنظیم کن
                        break;
                    case "تلویزیون":
                        product = new TV(name, price, brand, 55); // اندازه صفحه تنظیم شود
                        break;
                    case "یخچال":
                        product = new Refrigerator(name, price, brand, 300); // ظرفیت تنظیم شود
                        break;
                    default:
                        System.out.println("محصول ناشناخته: " + name);
                }

                if (product != null) {
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}
