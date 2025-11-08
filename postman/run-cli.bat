@echo off
echo ========================================
echo Starting Gmail Bulk Sender - CLI Mode
echo ========================================
echo.

REM Check if JAR file exists
if not exist "target\gmail-bulk-sender-1.0.0.jar" (
    echo JAR file not found. Building the project first...
    echo.
    call build.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Build failed. Cannot run the application.
        pause
        exit /b 1
    )
    echo.
)

REM Check if Java is installed
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if config file exists
if not exist "config.properties" (
    echo WARNING: config.properties not found in current directory
    echo.
)

REM Check if recipients file exists
if not exist "recipients.txt" (
    echo WARNING: recipients.txt not found in current directory
    echo.
)

echo Launching CLI application...
echo.

REM Use custom file paths if provided, otherwise use defaults
if "%~1"=="" (
    java -jar target\gmail-bulk-sender-1.0.0.jar
) else (
    if "%~2"=="" (
        java -jar target\gmail-bulk-sender-1.0.0.jar %1
    ) else (
        java -jar target\gmail-bulk-sender-1.0.0.jar %1 %2
    )
)

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application exited with an error.
    pause
)

