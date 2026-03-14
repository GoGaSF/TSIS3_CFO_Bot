# CFO Bot

CFO Bot is a Spring Boot monolithic web application that estimates monthly cloud infrastructure cost through a chatbot-style interface.

## Features

- chatbot-style web interface
- cloud cost estimation
- detailed cost breakdown
- Swagger API documentation
- system info endpoint
- static frontend served by Spring Boot

## Technology Stack

- Java 17
- Spring Boot
- Spring Web
- Springdoc OpenAPI / Swagger
- HTML
- CSS
- JavaScript
- Maven

## Supported Cost Components

- Compute
- Storage
- Bandwidth
- Database

## Pricing Model

### Compute
- small = $0.08/hour
- medium = $0.12/hour
- large = $0.20/hour

### Storage
- $0.02/GB

### Bandwidth
- $0.12/GB

### Database
- basic = $15/month
- standard = $60/month
- premium = $180/month

## Run the Project

Start the Spring Boot application.

Then open:

### Main Application
`http://localhost:8080`

### Swagger UI
`http://localhost:8080/swagger-ui/index.html`

## API Endpoints

### Health Check
`GET /api/cfo/ping`

### Structured Calculation
`POST /api/cfo/calculate`

### Chatbot Response
`POST /api/cfo/chat`

### System Info
`GET /api/system/info`

## Example Request

```json
{
  "computeTier": "medium",
  "computeHours": 300,
  "storageGb": 500,
  "bandwidthGb": 700,
  "databaseTier": "standard"
}