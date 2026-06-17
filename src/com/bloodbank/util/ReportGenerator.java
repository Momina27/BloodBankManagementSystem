package com.bloodbank.util;

import com.bloodbank.service.BloodInventory;
import com.bloodbank.service.DonorManager;
import java.util.Map;

public class ReportGenerator {
    public static String generateStockReport(BloodInventory inventory, DonorManager donorManager) {
        StringBuilder report = new StringBuilder();
        report.append("=========================================\n");
        report.append("       🔴 BBMS SYSTEM STATUS REPORT      \n");
        report.append("=========================================\n\n");
        report.append("👥 Total Registered Donors: ").append(donorManager.getAllDonors().size()).append("\n\n");
        report.append("🩸 Current Blood Stock Summary:\n");
        report.append("-----------------------------------------\n");
        int totalUnits = 0;
        for (Map.Entry<String, Integer> entry : inventory.getAllStock().entrySet()) {
            report.append(String.format("  • Blood Group %-4s : %d Units\n", entry.getKey(), entry.getValue()));
            totalUnits += entry.getValue();
        }
        report.append("-----------------------------------------\n");
        report.append("📊 Total Available Stock: ").append(totalUnits).append(" Units\n");
        report.append("=========================================");
        return report.toString();
    }
}