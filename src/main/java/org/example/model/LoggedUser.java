package org.example.model;

public class LoggedUser {

    private String userId;
    private String username;
    private String password;

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LoggedUser(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
    public LoggedUser(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
