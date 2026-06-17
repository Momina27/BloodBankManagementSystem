package com.bloodbank.model;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRoleName() { return "ADMIN"; }
}