# AGENTS — App Automation

Guidelines for creating **native mobile test scripts** using Appium Java in this repository.

For system design, see [`ARCHITECTURE.md`](ARCHITECTURE.md). For shared rules, see [`AGENTS.md`](AGENTS.md).

---

## Scope

| Aspect | Detail |
|--------|--------|
| **Product** | Native App (`app`) |
| **Driver** | Appium (`AppiumDriver`, Android UiAutomator2 / iOS XCUITest) |
| **Runner** | `AppTestRunner` / `AppFailedTestRunner` |
| **Maven profile** | `AppTests` |
| **Qase prefix** | `APP` |

---

## Key Paths

| Layer | Path |
|-------|------|
| Runner | `src/test/java/CucumberRunner/AppTestRunner.java` |
| Failed rerun | `src/test/java/CucumberRunner/AppFailedTestRunner.java` |
| Features | `src/test/java/Features/NativeApp/` |
| Step definitions | `src/test/java/StepDefinitions/NativeApp/` |
| Page objects | `src/main/java/PageObject/NativeApp/` |
| PO manager | `AppPOManager` |
| Base utilities | `src/main/java/utils/BaseTest.java` |
| Mobile helpers | `src/main/java/AbstractComponent/MobileAbstractComponents.java` |
| Mobile driver | `src/main/java/utils/app/MobileDriver.java` |
| App config | `src/main/java/utils/app/AppConfig.java` |
| Platform config | `src/main/java/utils/app/MobilePlatform.java` |
| Credentials | `src/test/java/Data/AppCredential.java` |
| Trade symbols | `src/test/java/Data/TradeSymbolConfig.java` |
| APK/IPA binaries | `src/main/resources/` |
| Hooks | `src/test/java/StepDefinitions/Hooks.java` |

---

## Feature Organization

```
src/test/java/Features/NativeApp/
├── trade/
├── aoApplication/
└── onboarding/
```

| Step definition package | Scope |
|-------------------------|-------|
| `StepDefinitions/NativeApp/login/` | Login and signup |
| `StepDefinitions/NativeApp/aoSteps/` | Account opening |
| `StepDefinitions/NativeApp/tradeSteps/` | Trading flows |
| `StepDefinitions/NativeApp/common/` | Shared app steps |

Feature files: `APP-<CaseId>.feature` (e.g. `APP-2989.feature`).

---

## Runtime Configuration

| Parameter | Source | Example values |
|-----------|--------|----------------|
| `product` | `-Dproduct=app` | `app` |
| `env` | `GlobalData.properties` / `-Denv` | `mt5uat`, `bauuat` |
| `entity` | `GlobalData.properties` / `-Dentity` | `EBL_MT5`, `EIEHK` |
| `platform` | `qase-nativeApp.properties` / `-Dplatform` | `ANDROID`, `IOS` |

`AppConfig` selects APK path and package name by `entity` + `env`.  
`MobileDriver` starts Appium on port 4723 and creates the session.

---

## Working Rules

1. Reuse existing mobile step and page object patterns before creating new structures.
2. **Appium only** for mobile — use `AppiumDriver` and `AppPOManager`.
3. Feature files: `APP-<CaseId>.feature` under `Features/NativeApp/<module>/`.
4. Launch the app in the first `@Given` step via `initAppDriver()`, then create `AppPOManager`.
5. Access pages **only through** `AppPOManager`. Register new page classes in the manager.
6. Use `AppCredential` for entity-scoped login credentials when appropriate.
7. Use `MobileAbstractComponents` for gestures, waits, and shared mobile interactions.
8. Use `TradeSymbolConfig` / `TradeSymbol.properties` for trading symbol data.
9. Tag scenarios with `@Test` plus module tags (`@Regression`, `@Smoke`, entity tags).
10. Do not break web automation flows when adding mobile tests.

### Step definition pattern

```java
driver = initAppDriver();
appPOManager = new AppPOManager(driver);
appPOManager.getAppLoginPage().fillCredential(username, password);
Assert.assertTrue(appPOManager.getAppHomePage().bottomButtonIsTapped());
```

---

## When User Provides Test Steps

1. Create/extend `.feature` file under `Features/NativeApp/<module>/`, named `APP-<id>.feature`.
2. Implement step definitions in the matching `StepDefinitions/NativeApp/` package.
3. Reuse existing step bindings when text matches exactly; otherwise create new bindings.
4. Add/update page object methods in `PageObject.NativeApp`.
5. Update `AppPOManager` if a new page class is introduced.
6. Include assertion steps, not only actions.
7. Keep runner compatibility: `glue = "StepDefinitions"`, `tags = "@Test"`.

---

## Input Checklist (Ask If Missing)

- **Module** (trade, account opening, onboarding, login)
- **Scenario name** and **Qase case ID** (`APP-<id>`)
- **Tags** (`@Test`, `@Regression`, `@Smoke`, entity tags)
- **Platform** (`ANDROID` / `IOS`)
- **Environment** and **entity**
- **Test data** (credentials, symbols, order values, etc.)
- **Expected assertions**

---

## Output Expectations

- Feature file, step definitions, page object methods, `AppPOManager` updates
- Runnable Maven command
- Reference prompt under `generated-prompts/APP-<id>_<scenarioTitleSafe>.md`

```bash
# Android
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EBL_MT5 -Dplatform=ANDROID

# iOS
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EIEHK -Dplatform=IOS
```

---

## Execution Policy

- Ensure Appium server and device/emulator are available before running.
- Do **not** execute Maven commands without explicit user approval.

---

## Definition of Done

1. Feature with `@Test` tag, named `APP-<CaseId>.feature` in the correct module folder.
2. Every step has an implemented step definition.
3. Step definitions delegate to `AppPOManager` methods (no stubs/TODO).
4. Assertions cover expected results.
5. Compile-ready with clean imports.
6. Maven command with `-PAppTests` and correct `-D` overrides provided.
7. Assumptions listed when test data was not provided.
8. Reference prompt file created under `generated-prompts/`.

---

## Default Behavior

If the user gives plain-language steps only:

1. Propose scenario title, Qase case ID (`APP-<id>`), module, platform, and tags.
2. Generate complete feature + steps + page objects.
3. List assumptions at the end.
