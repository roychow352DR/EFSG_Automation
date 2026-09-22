# AGENTS — Native app automation

Apply [AGENTS.md](AGENTS.md) first. Reviewed against the working tree on **2026-09-21**. Use Appium Java for repository tests and Appium MCP for explicitly requested device inspection. See [ARCHITECTURE.md](ARCHITECTURE.md) for lifecycle and reporting details.

When delegated as `efsg-app`, follow the [specialist contract](AGENTS-ORCHESTRATOR.md#specialist-contract): work within assigned file ownership, report API/session dependencies to the orchestrator, and return changes and validation evidence. The orchestrator assigns exclusive device ownership and coordinates execution approval.

## Scope and source map

- Product: `app`; feature prefix: `APP`; profiles: `AppTests` and `AppFailedTests`.
- Features: `src/test/java/Features/NativeApp/{trade,aoApplication,onboarding}/`.
- Bindings: `src/test/java/StepDefinitions/NativeApp/{login,common,tradeSteps,aoSteps}/`.
- Page objects: `src/main/java/PageObject/NativeApp/`, accessed from steps through [AppPOManager.java](src/main/java/PageObject/NativeApp/AppPOManager.java).
- Initialization: [BaseTest.java](src/main/java/utils/BaseTest.java), [MobileDriver.java](src/main/java/utils/app/MobileDriver.java), [MobilePlatform.java](src/main/java/utils/app/MobilePlatform.java), and [AppConfig.java](src/main/java/utils/app/AppConfig.java).
- Gestures/readiness/normalization: [MobileAbstractComponents.java](src/main/java/AbstractComponent/MobileAbstractComponents.java).
- Cached hierarchy extraction: [GetPageElement.java](src/main/java/utils/GetPageElement.java).
- Trading setup/metadata: [TradeRecord.java](src/test/java/Data/TradeRecord.java), [TradeSymbolConfig.java](src/test/java/Data/TradeSymbolConfig.java), and `DataResources/TradeSymbol.properties` under `src/main/java`.
- Entity-scoped credentials: `src/test/java/Data/AppCredential.java`; do not copy its values into generated documentation.

## Configuration and actual platform coverage

Use explicit `-Dproduct=app`, `-Denv`, `-Dentity`, and uppercase `-Dplatform=ANDROID` or `IOS`. `-Dsymbol` overrides the default symbol used by `TradeRecord`. Symbol precision, lot bounds, contract size, and margin defaults are implemented in `TradeSymbolConfig`, not the properties file.

Android `AppConfig` currently maps `bauuat` and `mt5uat` for:

| Entity | Package | Artifact under `src/main/resources` |
|---|---|---|
| `EBL_MT5` | `com.emperorfs.ebltrading.android` | `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip` |
| `EIEHK` | `com.efsg.eiehktrading.android_uat` | `com.efsg.eiehktrading.android_uat-0.0.214-0805.apk` |

The EIEHK APK is absent from the reviewed checkout. A missing APK can fall back to an already installed package. Other Android entities are not mapped even when feature tags mention them. Both supported environments currently select the same artifact/package for a given entity.

The iOS path uses `CopyMaster.app`, a hardcoded `iPhone 14 Pro` device name, and XCUITest options. It is not selected by entity/environment like Android. Many page methods are Android-only or have incomplete iOS branches; do not claim platform parity. Confirm the target simulator/device, app compatibility, and implemented iOS locators before proposing an executable iOS scenario.

The Java driver does not read arbitrary `-Dudid`, `-DappiumServerUrl`, or `-DnoReset` overrides. Do not advertise unsupported switches. Root `capabilities.json` and `wdio.android.conf.ts` do not configure the Java driver.

## Java launch and session behavior

1. `loginSteps`' `the user launch the app` calls `initAppDriver()`, builds `AppPOManager`, and checks Home readiness.
2. `BaseTest.initializeDriver()` resolves product/entity/environment/platform. It can reuse a live Android session when called again; this is not a session pool or an MCP attachment mechanism.
3. `MobileDriver` checks `http://127.0.0.1:4723/status`, reuses a responding server, or starts one through `AppiumServiceBuilder`. It tries `/usr/local/lib/node_modules/appium/build/lib/main.js` when present.
4. Android uses `com.mfinance.copymaster.MainActivity`, wildcard activity waiting, zero implicit wait, `noReset=true`, `fullReset=false`, and automatic permission handling. A supplied APK ZIP is extracted to `target/extracted-apk`, skipping macOS metadata and selecting the largest APK entry.
5. Android session creation retries selected UiAutomator2/proxy failures up to three times. A readiness timeout is propagated. `BaseTest` separately retries session creation once for a recording proxy failure.
6. `waitUntilLaunchComplete(Duration)` recovers foreground state, handles recognized launch blockers, and waits for Home/login chrome. It does **not** establish that market data or content cards finished loading.
7. Android recording starts after creation. Active teardown stops/saves recording, terminates the configured app, quits the driver, and resets captured trading state. There is no corresponding automatic local Appium service stop.

Do not rely on a clean login state with `noReset=true`. Reuse `AppLoginPage.loginAs(...)` for its guest-home, Me-tab, and leftover-session handling when login is part of the requested scenario.

## Page objects and element handling

`AppPOManager` owns 17 page/helper instances: login, welcome, Home, signup, Me, Markets, footer, client agreement, portfolio, trade view, instrument details, edit/close position, modify order, position details, settings, and pending-order details. `BiometricsPage` also exists outside those manager accessors.

- From steps, use the manager for page access. Reuse `getAppFooter().tapFooterButton(...)` for navigation instead of copying `AppHomePage`'s older footer selectors.
- Prefer observed accessibility IDs, then resource IDs, native UiSelector/iOS selectors, and scoped XPath. Avoid copying full hierarchy paths or fixed coordinates into new scenarios.
- Recent Android Home inspection exposed `Home`, `Markets`, `Portfolio`, `Me`, `Highlight`, and `Recently viewed` accessibility IDs. Reconfirm against the target build; the two product filters require scrolling. An exposed footer does not prove the content loaded.
- Reuse `tapVisible`, `tapBottomMost`, `tapElement`, and bounded swipe helpers. These handle React Native elements whose reported location differs from their Android `bounds`.
- `typeWithAndroidKeys()` has a limited character map and numeric-field recovery; it is not a general text-entry method for arbitrary credentials.
- Prefer explicit, bounded waits on the expected next screen/state. Existing helpers include long waits and methods with side effects: `waitUntilElementVisible(WebElement)` also clicks, unlike its `By` overload.
- Refresh `GetPageElement` snapshots after navigation or a relevant state change. Use its label mapping and normalization for currencies, volumes, prices, and detail fields. Do not replace absent UI data with the expected value.

## Trading flow and assertions

`TradeRecord` coordinates market/pending-order setup, direction, lot size, stop/limit prices, TP/SL controls, validity, and confirmation through the manager. Reuse its workflows instead of duplicating order-placement sequences.

- Metadata currently covers `XAUUSD`, `XAGUSD`, `RKGCNH`, and `HKGHKD`. Confirm expectations for the selected symbol and account rather than assuming all entities share the same trading setup.
- Use `placePendingOrderWithValiditySelected(...)` when validity matters. Current examples are [APP-1494.feature](src/test/java/Features/NativeApp/trade/APP-1494.feature) for Today and [APP-1495.feature](src/test/java/Features/NativeApp/trade/APP-1495.feature) for GTC.
- `AppPendingOrderDetailsPage.getDetailValue()` can open details from the portfolio, refresh its hierarchy snapshot, resolve product names, and normalize values. `getValidationValue("Validity")` reads the captured `AppInstrumentDetailsPage.validity`.
- Compare independently observed UI values to captured selections/calculated expectations. Assert that required values are present before comparing them; two nulls must not constitute a successful check.
- Several existing `Then` steps close positions or cancel orders after asserting. In particular, the pending-order-detail value step cancels the order. Do not chain multiple such assertions assuming the details remain open.
- Captured prices, directions, counts, and selected values are often static. Preserve reset calls in the five trading pages and `TradeRecord.isOpenPosition`; teardown does not reset every static field, including `TradeSymbolConfig.isInitialMarginZero`.
- `Data.PositionDetail` contains an alternate Tess4J/OCR reader requiring English tessdata/native Tesseract support. The main page readers use `GetPageElement`; do not introduce OCR where an accessible locator or hierarchy value works.

## Appium MCP inspection workflow

For an explicitly requested inspection:

1. For a local target, discover devices with `select_device` for the requested platform and select the intended device. Prepare iOS simulators with `prepare_ios_simulator` before session creation; follow tool-provided WDA preparation for physical iOS devices. If the user supplies a remote server URL, skip local discovery/preparation and use the requested remote capabilities.
2. List/reuse a suitable managed session or create one with `appium_session_management`. For local embedded mode, omit `remoteServerUrl`; use a remote URL only when the user supplies it. The MCP session is separate from Java's port-4723 session.
3. Identify the Android package from `AppConfig` and the installed app, or establish the correct iOS bundle ID. Preserve data with `noReset=true` for inspection; do not reinstall or clear app data unnecessarily.
4. Launch with `appium_app_lifecycle`, verify foreground state, then inspect with `appium_get_page_source`, `generate_locators`, and `appium_screenshot` as needed.
5. Validate promising selectors with `appium_find_element`. Use `appium_gesture` for necessary navigation and bounded scrolling to off-screen targets.
6. Record platform, package, screen state, locator strategy/value, and evidence. Distinguish loaded controls from placeholders and unverified destinations. Do not store session-specific element UUIDs as permanent locators.

MCP discovery is not a Maven test result. Avoid simultaneous MCP/Java sessions competing for the same device. Local SDK/driver installation or a successful previous session does not guarantee readiness for a different device/build.

## Generation and validation

Create `APP-<CaseId>.feature` in the appropriate native module, with `@Test` and relevant tags. Reuse implemented bindings in `loginSteps`, `appCommonSteps`, `tradeSteps`, and `aoSteps`. Some bindings are empty or only delay execution (for example the Markets-button display step and biometric-skip step); inspect and complete a reused path when required by the new case.

Establish the case ID, platform, entity/environment, target build/device, test data, expected assertions, and any order/position cleanup the scenario requires. Deliver all implemented code, the reference prompt, assumptions, and exact command required by [AGENTS.md](AGENTS.md).

Examples, **only after explicit execution approval**:

```bash
# A single existing Android GTC case
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-1495.feature \
  -Dcucumber.filter.tags="@App"

# Rerun the recorded native failures; preserve target/app_failed_scenarios.txt
mvn test -PAppFailedTests -Dproduct=app -Denv=mt5uat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD
```

Keep reruns on the same device/build and relevant configuration. Do not change runners, Qase hooks, or unrelated web/API flows to make a new app case run.
