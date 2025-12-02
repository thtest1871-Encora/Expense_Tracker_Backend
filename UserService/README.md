# User Service

## 📌 Overview
The **User Service** manages user profiles and personal information.

## 🔌 Key Endpoints

### 1. Create Profile
- **POST** `/users`
- **Body:** `{ "userId": 1, "fullName": "John Doe" }`

### 2. Get Profile
- **GET** `/users/{id}`

## ⚙️ Configuration
- **Port:** `8081`
- **Database:** `expense_tracker_dev` (MySQL)
