# User Service - Code Explanation

## Overview
The **User Service** manages the user's profile information. While the **Auth Service** handles the login credentials (email/password), this service handles the "Profile" (Full Name, Bio, Avatar, etc.).

## Key Features for Frontend Developers

### 1. Create Profile
- **Endpoint**: `POST /users`
- **Trigger**: Usually called automatically by the Auth Service when a user registers.
- **Input**: `userId`, `fullName`, `email`.

### 2. Get Profile
- **Endpoint**: `GET /users/{id}`
- **Returns**: The full profile object.
- **Usage**: Call this after login to display "Welcome, [Name]" on the dashboard.

## Data Models

### UserProfile
- `userId`: The ID from the Auth Service.
- `fullName`: Display name.
- `email`: Contact email.
- `createdAt`: When they joined.

## Technical Summary (Simple Terms)
- **Separation of Concerns**: We keep "Credentials" (Auth) separate from "Profile" (User) for security and architectural reasons.
- **Sync**: The Auth Service sends a message to this service whenever a new user signs up.
