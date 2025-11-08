@echo off
cls
echo ========================================
echo   Gmail Bulk Sender Application
echo ========================================
echo.
echo Please select an option:
echo.
echo   1. Run GUI Mode (Recommended)
echo   2. Run CLI Mode
echo   3. Build Project
echo   4. Exit
echo.
set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" (
    call run-gui.bat
) else if "%choice%"=="2" (
    call run-cli.bat
) else if "%choice%"=="3" (
    call build.bat
) else if "%choice%"=="4" (
    exit /b 0
) else (
    echo Invalid choice. Please run the script again.
    pause
)

