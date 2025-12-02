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
- **Rules:**
  - **INCOME Category:** Amount must be positive.
  - **EXPENSE Category:** Amount is stored as negative. If you send a positive amount, it is automatically converted.
- **Response:** Includes enriched category metadata (`categoryName`, `categoryEmoji`, `categoryType`).

### 2. Filter Transactions
- **GET** `/transactions/filter?type=EXPENSE`
- **GET** `/transactions/filter?type=INCOME`
- **Sorting:** Results are always sorted by **Date (Newest First)**.

### 3. Summaries
- **GET** `/transactions/summary/monthly?year=2025`
- **GET** `/transactions/summary/by-category`

## ⚙️ Configuration
- **Port:** `8084`
- **Database:** `expense_tracker_dev` (MySQL)
