package com.bloodbank.model;

public class Donor extends User {
    private int donorId;
    private String name;
    private String bloodGroup;
    private String contact;

    public Donor(int donorId, String name, String bloodGroup, String contact, String username, String password) {
        super(username, password);
        this.donorId = donorId;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
    }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String getRoleName() { return "DONOR"; }
}