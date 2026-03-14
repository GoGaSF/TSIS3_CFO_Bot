# CFO Bot - System Specification (SSOT)

## 1. Project Overview

CFO Bot is a monolithic web application built with Spring Boot that estimates monthly cloud infrastructure costs through a chatbot-style interface. The system allows users to enter cloud usage assumptions and receive a detailed monthly cost breakdown for compute, storage, bandwidth, and database services.

This specification acts as the Single Source of Truth (SSOT) for the entire project. All implementation decisions, testing scenarios, and deployment behavior must align with this document.

---

## 2. Project Goal

The goal of CFO Bot is to provide a simple and understandable cloud cost estimation tool for startup founders, project managers, and students who need to model infrastructure expenses without integrating with real cloud billing APIs.

---

## 3. Scope

### In Scope
- Cloud cost estimation via chatbot-style web interface
- Monthly cost calculation for selected cloud components
- Breakdown of cost categories
- Swagger API testing support
- Monolithic Spring Boot deployment
- Input validation and error handling

### Out of Scope
- Real billing API integrations
- Persistent storage of calculations
- Historical analytics
- Multi-user dashboards
- Role-based administration
- Real-time cloud price synchronization

---

## 4. Supported Cloud Components

The system supports the following cost categories:

1. Compute
2. Storage
3. Bandwidth
4. Database

---

## 5. Pricing Model

The project uses a fixed internal pricing model.

### 5.1 Compute Pricing
- small = $0.08 per hour
- medium = $0.12 per hour
- large = $0.20 per hour

Formula:

`computeCost = computeHours × computeTierRate`

### 5.2 Storage Pricing
- storage = $0.02 per GB per month

Formula:

`storageCost = storageGb × 0.02`

### 5.3 Bandwidth Pricing
- bandwidth = $0.12 per GB

Formula:

`bandwidthCost = bandwidthGb × 0.12`

### 5.4 Database Pricing
- basic = $15 per month
- standard = $60 per month
- premium = $180 per month

Formula:

`databaseCost = fixed monthly tier price`

### 5.5 Total Cost Formula

`totalCost = computeCost + storageCost + bandwidthCost + databaseCost`

All monetary values are rounded to 2 decimal places.

---

## 6. Input Parameters

The chatbot accepts the following input fields:

- `computeTier` — string, allowed values: `small`, `medium`, `large`
- `computeHours` — number, range: `0` to `744`
- `storageGb` — number, must be `>= 0`
- `bandwidthGb` — number, must be `>= 0`
- `databaseTier` — string, allowed values: `basic`, `standard`, `premium`

---

## 7. Functional Requirements

### FR-1
The system shall provide a homepage with a chatbot-style user interface.

### FR-2
The system shall allow the user to enter cloud usage assumptions through a web form.

### FR-3
The system shall send the input data to the backend through a REST endpoint.

### FR-4
The system shall calculate compute, storage, bandwidth, and database costs.

### FR-5
The system shall return a full cost breakdown and total monthly estimate.

### FR-6
The system shall provide a chatbot-style textual response.

### FR-7
The system shall provide an API endpoint for direct cost calculation.

### FR-8
The system shall expose Swagger documentation for API testing.

### FR-9
The system shall provide a system information endpoint.

### FR-10
The system shall support chat reset and example loading in the frontend.

---

## 8. Non-Functional Requirements

### NFR-1 Performance
The system should return a calculation response in under 2 seconds on local environment.

### NFR-2 Usability
The UI should be simple, beginner-friendly, and suitable for live demo.

### NFR-3 Maintainability
Business logic should be separated into service classes.

### NFR-4 Deployability
The application should run as a single Spring Boot monolith.

### NFR-5 Testability
The pricing model must be deterministic and easy to test with predefined inputs.

### NFR-6 Accessibility
The application should remain usable on desktop and mobile screens.

---

## 9. Validation Rules

- Request body must not be null
- `computeTier` must be one of: `small`, `medium`, `large`
- `databaseTier` must be one of: `basic`, `standard`, `premium`
- `computeHours` must be between `0` and `744`
- `storageGb` cannot be negative
- `bandwidthGb` cannot be negative

If validation fails, the API returns HTTP 400.

---

## 10. Architecture Constraints

- Architecture type: monolithic
- Backend: Spring Boot
- Frontend: static HTML, CSS, JavaScript served from Spring Boot
- API style: REST
- No database required
- No external cloud provider API integration required

---

## 11. API Endpoints

### `GET /api/cfo/ping`
Health check endpoint.

### `POST /api/cfo/calculate`
Returns structured cost breakdown in JSON form.

### `POST /api/cfo/chat`
Returns chatbot-style text response with total estimate.

### `GET /api/system/info`
Returns basic information about the system.

### `GET /swagger-ui/index.html`
Provides API documentation and testing interface.

---

## 12. Frontend Requirements

The frontend must include:
- Project title and subtitle
- Chatbox for messages
- Form inputs for all pricing parameters
- Send button
- Reset chat button
- Load example button
- Swagger shortcut link

---

## 13. Acceptance Criteria

The project is considered complete when:
- the application starts successfully
- the homepage opens on `http://localhost:8080`
- Swagger opens on `http://localhost:8080/swagger-ui/index.html`
- `/api/cfo/chat` returns chatbot-style output
- `/api/cfo/calculate` returns correct JSON breakdown
- invalid input is rejected
- the user can demonstrate one full example scenario live

---

## 14. Example Scenario

Input:
- computeTier = medium
- computeHours = 300
- storageGb = 500
- bandwidthGb = 700
- databaseTier = standard

Expected calculation:
- compute = 300 × 0.12 = 36
- storage = 500 × 0.02 = 10
- bandwidth = 700 × 0.12 = 84
- database = 60
- total = 190

Expected output:
A chatbot-style answer with full infrastructure breakdown and total monthly estimate of `$190`.