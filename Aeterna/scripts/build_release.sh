#!/bin/bash
# Release Build Script for Aeterna
# Creates production-ready builds with signing and optimization

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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

# Configuration
BUILD_DIR="build/outputs"
RELEASE_DIR="releases/$(date +%Y-%m-%d)"
VERSION_NAME=$(./gradlew -q printVersionName || echo "1.0.0")
VERSION_CODE=$(./gradlew -q printVersionCode || echo "1")

echo "🚀 Aeterna Release Build"
echo "======================="
echo "Version: $VERSION_NAME ($VERSION_CODE)"
echo "Date: $(date)"
echo ""

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    if [ -z "$ANDROID_HOME" ]; then
        print_error "ANDROID_HOME not set. Please set it to your Android SDK path."
        exit 1
    fi
    
    # Check for keystore (required for release builds)
    if [ ! -f "keystore/release.keystore" ] && [ -z "$AETERNA_KEYSTORE_PATH" ]; then
        print_warning "Release keystore not found. Creating debug keystore for testing."
        mkdir -p keystore
        keytool -genkey -v -keystore keystore/release.keystore -alias aeterna -keyalg RSA -keysize 2048 -validity 10000 \
            -dname "CN=Aeterna, OU=Development, O=Aeterna, L=City, S=State, C=US" \
            -storepass android -keypass android
    fi
    
    print_success "Prerequisites check passed"
}

# Clean and prepare
clean_and_prepare() {
    print_status "Cleaning and preparing build environment..."
    
    ./gradlew clean
    
    # Create release directory
    mkdir -p "$RELEASE_DIR"
    
    print_success "Build environment prepared"
}

# Run tests before release
run_tests() {
    print_status "Running tests before release build..."
    
    # Unit tests
    ./gradlew testReleaseUnitTest
    
    # Lint checks
    ./gradlew lintRelease
    
    print_success "All tests passed"
}

# Build release APK
build_release_apk() {
    print_status "Building release APK..."
    
    ./gradlew assembleRelease
    
    # Copy APK to release directory
    cp app/build/outputs/apk/release/app-release.apk "$RELEASE_DIR/aeterna-v$VERSION_NAME-$VERSION_CODE.apk"
    
    print_success "Release APK built: $RELEASE_DIR/aeterna-v$VERSION_NAME-$VERSION_CODE.apk"
}

# Build release AAB (for Play Store)
build_release_bundle() {
    print_status "Building release bundle (AAB)..."
    
    ./gradlew bundleRelease
    
    # Copy AAB to release directory
    cp app/build/outputs/bundle/release/app-release.aab "$RELEASE_DIR/aeterna-v$VERSION_NAME-$VERSION_CODE.aab"
    
    print_success "Release bundle built: $RELEASE_DIR/aeterna-v$VERSION_NAME-$VERSION_CODE.aab"
}

# Generate checksums
generate_checksums() {
    print_status "Generating checksums..."
    
    cd "$RELEASE_DIR"
    
    for file in *.apk *.aab; do
        if [ -f "$file" ]; then
            sha256sum "$file" > "$file.sha256"
            md5sum "$file" > "$file.md5"
        fi
    done
    
    cd - > /dev/null
    
    print_success "Checksums generated"
}

# Create release notes
create_release_notes() {
    print_status "Creating release notes..."
    
    cat > "$RELEASE_DIR/RELEASE_NOTES.md" << EOF
# Aeterna v$VERSION_NAME (Build $VERSION_CODE)

Released: $(date +"%B %d, %Y")

## Features

- **Complete YouTube Music Client**: Full-featured music streaming experience
- **Material You Design**: Adaptive theming with dynamic colors
- **Offline Downloads**: Download songs for offline listening
- **Advanced Audio Processing**: Custom tempo and pitch controls
- **Live Lyrics**: Synchronized lyrics display
- **Accessibility Support**: Complete accessibility features including TalkBack support
- **Responsive Design**: Optimized for phones, tablets, and foldables

## Accessibility Features

- High contrast mode for better visibility
- Screen reader optimization with TalkBack
- Large text support
- Reduced motion options
- Voice control compatibility
- Minimum 48dp touch targets

## Technical Details

- **Target SDK**: Android 14 (API 34)
- **Minimum SDK**: Android 7.0 (API 24)
- **Architecture**: Clean Architecture with MVVM
- **UI Framework**: Jetpack Compose
- **Audio Engine**: ExoPlayer with custom processors

## Installation

### APK Installation
1. Enable "Install from unknown sources" in Android settings
2. Download the APK file
3. Open the APK file to install
4. Grant necessary permissions when prompted

### Play Store Installation (AAB)
The AAB file is for Play Store distribution only.

## Permissions Required

- **INTERNET**: Stream music from YouTube Music
- **ACCESS_NETWORK_STATE**: Check network connectivity
- **WRITE_EXTERNAL_STORAGE**: Download music for offline use
- **READ_EXTERNAL_STORAGE**: Access downloaded music files
- **WAKE_LOCK**: Keep device awake during playback
- **FOREGROUND_SERVICE**: Background music playback

## Known Issues

- Initial setup requires YouTube Music account authentication
- Some premium features require YouTube Music Premium subscription
- Offline downloads subject to YouTube Music terms of service

## Support

For issues, feature requests, or contributions:
- GitHub: https://github.com/aeterna/aeterna
- Email: support@aeterna.app
- Documentation: https://docs.aeterna.app

---
*Built with ❤️ for the open source community*
EOF

    print_success "Release notes created: $RELEASE_DIR/RELEASE_NOTES.md"
}

# Create installation guide
create_installation_guide() {
    print_status "Creating installation guide..."
    
    cat > "$RELEASE_DIR/INSTALLATION.md" << EOF
# Aeterna Installation Guide

## System Requirements

- Android 7.0 (API 24) or higher
- 100MB free storage space
- Internet connection for streaming
- YouTube Music account

## Installation Methods

### Method 1: APK Installation (Recommended for testing)

1. **Download APK**
   - Download \`aeterna-v$VERSION_NAME-$VERSION_CODE.apk\`
   - Verify checksum (optional but recommended)

2. **Enable Unknown Sources**
   - Go to Settings > Security
   - Enable "Install from unknown sources"
   - Or enable for your browser/file manager specifically

3. **Install APK**
   - Open the downloaded APK file
   - Tap "Install" when prompted
   - Wait for installation to complete

4. **Grant Permissions**
   - Open Aeterna app
   - Grant requested permissions when prompted
   - Complete initial setup and authentication

### Method 2: Play Store (AAB) - For Store Distribution

The AAB file is intended for Google Play Store distribution and cannot be installed directly.

## Initial Setup

1. **Launch App**
   - Open Aeterna from your app drawer
   - Accept terms of service

2. **YouTube Music Authentication**
   - Tap "Sign in with YouTube Music"
   - Complete OAuth authentication process
   - Grant necessary permissions

3. **Configure Preferences**
   - Set audio quality preferences
   - Configure download settings
   - Enable accessibility features if needed

4. **Start Listening**
   - Search for your favorite music
   - Create playlists
   - Download songs for offline listening

## Troubleshooting

### Installation Issues

**"App not installed" error**
- Ensure sufficient storage space
- Try uninstalling any previous versions
- Clear download cache and retry

**"Unknown sources" disabled**
- Enable installation from unknown sources
- Some devices require enabling per-app

### Authentication Issues

**OAuth login fails**
- Check internet connection
- Ensure date/time is correct
- Try clearing browser cache

**YouTube Music not accessible**
- Verify YouTube Music account status
- Check regional availability
- Ensure account is in good standing

### Performance Issues

**App crashes on startup**
- Restart device
- Clear app cache
- Ensure Android version compatibility

**Slow performance**
- Close background apps
- Restart the app
- Check available storage space

## Uninstallation

1. Go to Settings > Apps
2. Find "Aeterna" in the app list
3. Tap "Uninstall"
4. Confirm uninstallation

Note: This will remove all downloaded music and app data.

## Support

If you encounter issues not covered in this guide:
- Check the FAQ in the app settings
- Visit our GitHub repository for known issues
- Contact support with device and Android version information

EOF

    print_success "Installation guide created: $RELEASE_DIR/INSTALLATION.md"
}

# Main execution
main() {
    # Parse command line arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            --skip-tests)
                SKIP_TESTS=true
                shift
                ;;
            --debug)
                DEBUG_BUILD=true
                shift
                ;;
            --apk-only)
                APK_ONLY=true
                shift
                ;;
            --aab-only)
                AAB_ONLY=true
                shift
                ;;
            -h|--help)
                echo "Usage: $0 [OPTIONS]"
                echo "Options:"
                echo "  --skip-tests     Skip running tests before build"
                echo "  --debug          Create debug build instead of release"
                echo "  --apk-only       Build only APK (not AAB)"
                echo "  --aab-only       Build only AAB (not APK)"
                echo "  -h, --help       Show this help message"
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                exit 1
                ;;
        esac
    done
    
    check_prerequisites
    clean_and_prepare
    
    if [ "$SKIP_TESTS" != true ]; then
        run_tests
    fi
    
    if [ "$AAB_ONLY" != true ]; then
        build_release_apk
    fi
    
    if [ "$APK_ONLY" != true ]; then
        build_release_bundle
    fi
    
    generate_checksums
    create_release_notes
    create_installation_guide
    
    echo ""
    echo "🎉 Release build completed successfully!"
    echo "📦 Release artifacts available in: $RELEASE_DIR"
    echo ""
    echo "Files created:"
    ls -la "$RELEASE_DIR"
    echo ""
    print_success "Ready for distribution!"
}

main "$@"