package com.cfo.cfobot.model;

public class CloudPricing {

    public static final double SMALL_COMPUTE_RATE = 0.08;
    public static final double MEDIUM_COMPUTE_RATE = 0.12;
    public static final double LARGE_COMPUTE_RATE = 0.20;

    public static final double STORAGE_RATE_PER_GB = 0.02;
    public static final double BANDWIDTH_RATE_PER_GB = 0.12;

    public static final double BASIC_DATABASE_RATE = 15.0;
    public static final double STANDARD_DATABASE_RATE = 60.0;
    public static final double PREMIUM_DATABASE_RATE = 180.0;

    private CloudPricing() {
    }
}
