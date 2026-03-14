package com.cfo.cfobot.service.impl;

import com.cfo.cfobot.dto.CalculationRequest;
import com.cfo.cfobot.dto.CalculationResponse;
import com.cfo.cfobot.dto.HistoryItemResponse;
import com.cfo.cfobot.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final LinkedList<HistoryItemResponse> history = new LinkedList<>();
    private static final int MAX_HISTORY_SIZE = 10;

    @Override
    public synchronized void save(CalculationRequest request, CalculationResponse response) {
        history.addFirst(new HistoryItemResponse(copyRequest(request), copyResponse(response)));

        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeLast();
        }
    }

    @Override
    public synchronized List<HistoryItemResponse> getHistory() {
        return List.copyOf(history);
    }

    private CalculationRequest copyRequest(CalculationRequest request) {
        CalculationRequest copy = new CalculationRequest();
        copy.setComputeTier(request.getComputeTier());
        copy.setComputeHours(request.getComputeHours());
        copy.setStorageGb(request.getStorageGb());
        copy.setBandwidthGb(request.getBandwidthGb());
        copy.setDatabaseTier(request.getDatabaseTier());
        return copy;
    }

    private CalculationResponse copyResponse(CalculationResponse response) {
        return new CalculationResponse(
                response.getComputeCost(),
                response.getStorageCost(),
                response.getBandwidthCost(),
                response.getDatabaseCost(),
                response.getTotalCost(),
                response.getExplanation()
        );
    }
}