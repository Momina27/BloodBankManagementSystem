package com.bloodbank.interfaces;

import com.bloodbank.model.BloodRequest;
import java.util.List;

public interface IRequestHandler {
    void createRequest(BloodRequest request);
    void approveRequest(int requestId);
    void rejectRequest(int requestId);
    List<BloodRequest> getAllRequests();
}