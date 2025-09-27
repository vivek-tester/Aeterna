#!/bin/bash
# Deployment Script for Aeterna
# Handles deployment to various distribution channels

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
VERSION_NAME=$(./gradlew -q printVersionName 2>/dev/null || echo "1.0.0")
VERSION_CODE=$(./gradlew -q printVersionCode 2>/dev/null || echo "1")
RELEASE_DIR="releases/$(date +%Y-%m-%d)"

echo "🚀 Aeterna Deployment Script"
echo "============================"
echo "Version: $VERSION_NAME ($VERSION_CODE)"
echo "Date: $(date)"
echo ""

# GitHub Release
deploy_to_github() {
    print_status "Deploying to GitHub Releases..."
    
    if [ -z "$GITHUB_TOKEN" ]; then
        print_error "GITHUB_TOKEN not set. Please set your GitHub personal access token."
        return 1
    fi
    
    # Create GitHub release using gh CLI or API
    if command -v gh &> /dev/null; then
        # Using GitHub CLI
        gh release create "v$VERSION_NAME" \
            "$RELEASE_DIR"/*.apk \
            "$RELEASE_DIR"/*.aab \
            "$RELEASE_DIR"/*.sha256 \
            "$RELEASE_DIR"/*.md \
            --title "Aeterna v$VERSION_NAME" \
            --notes-file "$RELEASE_DIR/RELEASE_NOTES.md" \
            --draft
        
        print_success "GitHub release created (draft)"
    else
        print_warning "GitHub CLI (gh) not installed. Please upload manually."
        echo "Upload these files to GitHub Releases:"
        ls -la "$RELEASE_DIR"
    fi
}

# F-Droid Preparation
prepare_fdroid() {
    print_status "Preparing F-Droid metadata..."
    
    mkdir -p fdroid/metadata/com.aeterna.aeterna
    
    # Create F-Droid metadata
    cat > fdroid/metadata/com.aeterna.aeterna.yml << EOF
Categories:
  - Multimedia
  - Internet

License: GPL-3.0-or-later

AuthorName: Aeterna Team
AuthorEmail: dev@aeterna.app
AuthorWebSite: https://aeterna.app

WebSite: https://aeterna.app
SourceCode: https://github.com/aeterna/aeterna
IssueTracker: https://github.com/aeterna/aeterna/issues
Changelog: https://github.com/aeterna/aeterna/releases

AutoName: Aeterna
Name: Aeterna - YouTube Music Client

RepoType: git
Repo: https://github.com/aeterna/aeterna.git

Builds:
  - versionName: $VERSION_NAME
    versionCode: $VERSION_CODE
    commit: v$VERSION_NAME
    subdir: app
    gradle:
      - release

Description: |
    Aeterna is an open-source YouTube Music client for Android that provides a premium music streaming experience with advanced features.

    Features:
    * Clean Material You interface with dynamic theming
    * High-quality audio streaming from YouTube Music
    * Offline downloads for premium subscribers
    * Advanced audio processing with tempo and pitch controls
    * Live synchronized lyrics support
    * Comprehensive accessibility features
    * Responsive design for phones, tablets, and foldables
    * Ad-free experience with premium features

    Accessibility:
    * Full TalkBack and screen reader support
    * High contrast mode for better visibility
    * Large text support
    * Reduced motion options
    * Voice control compatibility
    * WCAG 2.1 AA compliance

    Note: Requires YouTube Music account. Some features require YouTube Music Premium subscription.

RequiresRoot: false

AutoUpdateMode: Version v%v
UpdateCheckMode: Tags
CurrentVersion: $VERSION_NAME
CurrentVersionCode: $VERSION_CODE
EOF

    print_success "F-Droid metadata prepared"
}

# IzzyOnDroid Repository
prepare_izzy() {
    print_status "Preparing IzzyOnDroid submission..."
    
    mkdir -p izzy-submission
    
    # Create IzzyOnDroid submission info
    cat > izzy-submission/submission.md << EOF
# Aeterna - YouTube Music Client Submission

## App Information
- **Name**: Aeterna - YouTube Music Client  
- **Package**: com.aeterna.aeterna
- **Version**: $VERSION_NAME ($VERSION_CODE)
- **License**: GPL-3.0-or-later
- **Category**: Music & Audio

## Links
- **Source Code**: https://github.com/aeterna/aeterna
- **Issue Tracker**: https://github.com/aeterna/aeterna/issues
- **Website**: https://aeterna.app

## Description
Open-source YouTube Music client with premium features, accessibility support, and Material You design.

## Features
- Clean, modern interface with dynamic theming
- High-quality music streaming
- Offline downloads (with premium subscription)
- Advanced audio processing
- Live lyrics support
- Full accessibility features
- Ad-free experience

## Technical Details
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: Clean Architecture with MVVM
- **UI**: Jetpack Compose with Material 3

## Permissions
- INTERNET: Stream music
- ACCESS_NETWORK_STATE: Check connectivity
- WRITE_EXTERNAL_STORAGE: Download music
- WAKE_LOCK: Background playback
- FOREGROUND_SERVICE: Music service

## Screenshots
Please find screenshots in the screenshots/ directory.

## APK
- File: aeterna-v$VERSION_NAME-$VERSION_CODE.apk
- SHA256: $(cat "$RELEASE_DIR"/*.apk.sha256 2>/dev/null || echo "TBD")

## Notes
- App requires YouTube Music account
- Some features require YouTube Music Premium
- Complies with YouTube API Terms of Service
- Focuses on accessibility and inclusive design
EOF

    print_success "IzzyOnDroid submission prepared"
}

# APK Distribution Preparation
prepare_apk_distribution() {
    print_status "Preparing APK distribution package..."
    
    mkdir -p distribution
    
    # Copy release files
    cp -r "$RELEASE_DIR"/* distribution/
    
    # Create distribution README
    cat > distribution/README.md << EOF
# Aeterna v$VERSION_NAME Distribution Package

This package contains all files needed to distribute Aeterna v$VERSION_NAME.

## Files Included

### APK Files
- \`aeterna-v$VERSION_NAME-$VERSION_CODE.apk\` - Main application
- \`aeterna-v$VERSION_NAME-$VERSION_CODE.aab\` - Play Store bundle

### Checksums
- \`*.sha256\` - SHA256 checksums for verification
- \`*.md5\` - MD5 checksums for verification

### Documentation
- \`RELEASE_NOTES.md\` - Release notes and changelog
- \`INSTALLATION.md\` - Installation instructions
- \`README.md\` - This file

## Verification

To verify file integrity:

\`\`\`bash
# Verify SHA256
sha256sum -c aeterna-v$VERSION_NAME-$VERSION_CODE.apk.sha256

# Verify MD5
md5sum -c aeterna-v$VERSION_NAME-$VERSION_CODE.apk.md5
\`\`\`

## Distribution Channels

### Direct APK Distribution
- Upload APK and checksums to download servers
- Provide installation instructions
- Include virus scanning results

### Repository Distribution
- F-Droid: Submit to F-Droid repository
- IzzyOnDroid: Submit to IzzyOnDroid repository
- Custom repositories: Add to custom F-Droid repos

### GitHub Releases
- Create GitHub release with all artifacts
- Tag with version number
- Include comprehensive release notes

## Support

For distribution support:
- GitHub Issues: Report distribution problems
- Email: support@aeterna.app
- Documentation: https://docs.aeterna.app/distribution

---
Generated: $(date)
EOF

    print_success "APK distribution package prepared"
}

# Create screenshots for store listings
create_screenshots() {
    print_status "Creating screenshots directory structure..."
    
    mkdir -p screenshots/{phone,tablet,accessibility}
    
    # Create screenshot guidelines
    cat > screenshots/README.md << EOF
# Aeterna Screenshots Guide

## Directory Structure
- \`phone/\` - Phone screenshots (1080x1920 or similar)
- \`tablet/\` - Tablet screenshots (2048x1536 or similar)  
- \`accessibility/\` - Accessibility feature screenshots

## Required Screenshots

### Phone Screenshots (5-8 images)
1. **Home Screen** - Welcome screen with quick picks
2. **Music Player** - Now playing screen with controls
3. **Search** - Search interface with results
4. **Library** - User's music library and playlists
5. **Lyrics** - Live lyrics display
6. **Settings** - App settings and preferences
7. **Offline** - Downloaded music interface
8. **Accessibility** - High contrast mode demonstration

### Tablet Screenshots (2-4 images)
1. **Home Screen** - Tablet layout showing responsive design
2. **Music Player** - Tablet player interface
3. **Library** - Multi-column library view
4. **Settings** - Tablet settings layout

### Accessibility Screenshots (3-5 images)
1. **High Contrast** - High contrast mode enabled
2. **Large Text** - Large text support demonstration
3. **TalkBack** - TalkBack overlay demonstration
4. **Voice Control** - Voice Access interface
5. **Settings** - Accessibility settings panel

## Guidelines

### Technical Requirements
- **Format**: PNG (preferred) or JPEG
- **Phone**: 1080x1920, 1440x2560, or similar 16:9/18:9 ratios
- **Tablet**: 2048x1536, 2732x2048, or similar 4:3 ratios
- **Quality**: High quality, no compression artifacts
- **Text**: Clearly readable, avoid small fonts

### Content Guidelines
- Show actual app content, not placeholder text
- Demonstrate key features prominently
- Include variety of music content
- Show different themes/colors
- Avoid showing copyrighted content prominently
- Include accessibility features in action

### Store-Specific Requirements

#### Google Play Store
- 2-8 screenshots per form factor
- Minimum 320px on any side
- Maximum 3840px on any side
- 16:9 or 18:9 aspect ratio for phones
- No excessive text overlay

#### F-Droid
- Any reasonable resolution
- PNG format preferred
- Show actual functionality
- Avoid marketing language

## Automation

Screenshots can be automated using:
- Fastlane Screengrab for Android
- Espresso UI tests with screenshots
- Manual capture during testing

## Naming Convention
- \`01_home.png\` - Home screen
- \`02_player.png\` - Music player
- \`03_search.png\` - Search interface
- \`04_library.png\` - Library view
- \`05_lyrics.png\` - Lyrics display
- \`06_settings.png\` - Settings
- \`07_offline.png\` - Offline mode
- \`08_accessibility.png\` - Accessibility features

EOF

    print_success "Screenshots directory and guidelines created"
}

# Main execution
main() {
    # Check if release directory exists
    if [ ! -d "$RELEASE_DIR" ]; then
        print_error "Release directory not found: $RELEASE_DIR"
        print_error "Please run build_release.sh first to create release artifacts."
        exit 1
    fi
    
    # Parse command line arguments
    DEPLOY_GITHUB=false
    PREPARE_FDROID=false
    PREPARE_IZZY=false
    PREPARE_APK=false
    CREATE_SCREENSHOTS=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --github)
                DEPLOY_GITHUB=true
                shift
                ;;
            --fdroid)
                PREPARE_FDROID=true
                shift
                ;;
            --izzy)
                PREPARE_IZZY=true
                shift
                ;;
            --apk)
                PREPARE_APK=true
                shift
                ;;
            --screenshots)
                CREATE_SCREENSHOTS=true
                shift
                ;;
            --all)
                DEPLOY_GITHUB=true
                PREPARE_FDROID=true
                PREPARE_IZZY=true
                PREPARE_APK=true
                CREATE_SCREENSHOTS=true
                shift
                ;;
            -h|--help)
                echo "Usage: $0 [OPTIONS]"
                echo "Options:"
                echo "  --github        Deploy to GitHub Releases"
                echo "  --fdroid        Prepare F-Droid metadata"
                echo "  --izzy          Prepare IzzyOnDroid submission"
                echo "  --apk           Prepare APK distribution package"
                echo "  --screenshots   Create screenshots directory structure"
                echo "  --all           Prepare all distribution channels"
                echo "  -h, --help      Show this help message"
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                exit 1
                ;;
        esac
    done
    
    # If no specific options, prepare everything
    if [ "$DEPLOY_GITHUB" = false ] && [ "$PREPARE_FDROID" = false ] && [ "$PREPARE_IZZY" = false ] && [ "$PREPARE_APK" = false ] && [ "$CREATE_SCREENSHOTS" = false ]; then
        DEPLOY_GITHUB=true
        PREPARE_FDROID=true
        PREPARE_IZZY=true
        PREPARE_APK=true
        CREATE_SCREENSHOTS=true
    fi
    
    # Execute selected actions
    if [ "$DEPLOY_GITHUB" = true ]; then
        deploy_to_github
    fi
    
    if [ "$PREPARE_FDROID" = true ]; then
        prepare_fdroid
    fi
    
    if [ "$PREPARE_IZZY" = true ]; then
        prepare_izzy
    fi
    
    if [ "$PREPARE_APK" = true ]; then
        prepare_apk_distribution
    fi
    
    if [ "$CREATE_SCREENSHOTS" = true ]; then
        create_screenshots
    fi
    
    echo ""
    echo "🎉 Deployment preparation completed!"
    echo "📋 Next steps:"
    echo "   1. Review generated files and metadata"
    echo "   2. Take screenshots following the guidelines"
    echo "   3. Submit to appropriate distribution channels"
    echo "   4. Update documentation and announcements"
    echo ""
    print_success "Ready for distribution!"
}

main "$@"