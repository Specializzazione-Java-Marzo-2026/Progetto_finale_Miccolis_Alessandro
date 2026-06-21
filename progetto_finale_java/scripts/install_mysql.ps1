$msi = Join-Path $env:TEMP 'mysql-installer-community.msi'
$uri = 'https://dev.mysql.com/get/Downloads/MySQLInstaller/mysql-installer-community-8.0.33.0.msi'
Write-Host "Downloading $uri to $msi"
Invoke-WebRequest -Uri $uri -OutFile $msi -UseBasicParsing
Write-Host "Downloaded to $msi"
Start-Process -FilePath $msi -Verb runAs -Wait
