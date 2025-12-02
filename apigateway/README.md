# API Gateway

## 📌 Overview
The **API Gateway** is the single entry point for the entire backend. Frontend applications should **only** communicate with this service, not the individual microservices.

## 🚀 Usage
- **Base URL:** `http://localhost:8080`
- **Routing:**
  - `/auth/**` -> Auth Service
  - `/users/**` -> User Service
  - `/transactions/**` -> Transaction Service
  - `/api/categories/**` -> Category Service
  - `/notifications/**` -> Notification Service
  - `/subscriptions/**` -> Subscription Service
  - `/api/v1/bills/**` -> Vault Service

## ⚙️ Configuration
- **Port:** `8080`
