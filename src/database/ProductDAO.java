package database;

import models.Appliance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	    String imagePath = rs.getString("image_path");
	    int stock = rs.getInt("stock");
	    String type = rs.getString("type");
	    
	    Appliance product = null;
	    
	    switch (type.toLowerCase()) {
	        case "stove":
	            product = new Stove(id, name, price, brand, imagePath, stock, type, 4);
	            break;
	        case "tv":
	            product = new TV(id, name, price, brand, imagePath, stock, type, 55);
	            break;
	        case "refrigerator":
	            product = new Refrigerator(id, name, price, brand, imagePath, stock, type, 300);
	            break;
	        default:
	            product = new Appliance(id, name, price, brand, imagePath, stock, type) {};
	    }
	    
	    return product;
	}

	public static boolean updateProductStock(int productId, int newStock) {
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
