# Transaction Service - Code Explanation

## Overview
The **Transaction Service** is the core of the Expense Tracker. It records every single penny earned or spent. It also calculates totals and summaries for charts.

## Key Features for Frontend Developers

### 1. Create Transaction
- **Endpoint**: `POST /transactions`
- **Input**:
  - `amount`: Number (positive).
  - `categoryId`: ID of the category (from Category Service).
  - `description`: Text note.
  - `date`: (Optional) Date of transaction. Defaults to now.
- **Important**: The backend determines if it is Income or Expense based on the `categoryId` you provide.

### 2. Filter Transactions
- **Endpoint**: `GET /transactions/filter`
- **Query Params**:
  - `type`: `EXPENSE` or `INCOME`.
  - `startDate` / `endDate`: Date range.
- **Returns**: A list of transactions matching the criteria.

### 3. Summaries (For Charts)
- **Monthly Summary**: `GET /transactions/summary/monthly?year=2025`
  - Returns total income and expense for each month of the year.
- **Category Summary**: `GET /transactions/summary/by-category`
  - Returns total spending per category (e.g., Food: 500, Travel: 200). Great for Pie Charts.

## Data Models

### Transaction
- `id`: Unique ID.
- `amount`: Money value.
- `type`: `EXPENSE` or `INCOME`.
- `date`: When it happened.
- `categoryId`: Link to the category.

## Technical Summary (Simple Terms)
- **Logic**: When you save a transaction, this service:
  1. Checks if the category exists.
  2. Saves the record.
  3. Tells the **Notification Service** to create an alert.
