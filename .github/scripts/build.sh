#!/bin/bash

# GitHub Actions Build Script for Aeterna Android App
# This script bypasses YAML command parsing issues

set -e  # Exit on any error

echo "🚀 Starting Aeterna Android build..."
echo "Current directory: $(pwd)"
echo "Java version:"
java -version

echo "📦 Setting up Gradle wrapper permissions..."
chmod +x gradlew

echo "🔍 Listing all available Gradle tasks..."
./gradlew tasks --all

echo "🔧 Building debug APK..."
./gradlew assembleDebug --stacktrace --no-daemon

echo "✅ Build completed successfully!"
echo "APK location: app/build/outputs/apk/debug/app-debug.apk"

# Verify APK was created
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "✅ APK file confirmed - build successful!"
    ls -la app/build/outputs/apk/debug/app-debug.apk
else
    echo "❌ APK file not found - build may have failed"
    exit 1
fi