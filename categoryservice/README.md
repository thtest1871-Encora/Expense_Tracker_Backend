# Category Service

## 📌 Overview
The **Category Service** manages the classification of expenses and income. It provides predefined categories and allows users to create custom ones.

## 🔌 Key Endpoints

### 1. Get All Categories
- **GET** `/api/categories`
- **Returns:** List of categories (both default and user-created).

### 2. Create Category
- **POST** `/api/categories`
- **Body:**
  ```json
  {
    "name": "Freelance",
    "type": "INCOME",
    "emoji": "💻"
  }
  ```

## ⚙️ Configuration
- **Port:** `8087`
- **Database:** `expense_tracker_dev` (MySQL)
