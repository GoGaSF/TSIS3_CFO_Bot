# CFO Bot - Pricing Strategy Document

## 1. Purpose

This document explains the business logic behind the pricing model used in CFO Bot and justifies the selected cloud architecture assumptions.

The purpose is not to exactly reproduce a live cloud provider invoice, but to provide a transparent and realistic estimation model for monthly infrastructure planning.

---

## 2. Strategy Overview

CFO Bot uses a simplified pricing strategy based on four major cloud cost categories:
- compute
- storage
- bandwidth
- database

This structure reflects the most common recurring cost drivers in modern cloud applications.

---

## 3. Why These Components Were Selected

### Compute
Compute cost reflects server runtime and workload intensity. It is a core cost driver for application logic and API processing.

### Storage
Storage cost reflects files, application assets, and persistent data volume.

### Bandwidth
Bandwidth is important because traffic growth often increases cost significantly, especially for user-facing applications.

### Database
Database tiering allows the system to represent fixed platform cost depending on reliability and scale requirements.

---

## 4. Pricing Assumptions

### Compute
- small = $0.08/hour
- medium = $0.12/hour
- large = $0.20/hour

### Storage
- $0.02/GB per month

### Bandwidth
- $0.12/GB

### Database
- basic = $15/month
- standard = $60/month
- premium = $180/month

---

## 5. Business Scenarios

## Scenario 1 - Starter Project
Input:
- computeTier = small
- computeHours = 120
- storageGb = 100
- bandwidthGb = 150
- databaseTier = basic

Calculation:
- compute = 120 × 0.08 = 9.6
- storage = 100 × 0.02 = 2
- bandwidth = 150 × 0.12 = 18
- database = 15

Total:
- $44.60/month

Interpretation:
This scenario is appropriate for a small MVP, student startup, or internal prototype.

---

## Scenario 2 - Growth Stage
Input:
- computeTier = medium
- computeHours = 300
- storageGb = 500
- bandwidthGb = 700
- databaseTier = standard

Calculation:
- compute = 36
- storage = 10
- bandwidth = 84
- database = 60

Total:
- $190/month

Interpretation:
This scenario fits a growing application with real user traffic and stable backend requirements.

---

## Scenario 3 - High Scale
Input:
- computeTier = large
- computeHours = 650
- storageGb = 1500
- bandwidthGb = 2200
- databaseTier = premium

Calculation:
- compute = 130
- storage = 30
- bandwidth = 264
- database = 180

Total:
- $604/month

Interpretation:
This scenario fits a larger product with heavy traffic and stronger infrastructure needs.

---

## 6. Strategic Conclusions

1. Bandwidth becomes one of the strongest cost drivers as the product grows.
2. Database tier introduces a predictable fixed monthly cost.
3. Compute cost grows proportionally with system activity.
4. The selected pricing structure is simple enough for transparent explanation and realistic enough for academic business modeling.

---

## 7. Recommended Positioning

CFO Bot should be positioned as:
- a cloud economics estimation tool
- a planning assistant for early-stage architecture decisions
- a chatbot interface for infrastructure budgeting

It is especially useful in educational settings because it combines technical architecture, business planning, and cost analysis in one application.

---

## 8. Final Recommendation

The most balanced default recommendation is:
- medium compute
- standard database
- moderate storage
- controlled bandwidth usage

This combination provides reasonable cost control while still representing a production-like environment.