package database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.AdminConfig;
import models.User;

public class AuthDAO {
	public static void initializeAdmin() {
        try (Connection conn = Database.getConnection()) {
            // بررسی وجود ادمین
            boolean adminExists = checkAdminExists(conn);
            
            if (!adminExists) {
                createAdminUser(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	 private static boolean checkAdminExists(Connection conn) throws SQLException {
	        String sql = "SELECT 1 FROM users WHERE username = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, AdminConfig.ADMIN_USERNAME);
	            return stmt.executeQuery().next();
	        }
	    }
	    
	    private static void createAdminUser(Connection conn) throws SQLException {
	        String sql = "INSERT INTO users (username, password_hash, is_admin) VALUES (?, ?, ?)";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, AdminConfig.ADMIN_USERNAME);
	            stmt.setString(2, md5(AdminConfig.ADMIN_PASSWORD));
	            stmt.setBoolean(3, AdminConfig.IS_ADMIN);
	            stmt.executeUpdate();
	        }
	    }
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean registerUser(String username, String password) {
        String hashedPassword = md5(password);
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User loginUser(String username, String password) {
        String hashedPassword = md5(password);
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getDouble("balance"),
                    rs.getBoolean("is_admin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}