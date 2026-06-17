package com.bloodbank.interfaces;

public interface IAlertObserver {
    void onStockCritical(String bloodGroup, int currentStock);
}