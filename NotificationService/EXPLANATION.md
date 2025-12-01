# Notification Service - Code Explanation

## Overview
The **Notification Service** is responsible for alerting the user about important events, such as high spending or receiving income.

## Key Features for Frontend Developers

### 1. Fetching Alerts
- **Endpoint**: `GET /notifications`
- **What it returns**: A list of messages for the logged-in user.
- **Example**: "You spent ₹500 on Food."

### 2. Marking as Read
- **Endpoint**: `PATCH /notifications/read-all`
- **What it does**: Marks all current notifications as "read" so they don't show up as "new" in the UI (e.g., clearing the red dot badge).

## How Notifications are Created
- This service listens to the **Transaction Service**.
- When a user adds a transaction, the Transaction Service talks to this service (via `Feign Client` - a way for Java apps to talk to each other) to trigger a notification.
- **Logic**:
  - If `EXPENSE`: "₹{amount} spent on {category}"
  - If `INCOME`: "₹{amount} received from {category}"

## Data Models

### Notification
- `id`: Unique ID.
- `userId`: Who the message is for.
- `message`: The text content.
- `isRead`: `true` or `false`.
- `createdAt`: Timestamp.

## Technical Summary (Simple Terms)
- **Polling**: Currently, the Frontend must "ask" (GET request) for notifications. It is not a real-time WebSocket push (yet). You might want to call this endpoint every minute or on page load.
