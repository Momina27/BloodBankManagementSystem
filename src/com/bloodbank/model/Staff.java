package com.bloodbank.model;

public class Staff extends User {
    public Staff(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRoleName() { return "STAFF"; }
}