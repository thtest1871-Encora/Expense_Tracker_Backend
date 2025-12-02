# Subscription Service

## 📌 Overview
The **Subscription Service** tracks recurring payments (Netflix, Spotify, Rent) and calculates upcoming due dates.

## 🔌 Key Endpoints

### 1. Get Default Plans
- **GET** `/subscriptions/plans/default`
- **Returns:** List of popular plans (Netflix, Prime, etc.) to choose from.

### 2. Add Subscription
- **POST** `/subscriptions/add`
- **Body:**
  ```json
  {
    "platformName": "Netflix",
    "planName": "Premium",
    "amount": 649,
    "billingCycle": "MONTHLY"
  }
  ```

## ⚙️ Configuration
- **Port:** `8086`
- **Database:** `expense_tracker_dev` (MySQL)
