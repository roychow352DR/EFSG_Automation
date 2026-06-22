# AGENTS — Web Automation

Guidelines for creating **web UI test scripts** using Playwright Java in this repository.

For system design, see [`ARCHITECTURE.md`](ARCHITECTURE.md). For shared rules, see [`AGENTS.md`](AGENTS.md).

---

## Scope

| Aspect | Detail |
|--------|--------|
| **Products** | Admin Portal (`adminPortal`), MIO Admin (`mio`) |
| **Driver** | Playwright (`Page`, `Locator`, `BrowserContext`) |
| **Runner** | `WebTestRunner` / `WebFailedTestRunner` |
| **Maven profile** | `WebTests` / `WebFailedTests` |
| **Legacy** | Selenium (`PageObject.AdminPortal`) — do **not** extend |

---

## Key Paths

| Layer | Path |
|-------|------|
| Runner | `src/test/java/CucumberRunner/WebTestRunner.java` |
| Failed rerun | `src/test/java/CucumberRunner/WebFailedTestRunner.java` |
| Features | `src/test/java/Features/AdminPortal/`, `Features/MIO/` |
| Step definitions | `src/test/java/StepDefinitions/AdminPortal/`, `StepDefinitions/MIO/` |
| Page objects | `src/main/java/PageObject/AdminPortalPW/`, `PageObject/MIOadmin/` |
| PO managers | `AOPOManager`, `MIOPOManager` |
| Base utilities | `src/main/java/utils/BaseTest.java` |
| Shared helpers | `src/main/java/AbstractComponent/AbstractComponentsPW.java` |
| Hooks | `src/test/java/StepDefinitions/Hooks.java` |

---

## Product Mapping

| Product | `-Dproduct` | PO package | PO manager | Qase prefix | Feature folder |
|---------|-------------|------------|------------|-------------|----------------|
| Admin Portal | `adminPortal` | `PageObject.AdminPortalPW` | `AOPOManager` | `AP` | `Features/AdminPortal/` |
| MIO Admin | `mio` | `PageObject.MIOadmin` | `MIOPOManager` | `MIO` | `Features/MIO/` |

### Admin Portal modules

| Feature folder | Step definitions |
|----------------|------------------|
| `Features/AdminPortal/login/` | `StepDefinitions/AdminPortal/login/` |
| `Features/AdminPortal/aoApplication/` | `StepDefinitions/AdminPortal/aoApplicationSteps/` |
| `Features/AdminPortal/cm/` | `StepDefinitions/AdminPortal/cm/` |
| `Features/AdminPortal/aoBlacklist/` | `StepDefinitions/AdminPortal/aoBlacklistSteps/` |
| `Features/AdminPortal/aoUserManagement/` | `StepDefinitions/AdminPortal/aoUserManagementSteps/` |
| `Features/AdminPortal/aoRolesPermission/` | `StepDefinitions/AdminPortal/aoRolesPermissionSteps/` |

### MIO modules

| Feature folder | Step definitions |
|----------------|------------------|
| `Features/MIO/login/` | `StepDefinitions/MIO/login/` |
| `Features/MIO/` (transaction) | `StepDefinitions/MIO/transactionManagement/` |

---

## Environment URLs

Resolved by `BaseTest.setDomain(env, product, entity)` during `initializePage()`.

**Admin Portal** (`product=adminPortal`):

| `env` | URL |
|-------|-----|
| `bausit` | `https://d13ckj22o5rgah.cloudfront.net/login` |
| `bauuat` | `https://bau-uat-aocm-ap.empfs.net/login` |
| `mt5sit` | `https://d3lyp6p86bdjbb.cloudfront.net/login` |
| `mt5uat` | `https://uat-aocm-ap.empfs.net/login` |
| `egmuat` | `https://uat-aocm-ap.empfs.net/login` |

**MIO Admin** (`product=mio`):

| `env` | URL |
|-------|-----|
| `bausit` / `bauuat` | `https://d27ekljjcs6mcs.cloudfront.net/login` |
| `mt5uat` | `https://uat-mt5mio-ap.empfs.net/login` |

System properties (`-Denv`, `-Dentity`, `-Dbrowser`) override `GlobalData.properties`.

---

## Working Rules

1. Reuse existing framework patterns before creating new structures.
2. **Playwright only** — use `Page`, `Locator`, `PlaywrightAssertions.assertThat(...)`. Do not add Selenium page objects.
3. Feature files: `<QaseProject>-<CaseId>.feature` (e.g. `AP-141.feature`).
4. In the first `@Given` step: `page = initializePage()`, then create the PO manager.
5. Access pages **only through** `AOPOManager` or `MIOPOManager`. Register new page classes in the manager.
6. Prefer robust locators: `getByRole`, `getByLabel`, `getByTestId`, stable text.
7. Prefer Playwright auto-wait; add explicit waits only when needed.
8. Use `AbstractComponentsPW.userinfoList()` for randomized test data.
9. Tag scenarios with `@Test` (required) plus module/entity tags (`@Regression`, `@Smoke`, `@EBL_MT5`, `@CM`, etc.).
10. For API or DB assertions in the same scenario, reuse steps from `BackendSteps` — see [`AGENTS-API.md`](AGENTS-API.md).

### Step definition pattern

```java
page = initializePage();
aopoManager = new AOPOManager(page);
aopoManager.getAdminLoginPage().fillCredential(username, password);
assertThat(aopoManager.getApplicationListPage().getMenuText()).isVisible();
```

---

## When User Provides Test Steps

1. Create/extend `.feature` file under `Features/<Product>/<module>/`, named `<QaseProject>-<id>.feature`.
2. Implement step definitions in the matching `StepDefinitions/<product>/` package.
3. Reuse existing step bindings when text matches exactly; otherwise create new bindings.
4. Add/update page object methods in `AdminPortalPW` or `MIOadmin`.
5. Update `AOPOManager` or `MIOPOManager` if a new page class is introduced.
6. Include assertion steps, not only actions.
7. Keep runner compatibility: `glue = "StepDefinitions"`, `tags = "@Test"`.

---

## Input Checklist (Ask If Missing)

- **Product** (`adminPortal` or `mio`)
- **Module/page** (login, customer management, deposit management, etc.)
- **Scenario name** and **Qase case ID**
- **Tags** (`@Test`, `@Regression`, `@Smoke`, entity tags)
- **Test data** (username, password, client type, search text, etc.)
- **Expected assertions**
- **Environment** (`bauuat`, `mt5uat`, etc.) and **entity** (`EBL_MT5`, `EIEHK`, `XPro`, `EGM`)
- **Browser** (`chrome`, `firefox`, `edge`, `webkit`)

---

## Output Expectations

- Feature file, step definitions, page object methods, PO manager updates
- Runnable Maven command (headed mode)
- Reference prompt under `generated-prompts/<featureId>_<scenarioTitleSafe>.md`

```bash
# Admin Portal
mvn test -PWebTests -Dproduct=adminPortal -Denv=bauuat -Dentity=EBL_MT5 -Dbrowser=chrome

# MIO Admin
mvn test -PWebTests -Dproduct=mio -Denv=mt5uat -Dbrowser=chrome

# Rerun failed web tests
mvn test -PWebFailedTests -Dproduct=adminPortal -Denv=bauuat -Dentity=EBL_MT5 -Dbrowser=chrome
```

---

## Execution Policy

- Run in **headed mode** only (no `*-headless`).
- Do **not** execute Maven commands without explicit user approval.

---

## Definition of Done

1. Feature with `@Test` tag, correct filename and folder.
2. Every step has an implemented step definition.
3. Step definitions delegate to PO manager methods (no stubs/TODO).
4. Assertions cover expected results.
5. Compile-ready with clean imports.
6. Headed Maven command provided.
7. Assumptions listed when test data was not provided.
8. Reference prompt file created under `generated-prompts/`.

---

## Default Behavior

If the user gives plain-language steps only:

1. Propose scenario title, Qase case ID, product, and tags.
2. Generate complete feature + steps + page objects.
3. List assumptions at the end.
