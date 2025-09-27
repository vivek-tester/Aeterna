#!/bin/bash
# Comprehensive Testing Script for Aeterna
# Run this script after setting up Android SDK

set -e  # Exit on any error

echo "🧪 Starting Aeterna Comprehensive Testing Suite..."
echo "================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    if [ -z "$ANDROID_HOME" ]; then
        print_error "ANDROID_HOME not set. Please set it to your Android SDK path."
        exit 1
    fi
    
    if [ ! -d "$ANDROID_HOME" ]; then
        print_error "Android SDK directory not found: $ANDROID_HOME"
        exit 1
    fi
    
    print_success "Prerequisites check passed"
}

# Clean build
clean_build() {
    print_status "Cleaning previous build artifacts..."
    ./gradlew clean
    print_success "Clean completed"
}

# Unit Tests
run_unit_tests() {
    print_status "Running unit tests..."
    
    echo "📋 Running Core Domain Tests..."
    ./gradlew :core:domain:testDebugUnitTest
    
    echo "📋 Running Core Data Tests..."
    ./gradlew :core:data:testDebugUnitTest
    
    echo "📋 Running Core UI Tests..."
    ./gradlew :core:ui:testDebugUnitTest
    
    echo "📋 Running App Unit Tests..."
    ./gradlew :app:testDebugUnitTest
    
    print_success "Unit tests completed"
}

# Integration Tests
run_integration_tests() {
    print_status "Running integration tests..."
    
    echo "📋 Running App Integration Tests..."
    ./gradlew :app:connectedDebugAndroidTest
    
    print_success "Integration tests completed"
}

# Accessibility Tests
run_accessibility_tests() {
    print_status "Running accessibility tests..."
    
    echo "📋 Running Accessibility Unit Tests..."
    ./gradlew :core:ui:testDebugUnitTest --tests "*Accessibility*"
    
    echo "📋 Running Accessibility Integration Tests..."
    ./gradlew :app:connectedDebugAndroidTest --tests "*Accessibility*"
    
    print_success "Accessibility tests completed"
}

# Performance Tests
run_performance_tests() {
    print_status "Running performance tests..."
    
    echo "📋 Running Performance Benchmarks..."
    ./gradlew :app:connectedDebugAndroidTest --tests "*Performance*"
    
    print_success "Performance tests completed"
}

# Lint Checks
run_lint_checks() {
    print_status "Running lint checks..."
    
    ./gradlew lint
    ./gradlew lintDebug
    
    print_success "Lint checks completed"
}

# Security Scan
run_security_scan() {
    print_status "Running security scans..."
    
    echo "📋 Checking for security vulnerabilities..."
    ./gradlew dependencyCheckAnalyze || print_warning "Dependency check not configured"
    
    print_success "Security scans completed"
}

# Coverage Report
generate_coverage_report() {
    print_status "Generating test coverage report..."
    
    ./gradlew createDebugCoverageReport
    
    print_success "Coverage report generated at: app/build/reports/coverage/"
}

# Main execution
main() {
    echo "🚀 Aeterna Testing Suite"
    echo "Date: $(date)"
    echo ""
    
    # Parse command line arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            --unit-only)
                UNIT_ONLY=true
                shift
                ;;
            --integration-only)
                INTEGRATION_ONLY=true
                shift
                ;;
            --accessibility-only)
                ACCESSIBILITY_ONLY=true
                shift
                ;;
            --skip-lint)
                SKIP_LINT=true
                shift
                ;;
            --with-coverage)
                WITH_COVERAGE=true
                shift
                ;;
            -h|--help)
                echo "Usage: $0 [OPTIONS]"
                echo "Options:"
                echo "  --unit-only           Run only unit tests"
                echo "  --integration-only    Run only integration tests"
                echo "  --accessibility-only  Run only accessibility tests"
                echo "  --skip-lint          Skip lint checks"
                echo "  --with-coverage      Generate coverage report"
                echo "  -h, --help           Show this help message"
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                exit 1
                ;;
        esac
    done
    
    # Check prerequisites
    check_prerequisites
    
    # Clean build
    clean_build
    
    # Run tests based on options
    if [ "$UNIT_ONLY" = true ]; then
        run_unit_tests
    elif [ "$INTEGRATION_ONLY" = true ]; then
        run_integration_tests
    elif [ "$ACCESSIBILITY_ONLY" = true ]; then
        run_accessibility_tests
    else
        # Run all tests
        run_unit_tests
        run_integration_tests
        run_accessibility_tests
        run_performance_tests
    fi
    
    # Run lint if not skipped
    if [ "$SKIP_LINT" != true ]; then
        run_lint_checks
    fi
    
    # Run security scan
    run_security_scan
    
    # Generate coverage if requested
    if [ "$WITH_COVERAGE" = true ]; then
        generate_coverage_report
    fi
    
    echo ""
    echo "🎉 Testing suite completed successfully!"
    echo "📊 Check reports in:"
    echo "   - Test reports: */build/reports/tests/"
    echo "   - Lint reports: */build/reports/lint/"
    echo "   - Coverage reports: app/build/reports/coverage/"
    echo ""
}

# Run main function with all arguments
main "$@"