# Microservice Learning Project

This project is a simple implementation of a microservice architecture for an e-commerce application.

## Architecture

The project consists of three main services:

-   **Customer Service**: Manages customer data.
-   **Product Service**: Manages product data.
-   **Order Service**: Manages customer orders.

All three services are connected to the same PostgreSQL database named `microservice_db`.

## Technology Stack

-   **Java**: 17
-   **Spring Boot**: 3.5.4
-   **Maven**: Dependency Management
-   **PostgreSQL**: Database

## Prerequisites

Before running this project, ensure you have the following software installed:

-   Java (version 17 or higher)
-   Maven
-   PostgreSQL

## How to Run

1.  **Clone the Repository**
    Clone this repository to your local machine.

2.  **Database Setup**
    Make sure your PostgreSQL server is running and create a new database named `microservice_db`.

3.  **Run the Services**
    Open a separate terminal for each service directory (`customer-service`, `order-service`, `product-service`) and run the command below:

    ```bash
    mvn spring-boot:run
    ```

    Once successfully started, the services will be available at the following ports:
    -   **Customer Service**: `http://localhost:9091`
    -   **Order Service**: `http://localhost:9092`
    -   **Product Service**: `http://localhost:9093`

## Swagger UI

Each microservice now includes Swagger UI for API documentation. You can access them at the following endpoints:

-   **Customer Service**: `http://localhost:9091/swagger-ui.html`
-   **Order Service**: `http://localhost:9092/swagger-ui.html`
-   **Product Service**: `http://localhost:9093/swagger-ui.html`
