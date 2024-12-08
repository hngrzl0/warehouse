package com.example.warehouse.model;

/**
 * Represents a singleton user session for managing the current user.
 * <p>
 * The {@code UserSession} class follows the singleton pattern, ensuring that there is only one instance
 * throughout the application. It manages the currently logged-in user and provides methods to get
 * and set the user as well as check the user's role.
 * </p>
 * @author Margad
 * @version 1.0
 * @since 2024-12-08
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() { }

    /**
     * Returns the singleton instance of the user session.
     *
     * @return the singleton instance of {@code UserSession}
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Sets the current user of the session.
     *
     * @param user the {@link User} object representing the logged-in user
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Retrieves the current user of the session.
     *
     * @return the {@link User} object representing the currently logged-in user
     */
    public User getUser() {
        return currentUser;
    }

    /**
     * Checks if the current user has a specific role.
     *
     * @param role the role to check
     * @return {@code true} if the current user has the specified role, {@code false} otherwise
     */
    public boolean hasRole(String role) {
        return currentUser != null && currentUser.getRole().equals(role);
    }
}
