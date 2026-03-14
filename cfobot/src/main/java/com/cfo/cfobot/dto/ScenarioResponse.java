package com.cfo.cfobot.dto;

public class ScenarioResponse {

    private String name;
    private String description;
    private CalculationRequest request;
    private CalculationResponse result;

    public ScenarioResponse() {
    }

    public ScenarioResponse(String name, String description, CalculationRequest request, CalculationResponse result) {
        this.name = name;
        this.description = description;
        this.request = request;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CalculationRequest getRequest() {
        return request;
    }

    public void setRequest(CalculationRequest request) {
        this.request = request;
    }

    public CalculationResponse getResult() {
        return result;
    }

    public void setResult(CalculationResponse result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}