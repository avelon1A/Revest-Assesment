# Product Browser — Kotlin Multiplatform (KMM + Compose Multiplatform)

A cross-platform product catalog prototype for **Android** and **iOS**, built with Kotlin
Multiplatform and Compose Multiplatform. Product data is consumed from the public
[DummyJSON Products API](https://dummyjson.com/docs/products).

---

## 1. Business requirements

Revest is exploring a cross-platform product-catalog prototype for internal use. The app lets a
user:

1. **Browse products** — a scrollable list showing each product's name, price, and thumbnail.
2. **View product details** — tap a product to see its title, description, brand, price, and rating.
3. **Search** — filter the catalog by keyword, backed by the API's `/products/search` endpoint.
4. **Filter by category** *(optional, implemented)* — a chip row backed by `/products/category-list`
   and `/products/category/{slug}`.

Loading and error states (with retry) are handled on both screens.

---

## 2. Architecture overview

The app follows **Clean Architecture** with a strict dependency direction
(`presentation → domain ← data`). All layers live in the shared module and are consumed as-is by
both platforms.

```
shared/src/commonMain/kotlin/com/example/assignment
├── data/                     # DATA LAYER — how data is fetched
│   ├── remote/
│   │   ├── HttpClientFactory.kt   # Ktor client + JSON config (platform engine injected)
│   │   ├── ProductApi.kt          # typed wrapper over dummyjson.com endpoints
│   │   └── dto/                   # @Serializable wire models
│   ├── mapper/ProductMapper.kt    # DTO → domain model
│   └── repository/ProductRepositoryImpl.kt
│
├── domain/                   # DOMAIN LAYER — business rules (no framework deps)
│   ├── model/Product.kt           # core business model
│   ├── repository/ProductRepository.kt   # abstraction (dependency inversion)
│   └── usecase/                   # GetProducts, SearchProducts, GetProductDetail,
│                                  #   GetCategories, GetProductsByCategory
│
├── presentation/             # PRESENTATION LAYER — Circuit screens
│   ├── navigation/Screens.kt      # Circuit Screen keys (expect/actual, see trade-offs)
│   ├── list/                      # ProductListState / Presenter / Ui
│   ├── detail/                    # ProductDetailState / Presenter / Ui
│   ├── components/CommonUi.kt     # shared composables (Coil image, rating, price format)
│   └── CircuitFactories.kt        # wires Screens → Presenters + UIs
│
├── di/                       # Koin modules + initKoin()
└── App.kt                    # Compose entry point (Coil + Circuit + Koin wiring)
```

### Tech stack

| Concern              | Library |
|----------------------|---------|
| Shared UI            | **Compose Multiplatform** (Material 3) |
| Presentation/nav     | **Circuit** (Slack) — presenters + navigable back stack |
| Dependency injection | **Koin** |
| Networking           | **Ktor Client** (OkHttp on Android, Darwin on iOS) |
| JSON                 | **kotlinx.serialization** |
| Image loading        | **Coil 3** (Ktor network fetcher, multiplatform) |
| Async                | **kotlinx.coroutines** |
| Testing              | kotlin.test + coroutines-test + Ktor **MockEngine** |

### Data flow (unidirectional)

```
Ui  ──(event via eventSink)──▶  Presenter  ──▶  UseCase  ──▶  Repository  ──▶  ProductApi (Ktor)
 ▲                                                                                     │
 └──────────────────────  CircuitUiState (recomposition)  ◀── DTO → domain mapping ◀──┘
```

The **Circuit presenter** owns transient screen state and calls the use cases. A use case returns a
`Result<T>` (via `runCatching`), so the presenter renders `loading / content / error` without
try–catch in the UI.

---

## 3. Build & run

### Prerequisites
- JDK 17
- Android SDK (`local.properties` should point `sdk.dir` at it)
- Xcode 15+ (for iOS)
- The included Gradle wrapper (Gradle 9.1)

### Android
```bash
# Build a debug APK
./gradlew :androidApp:assembleDebug

# Or open the project in Android Studio and run the `androidApp` configuration on a device/emulator.
```
The APK is written to `androidApp/build/outputs/apk/debug/`.

### iOS
```bash
# Produce the shared framework (also run automatically by the Xcode build phase)
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```
Then open **`iosApp/iosApp.xcodeproj`** in Xcode and run the `iosApp` scheme on a simulator or
device. (Compose UI is hosted via `MainViewController` in `ContentView.swift`.)

### Run the unit tests
```bash
# Shared tests on the JVM/Android host
./gradlew :shared:testAndroidHostTest

# Same shared tests compiled & run natively on the iOS simulator
./gradlew :shared:iosSimulatorArm64Test
```

---

## 4. Tests

Tests live in `shared/src/commonTest` and therefore run on **every** target (Android host + iOS):

- **`SearchProductsUseCaseTest`** — verifies the search use case against a hand-written fake
  repository: a blank query falls back to the full catalog, non-blank queries are trimmed and
  delegated to search, and repository errors surface as `Result.failure`.
- **`ProductRepositoryImplTest`** — drives the real repository + `ProductApi` + mappers through a
  Ktor **`MockEngine`**, asserting the JSON envelope is parsed, single-object detail responses
  default their missing fields, and the category string array is decoded — all without network access.

---

## 5. Trade-offs & assumptions

- **Circuit instead of `ViewModel` + `StateFlow`.** The task suggested `StateFlow` in a
  `ViewModel`; the chosen library, **Circuit**, replaces that pattern with a `@Composable
  present()` presenter whose returned `CircuitUiState` *is* the observable state (backed by Compose
  snapshot state). This satisfies the intent — unidirectional data flow with testable presentation
  logic — using Circuit's idiom rather than fighting it. The data/domain layers stay plain coroutine
  `suspend` functions, so swapping in a `StateFlow`-based ViewModel later would not touch them.
- **Manual Circuit factories wired with Koin.** Circuit's KSP code-gen only targets
  Dagger/Hilt/Anvil/kotlin-inject/Metro. With Koin we register `Presenter.Factory` / `Ui.Factory`
  by hand (`CircuitFactories.kt`) — a few lines, and it avoids an annotation-processing step.
- **`Screen` keys are `expect/actual` classes.** On Android, Circuit's `Screen` extends
  `Parcelable` (so the back stack survives process death); on iOS it does not. The `@Parcelize`
  plugin does not fire through a `typealias`, so the screens are declared `expect` in common with a
  `@Parcelize actual` on Android and a plain `actual` on iOS.
- **Category filter uses `/products/category-list`** (a flat `List<String>`) rather than the richer
  `/products/categories` object array, keeping parsing and the chip UI simple.
- **Search vs. category are mutually exclusive** — typing a query clears the selected category and
  vice-versa, which matches how the API exposes the two.
- **Minimal, unstyled Material 3 UI** (per "no fancy UI, just workable"). A few glyphs (★, ✕, ←)
  are used instead of the `material-icons` artifact, which is not published for this Compose
  Multiplatform version and would add a dependency for three icons.
- **Configuration cache is disabled** in `gradle.properties`: the Kotlin Multiplatform metadata
  transform task is not configuration-cache compatible with this AGP/Kotlin combination.
- **Result size is capped at 30 products** (`ProductApi.DEFAULT_LIMIT`) — enough for a prototype;
  pagination was intentionally left out of scope.
