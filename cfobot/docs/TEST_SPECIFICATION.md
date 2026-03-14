# CFO Bot - Test Specification

## 1. Objective

The objective of testing is to verify that CFO Bot correctly calculates monthly cloud cost estimates, validates user input, and exposes working web/API interfaces.

---

## 2. Testing Scope

The following areas are included:
- pricing calculations
- validation logic
- API behavior
- chatbot response generation
- frontend interaction
- local system availability

---

## 3. Unit Test Scenarios

## 3.1 Compute Pricing

### Test Case UT-01
Input:
- computeTier = small
- computeHours = 100

Expected:
- computeCost = 8.00

### Test Case UT-02
Input:
- computeTier = medium
- computeHours = 300

Expected:
- computeCost = 36.00

### Test Case UT-03
Input:
- computeTier = large
- computeHours = 200

Expected:
- computeCost = 40.00

---

## 3.2 Storage Pricing

### Test Case UT-04
Input:
- storageGb = 500

Expected:
- storageCost = 10.00

### Test Case UT-05
Input:
- storageGb = 0

Expected:
- storageCost = 0.00

---

## 3.3 Bandwidth Pricing

### Test Case UT-06
Input:
- bandwidthGb = 700

Expected:
- bandwidthCost = 84.00

### Test Case UT-07
Input:
- bandwidthGb = 0

Expected:
- bandwidthCost = 0.00

---

## 3.4 Database Pricing

### Test Case UT-08
Input:
- databaseTier = basic

Expected:
- databaseCost = 15.00

### Test Case UT-09
Input:
- databaseTier = standard

Expected:
- databaseCost = 60.00

### Test Case UT-10
Input:
- databaseTier = premium

Expected:
- databaseCost = 180.00

---

## 4. Integration Test Scenarios

### Test Case IT-01
Input:
- computeTier = medium
- computeHours = 300
- storageGb = 500
- bandwidthGb = 700
- databaseTier = standard

Expected:
- computeCost = 36
- storageCost = 10
- bandwidthCost = 84
- databaseCost = 60
- totalCost = 190

### Test Case IT-02
The `/api/cfo/calculate` endpoint returns valid JSON response.

### Test Case IT-03
The `/api/cfo/chat` endpoint returns chatbot-formatted text message.

### Test Case IT-04
The `/api/system/info` endpoint returns application metadata.

---

## 5. Validation Test Scenarios

### Test Case VT-01
Input:
- computeHours = -1

Expected:
- HTTP 400

### Test Case VT-02
Input:
- computeHours = 800

Expected:
- HTTP 400

### Test Case VT-03
Input:
- storageGb = -10

Expected:
- HTTP 400

### Test Case VT-04
Input:
- bandwidthGb = -20

Expected:
- HTTP 400

### Test Case VT-05
Input:
- computeTier = ultra

Expected:
- HTTP 400

### Test Case VT-06
Input:
- databaseTier = enterprise

Expected:
- HTTP 400

### Test Case VT-07
Input:
- null request body

Expected:
- HTTP 400

---

## 6. UI Test Scenarios

### Test Case UI-01
Homepage opens successfully on:
`http://localhost:8080`

### Test Case UI-02
Swagger opens successfully on:
`http://localhost:8080/swagger-ui/index.html`

### Test Case UI-03
User can type input values and submit form.

### Test Case UI-04
Bot response appears in the chat window.

### Test Case UI-05
Load Example button fills the form.

### Test Case UI-06
Reset Chat button clears previous conversation.

---

## 7. Acceptance Test

The system passes acceptance testing if:
- all calculation formulas return correct values
- invalid input is rejected
- homepage is accessible
- Swagger is accessible
- at least one example scenario can be demonstrated successfully in class