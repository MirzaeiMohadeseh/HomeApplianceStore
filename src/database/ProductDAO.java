package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Appliance;
import models.Stove;
import models.TV;
import models.Refrigerator;

public class ProductDAO {
	public static List<Appliance> getAllProductsWithStock() {
	    List<Appliance> products = new ArrayList<>();
	    String sql = "SELECT * FROM products";
	    
	    try (Connection conn = Database.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            Appliance product = createProductFromResultSet(rs);
	            product.setStock(rs.getInt("stock"));
	            products.add(product);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return products;
	}
	private static Appliance createProductFromResultSet(ResultSet rs) throws SQLException {
	    int id = rs.getInt("id");
	    String name = rs.getString("name");
	    double price = rs.getDouble("price");
	    String brand = rs.getString("brand");
	    int stock = rs.getInt("stock");
	    String type = rs.getString("type");
	    
	    // حالا type از سوپرکلاس به ارث می‌رسد
	    switch (type.toLowerCase()) {
	        case "stove":
	            return new Stove(id, name, price, brand, 4);
	        case "tv":
	            return new TV(id, name, price, brand, 55);
	        case "refrigerator":
	            return new Refrigerator(id, name, price, brand, 300);
	        default:
	            return new Appliance(id, name, price, brand, type) {};
	    }
	}

	public static boolean updateStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newStock);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
