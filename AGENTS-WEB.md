# AGENTS — Web automation

Apply [AGENTS.md](AGENTS.md) first. Reviewed against the working tree on **2026-09-21**. New web scenarios use **Playwright Java**. See [ARCHITECTURE.md](ARCHITECTURE.md) for the implementation and [AGENTS-API.md](AGENTS-API.md) for backend assertions.

When delegated as `efsg-web`, follow the [specialist contract](AGENTS-ORCHESTRATOR.md#specialist-contract): work within assigned file ownership, report backend dependencies to the orchestrator, and return changes and validation evidence. The orchestrator coordinates shared files and execution approval.

## Product and source mapping

| Product setting | Features under `src/test/java/Features` | Page objects under `src/main/java/PageObject` | Manager | Qase prefix |
|---|---|---|---|---|
| `adminPortal` | `AdminPortal/` | `AdminPortalPW/` | `AOPOManager` | `AP` |
| `mio` | `MIO/` | `MIOadmin/` | `MIOPOManager` | `MIO` |

Admin Portal modules map as follows, relative to the respective feature/step roots:

- `AdminPortal/login` → `StepDefinitions/AdminPortal/login`
- `AdminPortal/aoApplication` → `StepDefinitions/AdminPortal/aoApplicationSteps`
- `AdminPortal/cm` → `StepDefinitions/AdminPortal/cm`
- `AdminPortal/aoBlacklist` → `StepDefinitions/AdminPortal/aoBlacklistSteps`
- `AdminPortal/aoUserManagement` → `StepDefinitions/AdminPortal/aoUserManagementSteps`
- `AdminPortal/aoRolesPermission` → `StepDefinitions/AdminPortal/aoRolesPermissionSteps`
- `MIO/login` → `StepDefinitions/MIO/login`
- `MIO/depositManagement` → `StepDefinitions/MIO/transactionManagement`

Use [BaseTest.java](src/main/java/utils/BaseTest.java) for initialization, [AbstractComponentsPW.java](src/main/java/AbstractComponent/AbstractComponentsPW.java) for shared helpers, and [Hooks.java](src/test/java/StepDefinitions/Hooks.java) for existing teardown behavior. Legacy `PageObject/AdminPortal` and `AbstractComponents` remain for compatibility; do not add new Selenium web page objects.

## Runtime and routing

`initializePage()` resolves `product`, `env`, `entity`, and `browser` from `src/main/java/DataResources/GlobalData.properties`, with system-property overrides. It creates Playwright, launches a browser, creates a video-recording context/page, navigates through `setDomain()`, and waits for `NETWORKIDLE`.

Browser values permitted for execution are `chrome`, `firefox`, `webkit`, and `edge`. `chrome` launches Playwright Chromium; `edge` uses Chromium's `msedge` channel. The implementation recognizes headless names, but repository policy requires headed execution.

Web environment mappings in `BaseTest.setDomain()`:

| Product | Environment | Login URL |
|---|---|---|
| Admin Portal | `bausit` | `https://d13ckj22o5rgah.cloudfront.net/login` |
| Admin Portal | `bauuat` | `https://bau-uat-aocm-ap.empfs.net/login` |
| Admin Portal | `mt5sit` | `https://d3lyp6p86bdjbb.cloudfront.net/login` |
| Admin Portal | `mt5uat`, `egmuat` | `https://uat-aocm-ap.empfs.net/login` |
| MIO | `bausit`, `bauuat` | `https://d27ekljjcs6mcs.cloudfront.net/login` |
| MIO | `mt5uat` | `https://uat-mt5mio-ap.empfs.net/login` |

The method accepts `entity` but does not use it to select the web URL. Entity still affects test data, filters, and Qase configuration. API/CRM mappings differ from these browser URLs; check them separately for mixed scenarios.

## Implementation workflow

1. Search the exact Gherkin text in `src/test/java/StepDefinitions` and inspect the binding body. Cucumber glue is shared globally; avoid duplicate expressions across modules.
2. Initialize the page in the existing first `Given` and create the correct manager. Reuse `AOLoginSteps` or `MIOLoginSteps` entry flows instead of introducing competing sessions.
3. Put UI actions and locator definitions in the product's page objects. Access pages from steps through the manager; register each new page there.
4. Prefer `getByRole`, `getByLabel`, stable test IDs, or exact/scoped text. Scope repeated labels to a dialog, row, or page section.
5. Prefer Playwright's locator auto-wait and retrying assertions. Wait for a concrete result/state when needed; do not add arbitrary sleeps or treat network-idle alone as a business assertion.
6. Use `AbstractComponentsPW.userinfoList()`, `blacklistInfoList()`, and existing AO/CM data helpers where appropriate. Cache generated data for the scenario: repeated calls can generate different random values.
7. Keep expected results in assertion steps. Use Playwright `assertThat` for locator state and TestNG assertions for returned values. Verify record identity before making a backend assertion.
8. For API/SQL work, establish the logged-in page, initialized environment/entity, token, and UI-derived identity required by the backend bindings.

Existing initialization pattern:

```java
page = initializePage();
aopoManager = new AOPOManager(page);
aopoManager.getAdminLoginPage().fillCredential(username, password);
aopoManager.getAdminLoginPage().clickLogin();
assertThat(aopoManager.getApplicationListPage().getMenuText()).isVisible();
```

Use existing framework imports and supplied test data. For MIO, the shared manager is `MIOLoginSteps.mioPoManager`; its login accessor is `getLoginPage()`.

## Existing implementation limits

- A profile or `-Dproduct` does not select only that product's features: normal runners start at the shared feature root. Always scope the command to a product folder or case file and an appropriate tag filter.
- `BaseTest.page`, `context`, `browser`, managers, and captured data are shared/static. Do not assume parallel scenario safety from Surefire's configured parallel options.
- `initializePage()` catches initialization errors and can return a null/stale page. Diagnose the first setup error before adding locator retries.
- The blank-credentials binding in `AOLoginSteps` still calls a legacy Selenium `login` field; do not assume every web step has completed the Playwright migration.
- Active `@After` resets the Admin Portal menu where applicable and closes the page/context/browser. It does not close the locally created Playwright owner.
- Qase hooks and per-step failure screenshot capture are currently disabled. Video capture is configured, but preservation/upload depends on teardown and cleanup flags. Do not promise screenshots, Qase results, or generated HTML merely because an output directory exists.

Preserve unrelated legacy behavior. If a requested scenario depends on an incomplete binding, implement that path and explain the change rather than silently leaving it ineffective.

## Features and deliverables

Use `AP-<CaseId>.feature` or `MIO-<CaseId>.feature` in the correct module. New default-runner scenarios require `@Test`; add relevant product, module, suite, and entity tags. Existing tags differ, so inspect the selected feature before writing its command. Case IDs for Qase come from filenames, not tags.

Establish product/module, case ID/title, environment/entity, browser, test data, and expected assertions from the request and existing context. Generate all needed feature, binding, page-object, and manager changes plus `generated-prompts/<featureId>_<scenarioTitleSafe>.md`. List assumptions and validation performed without exposing credentials.

Examples for existing login cases, **only after explicit execution approval**:

```bash
# Admin Portal: this existing case has @Login, not @Test
mvn test -PWebTests -Dproduct=adminPortal -Denv=bauuat -Dentity=EBL_MT5 -Dbrowser=chrome \
  -Dcucumber.features=src/test/java/Features/AdminPortal/login/AP-560.feature \
  -Dcucumber.filter.tags="@Login"

# MIO: explicitly select its feature and existing tag
mvn test -PWebTests -Dproduct=mio -Denv=mt5uat -Dentity=EBL_MT5 -Dbrowser=chrome \
  -Dcucumber.features=src/test/java/Features/MIO/login/MIO-429.feature \
  -Dcucumber.filter.tags="@MIO"

# Rerun recorded web failures with matching original product/environment
mvn test -PWebFailedTests -Dproduct=adminPortal -Denv=bauuat -Dentity=EBL_MT5 -Dbrowser=chrome
```

Do not run `clean` before rerunning, or override the rerun feature source with the normal feature root. All execution remains subject to [AGENTS.md](AGENTS.md): headed web only and explicit approval before Maven/test commands.
