param(
  [string]$Host = 'localhost',
  [int]$Port = 3306,
  [string]$Database = 'Progetto_finale',
  [string]$Username = 'root',
  [string]$Password = 'root',
  [string]$SqlDir = "$PSScriptRoot\..\sql"
)

Write-Host "Running SQL scripts against ${Host}:${Port}/${Database}"

$files = @('drop.sql','create.sql','insert.sql') | ForEach-Object { Join-Path -Path $SqlDir -ChildPath $_ }

foreach ($f in $files) {
    if (Test-Path $f) {
        Write-Host "Executing $f..."
        $cmd = "mysql -h $Host -P $Port -u $Username -p$Password $Database < \"$f\""
        cmd.exe /c $cmd
        if ($LASTEXITCODE -ne 0) { Write-Host "Error executing $f (exit $LASTEXITCODE)"; break }
    } else {
        Write-Host "File not found: $f"
    }
}
