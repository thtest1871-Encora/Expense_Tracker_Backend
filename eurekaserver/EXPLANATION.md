# Eureka Server - Code Explanation

## Overview
The **Eureka Server** is the "Phonebook" of the backend. Frontend developers **do not** interact with this service directly.

## How it Works (Simple Terms)
1.  **Registration**: When you start the `AuthService` or `TransactionService`, they automatically "call" Eureka and say: "I am the Transaction Service, and I am living at IP address 192.168.x.x".
2.  **Discovery**: When the **API Gateway** receives a request for `/transactions`, it asks Eureka: "Where is the Transaction Service right now?". Eureka gives the address, and the Gateway forwards the request.

## Why is this here?
- It allows us to run multiple copies of a service (scaling) without changing the Frontend code.
- It handles dynamic IP addresses if services restart.

## Frontend Interaction
- **None**. You just talk to the Gateway (`localhost:8080`). You can ignore this folder entirely.
