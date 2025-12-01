# Category Service - Code Explanation

## Overview
The **Category Service** manages the labels you attach to transactions, like "Food", "Rent", "Salary", or "Freelance". It helps organize finances into **Income** and **Expense** buckets.

## Key Features for Frontend Developers

### 1. Types of Categories
Every category belongs to one of two types:
- **EXPENSE**: Money going out (e.g., Food, Travel).
- **INCOME**: Money coming in (e.g., Salary, Bonus).

### 2. Managing Categories
- **Create**: `POST /api/categories`
  - You send: `name` ("Groceries"), `type` ("EXPENSE"), and an `emoji` ("🍎").
  - The `emoji` is just a string, so you can pass any icon or text.
- **List**: `GET /api/categories/{userId}`
  - Returns all categories created by that user.
  - **Note**: Currently, this service might return *all* categories if not strictly filtered, but the design intends for user-specific lists.

## Data Models

### Category
- `id`: Unique ID.
- `name`: Display name.
- `type`: `EXPENSE` or `INCOME`.
- `userId`: The owner of this category.
- `emoji`: Visual icon for the UI.

## Technical Summary (Simple Terms)
- **Microservice**: This is a standalone mini-app just for labels.
- **Why separate?**: It allows the "Transaction Service" to just refer to a `categoryId` without worrying about the details of the category name or icon.
