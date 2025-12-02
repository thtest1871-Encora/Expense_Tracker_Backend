# Vault Service

## 📌 Overview
The **Vault Service** acts as a secure digital filing cabinet for the Expense Tracker. It allows users to upload, store, and manage files (receipts, invoices, warranty cards) linked to their expenses. Files are stored securely in AWS S3.

## 🔌 Key Endpoints

### 1. Upload File
- **POST** `/api/v1/bills/upload`
- **Content-Type:** `multipart/form-data`
- **Parameters:**
  - `file`: The file to upload (Image/PDF).
  - `billDescription`: Short description (e.g., "Lunch Receipt").
  - `categoryId`: ID of the related category.
  - `date`: (Optional) Date of the bill (YYYY-MM-DD).

### 2. List Files
- **GET** `/api/v1/bills`
- **Returns:** List of all uploaded files for the logged-in user.

### 3. Download File
- **GET** `/api/v1/bills/{id}`
- **Returns:** The binary file stream (image/pdf).
- **Note:** If you get a `403 Forbidden`, it means the backend has write-only access to S3.

### 4. Delete File
- **DELETE** `/api/v1/bills/{id}`
- **Note:** Permanently removes file from S3 and database.

## ⚙️ Configuration
- **Port:** `8085`
- **Database:** `expense_tracker_dev` (MySQL)
- **Storage:** AWS S3 (`expense-tracker-bills-subhadeep`)
