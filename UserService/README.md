# User Service

## 📌 Overview
The **User Service** manages user profiles and personal information.

## 🔌 Key Endpoints

### 1. Create Profile (Internal)
- **POST** `/users/internal/base-profile`
- **Description:** Called by Auth Service upon registration.
- **Body:** `{ "userId": 1, "fullName": "John Doe", "email": "john@example.com" }`

### 2. Get Profile
- **GET** `/users/{id}`

### 3. Update Profile
- **PUT** `/users`
- **Description:** Update personal details including DOB and Age.
- **Body:**
  ```json
  {
    "fullName": "John Doe",
    "phone": "1234567890",
    "dob": "1995-08-15",
    "age": 29
  }
  ```

## ⚙️ Configuration
- **Port:** `8081`
- **Database:** `expense_tracker_dev` (MySQL)
