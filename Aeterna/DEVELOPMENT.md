# Android SDK Development Guide

## For Development Setup

Since this project requires Android SDK for full compilation, you have several options:

### Option 1: Install Android SDK
1. Install Android Studio from https://developer.android.com/studio
2. The SDK will be automatically installed with Android Studio
3. Update `local.properties` with your SDK path:
   ```
   sdk.dir=C:\\Users\\{YOUR_USERNAME}\\AppData\\Local\\Android\\Sdk
   ```

### Option 2: Command Line Tools Only
1. Download Android Command Line Tools from https://developer.android.com/studio#command-tools
2. Extract to a folder (e.g., `C:\Android\cmdline-tools`)
3. Set ANDROID_HOME environment variable
4. Update `local.properties` with the SDK path

### Option 3: CI/Testing Mode (Current)
For testing and development without full Android SDK:
- The project includes stub configurations
- Some Android-specific features will be simulated
- Unit tests can run without Android SDK
- Full build requires proper Android SDK

## Build Commands

### With Android SDK:
```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests
```

### Without Android SDK (Testing):
```bash
./gradlew testDebugUnitTest      # Run unit tests only
./gradlew lint --continue        # Run lint checks
./gradlew dependencies           # Check dependencies
```

## Project Structure Testing

The project structure and dependencies can be validated even without full Android SDK installation.