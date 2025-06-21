package config;

public class AdminConfig {
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "SecureAdmin123!"; // رمز عبور قوی
    public static final boolean IS_ADMIN = true;
    
    // این متد بررسی می‌کند آیا کاربر ادمین اصلی است یا خیر
    public static boolean isMainAdmin(String username) {
        return ADMIN_USERNAME.equals(username);
    }
}