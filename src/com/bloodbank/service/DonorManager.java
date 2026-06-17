package com.bloodbank.service;

import com.bloodbank.model.Donor;
import java.util.ArrayList;
import java.util.List;

public class DonorManager {
    private List<Donor> donorList = new ArrayList<>();

    public void addDonor(Donor donor) { donorList.add(donor); }
    public List<Donor> getAllDonors() { return donorList; }

    public void updateDonor(int id, String newName, String newBloodGroup, String newContact) {
        for (Donor d : donorList) {
            if (d.getDonorId() == id) {
                d.setName(newName);
                d.setBloodGroup(newBloodGroup);
                d.setContact(newContact);
                break;
            }
        }
    }

    public void deleteDonor(int id) { donorList.removeIf(d -> d.getDonorId() == id); }

    public Donor searchDonor(int id) {
        for (Donor d : donorList) {
            if (d.getDonorId() == id) return d;
        }
        return null;
    }

    public List<Donor> searchDonor(String name) {
        List<Donor> results = new ArrayList<>();
        for (Donor d : donorList) {
            if (d.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(d);
            }
        }
        return results;
    }
}