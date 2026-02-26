# Building APK for Accessibility Navigation App

This guide provides step-by-step instructions to build and release the Android APK for the accessibility navigation platform.

## Prerequisites

- Android SDK (API Level 26+)
- Android Studio (latest version)
- Java Development Kit (JDK 11+)
- Gradle 8.0+

## Project Structure

```
app/
├── src/main/
│   ├── java/app/what/investtravel/
│   │   ├── data/          # Data layer (API services, models)
│   │   ├── domain/        # Domain layer (controllers, state management)
│   │   ├── features/      # Feature modules
│   │   │   ├── main/      # Main/Maps feature
│   │   │   ├── places/    # Places browser
│   │   │   ├── reviews/   # Reviews system
│   │   │   └── routes/    # Route planning
│   │   └── MainActivity.kt
│   └── AndroidManifest.xml
├── keystore/             # Signing configuration
└── build.gradle.kts      # Build configuration

core/
├── foundation/           # Core utilities and DI
└── navigation/           # Navigation components

backend/
├── main.py              # FastAPI backend
└── requirements.txt     # Backend dependencies
```

## API Configuration

The app uses the following API endpoint for the backend services:

```
Base URL: http://45.155.207.232:1478
Maps API Key: 576b91a0-ac5c-421a-a932-38cbe1d4c633
```

Update the backend URL in `app/src/main/java/app/what/investtravel/data/remote/ApiClient.kt` if deploying to a different server.

## Signing Configuration

### Option 1: Using Keystore Properties File (Recommended)

1. Create `app/keystore/keystore_config.properties`:

```properties
storeFile=./what_apps_keystore.keystore
storePassword=your_keystore_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

2. Place your keystore file at `app/keystore/what_apps_keystore.keystore`

3. Build release APK:
```bash
./gradlew assembleRelease
```

### Option 2: Using Environment Variables

Set environment variables before building:

```bash
export KEYSTORE_PASSWORD="your_keystore_password"
export RELEASE_SIGN_KEY_ALIAS="your_key_alias"
export RELEASE_SIGN_KEY_PASSWORD="your_key_password"

./gradlew assembleRelease
```

### Option 3: Creating a New Keystore

If you don't have a keystore, create one:

```bash
keytool -genkey -v -keystore app/keystore/what_apps_keystore.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias investtravel -storepass keystore_password \
  -keypass key_password
```

Then use Option 1 with the passwords you provided.

## Building Debug APK

```bash
# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

## Building Release APK (Signed)

### Step 1: Ensure Signing Configuration

Create `app/keystore/keystore_config.properties` or set environment variables (see Signing Configuration section above).

### Step 2: Build Release APK

```bash
# Clean and build
./gradlew clean assembleRelease

# Or just build
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

### Step 3: Verify APK Signature

```bash
jarsigner -verify -verbose app/build/outputs/apk/release/app-release.apk
```

## Building Bundle (AAB) for Play Store

Android App Bundle is required for uploading to Google Play Store:

```bash
./gradlew bundleRelease

# Output: app/build/outputs/bundle/release/app-release.aab
```

## ProGuard Configuration

ProGuard is enabled for release builds to:
- Minimize APK size by removing unused code
- Obfuscate code for security
- Optimize performance

Configuration files:
- `proguard-rules.pro` - Custom ProGuard rules
- `getDefaultProguardFile("proguard-android-optimize.txt")` - Android default rules

### Important: Keep Serialization Classes

The following classes should NOT be obfuscated (already configured in proguard-rules.pro):

```
-keep class app.what.investtravel.data.remote.** { *; }
-keep class kotlinx.serialization.** { *; }
```

## APK Installation

### Install Debug APK

```bash
./gradlew installDebug
```

### Install Release APK

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

### Install from Android Studio

1. Connect Android device via USB (or use emulator)
2. Click "Run" or "Debug" in Android Studio
3. Select target device

## Performance Optimization

The release build includes:
- **Code Minification**: ProGuard removes unused code
- **Resource Shrinking**: Unused resources are removed
- **Code Optimization**: ProGuard optimizes bytecode

To verify optimization:

```bash
# Check APK size
du -h app/build/outputs/apk/release/app-release.apk

# Extract and inspect
unzip -l app/build/outputs/apk/release/app-release.apk | wc -l
```

## Backend Deployment

Before releasing, ensure the backend API is deployed:

```bash
# Install backend dependencies
pip install -r backend/requirements.txt

# Run backend locally
python backend/main.py

# For production, deploy to a cloud service:
# - AWS EC2
# - Google Cloud Run
# - Azure App Service
# - DigitalOcean
# - Heroku
```

Update `ApiClient.kt` with the production backend URL.

## Publishing to Google Play Store

1. Create Google Play Developer Account
2. Create new app in Google Play Console
3. Fill app details, screenshots, description
4. Upload signed APK/AAB (`app-release.aab`)
5. Configure pricing and distribution
6. Submit for review

## Troubleshooting

### Build Fails with "Missing Keystore"

Solution: Create keystore file or set environment variables (see Signing Configuration).

### APK Not Installable

- Check that `minSdk` matches device OS level
- Verify APK is signed correctly: `jarsigner -verify app-release.apk`

### Large APK Size

- Check for large assets in `src/main/assets/`
- Verify ProGuard is enabled for release build
- Use bundle (AAB) instead of APK

### API Connection Issues

- Verify backend server is running
- Check network connectivity
- Update `BASE_URL` in `ApiClient.kt`
- Check firewall rules if on corporate network

## Release Checklist

- [ ] Update version code/name in `build.gradle.kts`
- [ ] Update `versionCode` and `versionName`
- [ ] Configure signing (keystore password, etc.)
- [ ] Test on physical device (multiple API levels if possible)
- [ ] Verify API connectivity
- [ ] Build release APK/AAB
- [ ] Check APK signature
- [ ] Test installation from APK file
- [ ] Create release notes
- [ ] Upload to Play Store

## Version Management

Update in `app/build.gradle.kts`:

```kotlin
versionCode = 2        // Increment for each release
versionName = "1.1"    // Semantic versioning
```

## Support

For issues during building:
1. Check Gradle sync (File → Sync Now)
2. Clean project: `./gradlew clean`
3. Invalidate Caches: File → Invalidate Caches
4. Check SDK versions match `build.gradle.kts`

## Additional Resources

- [Android Build System Documentation](https://developer.android.com/studio/build)
- [Signing Your Application](https://developer.android.com/studio/publish/app-signing)
- [Prepare for Release](https://developer.android.com/studio/publish/preparing)
