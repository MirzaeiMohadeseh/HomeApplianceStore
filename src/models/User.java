package models;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private double balance;
    private boolean isAdmin;

    public User(int id, String username, String passwordHash, double balance, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public boolean isAdmin() { return isAdmin; }
}