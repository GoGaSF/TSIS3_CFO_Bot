package com.cfo.cfobot.service;

import com.cfo.cfobot.dto.CalculationRequest;
import com.cfo.cfobot.dto.CalculationResponse;
import com.cfo.cfobot.dto.ChatResponse;
import com.cfo.cfobot.dto.RecommendationResponse;
import com.cfo.cfobot.dto.ScenarioResponse;

import java.util.List;
import java.util.Map;

public interface CalculationService {

    CalculationResponse calculate(CalculationRequest request);

    ChatResponse chatCalculate(CalculationRequest request);

    RecommendationResponse getRecommendations(CalculationRequest request);

    List<ScenarioResponse> getScenarios();

    Map<String, Object> getPricingModel();
}