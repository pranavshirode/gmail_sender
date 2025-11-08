@echo off
echo ========================================
echo Building Gmail Bulk Sender Application
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your system PATH
    echo Download from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo Compiling and packaging the application...
echo.

mvn clean package

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build completed successfully!
    echo JAR file created in: target\gmail-bulk-sender-1.0.0.jar
    echo ========================================
) else (
    echo.
    echo ========================================
    echo Build failed! Please check the errors above.
    echo ========================================
    pause
    exit /b 1
)

pause

