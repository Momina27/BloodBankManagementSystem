package com.bloodbank.model;

public class BloodRequest {
    private int requestId;
    private String hospitalName;
    private String bloodGroup;
    private int quantity;
    private String status;

    public BloodRequest(int requestId, String hospitalName, String bloodGroup, int quantity) {
        this.requestId = requestId;
        this.hospitalName = hospitalName;
        this.bloodGroup = bloodGroup;
        this.quantity = quantity;
        this.status = "Pending";
    }

    public int getRequestId() { return requestId; }
    public String getHospitalName() { return hospitalName; }
    public String getBloodGroup() { return bloodGroup; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}