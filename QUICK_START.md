# Quick Start Guide

## 1. Clone and Setup (5 minutes)

```bash
# Clone the repository
git clone <repository-url>
cd incalid_tracker

# Checkout the branch
git checkout navigation-app-for-disabled

# Open in Android Studio
# File → Open → Select project folder
```

## 2. Start Backend Server (5 minutes)

```bash
cd backend

# Install Python dependencies
pip install -r requirements.txt

# Run the server
python main.py

# Backend is now running at http://localhost:8000
# API docs at http://localhost:8000/docs
```

## 3. Build and Run Android App (10 minutes)

### Option A: Using Android Studio (Easiest)

1. Open project in Android Studio
2. Wait for Gradle sync to complete
3. Connect Android device via USB or use emulator
4. Click "Run" (green play button)
5. Select target device

### Option B: Using Command Line

```bash
# Build and run debug version
./gradlew installDebug

# Or build only
./gradlew assembleDebug

# Install to connected device
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## 4. Test the App

Once the app is running:

1. **Maps Screen** - Shows accessible places near Rostov-on-Don
2. **Places Tab** - Browse and search accessible places
3. **Routes Tab** - Create and view accessible routes
4. **Saved Tab** - View your saved favorite places

### Sample Data

The backend includes mock data:
- 1 sample accessible cafe at coordinates (47.2314, 39.7258)
- You can create additional places/reviews via the API

### Test API Endpoint

```bash
# Get all places
curl "http://localhost:8000/places/search?latitude=47.2314&longitude=39.7258&radius_km=5"

# Get API docs
curl http://localhost:8000/docs
```

## 5. Build Release APK (10 minutes)

For creating a release-ready APK that can be shared or published:

### Create Signing Keystore

```bash
# Create keystore (one-time)
keytool -genkey -v -keystore app/keystore/what_apps_keystore.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias investtravel -storepass keystore_pass -keypass key_pass
```

### Create Keystore Config File

Create `app/keystore/keystore_config.properties`:

```properties
storeFile=./what_apps_keystore.keystore
storePassword=keystore_pass
keyAlias=investtravel
keyPassword=key_pass
```

### Build Release APK

```bash
# Clean and build
./gradlew clean assembleRelease

# APK location: app/build/outputs/apk/release/app-release.apk
```

### Install Release APK

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Project Structure Overview

```
incalid_tracker/
├── app/                    # Main Android application
│   ├── src/main/java/     # Kotlin source code
│   │   └── app/what/investtravel/
│   │       ├── data/      # API services, models
│   │       ├── features/  # App features (places, reviews, routes)
│   │       └── MainActivity.kt
│   ├── keystore/          # Release signing keys
│   └── build.gradle.kts   # App build config
│
├── backend/               # FastAPI backend server
│   ├── main.py           # API implementation
│   └── requirements.txt   # Python dependencies
│
├── core/                 # Shared modules
│   ├── foundation/       # DI, utilities
│   └── navigation/       # Navigation components
│
├── PROJECT_README.md     # Full documentation
├── APK_BUILD_GUIDE.md    # Release build guide
└── QUICK_START.md        # This file
```

## Key Files

**Android App**:
- `app/src/main/java/app/what/investtravel/data/remote/Models.kt` - Data models
- `app/src/main/java/app/what/investtravel/data/remote/Services.kt` - API services
- `app/src/main/java/app/what/investtravel/features/main/presentation/MainView.kt` - Maps UI
- `app/build.gradle.kts` - Dependencies and build config

**Backend**:
- `backend/main.py` - API endpoints implementation
- `backend/requirements.txt` - Dependencies

## Configuration

### Backend URL

Edit `app/src/main/java/app/what/investtravel/data/remote/ApiClient.kt`:

```kotlin
companion object {
    const val BASE_URL = "http://your-server:8000"  // Change this
}
```

### Maps API Key

The app is configured to use:
```
576b91a0-ac5c-421a-a932-38cbe1d4c633
```

## Troubleshooting

### "Module initialization failed"
```bash
# Clean Gradle cache
./gradlew clean

# Sync project
./gradlew sync
```

### Backend connection error
1. Verify backend is running: `python backend/main.py`
2. Check URL in `ApiClient.kt` matches backend address
3. Verify firewall allows port 8000

### Cannot find SDK
```bash
# Update SDK location in Android Studio
Tools → SDK Manager → Android SDK Location
```

### Build errors
```bash
# Full clean build
./gradlew clean
./gradlew build
```

## Features Checklist

- [x] Maps with accessible place markers
- [x] Places browser and detailed view
- [x] Accessibility information display
- [x] Reviews with accessibility feedback
- [x] Route planning with accessibility tags
- [x] Saved places management
- [x] User authentication (mock)
- [x] Backend REST API
- [x] Release build configuration
- [x] Proper signing for APK

## Next Steps

1. **Customize Backend**: Update database and data model if needed
2. **Connect Real Maps**: Integrate Google Maps or Yandex Maps SDK
3. **Deploy Backend**: Deploy to AWS, Google Cloud, Azure, or Heroku
4. **Add Authentication**: Connect to real auth system
5. **Publish to Play Store**: Upload release APK to Google Play

## Useful Commands

```bash
# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease

# Run on device
./gradlew installDebug

# View logs
adb logcat

# List devices
adb devices

# Clear app data
adb shell pm clear app.what.investtravel

# Build APK + Bundle
./gradlew build

# Check dependencies
./gradlew dependencies
```

## Documentation

- **Full Guide**: See [PROJECT_README.md](PROJECT_README.md)
- **Release Build**: See [APK_BUILD_GUIDE.md](APK_BUILD_GUIDE.md)
- **Backend Setup**: See [backend/README.md](backend/README.md)

## Get Help

1. Check logs: `adb logcat | grep investtravel`
2. View API docs: http://localhost:8000/docs
3. Check backend console output
4. Review code comments and documentation

## Support Contact

For accessibility questions or feature requests:
- Aleksey Olegovich Serov (@LeksGray)
- Rostov State University of Economics (RINX)

---

**Ready to build!** Start with step 1 above and you'll have the app running locally in ~30 minutes.
