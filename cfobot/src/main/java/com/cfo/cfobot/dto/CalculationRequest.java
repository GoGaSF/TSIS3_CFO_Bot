package com.cfo.cfobot.dto;

public class CalculationRequest {

    private String computeTier;     // small, medium, large
    private double computeHours;
    private double storageGb;
    private double bandwidthGb;
    private String databaseTier;    // basic, standard, premium

    public CalculationRequest() {
    }

    public String getComputeTier() {
        return computeTier;
    }

    public void setComputeTier(String computeTier) {
        this.computeTier = computeTier;
    }

    public double getComputeHours() {
        return computeHours;
    }

    public void setComputeHours(double computeHours) {
        this.computeHours = computeHours;
    }

    public double getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(double storageGb) {
        this.storageGb = storageGb;
    }

    public double getBandwidthGb() {
        return bandwidthGb;
    }

    public void setBandwidthGb(double bandwidthGb) {
        this.bandwidthGb = bandwidthGb;
    }

    public String getDatabaseTier() {
        return databaseTier;
    }

    public void setDatabaseTier(String databaseTier) {
        this.databaseTier = databaseTier;
    }
}