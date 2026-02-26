# Accessibility Navigation Platform - Android Application

A comprehensive Android application built with Kotlin and Jetpack Compose for helping people with disabilities discover and navigate to accessible locations in Rostov-on-Don and beyond.

## Overview

This application serves as an information and navigation platform specifically designed for people with disabilities. It provides:

- **Accessible Places Discovery**: Find cafes, cinemas, restaurants, parks, and other venues with detailed accessibility information
- **Accessibility Feedback**: Read and write reviews with specific accessibility feedback from real users
- **Route Planning**: Plan journeys through accessible waypoints with wheelchair-friendly routes
- **Location Services**: Integration with maps API to visualize accessible places and routes

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM with clean separation of concerns
- **Dependency Injection**: Koin
- **Networking**: Ktor Client with Kotlinx Serialization
- **Database**: Room (for local caching)
- **Backend**: FastAPI (Python)
- **Maps**: Yandex Maps / Google Maps API integration

## Project Structure

```
app/
├── src/main/
│   ├── java/app/what/investtravel/
│   │   ├── data/
│   │   │   ├── local/          # Local database (Room)
│   │   │   └── remote/         # API services and models
│   │   ├── domain/             # Business logic and controllers
│   │   ├── features/
│   │   │   ├── auth/           # Authentication
│   │   │   ├── main/           # Maps and home screen
│   │   │   ├── places/         # Places browser and details
│   │   │   ├── reviews/        # Reviews and feedback
│   │   │   ├── routes/         # Route planning
│   │   │   └── settings/       # User settings
│   │   └── MainActivity.kt
│   └── res/                    # Android resources
│
├── keystore/                   # Release signing configuration
└── build.gradle.kts            # Build configuration

core/
├── foundation/                 # Core utilities, DI setup
└── navigation/                 # Navigation components

backend/
├── main.py                    # FastAPI server
├── requirements.txt            # Python dependencies
└── README.md                   # Backend setup instructions
```

## Features

### 1. Maps with Accessible Place Markers
- Display accessible places on interactive map
- Show accessibility indicators and ratings
- Tap markers for quick place information
- Real-time location tracking

### 2. Places Discovery
- Browse all accessible places by category
- Filter by accessibility features (wheelchair access, elevators, accessible restrooms, etc.)
- Search by location and radius
- View detailed place information with contact details
- Save favorite places

### 3. Accessibility Feedback System
- Write detailed reviews with accessibility feedback
- Rate difficulty level (easy, moderate, hard)
- Record specific accessibility issues encountered
- Provide recommendations for improvements
- Read reviews from other users with disabilities
- View staff helpfulness ratings

### 4. Route Planning
- Plan journeys with multiple waypoints
- Set difficulty level and accessibility requirements
- Filter by accessibility tags (no-stairs, elevator-access, parking, etc.)
- Create wheelchair-friendly routes
- Save and share routes
- Estimated travel time and distance

### 5. User Management
- User authentication and profiles
- Saved places and favorite routes
- Review history and contributions
- Accessibility preferences

## Data Models

### AccessiblePlace
```kotlin
- id: Int
- name: String
- address: String
- city: String
- latitude: Double
- longitude: Double
- category: String (cafe, cinema, restaurant, park, transport, theater, shop, medical)
- accessibilityInfo: AccessibilityInfo
- rating: Float
- reviewCount: Int
- phone: String?
- email: String?
- website: String?
```

### AccessibilityInfo
```kotlin
- wheelchairAccessible: Boolean
- elevator: Boolean
- accessibleToilet: Boolean
- hearingLoop: Boolean
- visualGuides: Boolean
- parkingAccessible: Boolean
- petFriendly: Boolean
- serviceAnimalsAllowed: Boolean
- staffTrained: Boolean
- notes: String?
```

### Review with Accessibility Feedback
```kotlin
- id: Int
- placeId: Int
- userId: Int
- rating: Float (1-5)
- text: String
- accessibilityFeedback:
  - difficultyLevel: String (easy, moderate, hard)
  - issuesEncountered: List<String>
  - recommendations: String
  - staffHelpfulness: Int (1-5)
  - visitedDate: String
```

### AccessibleRoute
```kotlin
- id: Int
- name: String
- startPoint: RoutePoint
- endPoint: RoutePoint
- waypoints: List<RoutePoint>
- totalDistanceKm: Float
- estimatedDurationMinutes: Int
- accessibilityTags: List<String>
- difficultyLevel: String (easy, moderate, hard)
- wheelchairFriendly: Boolean
- restPoints: List<AccessiblePlace>
```

## API Endpoints

### Places
```
GET    /places/search                  # Search with filters and location
GET    /places/{id}                    # Get place details
POST   /places/create                  # Create new place
POST   /places/filter                  # Filter by accessibility criteria
POST   /places/save                    # Save to favorites
GET    /places/saved                   # Get saved places
```

### Reviews
```
POST   /reviews/create                 # Create review with accessibility feedback
GET    /reviews/place/{placeId}        # Get reviews for a place
GET    /reviews/my                     # Get user's reviews
```

### Routes
```
POST   /routes/create                  # Create accessible route
GET    /routes/accessible              # List routes with filters
GET    /routes/{id}                    # Get route details
POST   /routes/{id}/add-place          # Add place to route
POST   /routes/filter                  # Filter routes by accessibility
```

### Authentication
```
POST   /auth/login/                    # User login
```

## Setup Instructions

### Prerequisites
- Android SDK Level 26+
- Android Studio (latest)
- Java 11+
- Python 3.8+ (for backend)

### Android App Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd incalid_tracker
   git checkout navigation-app-for-disabled
   ```

2. **Configure Backend URL**
   - Edit `app/src/main/java/app/what/investtravel/data/remote/ApiClient.kt`
   - Update `BASE_URL` to your backend server

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   # Or use Android Studio: Run → Run 'app'
   ```

### Backend Setup

1. **Install dependencies**
   ```bash
   cd backend
   pip install -r requirements.txt
   ```

2. **Run the server**
   ```bash
   python main.py
   ```
   The API will be available at `http://localhost:8000`

3. **View API documentation**
   Open `http://localhost:8000/docs` in browser

## Building for Release

See [APK_BUILD_GUIDE.md](APK_BUILD_GUIDE.md) for detailed instructions on:
- Creating signing keystore
- Building release APK
- Configuring ProGuard
- Publishing to Google Play Store

Quick build:
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

## Configuration

### Maps API Key
The application uses the following maps API key:
```
576b91a0-ac5c-421a-a932-38cbe1d4c633
```

Update in `ApiClient.kt` if needed.

### Backend API
```
Base URL: http://45.155.207.232:1478
```

Update in `ApiClient.kt` for production deployment.

## AI Features

The application includes mock AI implementations for:
- **Accessibility Analysis**: Analyze review text for accessibility insights
- **Route Generation**: Generate optimal accessible routes based on preferences
- **Recommendations**: Suggest accessibility improvements for places

These are placeholder implementations. Connect to actual AI services (OpenAI, Claude, etc.) as needed.

## Accessibility Considerations

This app itself is designed with accessibility in mind:
- Large, easy-to-read text
- High contrast colors
- Proper spacing and layouts
- Support for screen readers (TalkBack)
- Keyboard navigation support
- Clear iconography

## Performance

The release build includes:
- **Code Minification**: ProGuard removes unused code
- **Resource Shrinking**: Unused resources removed
- **Optimization**: Bytecode optimization for faster execution
- **Local Caching**: Room database caches frequently accessed data

## Architecture

### MVVM Pattern
- **Model**: Data models and repository layer
- **View**: Jetpack Compose UI components
- **ViewModel**: State management and business logic via controllers

### Clean Architecture
- **Data Layer**: API services, database, models
- **Domain Layer**: Controllers, state, events, actions
- **Presentation Layer**: Composable UI components

### Dependency Injection
- **Koin**: Framework for DI setup
- **Modules**: Separated by feature for modularity

## Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumentation tests:
```bash
./gradlew connectedAndroidTest
```

## Contributing

1. Clone the repository
2. Create a feature branch
3. Make changes
4. Push and create a pull request

## License

MIT License - See LICENSE file for details

## Support

For issues, feature requests, or questions:
1. Check existing documentation
2. Review backend logs
3. Verify API connectivity
4. Check Logcat output in Android Studio

## Future Enhancements

- [ ] Real-time crowd-sourced accessibility updates
- [ ] Integration with real accessibility databases
- [ ] Advanced route optimization algorithms
- [ ] Video walkthrough of accessible routes
- [ ] Multi-language support
- [ ] Offline map caching
- [ ] Social features (share routes, follow users)
- [ ] Integration with public transport accessibility info
- [ ] Audio/visual navigation assistance
- [ ] Integration with accessibility certification authorities

## Team

Created for the Rostov State University of Economics (RINX) accessibility platform initiative.

Contact: @LeksGray (Aleksey Olegovich Serov)

## Changelog

### Version 1.0 (Initial Release)
- Maps feature with accessible place markers
- Places browser and search
- Reviews with accessibility feedback system
- Route planning with accessibility tags
- User authentication
- Favorite places management
- Release ready APK build configuration
