package org.softdesign.model.login;

public class LoginRequest {

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\": \"" + username + "\"," +
                "\"password\": \"" + password + "\"" +
                "}";
    }
}
