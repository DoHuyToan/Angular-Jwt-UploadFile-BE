package com.example.angularjwtuploadfilebe.dto.request;

public class SigInForm {
    private String username;
    private String password;

    public SigInForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SigInForm() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
