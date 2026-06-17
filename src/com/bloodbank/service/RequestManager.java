package com.bloodbank.service;

import com.bloodbank.interfaces.IRequestHandler;
import com.bloodbank.model.BloodRequest;
import java.util.ArrayList;
import java.util.List;

public class RequestManager implements IRequestHandler {
    private List<BloodRequest> requestList = new ArrayList<>();
    private BloodInventory inventory;

    public RequestManager(BloodInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void createRequest(BloodRequest request) { requestList.add(request); }

    @Override
    public void approveRequest(int requestId) {
        for (BloodRequest req : requestList) {
            if (req.getRequestId() == requestId && req.getStatus().equals("Pending")) {
                if (inventory.reduceStock(req.getBloodGroup(), req.getQuantity())) {
                    req.setStatus("Approved");
                } else {
                    req.setStatus("Rejected (Stock Low)");
                }
                break;
            }
        }
    }

    @Override
    public void rejectRequest(int requestId) {
        for (BloodRequest req : requestList) {
            if (req.getRequestId() == requestId && req.getStatus().equals("Pending")) {
                req.setStatus("Rejected");
                break;
            }
        }
    }

    @Override
    public List<BloodRequest> getAllRequests() { return requestList; }
}