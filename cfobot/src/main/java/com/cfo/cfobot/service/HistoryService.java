package com.cfo.cfobot.service;

import com.cfo.cfobot.dto.CalculationRequest;
import com.cfo.cfobot.dto.CalculationResponse;
import com.cfo.cfobot.dto.HistoryItemResponse;

import java.util.List;

public interface HistoryService {

    void save(CalculationRequest request, CalculationResponse response);

    List<HistoryItemResponse> getHistory();
}
