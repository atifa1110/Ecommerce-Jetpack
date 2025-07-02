# Tokopaerbe - Ecommerce App
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/atifa1110/Ecommerce-Jetpack/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/atifa1110/Ecommerce-Jetpack/tree/master)
[![codecov](https://codecov.io/gh/atifa1110/Ecommerce-Jetpack/branch/master/graph/badge.svg)](https://codecov.io/gh/atifa1110/Ecommerce-Jetpack)

Tokopaerbe is an e-commerce application that sells a variety of electronic products, featuring functionalities such as pre-login, store browsing, order fulfillment, shopping cart, wishlist, product details, and Firebase integration 

---

## Features

- Adaptive layout based on screen size (Phone vs Tablet) using Material 3 Window Size Classes
   - Bottom navigation for phones
   - Navigation rail for tablets
- User authentication and Google OAuth integration
- Product browsing, cart, and checkout flow
- Payment method selection and integration
- Real-time remote configuration using Firebase Remote Config
- Push notifications with Firebase Cloud Messaging
- Crash reporting and analytics with Firebase Crashlytics and Analytics
- Offline caching with Room database
- Network communication using Retrofit with response inspection via Chucker
- Image loading with Glide and Coil
- MVVM architecture with clean separation of concerns
- Reactive data flow with Kotlin Coroutines, LiveData, and Flow
- Dependency injection via Hilt (optionally Koin or manual DI)
- Modularized codebase (optional) for scalable app structure
- Code quality enforcement with Detekt and Proguard for obfuscation and optimization
- Unit testing for core modules

---

## Technology Stack

| Category                  | Technology / Library                                                   |
|---------------------------|------------------------------------------------------------------------|
| Language                  | Kotlin                                                                 |
| UI                        | Material 3 + Jetpack Compose                                           |
| Responsive Layout         | Window Size Classes                                                    |
| Concurrency & Reactive    | Kotlin Coroutines, LiveData, Flow                                      |
| Dependency Injection      | Hilt, Manual DI                                                        |
| Image Loading             | Glide, Coil                                                            |
| JSON Parsing              | Gson                                                                   |
| Data Storage              | SharedPreferences, DataStore                                           |
| Build System              | Gradle Kotlin DSL + Version Catalog                                    |
| Architecture              | Single Activity, MVVM, Clean Architecture                              |
| Local Database            | Room                                                                   |
| Network                   | Retrofit, Chucker                                                      |
| Firebase Services         | Authentication, Remote Config, Cloud Messaging, Crashlytics, Analytics |
| Testing                   | JUnit, Mockito, MockK, Robolectric                                     |
| Code Quality              | Detekt, Proguard                                                       |

---

## Architecture

The app follows **Clean Architecture** principles divided into layers:

- **UI Layer**: Activities, Fragments, Composables, ViewBinding, and UI state management
- **ViewModel Layer**: State holders using LiveData / StateFlow, coroutine scopes
- **Domain Layer**: Business logic and UseCases
- **Data Layer**: Repository implementations, data sources (Network, Local DB, Preferences, Firebase)

---

## Setup Instructions

1. Clone the repository
2. Configure Firebase project and add `google-services.json` to `app/`
3. Set up Google OAuth scopes for Firebase Cloud Messaging:
    - Visit [Google OAuth Playground](https://developers.google.com/oauthplayground)
    - Input the scope `https://www.googleapis.com/auth/firebase.messaging`
4. Build and run the app using Android Studio
5. Customize Remote Config parameters via Firebase Console

---

## Usage

- Adaptive layout: Uses `WindowSizeClass` to adapt UI based on screen width
   - Displays bottom navigation on phones
   - Uses navigation rail on tablets
- Firebase Remote Config is used for controlling payment methods and feature toggles
- Retrofit is used for all API calls with Chucker for debugging
- ViewModel with Flow and LiveData handles UI reactivity
- Push notifications are managed via FCM
- Crashlytics and Analytics log errors and user behavior
---

## Notes

- During development, Firebase Remote Config fetch interval is set to `0` for immediate updates
- In production, a fetch interval of `3600` seconds (1 hour) is used to optimize network usage
- The app supports modularization for scalability; modules can be found under `/modules` (optional)
- Code quality and obfuscation is ensured using Detekt and Proguard

---

## Code Quality

- Static code analysis is enforced using [Detekt](https://github.com/detekt/detekt)
   - Configured using `config/detekt/detekt.yml`
   - Runs via Gradle task: `./gradlew detekt`
   - Custom rules adapted for Jetpack Compose and Clean Architecture
   - Formatting enforced via `detekt-formatting` (based on ktlint)
- Obfuscation and optimization enabled via Proguard

---

## Testing

- Unit tests are located in the `test` directory
- Run tests with `./gradlew test` or via Android Studio

---

## Contribution

Contributions are welcome! Feel free to submit pull requests or open issues.

---

## License

Specify your license here.
