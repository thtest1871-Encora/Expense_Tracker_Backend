# Subscription Service - Code Explanation

## Overview
The **Subscription Service** handles recurring payments. It helps users track bills like Netflix, Spotify, or Rent that happen automatically every month or year.

## Key Features for Frontend Developers

### 1. Default Plans
- **Endpoint**: `GET /subscriptions/plans/default`
- **Purpose**: Provides a list of popular services (Netflix, Prime, etc.) so the user doesn't have to type them in manually.
- **Returns**: A list of plan objects with names and default prices.

### 2. Add Subscription
- **Endpoint**: `POST /subscriptions/add`
- **Input**:
  - `platformName`: e.g., "Netflix"
  - `planName`: e.g., "Premium 4K"
  - `amount`: Cost.
  - `billingCycle`: `MONTHLY` or `YEARLY`.
- **What it does**: Saves this recurring expense to the user's profile.

## Data Models

### Subscription
- `id`: Unique ID.
- `userId`: Owner.
- `platformName`: Service provider.
- `amount`: Cost.
- `billingCycle`: Frequency.
- `nextBillingDate`: Calculated date for the next payment.

## Technical Summary (Simple Terms)
- This service is currently a simple tracker. It does **not** actually process payments or charge credit cards. It is for record-keeping only.
