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

function Print-Trace ($Message) {
    Write-Host "   [INTER-SERVICE TRACE] $Message" -ForegroundColor DarkYellow
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
Print-Section "3. USER SERVICE: VERIFY PROFILE & UPDATE"
# Note: Profile is now auto-created by Auth Service upon registration.
# We will verify this by fetching it immediately.

try {
    # Get Profile (Verify Auto-Creation)
    Print-Request "GET" "/users/$UserId" $null
    $GetProfileResponse = Invoke-RestMethod -Uri "$BaseUrl/users/$UserId" -Method Get -Headers $Headers
    Print-Response $GetProfileResponse

    # Update Profile (Add DOB & Age)
    $UpdateBody = @{
        dob = "1995-08-15"
        age = 29
        phone = "9876543210"
    }
    Print-Request "PUT" "/users" $UpdateBody
    $UpdateResponse = Invoke-RestMethod -Uri "$BaseUrl/users" -Method Put -Headers $Headers -Body ($UpdateBody | ConvertTo-Json) -ContentType "application/json"
    Print-Response $UpdateResponse

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
Print-Trace "Transaction Service receives request..."
Print-Trace "-> Calls Category Service (GET /api/categories/$CategoryIdExp) to validate ID & fetch Emoji..."
Print-Trace "-> Saves Transaction to Database..."
Print-Trace "-> Calls Notification Service (POST /notifications/internal) to trigger alert..."

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
Print-Trace "Transaction Service receives request..."
Print-Trace "-> Calls Category Service (GET /api/categories/$CategoryIdInc) to validate ID..."
Print-Trace "-> Saves Transaction..."
Print-Trace "-> Calls Notification Service to trigger alert..."

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
Print-Trace "Transaction Service needs to filter by 'INCOME'..."
Print-Trace "-> Calls Category Service to get ALL category IDs that are type='INCOME'..."
Print-Trace "-> Queries Database using those Category IDs..."

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
Print-Trace "Transaction Service calculates totals from DB..."
Print-Trace "-> Calls Category Service (GET /api/categories) to fetch names & emojis for the IDs..."
Print-Trace "-> Merges data to return full summary..."

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

# 8. Vault Service (Bills)
Print-Section "8. BILLS SERVICE: UPLOAD, DOWNLOAD & DELETE"

# Image Path provided by user
$ImagePath = "C:\Users\Aditya.Sharma3\Pictures\Screenshots\Screenshot 2025-08-11 223646.png"

if (-not (Test-Path $ImagePath)) {
    Print-Error "Image file not found at: $ImagePath"
    # Fallback or exit? The user specifically asked for this file. I'll exit or skip.
    # Let's just print error and skip the upload part but continue script or exit.
    # The original script used 'exit' on critical failures, but here maybe just skip.
} else {
    # Use .NET HttpClient for reliable multipart binary upload
    Add-Type -AssemblyName System.Net.Http
    $HttpClient = New-Object System.Net.Http.HttpClient
    $HttpClient.DefaultRequestHeaders.Add("Authorization", "Bearer $Token")
    $HttpClient.DefaultRequestHeaders.Add("X-User-Id", "$UserId")

    $MultipartContent = New-Object System.Net.Http.MultipartFormDataContent
    
    # File Content
    $FileStream = [System.IO.File]::OpenRead($ImagePath)
    $StreamContent = New-Object System.Net.Http.StreamContent($FileStream)
    $StreamContent.Headers.ContentType = [System.Net.Http.Headers.MediaTypeHeaderValue]::Parse("image/png")
    $MultipartContent.Add($StreamContent, "file", "Screenshot.png")

    # Other Fields
    $MultipartContent.Add((New-Object System.Net.Http.StringContent("Lunch Receipt Screenshot")), "billDescription")
    $MultipartContent.Add((New-Object System.Net.Http.StringContent("$CategoryIdExp")), "categoryId")

    Print-Request "POST" "/api/v1/bills/upload" @{ file=$ImagePath; billDescription="Lunch Receipt Screenshot"; categoryId=$CategoryIdExp }

    try {
        $PostTask = $HttpClient.PostAsync("$BaseUrl/api/v1/bills/upload", $MultipartContent)
        $PostTask.Wait()
        $Result = $PostTask.Result
        $ResponseContent = $Result.Content.ReadAsStringAsync().Result

        if ($Result.IsSuccessStatusCode) {
            $UploadResponse = $ResponseContent | ConvertFrom-Json
            Print-Response $UploadResponse
            $FileId = $UploadResponse.id
            Write-Host "Image uploaded successfully! ID: $FileId" -ForegroundColor Green

            # List Files
            Print-Request "GET" "/api/v1/bills" $null
            $ListResponse = Invoke-RestMethod -Uri "$BaseUrl/api/v1/bills" -Method Get -Headers $Headers
            Print-Response $ListResponse

            # Download File (Metadata check)
            if ($FileId) {
                Print-Request "GET" "/api/v1/bills/$FileId" $null
                try {
                    # We won't print binary content to console
                    $DownloadResponse = Invoke-RestMethod -Uri "$BaseUrl/api/v1/bills/$FileId" -Method Get -Headers $Headers
                    Write-Host "File downloaded successfully (Binary content received)." -ForegroundColor Cyan
                } catch {
                    $Ex = $_.Exception
                    if ($Ex.Response.StatusCode -eq [System.Net.HttpStatusCode]::Forbidden) {
                            Print-Error "Download failed: 403 Forbidden. (This is likely due to AWS IAM 'Explicit Deny' on GetObject for this user. Upload worked, so keys are valid.)"
                    } else {
                            Print-Error "Download failed: $($Ex.Message)"
                    }
                }

                # 8b. Filter Files
                Print-Section "8b. BILLS SERVICE: FILTERING"
                
                # Filter by Category
                Print-Request "GET" "/api/v1/bills/filter?categoryId=$CategoryIdExp" $null
                try {
                    $FilterCatResponse = Invoke-RestMethod -Uri "$BaseUrl/api/v1/bills/filter?categoryId=$CategoryIdExp" -Method Get -Headers $Headers
                    Print-Response $FilterCatResponse
                } catch {
                    Print-Error $_.Exception.Message
                }

                # Filter by Date Range (Today)
                $Today = (Get-Date).ToString("yyyy-MM-dd")
                Print-Request "GET" "/api/v1/bills/filter?from=$Today&to=$Today" $null
                try {
                    $FilterDateResponse = Invoke-RestMethod -Uri "$BaseUrl/api/v1/bills/filter?from=$Today&to=$Today" -Method Get -Headers $Headers
                    Print-Response $FilterDateResponse
                } catch {
                    Print-Error $_.Exception.Message
                }
                
                # Delete File
                Print-Request "DELETE" "/api/v1/bills/$FileId" $null
                try {
                    Invoke-RestMethod -Uri "$BaseUrl/api/v1/bills/$FileId" -Method Delete -Headers $Headers
                    Write-Host "File deleted successfully." -ForegroundColor Green
                } catch {
                    $Ex = $_.Exception
                    if ($Ex.Response.StatusCode -eq [System.Net.HttpStatusCode]::Forbidden) {
                            Print-Error "Delete failed: 403 Forbidden. (This is likely due to AWS IAM 'Explicit Deny' on DeleteObject for this user.)"
                    } else {
                            Print-Error "Delete failed: $($Ex.Message)"
                    }
                }
            }

        } else {
            Print-Error "Upload Failed: $($Result.StatusCode) - $ResponseContent"
        }
    } catch {
        Print-Error "Bills Service Error: $($_.Exception.Message)"
    } finally {
        $FileStream.Dispose()
        $HttpClient.Dispose()
    }
}

Write-Host ""
Write-Host "DEMO COMPLETE" -ForegroundColor Green
Write-Host "Done."
