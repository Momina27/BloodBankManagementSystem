package com.bloodbank.util;

import com.bloodbank.model.BloodUnit;

public class BloodGroupFactory {
    public static BloodUnit createBloodUnit(String bloodGroup, int quantity) {
        if (bloodGroup == null || bloodGroup.isEmpty()) return null;
        String cleanGroup = bloodGroup.trim().toUpperCase();
        switch (cleanGroup) {
            case "A+": case "A-": case "B+": case "B-":
            case "O+": case "O-": case "AB+": case "AB-":
                return new BloodUnit(cleanGroup, quantity);
            default:
                throw new IllegalArgumentException("Invalid Blood Group: " + bloodGroup);
        }
    }
}