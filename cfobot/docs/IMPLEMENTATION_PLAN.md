# CFO Bot - Implementation Plan

## 1. Planning Strategy

The project followed a Spec-Driven Development (SDD) style workflow.  
Instead of starting from code, the system was first described through a specification that defined the supported components, pricing formulas, validation rules, endpoints, and UI behavior.

This specification was treated as the Single Source of Truth (SSOT).

---

## 2. Planned Development Phases

### Phase 1 - Specification Definition
The following system aspects were defined before coding:
- project scope
- supported cloud components
- pricing formulas
- input validation rules
- API structure
- UI requirements
- architectural constraints

### Phase 2 - Backend Development
The backend was implemented as a Spring Boot monolith.

Tasks:
- create DTO classes
- create pricing model constants
- implement service layer
- implement validation logic
- implement REST controller
- add Swagger support

### Phase 3 - Chatbot Response Layer
The system was extended from a plain calculator into a chatbot-style assistant.

Tasks:
- add `/api/cfo/chat`
- generate formatted text response
- improve user-facing language
- support live chat demo

### Phase 4 - Frontend Development
A static frontend was embedded directly inside the Spring Boot application.

Tasks:
- create `index.html`
- create `style.css`
- create `app.js`
- connect form submission to backend
- render chat messages
- add reset and example features

### Phase 5 - API Documentation
Swagger/OpenAPI documentation was added.

Tasks:
- configure OpenAPI metadata
- annotate endpoints
- verify local API testing experience

### Phase 6 - Documentation and Business Layer
Project documentation was prepared to support final defense.

Tasks:
- prepare SSOT
- prepare implementation plan
- prepare test specification
- prepare pricing strategy document
- prepare README
- prepare defense notes

---

## 3. Artifact-Oriented Development

The project intentionally produced the following artifacts before final submission:
- System Specification
- OpenAPI documentation
- Implementation Plan
- Test Specification
- Pricing Strategy Document
- Working web application

This aligns with the SDD idea of generating verifiable artifacts before treating the code as final.

---

## 4. Main Design Decisions

### 4.1 Monolithic Architecture
Chosen because:
- simpler deployment
- easier to run locally
- easier to defend
- no frontend/backend synchronization problems
- suitable for academic project scope

### 4.2 Fixed Pricing Model
Chosen because:
- deterministic calculations
- easier testing
- clearer demonstrations
- no dependency on external cloud APIs

### 4.3 Chatbot-Style Interface
Chosen because:
- directly matches the assignment theme
- easier for demo than a raw calculator form
- improves usability and presentation

### 4.4 Swagger Integration
Chosen because:
- easier local testing
- better visibility of API design
- stronger presentation during defense

---

## 5. Final Deliverables Produced

- working Spring Boot monolith
- chatbot-style web interface
- cloud cost calculation API
- Swagger/OpenAPI documentation
- system information endpoint
- project documentation
- business pricing strategy explanation

---

## 6. Future Improvements

Possible future versions may include:
- user authentication
- saved calculation history
- support for more cloud components
- cost comparison across providers
- Firebase deployment or cloud hosting
- richer UI and charts