package com.example.warehouse.model;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() { }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public boolean hasRole(String role) {
        return currentUser != null && currentUser.getRole().equals(role);
    }
}
