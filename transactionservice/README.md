# Transaction Service

## 📌 Overview
The **Transaction Service** is the core of the application. It manages all financial transactions (Income and Expenses), calculates summaries, and handles filtering.

## 🔌 Key Endpoints

### 1. Create Transaction
- **POST** `/transactions`
- **Body:**
  ```json
  {
    "amount": 150.00,
    "description": "Grocery Shopping",
    "categoryId": 190
  }
  ```
- **Note:** Positive amount for INCOME, negative is stored for EXPENSE (but send positive from frontend).

### 2. Filter Transactions
- **GET** `/transactions/filter?type=EXPENSE`
- **GET** `/transactions/filter?type=INCOME`

### 3. Summaries
- **GET** `/transactions/summary/monthly?year=2025`
- **GET** `/transactions/summary/by-category`

## ⚙️ Configuration
- **Port:** `8084`
- **Database:** `expense_tracker_dev` (MySQL)
