# Anime Watchlist - Full Startup Script
Write-Host "========================================" -ForegroundColor Green
Write-Host "Anime Watchlist Platform - Startup" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

$baseDir = "C:\Users\Tuncer\Documents\JAVA-Project\AnimeWatchList"

# Step 1: Start Docker Compose
Write-Host "`n[1/3] Starting Docker Compose..." -ForegroundColor Cyan
Set-Location $baseDir
docker-compose up -d
Start-Sleep -Seconds 5

# Step 2: Start Backend in new window
Write-Host "`n[2/3] Starting Backend (Java/Spring Boot)..." -ForegroundColor Cyan
$backendScript = @"
cd "$baseDir\backend"
Write-Host "Backend starting on port 8080..." -ForegroundColor Green
.\mvnw.cmd spring-boot:run
"@
$backendScript | Out-File "$env:TEMP\start-backend.ps1" -Encoding UTF8
Start-Process powershell -ArgumentList "-NoExit -File `"$env:TEMP\start-backend.ps1`""

Start-Sleep -Seconds 10

# Step 3: Start Frontend in new window
Write-Host "`n[3/3] Starting Frontend (React/Vite)..." -ForegroundColor Cyan
$frontendScript = @"
cd "$baseDir\frontend"
if (!(Test-Path node_modules)) {
    Write-Host "Installing npm packages..." -ForegroundColor Yellow
    npm install
}
Write-Host "Frontend starting on port 5174..." -ForegroundColor Green
npm run dev
"@
$frontendScript | Out-File "$env:TEMP\start-frontend.ps1" -Encoding UTF8
Start-Process powershell -ArgumentList "-NoExit -File `"$env:TEMP\start-frontend.ps1`""

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "✅ All services started!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "`n📍 Access the application:" -ForegroundColor Yellow
Write-Host "   Frontend: http://localhost:5174" -ForegroundColor Cyan
Write-Host "   Backend:  http://localhost:8080" -ForegroundColor Cyan
Write-Host "`n🔐 Test Credentials:" -ForegroundColor Yellow
Write-Host "   Email:    admin@example.com" -ForegroundColor Cyan
Write-Host "   Password: admin123" -ForegroundColor Cyan
Write-Host "`n💾 Logs are in separate windows - keep them open" -ForegroundColor Yellow
