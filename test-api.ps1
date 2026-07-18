# API Test Script - Fixed version using accessToken field
$ErrorActionPreference = "Continue"

Write-Host "=== STEP 1: Register user ===" -ForegroundColor Cyan
$regBody = '{"username":"testpravin99","displayName":"Pravin Test","email":"pravin.test99@example.com","password":"password123"}'
try {
  $reg = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/register' -Method POST -Body $regBody -ContentType 'application/json'
  Write-Host "Register OK:" $reg.message -ForegroundColor Green
} catch {
  Write-Host "Register (user may already exist, continuing...)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== STEP 2: Login ===" -ForegroundColor Cyan
$loginBody = '{"email":"pravin.test99@example.com","password":"password123"}'
$login = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method POST -Body $loginBody -ContentType 'application/json'
$token = $login.data.accessToken
if ($token) {
  Write-Host "Login OK - Token starts:" $token.Substring(0, [Math]::Min(30, $token.Length))"..." -ForegroundColor Green
} else {
  Write-Host "ERROR: No token received!" -ForegroundColor Red
  exit 1
}

$headers = @{Authorization="Bearer $token"}

Write-Host ""
Write-Host "=== STEP 3: GET /api/users/me ===" -ForegroundColor Cyan
try {
  $me = Invoke-RestMethod -Uri 'http://localhost:8080/api/users/me' -Headers $headers
  Write-Host "OK - User: @" $me.data.username "| Display:" $me.data.displayName "| ID:" $me.data.id -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 4: GET /api/notifications ===" -ForegroundColor Cyan
try {
  $notifs = Invoke-RestMethod -Uri 'http://localhost:8080/api/notifications' -Headers $headers
  Write-Host "OK - Notifications count:" $notifs.data.Count -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 5: GET /api/notifications/unread-count ===" -ForegroundColor Cyan
try {
  $unread = Invoke-RestMethod -Uri 'http://localhost:8080/api/notifications/unread-count' -Headers $headers
  Write-Host "OK - Unread count:" $unread.data.unreadCount -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 6: GET /api/friends ===" -ForegroundColor Cyan
try {
  $friends = Invoke-RestMethod -Uri 'http://localhost:8080/api/friends' -Headers $headers
  Write-Host "OK - Friends count:" $friends.data.Count -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 7: GET /api/groups ===" -ForegroundColor Cyan
try {
  $groups = Invoke-RestMethod -Uri 'http://localhost:8080/api/groups' -Headers $headers
  Write-Host "OK - Groups count:" $groups.data.Count -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 8: GET /api/feed ===" -ForegroundColor Cyan
try {
  $feed = Invoke-RestMethod -Uri 'http://localhost:8080/api/feed' -Headers $headers
  Write-Host "OK - Feed posts count:" $feed.data.content.Count "| Total:" $feed.data.totalElements -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 9: GET /api/users/search?q=test ===" -ForegroundColor Cyan
try {
  $search = Invoke-RestMethod -Uri 'http://localhost:8080/api/users/search?q=test' -Headers $headers
  Write-Host "OK - Search results:" $search.data.totalElements "total" -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 10: GET /api/posts/me ===" -ForegroundColor Cyan
try {
  $myPosts = Invoke-RestMethod -Uri 'http://localhost:8080/api/posts/me' -Headers $headers
  Write-Host "OK - My posts count:" $myPosts.data.Count -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 11: POST /api/posts (create test post) ===" -ForegroundColor Cyan
try {
  $postBody = '{"caption":"Test post from API test script!","visibilityType":"PUBLIC","visibleToGroupIds":[]}'
  $post = Invoke-RestMethod -Uri 'http://localhost:8080/api/posts' -Method POST -Body $postBody -ContentType 'application/json' -Headers $headers
  Write-Host "OK - Created post ID:" $post.data.id "| Caption:" $post.data.caption -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "=== STEP 12: GET /api/feed (verify post appears) ===" -ForegroundColor Cyan
try {
  $feed2 = Invoke-RestMethod -Uri 'http://localhost:8080/api/feed' -Headers $headers
  Write-Host "OK - Feed now has:" $feed2.data.content.Count "post(s)" -ForegroundColor Green
} catch { Write-Host "FAILED: $_" -ForegroundColor Red }

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   ALL BACKEND API TESTS COMPLETE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
