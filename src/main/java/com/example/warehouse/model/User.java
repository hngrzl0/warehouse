package com.example.warehouse.model;

/**
 * Represents a user in the warehouse system.
 * This class encapsulates user-related details, such as username, password, email,
 * name, phone number, and role. It is used for authentication, authorization,
 * and other user-specific functionality in the application.
 *
 * <p>Includes a no-argument constructor for Firebase deserialization purposes,
 * along with getters and setters for each field.</p>
 *
 * @author Khongorzul
 * @version 1.0
 * @since 2024-11-25
 */
public class User {
    private String username;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String role;


    /**
     * Default constructor required for Firebase deserialization.
     */
    public User() {
        // Required for Firebase deserialization
    }

    /**
     * Constructs a new {@code User} with the specified details.
     *
     * @param email     The user's email address.
     * @param name      The user's full name.
     * @param password  The user's password.
     * @param phone     The user's phone number.
     * @param role      The user's role in the system (e.g., admin, user).
     * @param username  The user's unique username.
     */
    public User(String email, String name, String password, String phone, String role, String username) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.username = username;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
