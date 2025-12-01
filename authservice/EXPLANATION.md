# Auth Service - Code Explanation

## Overview
The **Auth Service** handles the security basics: creating new accounts and logging users in. It is the only service that generates the **JWT Token** (the "key" used to access everything else).

## Key Features for Frontend Developers

### 1. Registration
- **Endpoint**: `POST /auth/register`
- **Input**: Name, Email, Password.
- **What it does**:
  - Checks if email already exists.
  - Encrypts the password (so it's not stored as plain text).
  - Saves the user to the database.
  - Calls the **User Service** to create an initial empty profile.

### 2. Login
- **Endpoint**: `POST /auth/login`
- **Input**: Email, Password.
- **What it does**:
  - Verifies the email and password.
  - If correct, it generates a **JWT (JSON Web Token)**.
- **Output**: Returns the `token`. You **MUST** save this token (e.g., in LocalStorage) and send it in the header of every subsequent request.

## Data Models

### UserCredential
This is the internal database table that stores:
- `id`: Unique ID (e.g., 1, 2, 3).
- `name`: User's full name.
- `email`: User's email (unique).
- `password`: The encrypted password string.

## Technical Summary (Simple Terms)
- **JWT**: Think of it as a digital wristband for a club. Once you show your ID (Login) and get the wristband (Token), you can go anywhere in the club (Backend) without showing your ID again.
- **Communication**: When a user registers, this service talks to the `UserService` behind the scenes to ensure profile data is synced.
