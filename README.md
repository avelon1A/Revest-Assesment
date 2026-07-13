# Product Browser — Kotlin Multiplatform

A cross-platform product catalog for **Android** and **iOS**, built with Kotlin Multiplatform +
Compose Multiplatform. Data comes from the [DummyJSON Products API](https://dummyjson.com/docs/products).

## Features
- Product list with name, price, and thumbnail
- Product detail: title, description, brand, price, rating
- Keyword search (API-backed)
- Category filter (optional)

## Architecture
Clean Architecture in the shared module: `presentation → domain ← data`.

- **data** — Ktor `ProductApi`, DTOs (kotlinx.serialization), `ProductRepositoryImpl`
- **domain** — `Product` model, `ProductRepository`, use cases (GetProducts, SearchProducts, …)
- **presentation** — Circuit screens (Presenter + UI + State)
- **di** — Koin module

**Stack:** Compose Multiplatform (Material 3) · Circuit · Koin · Ktor · kotlinx.serialization · Coil 3

## Build & run
Requires JDK 17, Android SDK, and Xcode (for iOS).

```bash
# Android
./gradlew :androidApp:assembleDebug          # or run `androidApp` in Android Studio

# iOS
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
# then open iosApp/iosApp.xcodeproj in Xcode and run the iosApp scheme

# Tests
./gradlew :shared:testAndroidHostTest
```

## Notes & trade-offs
- **Circuit** is used in place of ViewModel + StateFlow — its presenter returns the observable
  `CircuitUiState`. Data/domain layers stay plain `suspend` functions.
- Circuit factories are wired manually via Koin (codegen doesn't support Koin).
- `Screen` keys are `expect/actual` (`@Parcelize` on Android only).
- Gradle configuration cache is disabled (incompatible with the KMP metadata task here).
