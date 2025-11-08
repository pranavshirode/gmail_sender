@echo off
echo ========================================
echo Starting Gmail Bulk Sender - GUI Mode (Maven)
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your system PATH
    pause
    exit /b 1
)

echo Launching GUI application using Maven...
echo.

mvn exec:java -Dexec.mainClass="com.bulksender.BulkSenderGUI"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application exited with an error.
    pause
)

