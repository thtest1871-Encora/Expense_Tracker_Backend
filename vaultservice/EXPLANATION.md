# Vault Service - Code Explanation

## Overview
The **Vault Service** is a digital filing cabinet. It allows users to upload files like receipts, invoices, or warranty documents and link them to their expenses.

## Key Features for Frontend Developers

### 1. Upload File
- **Endpoint**: `POST /api/v1/vault/upload`
- **Format**: `multipart/form-data` (Not JSON!)
- **Input Fields**:
  - `file`: The actual file object (Image, PDF, Text).
  - `description`: "Lunch Receipt".
  - `category`: "Food".
  - `date`: "2025-12-01".
- **Constraints**: Max size 5MB. Allowed types: Images, PDF, Text.

### 2. List Files
- **Endpoint**: `GET /api/v1/vault/list`
- **Returns**: A JSON list of all files uploaded by the user.
- **Data**: Includes `filename`, `originalName`, `size`, `type`, `description`.

### 3. Download / Preview
- **Endpoint**: `GET /api/v1/vault/files/{id}`
- **Returns**: The actual binary file stream.
- **Usage**: Use this URL in an `<img>` tag or an `<a>` tag to let the user view the file.

### 4. Delete
- **Endpoint**: `DELETE /api/v1/vault/{id}`
- **What it does**: Permanently removes the file from storage and the database.

## Technical Summary (Simple Terms)
- **Storage**: Files are stored in **AWS S3** (Cloud Storage).
- **Security**: Users can only see and delete their own files.
- **Metadata**: We store details (name, size, type) in a database so we can list them quickly without asking AWS every time.
