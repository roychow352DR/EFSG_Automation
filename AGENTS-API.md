# AGENTS — API Automation

Guidelines for creating **API and backend validation** test steps in this repository. API tests are typically combined with web UI scenarios via shared Cucumber steps.

For system design, see [`ARCHITECTURE.md`](ARCHITECTURE.md). For shared rules, see [`AGENTS.md`](AGENTS.md).

---

## Scope

| Aspect | Detail |
|--------|--------|
| **API client** | Playwright `APIRequestContext` via `ApiClient` |
| **Business layer** | `CoreService` (REST orchestration, JSON parsing) |
| **Step definitions** | `StepDefinitions/Backend/BackendSteps.java` |
| **DB validation** | `StepDefinitions/Background/BackgroundSteps.java`, `Data/SQLDatabase.java` |
| **Typical usage** | Mixed UI + API + SQL assertions in the same feature scenario |

API automation does not have a separate Cucumber runner — API steps are invoked from web or app feature files through shared step bindings.

---

## Key Paths

| Layer | Path |
|-------|------|
| API client | `src/test/java/API/ApiClient.java` |
| Core service | `src/test/java/API/CoreService.java` |
| Backend steps | `src/test/java/StepDefinitions/Backend/BackendSteps.java` |
| Background steps | `src/test/java/StepDefinitions/Background/BackgroundSteps.java` |
| SQL helper | `src/test/java/Data/SQLDatabase.java` |
| API domain resolution | `AbstractComponentsPW.getApiEndpointDomain(env)` |
| CRM domain | `CoreService.getCrmDomain(entity, env)` |
| Auth token (from UI) | `BaseTest.retrieveLocalStorageVal()` |
| Test data builders | `Data/AoAccountCreation.java`, `Data/CmAccountStatus.java` |

---

## Architecture

```
Feature scenario (web UI steps)
        ↓
BackendSteps / BackgroundSteps
        ↓
CoreService  →  ApiClient  →  REST API
        ↓
SQLDatabase  →  MySQL (post-condition checks)
```

`CoreService` is constructed with a Playwright `Page` and environment — it uses `AbstractComponentsPW` for entity/env and API endpoint resolution.

---

## ApiClient

Thin wrapper around Playwright `APIRequestContext`:

- `get(url, token)` — Bearer auth via `Authorization` header
- `post(url, token, body)` — JSON POST with Bearer auth
- `post(url, token, body, extraHeaders)` — POST with custom headers
- Implements `AutoCloseable` — use try-with-resources

```java
try (ApiClient apiClient = new ApiClient()) {
    APIResponse response = apiClient.get(endpoint, token);
}
```

---

## CoreService

Business-level API methods (extend here for new endpoints):

| Method | Purpose |
|--------|---------|
| `getAccountStatus(token)` | Account opening status |
| `getAoAccountDetail(uuid, token, field)` | Single AO record detail |
| `getCmList(token, extractVal)` | Paginated CM list lookup |
| `getAoList(token, extractVal)` | Paginated AO list lookup |
| `getAoListItem(token, extractVal, conditionVal, conditionParam)` | Filtered AO item |
| `getAoClient(...)` | Client lookup with entity/creator filters |
| `getTradeGroupInfo(extractVal, token)` | Referral/trading group |
| `setParamVal(param, value)` | Set static filters (`clientType`, `status`) |
| `parseJson(responseBody, field)` | Gson JSON field extraction |

Response parsing expects `{ "response": { ... } }` structure with optional `content[]` arrays.

---

## BackendSteps (existing bindings)

Reuse these step patterns when possible:

| Step pattern | Action |
|--------------|--------|
| `{string} retrieved from api endpoint` | `getAoAccountDetail` |
| `the user extracts value {string} from the cm page api` | `getCmList` |
| `the parameter {string} is set to the value {string}` | `setParamVal` |
| `value {string} is retrieved according to the param value {string} of param {string} from the ao page api` | `getAoListItem` |
| `{string} is {string} in CM {string} database table where {string} retrieved by {string}` | SQL assertion |
| `{string} is updated to modified value in CM {string} database table where {string} retrieved by {string}` | SQL compare to UI data |

`BackendSteps` uses `retrieveLocalStorageVal()` for the bearer token and `aopoManager` for UI-derived values (e.g. email).

---

## Working Rules

1. Reuse `BackendSteps` bindings when step text matches exactly.
2. Add new API methods to `CoreService`, not directly in step definitions.
3. Use `ApiClient` inside `CoreService` with try-with-resources.
4. Use `ensureSuccess()` / response validation patterns already in `CoreService`.
5. Parse JSON via existing `getResponseObject`, `getContentArray`, `getString` helpers.
6. For DB checks, use `SQLDatabase` methods — do not embed raw SQL in step definitions.
7. API steps that need auth assume a prior web login step has populated Playwright `localStorage`.
8. Keep API step text generic and reusable across scenarios.
9. When adding CRM-specific calls, use `getCrmDomain(entity, env)` for endpoint resolution.

### Adding a new API method

1. Add method to `CoreService` using `ApiClient` and existing JSON helpers.
2. Add a Cucumber step binding in `BackendSteps` (or extend an existing one).
3. If the step needs UI context, read from `aopoManager` or `BaseTest` static state.
4. Add assertion via `Assert` or return value stored with `setRetrievedData()`.

---

## When User Provides API Test Steps

1. Check if an existing `BackendSteps` binding matches the step text.
2. If not, add the method to `CoreService` and a new step in `BackendSteps`.
3. For DB validation, use `SQLDatabase` with parameterized table/column/filter values.
4. Wire the API steps into the existing feature file (usually alongside UI steps).
5. Ensure a prior UI step provides auth token or test data the API step depends on.

---

## Input Checklist (Ask If Missing)

- **Endpoint or business operation** (AO status, CM list, referral code, etc.)
- **Auth context** — does the scenario include a web login step?
- **Filter parameters** (client type, status, entity, createdBy)
- **Expected response field and value**
- **DB table/column** for SQL assertions (if any)
- **Environment** and **entity**

---

## Output Expectations

- Updated `CoreService` method(s) if new API calls are needed
- Updated `BackendSteps` (and/or `BackgroundSteps`) bindings
- Feature file steps referencing the new bindings
- If mixed with UI: confirm web steps exist for login/data setup
- Runnable Maven command for the parent web scenario:

```bash
mvn test -PWebTests -Dproduct=adminPortal -Denv=bauuat -Dentity=EBL_MT5 -Dbrowser=chrome
```

---

## Execution Policy

- API steps run as part of web/app Cucumber scenarios — use the parent product's Maven profile.
- Do **not** execute Maven commands without explicit user approval.

---

## Definition of Done

1. API logic implemented in `CoreService` (not inline in steps).
2. Step definition binding exists and is reusable.
3. JSON parsing and error handling follow existing `CoreService` patterns.
4. SQL assertions use `SQLDatabase` helpers when applicable.
5. Feature scenario includes required UI setup steps (login, navigation) if token/UI data is needed.
6. Compile-ready with clean imports.
7. Assumptions listed (endpoint, test data, auth dependency).

---

## Default Behavior

If the user gives plain-language API validation steps:

1. Map to existing `BackendSteps` bindings first.
2. Propose `CoreService` method signature and step text.
3. Identify UI prerequisites (login, record creation) for mixed scenarios.
4. List assumptions at the end.
