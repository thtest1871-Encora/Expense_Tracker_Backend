# Notification Service

## 📌 Overview
The **Notification Service** listens for events (like high expenses or subscription renewals) and generates alerts for the user.

## 🔌 Key Endpoints

### 1. Get Notifications
- **GET** `/notifications`
- **Returns:** List of unread and read alerts.

### 2. Mark All Read
- **PATCH** `/notifications/read-all`

## ⚙️ Configuration
- **Port:** `8082`
- **Database:** `expense_tracker_dev` (MySQL)
