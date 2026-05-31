@REM Maven Wrapper for Windows
@echo off
setlocal enabledelayedexpansion

set MAVEN_HOME=%~dp0\.mvn\apache-maven-3.9.8
set MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd
set MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.8/apache-maven-3.9.8-bin.zip
set MAVEN_ZIP=%~dp0\.mvn\maven.zip

if not exist "%MAVEN_HOME%" (
    echo Downloading Maven 3.9.8...
    if not exist "%~dp0\.mvn" mkdir "%~dp0\.mvn"
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('%MAVEN_URL%', '%MAVEN_ZIP%')" || exit /b 1

    echo Extracting Maven...
    powershell -Command "Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::ExtractToDirectory('%MAVEN_ZIP%', '%~dp0\.mvn')" || exit /b 1
    del "%MAVEN_ZIP%"
    echo Maven ready!
)

"%MAVEN_CMD%" %*

