package com.cfo.cfobot.dto;

public class HistoryItemResponse {

    private CalculationRequest request;
    private CalculationResponse response;

    public HistoryItemResponse() {
    }

    public HistoryItemResponse(CalculationRequest request, CalculationResponse response) {
        this.request = request;
        this.response = response;
    }

    public CalculationRequest getRequest() {
        return request;
    }

    public void setRequest(CalculationRequest request) {
        this.request = request;
    }

    public CalculationResponse getResponse() {
        return response;
    }

    public void setResponse(CalculationResponse response) {
        this.response = response;
    }
}