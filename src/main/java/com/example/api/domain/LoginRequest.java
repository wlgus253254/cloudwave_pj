package com.example.api.domain;

public class LoginRequest {

    private String name;
    private String pw;

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String name) {
        this.name = name;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}