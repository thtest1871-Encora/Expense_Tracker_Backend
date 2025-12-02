# Auth Service

## 📌 Overview
The **Auth Service** handles user authentication and authorization. It issues JWT (JSON Web Tokens) that are required to access all other services.

## 🔌 Key Endpoints

### 1. Register
- **POST** `/auth/register`
- **Body:**
  ```json
  {
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securepassword"
  }
  ```

### 2. Login
- **POST** `/auth/login`
- **Body:**
  ```json
  {
    "email": "john@example.com",
    "password": "securepassword"
  }
  ```
- **Returns:** `{ "token": "eyJhbGci..." }`

## ⚙️ Configuration
- **Port:** `8083`
- **Database:** `expense_tracker_dev` (MySQL)
