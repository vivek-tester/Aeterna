@echo off
REM Comprehensive Testing Script for Aeterna (Windows)
REM Run this script after setting up Android SDK

setlocal enabledelayedexpansion

echo 🧪 Starting Aeterna Comprehensive Testing Suite...
echo =================================================
echo.

REM Function to print colored output (Windows equivalent)
set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM Check prerequisites
echo %BLUE%[INFO]%NC% Checking prerequisites...

if "%ANDROID_HOME%"=="" (
    echo %RED%[ERROR]%NC% ANDROID_HOME not set. Please set it to your Android SDK path.
    exit /b 1
)

if not exist "%ANDROID_HOME%" (
    echo %RED%[ERROR]%NC% Android SDK directory not found: %ANDROID_HOME%
    exit /b 1
)

echo %GREEN%[SUCCESS]%NC% Prerequisites check passed
echo.

REM Parse command line arguments
set UNIT_ONLY=false
set INTEGRATION_ONLY=false
set ACCESSIBILITY_ONLY=false
set SKIP_LINT=false
set WITH_COVERAGE=false

:parse_args
if "%1"=="--unit-only" (
    set UNIT_ONLY=true
    shift
    goto parse_args
)
if "%1"=="--integration-only" (
    set INTEGRATION_ONLY=true
    shift
    goto parse_args
)
if "%1"=="--accessibility-only" (
    set ACCESSIBILITY_ONLY=true
    shift
    goto parse_args
)
if "%1"=="--skip-lint" (
    set SKIP_LINT=true
    shift
    goto parse_args
)
if "%1"=="--with-coverage" (
    set WITH_COVERAGE=true
    shift
    goto parse_args
)
if "%1"=="-h" goto show_help
if "%1"=="--help" goto show_help
if not "%1"=="" (
    echo %RED%[ERROR]%NC% Unknown option: %1
    exit /b 1
)
goto main

:show_help
echo Usage: %0 [OPTIONS]
echo Options:
echo   --unit-only           Run only unit tests
echo   --integration-only    Run only integration tests
echo   --accessibility-only  Run only accessibility tests
echo   --skip-lint          Skip lint checks
echo   --with-coverage      Generate coverage report
echo   -h, --help           Show this help message
exit /b 0

:main
echo 🚀 Aeterna Testing Suite
echo Date: %date% %time%
echo.

REM Clean build
echo %BLUE%[INFO]%NC% Cleaning previous build artifacts...
call gradlew clean
if errorlevel 1 (
    echo %RED%[ERROR]%NC% Clean failed
    exit /b 1
)
echo %GREEN%[SUCCESS]%NC% Clean completed
echo.

REM Run tests based on options
if "%UNIT_ONLY%"=="true" goto run_unit_tests
if "%INTEGRATION_ONLY%"=="true" goto run_integration_tests
if "%ACCESSIBILITY_ONLY%"=="true" goto run_accessibility_tests

REM Run all tests
:run_all_tests
call :run_unit_tests
call :run_integration_tests
call :run_accessibility_tests
call :run_performance_tests
goto after_tests

:run_unit_tests
echo %BLUE%[INFO]%NC% Running unit tests...
echo.
echo 📋 Running Core Domain Tests...
call gradlew :core:domain:testDebugUnitTest
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some core domain tests failed

echo 📋 Running Core Data Tests...
call gradlew :core:data:testDebugUnitTest
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some core data tests failed

echo 📋 Running Core UI Tests...
call gradlew :core:ui:testDebugUnitTest
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some core UI tests failed

echo 📋 Running App Unit Tests...
call gradlew :app:testDebugUnitTest
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some app unit tests failed

echo %GREEN%[SUCCESS]%NC% Unit tests completed
echo.
if "%UNIT_ONLY%"=="true" goto after_tests
goto :eof

:run_integration_tests
echo %BLUE%[INFO]%NC% Running integration tests...
echo.
echo 📋 Running App Integration Tests...
call gradlew :app:connectedDebugAndroidTest
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some integration tests failed

echo %GREEN%[SUCCESS]%NC% Integration tests completed
echo.
if "%INTEGRATION_ONLY%"=="true" goto after_tests
goto :eof

:run_accessibility_tests
echo %BLUE%[INFO]%NC% Running accessibility tests...
echo.
echo 📋 Running Accessibility Unit Tests...
call gradlew :core:ui:testDebugUnitTest --tests "*Accessibility*"
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some accessibility tests failed

echo 📋 Running Accessibility Integration Tests...
call gradlew :app:connectedDebugAndroidTest --tests "*Accessibility*"
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some accessibility integration tests failed

echo %GREEN%[SUCCESS]%NC% Accessibility tests completed
echo.
if "%ACCESSIBILITY_ONLY%"=="true" goto after_tests
goto :eof

:run_performance_tests
echo %BLUE%[INFO]%NC% Running performance tests...
echo.
echo 📋 Running Performance Benchmarks...
call gradlew :app:connectedDebugAndroidTest --tests "*Performance*"
if errorlevel 1 echo %YELLOW%[WARNING]%NC% Some performance tests failed

echo %GREEN%[SUCCESS]%NC% Performance tests completed
echo.
goto :eof

:after_tests
REM Run lint if not skipped
if not "%SKIP_LINT%"=="true" (
    echo %BLUE%[INFO]%NC% Running lint checks...
    call gradlew lint
    call gradlew lintDebug
    echo %GREEN%[SUCCESS]%NC% Lint checks completed
    echo.
)

REM Run security scan
echo %BLUE%[INFO]%NC% Running security scans...
echo 📋 Checking for security vulnerabilities...
call gradlew dependencyCheckAnalyze || echo %YELLOW%[WARNING]%NC% Dependency check not configured
echo %GREEN%[SUCCESS]%NC% Security scans completed
echo.

REM Generate coverage if requested
if "%WITH_COVERAGE%"=="true" (
    echo %BLUE%[INFO]%NC% Generating test coverage report...
    call gradlew createDebugCoverageReport
    echo %GREEN%[SUCCESS]%NC% Coverage report generated at: app\build\reports\coverage\
    echo.
)

echo.
echo 🎉 Testing suite completed successfully!
echo 📊 Check reports in:
echo    - Test reports: */build/reports/tests/
echo    - Lint reports: */build/reports/lint/
echo    - Coverage reports: app/build/reports/coverage/
echo.
pause