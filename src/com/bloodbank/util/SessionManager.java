package com.bloodbank.util;

import com.bloodbank.model.*;
import com.bloodbank.service.InvalidLoginException;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public boolean login(String username, String password) throws InvalidLoginException {
        if (username.equals("momina") && password.equals("12345")) {
            currentUser = new Admin(username, password);
            return true;
        } else if (username.equals("staff") && password.equals("staff123")) {
            currentUser = new Staff(username, password);
            return true;
        }
        throw new InvalidLoginException("Wrong username &password try again.");
    }

    public User getCurrentUser() { return currentUser; }
    public void logout() { currentUser = null; }
}