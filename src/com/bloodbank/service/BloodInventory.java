package com.bloodbank.service;

import com.bloodbank.interfaces.IInventory;
import java.util.HashMap;
import java.util.Map;

public class BloodInventory implements IInventory {
    private Map<String, Integer> stock = new HashMap<>();
    private AlertService alertService;

    public BloodInventory(AlertService alertService) {
        this.alertService = alertService;
        String[] groups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        for (String group : groups) {
            stock.put(group, 0);
        }
    }

    @Override
    public void addBloodUnit(String bloodGroup, int quantity) {
        int newStock = stock.getOrDefault(bloodGroup, 0) + quantity;
        stock.put(bloodGroup, newStock);
        alertService.checkAndNotify(bloodGroup, newStock);
    }

    @Override
    public int checkStock(String bloodGroup) { return stock.getOrDefault(bloodGroup, 0); }

    @Override
    public boolean reduceStock(String bloodGroup, int quantity) {
        int current = checkStock(bloodGroup);
        if (current >= quantity) {
            int newStock = current - quantity;
            stock.put(bloodGroup, newStock);
            alertService.checkAndNotify(bloodGroup, newStock);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getAllStock() { return stock; }
}