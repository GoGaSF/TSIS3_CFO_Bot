package com.cfo.cfobot.dto;

public class CalculationResponse {

    private double computeCost;
    private double storageCost;
    private double bandwidthCost;
    private double databaseCost;
    private double totalCost;
    private String explanation;

    public CalculationResponse() {
    }

    public CalculationResponse(double computeCost, double storageCost, double bandwidthCost,
                               double databaseCost, double totalCost, String explanation) {
        this.computeCost = computeCost;
        this.storageCost = storageCost;
        this.bandwidthCost = bandwidthCost;
        this.databaseCost = databaseCost;
        this.totalCost = totalCost;
        this.explanation = explanation;
    }

    public double getComputeCost() {
        return computeCost;
    }

    public void setComputeCost(double computeCost) {
        this.computeCost = computeCost;
    }

    public double getStorageCost() {
        return storageCost;
    }

    public void setStorageCost(double storageCost) {
        this.storageCost = storageCost;
    }

    public double getBandwidthCost() {
        return bandwidthCost;
    }

    public void setBandwidthCost(double bandwidthCost) {
        this.bandwidthCost = bandwidthCost;
    }

    public double getDatabaseCost() {
        return databaseCost;
    }

    public void setDatabaseCost(double databaseCost) {
        this.databaseCost = databaseCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}