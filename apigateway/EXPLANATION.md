# API Gateway Service - Code Explanation

## Overview
The **API Gateway** is the "front door" of the entire backend system. Instead of the Frontend talking to 8 different services (Auth, User, Transaction, etc.) separately, it only talks to this one Gateway. The Gateway then forwards the request to the correct service.

## Key Responsibilities for Frontend Developers

### 1. Single Entry Point
- **Base URL**: `http://localhost:8080`
- You send **ALL** requests here.
- Example:
  - To login: `POST http://localhost:8080/auth/login` (Gateway sends this to `authservice`)
  - To get transactions: `GET http://localhost:8080/transactions` (Gateway sends this to `transactionservice`)

### 2. Authentication (Security)
- The Gateway checks for the **JWT Token** in the `Authorization` header.
- **Header Format**: `Authorization: Bearer <your-token-here>`
- If the token is missing or invalid, the Gateway returns `401 Unauthorized` immediately. The request never reaches the inner services.
- **Public Endpoints** (No token needed):
  - `/auth/register`
  - `/auth/login`
  - `/eureka/**`

### 3. User Identification (`X-User-Id`)
- When the Gateway validates your token, it extracts the `userId` hidden inside it.
- It adds a special header `X-User-Id` to the request before forwarding it.
- **Why this matters**: You don't need to send `userId` in the body for most requests (like "get my transactions"). The backend knows who you are because of this header.

## Routing Table
Here is how the Gateway maps your URLs to the microservices:

| URL Path | Destination Service | Description |
| :--- | :--- | :--- |
| `/auth/**` | **Auth Service** | Login, Register |
| `/users/**` | **User Service** | User Profiles |
| `/transactions/**` | **Transaction Service** | Expenses, Incomes, Summaries |
| `/notifications/**` | **Notification Service** | Alerts, Messages |
| `/subscriptions/**` | **Subscription Service** | Recurring payments |
| `/api/categories/**` | **Category Service** | Expense Categories |
| `/api/v1/vault/**` | **Vault Service** | File Uploads (Receipts) |

## Technical Summary (Simple Terms)
- **Technology**: Spring Cloud Gateway.
- **Filter (`JwtFilter.java`)**: A piece of code that intercepts every request to check for the "key" (Token) before opening the door.
