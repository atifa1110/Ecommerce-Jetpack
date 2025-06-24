# Tokopaerbe - Ecommerce App
[![codecov](https://codecov.io/gh/atifa1110/Ecommerce-Jetpack/branch/master/graph/badge.svg)](https://codecov.io/gh/atifa1110/Ecommerce-Jetpack)

Tokopaerbe is an e-commerce application that sells a variety of electronic products, featuring functionalities such as pre-login, store browsing, order fulfillment, shopping cart, wishlist, product details, and Firebase integration 

---

## Features

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

| Category                  | Technology / Library                 |
|---------------------------|------------------------------------|
| Language                  | Kotlin                             |
| UI                        | ViewBinding, Material 3            |
| Concurrency & Reactive    | Kotlin Coroutines, LiveData, Flow  |
| Dependency Injection      | Hilt, Koin (optional), Manual DI   |
| Image Loading             | Glide, Coil                       |
| JSON Parsing              | Gson, Moshi                      |
| Data Storage              | SharedPreferences, DataStore        |
| Build System              | Gradle Groovy, Kotlin DSL, buildSrc, build-logic  |
| Architecture             | Single Activity, MVVM, Clean Architecture        |
| Local Database            | Room                               |
| Network                   | Retrofit, Chucker                  |
| Firebase Services         | Authentication, Remote Config, Cloud Messaging, Crashlytics, Analytics  |
| Testing                   | Unit Testing                      |
| Code Quality              | Detekt, Proguard                  |

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

- The app uses **Firebase Remote Config** for payment method configuration and feature flags
- Network requests are handled with Retrofit and responses cached locally with Room
- User interactions follow the MVVM pattern with reactive updates via Flow and LiveData
- Push notifications are enabled and handled via Firebase Cloud Messaging
- Crash reports and analytics events are tracked using Firebase Crashlytics and Analytics

---

## Notes

- During development, Firebase Remote Config fetch interval is set to `0` for immediate updates
- In production, a fetch interval of `3600` seconds (1 hour) is used to optimize network usage
- The app supports modularization for scalability; modules can be found under `/modules` (optional)
- Code quality and obfuscation is ensured using Detekt and Proguard

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
