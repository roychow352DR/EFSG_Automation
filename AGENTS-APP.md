# AGENTS — Native app automation

Apply [AGENTS.md](AGENTS.md) first. Reviewed by static inspection against the working tree on **2026-10-08**. Use Appium Java for repository tests and Appium MCP for explicitly requested device inspection. See [ARCHITECTURE.md](ARCHITECTURE.md) for lifecycle and reporting details.

When delegated as `efsg-app`, follow the [specialist contract](AGENTS-ORCHESTRATOR.md#specialist-contract): work within assigned file ownership, report API/session dependencies to the orchestrator, and return changes and validation evidence. The orchestrator assigns exclusive device ownership and coordinates execution approval.

## Approval before changes

**Remain read-only until the user explicitly authorizes the presented scope with the exact lowercase standalone word `approve` or `approved`**, following [AGENTS.md](AGENTS.md#approval-before-changes). Inspect and propose the exact native feature, binding, page, capability/configuration, or device/application-state changes first. Requests to implement, "yes", "go ahead", mere mentions of the words, and agent assignments do not qualify. Before every mutation, verify the actual user approval and its scope; delegated briefs must include the exact quote, message reference when available, and approved files/actions. Reuse valid approval for the same unfinished scope; obtain fresh approval for new files/actions and stop further mutations if approval is withdrawn. If approval evidence is missing or unclear, return the proposal to the orchestrator, or directly to the user when invoked without a parent, and continue only read-only inspection. Device inspection approval covers only the requested inspection, not tests or trading workflows.

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
| `EIEHK` | `com.efsg.eiehktrading.android` | `com.efsg.eietrading.android_0.0.220-20261007.apk.zip` |

Both mapped archives are present in the reviewed checkout. A missing APK can fall back to an already installed package. Other Android entities are not mapped even when feature tags mention them. Both supported environments currently select the same artifact/package for a given entity. Verify the installed build/package before execution; an archive's presence is not evidence of device compatibility.

The iOS path uses `CopyMaster.app`, a hardcoded `iPhone 14 Pro` device name, and XCUITest options. It is not selected by entity/environment like Android. Many page methods are Android-only or have incomplete iOS branches; do not claim platform parity. Confirm the target simulator/device, app compatibility, and implemented iOS locators before proposing an executable iOS scenario.

The Java driver does not read arbitrary `-Dudid`, `-DappiumServerUrl`, or `-DnoReset` overrides. Do not advertise unsupported switches. Root `capabilities.json` and `wdio.android.conf.ts` do not configure the Java driver.

## Entity coverage from feature tags

**Every new or modified native test case derives its required entity coverage from its effective Gherkin tags.** Resolve tags per executable scenario/example, including inherited Feature, Rule, and Scenario/Scenario Outline tags and the applicable Examples tags. Tags are additive: a narrower tag does not remove an inherited entity tag. Do not combine unrelated scenarios' tags into every case's coverage.

- `@EIEHK` requires EIEHK coverage; `@EBL_MT5` requires EBL_MT5 coverage. When both apply, both entities are required. When only one applies, only that entity is required.
- Classification tags such as `@App`, `@Trade`, `@Smoke`, and `@Test` do not select an entity. Additional entity declarations require a verified mapping. Record ambiguous or unmapped declarations such as `@XPro` as unresolved until their meaning and mapping are established; do not silently discard or map them to another entity.
- If entity tags are missing or unresolved, report the gap and request clarification. Do not infer coverage from the configured default or assume both EIEHK and EBL_MT5. Continue useful read-only work and approved work for resolved entities, but do not execute an unresolved target or claim complete tagged coverage. Establish entity tags in the proposal for a new case; do not silently add/remove tags to change the approved coverage.

1. **Trace tagged coverage through the complete case.** Assess setup/login, navigation, element identity and location, interactions, assertions, and cleanup for each required entity, including reused page methods/helpers. For a shared-helper change, assess the union of entity requirements from all affected callers' effective tags while preserving each caller's own coverage set.
2. **Establish each runtime target.** Resolve the runtime entity through the existing configuration path and check its entity/environment/platform mapping. Each proposed run's explicit `-Dentity` must match an effective entity tag on every selected scenario/example and a supported mapping. Report mismatches before execution; do not silently retag cases or switch entities. During authorized setup, verify the package/bundle, installed build, and screen state before acting on test controls. Tags specify required coverage but do not launch or select the app. Reject unknown entities or unsupported combinations clearly; do not treat every non-EIEHK value as EBL_MT5. Android support does not establish iOS support.
3. **Keep UI differences in page objects/helpers.** Select entity-specific locators, navigation paths, and interactions using the resolved runtime entity, accessed through `AppPOManager`. Reuse common behavior only where evidence supports equivalence across the required tagged entities; do not duplicate features or step definitions solely because screen layouts differ. Inspect reused method bodies and their side effects before declaring the required paths supported.
4. **Locate controls by identity in the correct screen/record.** Prefer observed accessibility IDs, resource IDs, and scoped labels/containers. Do not assume that element indexes, text order, screen ratios, or coordinates from one entity identify the same control in another. Any necessary positional fallback must be justified by the target entity/build's observed hierarchy or bounds and confirm the intended control and destination. Refresh the hierarchy after navigation, use bounded scrolling/waits, and verify the resulting screen/state before a subsequent action or submission.
5. **Adapt assertions and cleanup as well as locators.** Check entity-specific labels, row/card grouping, field meanings, direction, date/number formats, precision, and applicable product rules against independent expectations. Preserve the same business intent without forcing identical UI structure. Scope interactions and cleanup to the intended account/order/position; never hide a missing control or unsupported path with a silent skip, no-op, fallback expected value, or a success result.
6. **State coverage separately for the tagged set.** Record each resolved entity's implementation status and UI-path evidence, together with unresolved declarations and approved limitations. Implement the tagged variants within the approved scope. If an entity tag conflicts with actual case applicability, report the conflict and obtain clarification rather than ignoring the tag. Missing implementation, build/device access, or locator evidence must be reported as blocked or unverified. A requested run or implementation limited to a subset of tagged entities must be reported as partial coverage; do not claim full tagged coverage while required paths remain unresolved or unimplemented.
7. **Separate evidence from execution.** Identify the entity/platform/build for source-inspection evidence, observed hierarchy/locator evidence, and actual runtime results; distinguish these statuses. One entity's observed screen or passing run does not validate another. Missing evidence does not authorize Appium MCP inspection, test execution, or trading workflows; request the required authorization and report the remaining gap.
8. **Review and validate the tagged paths.** Supply the independent reviewer with effective tags, resolved entity requirements, shared-helper impacts, evidence, and limitations. Prepare a separate scoped Maven command for each supported tagged entity using explicit configuration and feature/tag selection that satisfies the runtime-target rule above. Multiple entity tags do not create multiple executions. Execute only separately approved commands, sequentially under the existing device-ownership rules, and report results per entity.

## Java launch and session behavior

1. `loginSteps`' `the user launch the app` calls `initAppDriver()`, builds `AppPOManager`, and checks Home readiness.
2. `BaseTest.initializeDriver()` resolves product/entity/environment/platform. It can reuse a live Android session when called again; this is not a session pool or an MCP attachment mechanism.
3. `MobileDriver` checks `http://127.0.0.1:4723/status`, reuses a responding server, or starts one through `AppiumServiceBuilder`. It tries `/usr/local/lib/node_modules/appium/build/lib/main.js` when present.
4. Android uses `com.mfinance.copymaster.MainActivity`, wildcard activity waiting, zero implicit wait, `noReset=true`, `fullReset=false`, and automatic permission handling. A supplied APK ZIP is extracted to `target/extracted-apk`, skipping macOS metadata and selecting the largest APK entry.
5. Android session creation retries selected UiAutomator2/proxy failures up to three times. A readiness timeout is propagated. `BaseTest` separately retries session creation once for a recording proxy failure.
6. `waitUntilLaunchComplete(Duration)` recovers foreground state, handles recognized launch blockers, and waits for Home/login chrome. It does **not** establish that market data or content cards finished loading.
7. Android recording starts after creation. Active teardown first resets captured trading state, then attempts recording save, app termination, and driver quit. A recording failure can prevent driver cleanup; these are not unconditional cleanup guarantees. iOS also reaches cleanup that uses the Android package mapping. There is no corresponding automatic local Appium service stop.

Do not rely on a clean login state with `noReset=true`. For Android, reuse `AppLoginPage.loginAs(...)` for its guest-home, Me-tab, and leftover-session handling. It returns without logging in for non-Android drivers, so it does not implement iOS login. Prefer the implemented `the user logs in on the App login page` and `the l2 user logs in on the App login page` bindings, which read entity-scoped data from `AppCredential`, over embedding credentials in features or reference prompts.

## Page objects and element handling

`AppPOManager` owns the login, welcome, Home, signup, Me, Markets, footer, client agreement, portfolio, trade view, instrument details, edit/close position, modify order, position details, settings, pending-order details, deposit, and withdrawal page/helper instances. `BiometricsPage` also exists outside those manager accessors. Manager registration alone does not establish complete funding-flow coverage.

- From steps, use the manager for page access. Reuse `getAppFooter().tapFooterButton(...)` for navigation instead of copying `AppHomePage`'s older footer selectors.
- Prefer observed accessibility IDs, then resource IDs, native UiSelector/iOS selectors, and scoped XPath. Avoid copying full hierarchy paths or fixed coordinates into new scenarios.
- Treat historical Home/footer accessibility IDs and selector examples as candidates until observed on the target build. Record build/screen evidence for new inspection claims, and use bounded scrolling for off-screen controls. An exposed footer does not prove the content loaded.
- Reuse `tapVisible`, `tapBottomMost`, `tapElement`, and bounded swipe helpers. These handle React Native elements whose reported location differs from their Android `bounds`.
- `typeWithAndroidKeys()` has a limited character map and numeric-field recovery; it is not a general text-entry method for arbitrary credentials.
- Prefer explicit, bounded waits on the expected next screen/state. Existing helpers include long waits and methods with side effects: `waitUntilElementVisible(WebElement)` also clicks, unlike its `By` overload.
- Refresh `GetPageElement` snapshots after navigation or a relevant state change. Use its label mapping and normalization for currencies, volumes, prices, and detail fields. Do not replace absent UI data with the expected value.

## Trading flow and assertions

`TradeRecord` coordinates market/pending-order setup, direction, lot size, stop/limit prices, TP/SL controls, validity, and confirmation through the manager. Reuse its workflows instead of duplicating order-placement sequences.

- Metadata currently covers `XAUUSD`, `XAGUSD`, `RKGCNH`, and `HKGHKD`, with entity-specific lot bounds, step sizes, and margin settings. The EIEHK/XAUUSD confirmation-margin path uses observed confirmation price/volume and configured contract size/rate. Inspect the selected entity/symbol/account formula rather than generalizing one entity's margin calculation or lot step.
- Preserve entity-specific ticket handling: pending-order switches, separate target-profit controls, Buy-at price entry, and content-description chips can differ. Verify locator and coordinate fallbacks cannot select a different control; source branches alone do not prove the current build's behavior.
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
6. Record entity, environment, platform, installed package/bundle and build, screen state, locator strategy/value, and evidence for each authorized target. Keep observations separate by entity and identify the case's effective entity tags when inspection supports a test case. Distinguish loaded controls from placeholders and unverified destinations. Do not store session-specific element UUIDs as permanent locators.

MCP discovery is not a Maven test result. Avoid simultaneous MCP/Java sessions competing for the same device. Local SDK/driver installation or a successful previous session does not guarantee readiness for a different device/build.

## Generation and validation

Create `APP-<CaseId>.feature` in the appropriate native module, with `@Test` and relevant tags. Reuse implemented bindings in `loginSteps`, `appCommonSteps`, `tradeSteps`, and `aoSteps`. Some bindings are empty or only delay execution (for example the Markets-button display step and biometric-skip step); inspect and complete a reused path when required by the new case.

Establish the case ID, platform, entity/environment, target build/device, test data, expected assertions, and any order/position cleanup the scenario requires. Deliver all implemented code, the reference prompt, assumptions, and exact command required by [AGENTS.md](AGENTS.md).

For every case, record its effective entity tags and the resulting coverage set in the reference prompt and final report. Include a separate coverage entry for each resolved tagged entity, plus any missing, ambiguous, or unsupported declarations. Each entity entry must identify the target environment/platform/package/build (or explicitly unknown), applicable UI differences and page/helper paths, implementation status and approved limitations, source/device evidence, and runtime result (`not run`, `passed`, or `failed`, with evidence when run). Include the scoped command for each supported tagged target; do not invent a runnable command for an unsupported combination. Report unresolved or partial tagged coverage to the reviewer and user.

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
