#!/bin/bash

# Aeterna Development Environment Setup Script
# This script sets up the development environment for building and testing

echo "🚀 Setting up Aeterna Development Environment..."

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ]; then
    echo "⚠️  ANDROID_HOME not set. Please set it to your Android SDK path."
    echo "   Example: export ANDROID_HOME=/Users/\$USER/Library/Android/sdk"
    echo "   Or on Windows: set ANDROID_HOME=C:\\Users\\%USERNAME%\\AppData\\Local\\Android\\Sdk"
fi

# Make gradlew executable (Linux/Mac)
if [ -f "./gradlew" ]; then
    chmod +x ./gradlew
    echo "✅ Made gradlew executable"
fi

echo ""
echo "📋 Development Environment Requirements:"
echo "   • Android SDK API 36 (Android 14)"
echo "   • Kotlin 1.9.0+"
echo "   • Gradle 8.5+"
echo "   • JVM 17+"
echo ""

echo "🔧 Available Gradle Tasks:"
echo "   ./gradlew build                 - Build the entire project"
echo "   ./gradlew assembleDebug         - Build debug APK"
echo "   ./gradlew assembleRelease       - Build release APK"
echo "   ./gradlew test                  - Run unit tests"
echo "   ./gradlew connectedAndroidTest  - Run instrumented tests"
echo "   ./gradlew lint                  - Run lint checks"
echo "   ./gradlew clean                 - Clean build artifacts"
echo ""

echo "🧪 Testing Commands:"
echo "   ./gradlew testDebugUnitTest                    - Unit tests"
echo "   ./gradlew connectedDebugAndroidTest           - UI tests"
echo "   ./gradlew createDebugCoverageReport           - Test coverage"
echo ""

echo "📱 Build Commands:"
echo "   ./gradlew assembleDebug                       - Debug build"
echo "   ./gradlew bundleRelease                       - Release bundle (Play Store)"
echo "   ./gradlew installDebug                        - Install debug on device"
echo ""

echo "✨ Setup complete! Ready for development."