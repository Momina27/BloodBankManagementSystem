package com.bloodbank.interfaces;

public interface IInventory {
    void addBloodUnit(String bloodGroup, int quantity);
    int checkStock(String bloodGroup);
    boolean reduceStock(String bloodGroup, int quantity);
}