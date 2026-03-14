package com.cfo.cfobot.service.impl;

import com.cfo.cfobot.dto.CalculationRequest;
import com.cfo.cfobot.dto.CalculationResponse;
import com.cfo.cfobot.dto.ChatResponse;
import com.cfo.cfobot.dto.RecommendationResponse;
import com.cfo.cfobot.dto.ScenarioResponse;
import com.cfo.cfobot.model.CloudPricing;
import com.cfo.cfobot.service.CalculationService;
import com.cfo.cfobot.service.HistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculationServiceImpl implements CalculationService {

    private final HistoryService historyService;

    public CalculationServiceImpl(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    public CalculationResponse calculate(CalculationRequest request) {
        validateRequest(request);

        double computeCost = calculateComputeCost(request.getComputeTier(), request.getComputeHours());
        double storageCost = round(request.getStorageGb() * CloudPricing.STORAGE_RATE_PER_GB);
        double bandwidthCost = round(request.getBandwidthGb() * CloudPricing.BANDWIDTH_RATE_PER_GB);
        double databaseCost = calculateDatabaseCost(request.getDatabaseTier());

        double totalCost = round(computeCost + storageCost + bandwidthCost + databaseCost);

        String explanation = buildExplanation(request, computeCost, storageCost, bandwidthCost, databaseCost, totalCost);

        CalculationResponse response = new CalculationResponse(
                computeCost,
                storageCost,
                bandwidthCost,
                databaseCost,
                totalCost,
                explanation
        );

        historyService.save(request, response);

        return response;
    }

    @Override
    public ChatResponse chatCalculate(CalculationRequest request) {
        CalculationResponse result = calculateWithoutSaving(request);

        String message =
                "CFO Bot Cloud Cost Estimate\n\n" +
                        "  Infrastructure breakdown:\n" +
                        "• Compute (" + request.getComputeTier() + "): $" + result.getComputeCost() + "\n" +
                        "• Storage: $" + result.getStorageCost() + "\n" +
                        "• Bandwidth: $" + result.getBandwidthCost() + "\n" +
                        "• Database (" + request.getDatabaseTier() + "): $" + result.getDatabaseCost() + "\n\n" +
                        "----------------------------------\n" +
                        "  Total Monthly Cost: $" + result.getTotalCost() + "\n\n" +
                        "Recommendation:\n" +
                        getSingleRecommendationText(request, result);

        return new ChatResponse(message);
    }

    @Override
    public RecommendationResponse getRecommendations(CalculationRequest request) {
        CalculationResponse result = calculate(request);
        List<String> recommendations = new ArrayList<>();

        if (result.getBandwidthCost() >= result.getComputeCost() && result.getBandwidthCost() >= result.getDatabaseCost()) {
            recommendations.add("Bandwidth is your largest cost driver. Consider CDN usage or traffic optimization.");
        }

        if ("premium".equalsIgnoreCase(request.getDatabaseTier()) && request.getComputeHours() < 250) {
            recommendations.add("Premium database may be too expensive for this workload. Consider the standard tier.");
        }

        if ("large".equalsIgnoreCase(request.getComputeTier()) && request.getComputeHours() < 250) {
            recommendations.add("Large compute tier may be excessive for current workload. Medium tier could be enough.");
        }

        if (request.getComputeHours() > 600) {
            recommendations.add("Compute usage is very high. Consider autoscaling or moving background jobs to off-peak hours.");
        }

        if (request.getStorageGb() > 1000) {
            recommendations.add("Storage volume is high. Consider archiving cold files to cheaper storage classes.");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Your current configuration looks balanced for the provided workload.");
        }

        return new RecommendationResponse(recommendations);
    }

    @Override
    public List<ScenarioResponse> getScenarios() {
        List<ScenarioResponse> scenarios = new ArrayList<>();

        CalculationRequest starter = buildRequest("small", 120, 100, 150, "basic");
        CalculationRequest growth = buildRequest("medium", 300, 500, 700, "standard");
        CalculationRequest scale = buildRequest("large", 650, 1500, 2200, "premium");

        scenarios.add(new ScenarioResponse(
                "Starter",
                "Small MVP or student startup environment",
                starter,
                calculateWithoutSaving(starter)
        ));

        scenarios.add(new ScenarioResponse(
                "Growth",
                "Growing product with moderate traffic",
                growth,
                calculateWithoutSaving(growth)
        ));

        scenarios.add(new ScenarioResponse(
                "Scale",
                "Higher-scale product with heavier load",
                scale,
                calculateWithoutSaving(scale)
        ));

        return scenarios;
    }

    @Override
    public Map<String, Object> getPricingModel() {
        Map<String, Object> pricing = new LinkedHashMap<>();

        pricing.put("compute_small_per_hour", CloudPricing.SMALL_COMPUTE_RATE);
        pricing.put("compute_medium_per_hour", CloudPricing.MEDIUM_COMPUTE_RATE);
        pricing.put("compute_large_per_hour", CloudPricing.LARGE_COMPUTE_RATE);
        pricing.put("storage_per_gb", CloudPricing.STORAGE_RATE_PER_GB);
        pricing.put("bandwidth_per_gb", CloudPricing.BANDWIDTH_RATE_PER_GB);
        pricing.put("database_basic", CloudPricing.BASIC_DATABASE_RATE);
        pricing.put("database_standard", CloudPricing.STANDARD_DATABASE_RATE);
        pricing.put("database_premium", CloudPricing.PREMIUM_DATABASE_RATE);

        return pricing;
    }

    private CalculationResponse calculateWithoutSaving(CalculationRequest request) {
        validateRequest(request);

        double computeCost = calculateComputeCost(request.getComputeTier(), request.getComputeHours());
        double storageCost = round(request.getStorageGb() * CloudPricing.STORAGE_RATE_PER_GB);
        double bandwidthCost = round(request.getBandwidthGb() * CloudPricing.BANDWIDTH_RATE_PER_GB);
        double databaseCost = calculateDatabaseCost(request.getDatabaseTier());
        double totalCost = round(computeCost + storageCost + bandwidthCost + databaseCost);

        String explanation = buildExplanation(request, computeCost, storageCost, bandwidthCost, databaseCost, totalCost);

        return new CalculationResponse(
                computeCost,
                storageCost,
                bandwidthCost,
                databaseCost,
                totalCost,
                explanation
        );
    }

    private CalculationRequest buildRequest(String computeTier, double computeHours, double storageGb, double bandwidthGb, String databaseTier) {
        CalculationRequest request = new CalculationRequest();
        request.setComputeTier(computeTier);
        request.setComputeHours(computeHours);
        request.setStorageGb(storageGb);
        request.setBandwidthGb(bandwidthGb);
        request.setDatabaseTier(databaseTier);
        return request;
    }

    private void validateRequest(CalculationRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is empty");
        }

        if (request.getComputeTier() == null || request.getComputeTier().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Compute tier is required");
        }

        if (request.getDatabaseTier() == null || request.getDatabaseTier().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Database tier is required");
        }

        if (request.getComputeHours() < 0 || request.getComputeHours() > 744) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Compute hours must be between 0 and 744");
        }

        if (request.getStorageGb() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Storage GB cannot be negative");
        }

        if (request.getBandwidthGb() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bandwidth GB cannot be negative");
        }

        String computeTier = request.getComputeTier().toLowerCase();
        if (!computeTier.equals("small") && !computeTier.equals("medium") && !computeTier.equals("large")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Compute tier must be small, medium or large");
        }

        String databaseTier = request.getDatabaseTier().toLowerCase();
        if (!databaseTier.equals("basic") && !databaseTier.equals("standard") && !databaseTier.equals("premium")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Database tier must be basic, standard or premium");
        }
    }

    private double calculateComputeCost(String computeTier, double computeHours) {
        double rate;

        switch (computeTier.toLowerCase()) {
            case "small":
                rate = CloudPricing.SMALL_COMPUTE_RATE;
                break;
            case "medium":
                rate = CloudPricing.MEDIUM_COMPUTE_RATE;
                break;
            case "large":
                rate = CloudPricing.LARGE_COMPUTE_RATE;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid compute tier");
        }

        return round(computeHours * rate);
    }

    private double calculateDatabaseCost(String databaseTier) {
        switch (databaseTier.toLowerCase()) {
            case "basic":
                return CloudPricing.BASIC_DATABASE_RATE;
            case "standard":
                return CloudPricing.STANDARD_DATABASE_RATE;
            case "premium":
                return CloudPricing.PREMIUM_DATABASE_RATE;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid database tier");
        }
    }

    private String buildExplanation(CalculationRequest request,
                                    double computeCost,
                                    double storageCost,
                                    double bandwidthCost,
                                    double databaseCost,
                                    double totalCost) {

        return "Compute (" + request.getComputeTier() + "): $" + computeCost +
                ", Storage: $" + storageCost +
                ", Bandwidth: $" + bandwidthCost +
                ", Database (" + request.getDatabaseTier() + "): $" + databaseCost +
                ". Total monthly cost = $" + totalCost;
    }

    private String getSingleRecommendationText(CalculationRequest request, CalculationResponse result) {
        if (result.getBandwidthCost() >= result.getComputeCost() && result.getBandwidthCost() >= result.getDatabaseCost()) {
            return "Bandwidth is a major cost driver. Consider CDN usage or traffic optimization.";
        }

        if ("premium".equalsIgnoreCase(request.getDatabaseTier()) && request.getComputeHours() < 250) {
            return "Premium database may be excessive for this workload. Standard tier could be enough.";
        }

        if (request.getComputeHours() > 600) {
            return "Compute usage is high. Consider autoscaling or workload scheduling optimization.";
        }

        if (result.getTotalCost() < 80) {
            return "This is a lightweight environment and should be affordable for MVP usage.";
        }

        return "This configuration is suitable for medium-scale workloads.";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}