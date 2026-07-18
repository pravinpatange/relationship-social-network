$ErrorActionPreference = "Continue"
Write-Host "Creating dummy users..." -ForegroundColor Cyan

$users = @(
  @{ username="alice_smith"; displayName="Alice Smith"; email="alice@example.com"; password="password123"; bio="Hello world! I love photography." },
  @{ username="bob_jones"; displayName="Bob Jones"; email="bob@example.com"; password="password123"; bio="Tech enthusiast and coder." },
  @{ username="charlie_brown"; displayName="Charlie Brown"; email="charlie@example.com"; password="password123"; bio="Coffee addict. Always coding." },
  @{ username="diana_prince"; displayName="Diana Prince"; email="diana@example.com"; password="password123"; bio="Traveler, dreamer, doer." },
  @{ username="evan_wright"; displayName="Evan Wright"; email="evan@example.com"; password="password123"; bio="Just here to see what's up." }
)

foreach ($u in $users) {
  $body = $u | ConvertTo-Json
  try {
    $res = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/register' -Method POST -Body $body -ContentType 'application/json'
    Write-Host "Created user: $($u.username)" -ForegroundColor Green
  } catch {
    Write-Host "Failed to create $($u.username) (might already exist)" -ForegroundColor Yellow
  }
}

Write-Host "Done! You can now search for these users." -ForegroundColor Cyan
