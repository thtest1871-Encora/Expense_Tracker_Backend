# Expense Tracker Backend

A robust, microservices-based backend for a personal finance and expense tracking application. Built with Java, Spring Boot, and Spring Cloud.

## 🚀 Tech Stack

*   **Language:** Java 17+
*   **Framework:** Spring Boot 3.x
*   **Architecture:** Microservices
*   **Service Discovery:** Netflix Eureka
*   **API Gateway:** Spring Cloud Gateway
*   **Database:** MySQL
*   **Build Tool:** Maven

## 📂 Service Architecture

The system is composed of the following independent services:

1.  **Eureka Server** (`eurekaserver`): Service registry and discovery.
2.  **API Gateway** (`apigateway`): Single entry point for all client requests (runs on port `8080`).
3.  **Auth Service** (`authservice`): Handles user registration, login, and JWT token generation.
4.  **User Service** (`UserService`): Manages user profiles and settings.
5.  **Category Service** (`categoryservice`): Manages expense/income categories (metadata, emojis, types).
6.  **Transaction Service** (`transactionservice`): Core logic for adding, filtering, and summarizing transactions.
7.  **Notification Service** (`NotificationService`): Handles alerts and notifications for transactions and subscriptions.
8.  **Subscription Service** (`subscriptionservice`): Manages recurring subscriptions.
9.  **Vault Service** (`vaultservice`): Secure file storage for receipts/documents.

## 🛠️ Prerequisites

*   Java Development Kit (JDK) 17 or higher
*   MySQL Server (8.0+)
*   Maven (optional, wrapper included)

## 🏃‍♂️ Quick Start

1.  **Database:** Create a MySQL database named `expense_tracker_dev`.
2.  **Build:** Run `mvn clean package` in the root directory (or individually in each service).
3.  **Run:** Start the services in the following order:
    1.  Eureka Server
    2.  API Gateway
    3.  Auth Service
    4.  (All other services can start in parallel)

For detailed setup instructions for frontend developers, please refer to [DOCUMENTATION.md](DOCUMENTATION.md).

## 🧪 Testing

A PowerShell script `demo_backend.ps1` is provided to run a full end-to-end test of the API flows (Registration -> Login -> Create Data -> Verify).

```powershell
.\demo_backend.ps1
```
