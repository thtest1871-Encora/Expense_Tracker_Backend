# Expense Tracker Backend - API Demo Script
# --------------------------------------------------
# This script executes a full user flow against the running backend.
# It prints the Request and Response JSONs in a pretty format.
# Use these JSON outputs to design your Frontend types and interfaces.

$BaseUrl = "http://localhost:8080"
$ErrorActionPreference = "Stop"

# --- Helper Functions for Styling ---

function Print-Section ($Title) {
    Write-Host ""
    Write-Host "==================================================================" -ForegroundColor Cyan
    Write-Host "   $Title" -ForegroundColor White -BackgroundColor DarkBlue
    Write-Host "==================================================================" -ForegroundColor Cyan
    Write-Host ""
}

function Print-Request ($Method, $Endpoint, $Body) {
    Write-Host "REQUEST: " -NoNewline -ForegroundColor Yellow
    Write-Host "$Method $BaseUrl$Endpoint" -ForegroundColor White
    if ($Body) {
        Write-Host "PAYLOAD (What Frontend sends):" -ForegroundColor DarkGray
        $Json = $Body | ConvertTo-Json -Depth 5
        Write-Host $Json -ForegroundColor Gray
    }
    Write-Host ""
}

function Print-Response ($Response) {
    Write-Host "RESPONSE (What Frontend receives):" -ForegroundColor Green
    # Convert to JSON and back to ensure clean formatting
    $Json = $Response | ConvertTo-Json -Depth 10
    Write-Host $Json -ForegroundColor White
    Write-Host ""
}

function Print-Error ($Message) {
    Write-Host "ERROR: $Message" -ForegroundColor Red
}

# --- Main Execution ---

Clear-Host
Write-Host "STARTING BACKEND API DEMO..." -ForegroundColor Magenta
Write-Host "   Target: $BaseUrl" -ForegroundColor DarkGray

# 1. Registration
Print-Section "1. AUTH SERVICE: REGISTER NEW USER"
$Rand = Get-Random
$UserEmail = "frontend_dev_$Rand@example.com"
$UserPass = "password123"

$RegBody = @{
    name = "Frontend Developer"
    email = $UserEmail
    password = $UserPass
}

Print-Request "POST" "/auth/register" $RegBody

try {
    $RegResponse = Invoke-RestMethod -Uri "$BaseUrl/auth/register" -Method Post -Body ($RegBody | ConvertTo-Json) -ContentType "application/json"
    Print-Response $RegResponse
    $UserId = $RegResponse.data.userId
} catch {
    Print-Error $_.Exception.Message
    exit
}

# 2. Login
Print-Section "2. AUTH SERVICE: LOGIN"
$LoginBody = @{
    email = $UserEmail
    password = $UserPass
}

Print-Request "POST" "/auth/login" $LoginBody

try {
    $LoginResponse = Invoke-RestMethod -Uri "$BaseUrl/auth/login" -Method Post -Body ($LoginBody | ConvertTo-Json) -ContentType "application/json"
    $Token = $LoginResponse.data.token
    Print-Response $LoginResponse
} catch {
    Print-Error $_.Exception.Message
    exit
}

$Headers = @{
    Authorization = "Bearer $Token"
}

# 3. User Service
Print-Section "3. USER SERVICE: CREATE & GET PROFILE"
$ProfileBody = @{
    userId = $UserId
    fullName = "Frontend Developer"
}

Print-Request "POST" "/users" $ProfileBody

try {
    # Create Profile
    $ProfileResponse = Invoke-RestMethod -Uri "$BaseUrl/users" -Method Post -Headers $Headers -Body ($ProfileBody | ConvertTo-Json) -ContentType "application/json"
    Print-Response $ProfileResponse

    # Get Profile
    Print-Request "GET" "/users/$UserId" $null
    $GetProfileResponse = Invoke-RestMethod -Uri "$BaseUrl/users/$UserId" -Method Get -Headers $Headers
    Print-Response $GetProfileResponse
} catch {
    Print-Error $_.Exception.Message
}

# 4. Create Category
Print-Section "4. CATEGORY SERVICE: CREATE CATEGORIES (EXPENSE & INCOME)"

# 4a. Expense Category
$CatBodyExp = @{
    name = "Office Supplies"
    type = "EXPENSE"
    emoji = "Clip"
}

Print-Request "POST" "/api/categories" $CatBodyExp

try {
    $CatResponseExp = Invoke-RestMethod -Uri "$BaseUrl/api/categories" -Method Post -Headers $Headers -Body ($CatBodyExp | ConvertTo-Json) -ContentType "application/json"
    Print-Response $CatResponseExp
    $CategoryIdExp = $CatResponseExp.id
} catch {
    Print-Error $_.Exception.Message
}

# 4b. Income Category
$CatBodyInc = @{
    name = "Freelance Work"
    type = "INCOME"
    emoji = "Laptop"
}

Print-Request "POST" "/api/categories" $CatBodyInc

try {
    $CatResponseInc = Invoke-RestMethod -Uri "$BaseUrl/api/categories" -Method Post -Headers $Headers -Body ($CatBodyInc | ConvertTo-Json) -ContentType "application/json"
    Print-Response $CatResponseInc
    $CategoryIdInc = $CatResponseInc.id
} catch {
    Print-Error $_.Exception.Message
}

# 5. Create Transaction
Print-Section "5. TRANSACTION SERVICE: CREATE TRANSACTIONS"

# 5a. Expense Transaction
$TxBodyExp = @{
    amount = 249.99
    categoryId = $CategoryIdExp
    description = "New Mechanical Keyboard"
}

Print-Request "POST" "/transactions (EXPENSE)" $TxBodyExp

try {
    $TxResponseExp = Invoke-RestMethod -Uri "$BaseUrl/transactions" -Method Post -Headers $Headers -Body ($TxBodyExp | ConvertTo-Json) -ContentType "application/json"
    Print-Response $TxResponseExp
} catch {
    Print-Error $_.Exception.Message
}

# 5b. Income Transaction
$TxBodyInc = @{
    amount = 1500.00
    categoryId = $CategoryIdInc
    description = "Project Payment"
}

Print-Request "POST" "/transactions (INCOME)" $TxBodyInc

try {
    $TxResponseInc = Invoke-RestMethod -Uri "$BaseUrl/transactions" -Method Post -Headers $Headers -Body ($TxBodyInc | ConvertTo-Json) -ContentType "application/json"
    Print-Response $TxResponseInc
} catch {
    Print-Error $_.Exception.Message
}

# 5c. Transaction Filtering & Summaries
Print-Section "5c. TRANSACTION SERVICE: FILTER & SUMMARIES"

# Filter by Type (INCOME)
# Expected Output: Only INCOME transactions (positive amounts), strictly filtered by INCOME categories.
Print-Request "GET" "/transactions/filter?type=INCOME" $null
try {
    $FilterResponse = Invoke-RestMethod -Uri "$BaseUrl/transactions/filter?type=INCOME" -Method Get -Headers $Headers
    Print-Response $FilterResponse
} catch {
    Print-Error $_.Exception.Message
}

# Monthly Summary
$CurrentYear = (Get-Date).Year
Print-Request "GET" "/transactions/summary/monthly?year=$CurrentYear" $null
try {
    $MonthlyResponse = Invoke-RestMethod -Uri "$BaseUrl/transactions/summary/monthly?year=$CurrentYear" -Method Get -Headers $Headers
    Print-Response $MonthlyResponse
} catch {
    Print-Error $_.Exception.Message
}

# Category Summary
# Expected Output: List of categories with 'categoryName', 'categoryEmoji', 'categoryType'.
# 'totalAmount' should be absolute value for expenses. Only user's categories included.
Print-Request "GET" "/transactions/summary/by-category" $null
try {
    $CatSummaryResponse = Invoke-RestMethod -Uri "$BaseUrl/transactions/summary/by-category" -Method Get -Headers $Headers
    Print-Response $CatSummaryResponse
} catch {
    Print-Error $_.Exception.Message
}

# 6. Notifications
Print-Section "6. NOTIFICATION SERVICE: CHECK ALERTS & MARK READ"
Write-Host "(Checking for alerts triggered by the transactions above...)" -ForegroundColor DarkGray
Start-Sleep -Seconds 2

# Expected Output: Messages formatted as "₹{amount} spent on {category}" or "₹{amount} received from {category}".
# Timestamps should be in UTC ISO format (e.g., "2025-12-01T16:49:49Z").
Print-Request "GET" "/notifications" $null

try {
    $NotifResponse = Invoke-RestMethod -Uri "$BaseUrl/notifications" -Method Get -Headers $Headers
    Print-Response $NotifResponse
    
    # Mark All as Read
    if ($NotifResponse.data.Count -gt 0) {
        Print-Request "PATCH" "/notifications/read-all" $null
        $ReadResponse = Invoke-RestMethod -Uri "$BaseUrl/notifications/read-all" -Method Patch -Headers $Headers
        Print-Response $ReadResponse
    }

} catch {
    Print-Error $_.Exception.Message
}

# 7. Subscription
Print-Section "7. SUBSCRIPTION SERVICE: PLANS & ADD SUBSCRIPTION"

# 7a. Get Default Plans
Print-Request "GET" "/subscriptions/plans/default" $null

try {
    $PlansResponse = Invoke-RestMethod -Uri "$BaseUrl/subscriptions/plans/default" -Method Get -Headers $Headers
    Print-Response $PlansResponse
} catch {
    Print-Error $_.Exception.Message
}

# 7b. Add Subscription
$SubBody = @{
    platformName = "Adobe Creative Cloud"
    planName = "All Apps"
    amount = 54.99
    billingCycle = "MONTHLY"
}

Print-Request "POST" "/subscriptions/add" $SubBody

try {
    $SubResponse = Invoke-RestMethod -Uri "$BaseUrl/subscriptions/add" -Method Post -Headers $Headers -Body ($SubBody | ConvertTo-Json) -ContentType "application/json"
    Print-Response $SubResponse
} catch {
    Print-Error $_.Exception.Message
}

# 8. Vault Service
Print-Section "8. VAULT SERVICE: UPLOAD & LIST FILES (OPTIONAL)"
Write-Host "⚠ Vault Service optional - currently may return 500" -ForegroundColor Yellow

# Create a dummy file
$FilePath = "$PWD\receipt_demo.txt"
"This is a demo receipt content" | Set-Content -Path $FilePath

$FileName = "receipt_demo.txt"
$Boundary = "---------------------------" + [System.Guid]::NewGuid().ToString()
$LF = "`r`n"
$FileContent = [System.IO.File]::ReadAllText($FilePath)

$BodyLines = (
    "--$Boundary",
    "Content-Disposition: form-data; name=`"file`"; filename=`"$FileName`"",
    "Content-Type: text/plain",
    "",
    $FileContent,
    "--$Boundary",
    "Content-Disposition: form-data; name=`"description`"",
    "",
    "Lunch Receipt",
    "--$Boundary",
    "Content-Disposition: form-data; name=`"category`"",
    "",
    "Office Supplies",
    "--$Boundary--"
) -join $LF

Print-Request "POST" "/api/v1/vault/upload" @{ file="receipt_demo.txt"; description="Lunch Receipt"; category="Office Supplies" }

try {
    $UploadResponse = Invoke-RestMethod -Uri "$BaseUrl/api/v1/vault/upload" -Method Post -Headers $Headers -ContentType "multipart/form-data; boundary=$Boundary" -Body $BodyLines
    Print-Response $UploadResponse

    # List Files
    Print-Request "GET" "/api/v1/vault/list" $null
    $ListResponse = Invoke-RestMethod -Uri "$BaseUrl/api/v1/vault/list" -Method Get -Headers $Headers
    Print-Response $ListResponse

} catch {
    Write-Warning "Vault currently unavailable - skipping validation. Error: $($_.Exception.Message)"
}

if (Test-Path $FilePath) { Remove-Item $FilePath }

Write-Host ""
Write-Host "DEMO COMPLETE" -ForegroundColor Green
Write-Host "Done."
