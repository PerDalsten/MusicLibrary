#To run script in new shell if policy is restricted: powershell.exe -ExecutionPolicy Unrestricted

$url = "http://localhost:8080/MusicLibrary/rest/albums"

Invoke-WebRequest -uri $url -Outfile albums.js