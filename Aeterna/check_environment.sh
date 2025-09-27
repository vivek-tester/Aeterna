#!/bin/bash
# Quick Development Environment Check for Aeterna

echo "🔍 Aeterna Development Environment Check"
echo "========================================"
echo

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_check() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# Check Java
echo "🔧 Checking Development Prerequisites..."
echo

java -version >/dev/null 2>&1
print_check $? "Java Runtime Environment"

# Check Android SDK
if [ -n "$ANDROID_HOME" ] && [ -d "$ANDROID_HOME" ]; then
    print_check 0 "Android SDK location: $ANDROID_HOME"
else
    print_check 1 "Android SDK not found"
    print_warning "Set ANDROID_HOME or install Android Studio"
fi

# Check Gradle wrapper
if [ -f "./gradlew" ]; then
    print_check 0 "Gradle wrapper present"
    chmod +x ./gradlew 2>/dev/null
else
    print_check 1 "Gradle wrapper missing"
fi

# Check project files
echo
echo "📁 Checking Project Structure..."
echo

project_files=(
    "app/build.gradle.kts:Main app build file"
    "core/data/build.gradle.kts:Data module build file"
    "core/domain/build.gradle.kts:Domain module build file" 
    "core/ui/build.gradle.kts:UI module build file"
    "gradle/libs.versions.toml:Dependency catalog"
    "local.properties:Local configuration"
)

for item in "${project_files[@]}"; do
    file="${item%%:*}"
    desc="${item##*:}"
    if [ -f "$file" ]; then
        print_check 0 "$desc"
    else
        print_check 1 "$desc (missing: $file)"
    fi
done

# Check scripts
echo
echo "📜 Checking Build Scripts..."
echo

scripts=(
    "scripts/test.sh:Testing script"
    "scripts/build_release.sh:Release build script"
    "scripts/deploy.sh:Deployment script"
    "dev_setup.sh:Development setup"
)

for item in "${scripts[@]}"; do
    file="${item%%:*}"
    desc="${item##*:}"
    if [ -f "$file" ]; then
        print_check 0 "$desc"
        chmod +x "$file" 2>/dev/null
    else
        print_check 1 "$desc (missing: $file)"
    fi
done

# Check documentation
echo
echo "📚 Checking Documentation..."
echo

docs=(
    "README.md:Project README"
    "ACCESSIBILITY.md:Accessibility guide"
    "DEVELOPMENT.md:Development guide"
    "NEXT_STEPS.md:Next steps guide"
    "PROJECT_COMPLETE.md:Project completion summary"
)

for item in "${docs[@]}"; do
    file="${item%%:*}"
    desc="${item##*:}"
    if [ -f "$file" ]; then
        print_check 0 "$desc"
    else
        print_check 1 "$desc (missing: $file)"
    fi
done

echo
echo "🎯 Next Steps Recommendations:"
echo

if [ -z "$ANDROID_HOME" ] || [ ! -d "$ANDROID_HOME" ]; then
    echo "1. 📱 Install Android Studio: https://developer.android.com/studio"
    echo "2. ⚙️  Set ANDROID_HOME environment variable"
    echo "3. 🔄 Update local.properties with SDK path"
fi

echo "4. 🏗️  Run: ./gradlew clean build"
echo "5. 🧪 Run: ./scripts/test.sh"
echo "6. 📱 Run: ./gradlew installDebug (with device connected)"
echo "7. 🚀 Run: ./scripts/build_release.sh (for release build)"

echo
echo "📖 For detailed instructions, see: NEXT_STEPS.md"
echo
echo -e "${GREEN}🎉 Your Aeterna project is ready for development!${NC}"
echo