package com.bloodbank.service;

import com.bloodbank.interfaces.IAlertObserver;
import java.util.ArrayList;
import java.util.List;

public class AlertService {
    private List<IAlertObserver> observers = new ArrayList<>();
    private static final int CRITICAL_THRESHOLD = 2;

    public void addObserver(IAlertObserver observer) { observers.add(observer); }
    public void removeObserver(IAlertObserver observer) { observers.remove(observer); }

    public void checkAndNotify(String bloodGroup, int currentStock) {
        if (currentStock <= CRITICAL_THRESHOLD) {
            for (IAlertObserver observer : observers) {
                observer.onStockCritical(bloodGroup, currentStock);
            }
        }
    }
}