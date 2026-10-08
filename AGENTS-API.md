# AGENTS — API and database automation

Apply [AGENTS.md](AGENTS.md) first. Reviewed by static inspection against the working tree on **2026-10-08**; see [ARCHITECTURE.md](ARCHITECTURE.md) for the full lifecycle.

When delegated as `efsg-api`, follow the [specialist contract](AGENTS-ORCHESTRATOR.md#specialist-contract): work within assigned file ownership, agree service/binding contracts with the UI owner through the orchestrator, and return changes and validation evidence. The orchestrator coordinates shared files and execution approval.

## Approval before changes

**Ask for explicit user approval before making any change**, following [AGENTS.md](AGENTS.md#approval-before-changes). Inspect and propose the exact service, SQL, binding, configuration, fixture, or backend-state changes first. Apply only the explicitly approved scope; existing approval for that scope need not be requested again. If delegated without approval, return the proposal to the orchestrator and continue read-only inspection. Live service/database access and test execution must also remain within their separately authorized scope.

## Scope and prerequisites

The Java suite has no dedicated API runner/profile. Backend bindings share the Cucumber glue with UI scenarios. The existing authenticated API steps depend on an initialized, logged-in Playwright page; SQL assertions often also require Admin Portal page-object state. They are not automatically usable in a native-only scenario simply because the glue is shared.

## Implementation map

| Responsibility | Source |
|---|---|
| HTTP transport and resource lifecycle | [ApiClient.java](src/test/java/API/ApiClient.java) |
| Business requests and JSON parsing | [CoreService.java](src/test/java/API/CoreService.java) |
| API/CM assertion bindings | [BackendSteps.java](src/test/java/StepDefinitions/Backend/BackendSteps.java) |
| Existing-person-ID precondition | [BackgroundSteps.java](src/test/java/StepDefinitions/Background/BackgroundSteps.java) |
| Parameterized MySQL reads and allowed query shapes | [SQLDatabase.java](src/test/java/Data/SQLDatabase.java) |
| JDBC connection mapping | [SQLConnection.java](src/main/java/utils/SQLConnection.java) |
| Token and shared runtime state | [BaseTest.java](src/main/java/utils/BaseTest.java) |
| Core API domain and entity/test-data helpers | [AbstractComponentsPW.java](src/main/java/AbstractComponent/AbstractComponentsPW.java) |

AO application steps also call `CoreService` directly for referral/trading-group checks. Reuse the appropriate existing binding rather than duplicating it in `BackendSteps`.

## HTTP and authentication

`ApiClient` creates its own Playwright instance and `APIRequestContext`; it does not inherit browser cookies. It supports `get(url, token)`, `post(url, token, body)`, and POST with extra headers. It adds the exact `Bearer ` prefix unless already present and defaults POST content type to JSON unless a custom content type is supplied. Token normalization rejects null but accepts blank strings; callers must establish a usable token.

Use try-with-resources inside `CoreService`. Validate and extract the response before closing the client; closing disposes both the request context and Playwright. Do not return a live response whose context has already been disposed.

`BaseTest.retrieveLocalStorageVal()` searches browser localStorage keys containing `accessToken` and returns the last iterated match, or an empty string if none matched. Establish login and verify a usable token first; do not assume a unique token key or print the token.

`CoreService(Page, productEnv)` captures the environment and the entity from shared `BaseTest` state through `userinfoList()`. `SQLDatabase` also captures `BaseTest.productEnv` at construction. Construct both only after that state is initialized. `BackendSteps` constructs both in field initializers, and `BackgroundSteps` constructs its SQL helper the same way, so step order and object-creation timing matter.

## Current service contracts

| Method | Current behavior |
|---|---|
| `getAccountStatus(token)` | GET status for a hardcoded default account; prints it, returns `void` |
| `getAccountId(token)` | POST to initialize a level-3 individual customer; prints its ID; mutates backend data |
| `getAoAccountDetail(uuid, token, field)` | GET AO detail; prints a field, returns `void` |
| `getCmList(token, field)` | POST paginated CM query using static `clientType`/`status` and entity; checks only the first record per page, prints a usable field, returns `void` |
| `getAoList(token, field)` | Checks only the first record per page and returns a usable field from paginated AO data |
| `getAoListItem(token, field, conditionField, conditionValue)` | Returns a field from a matching AO record |
| `getAoClient(...)` | Filters AO records by field, client type, entity, and creator |
| `getDefaultTradeGroupInfo(field, token, entity)` | Referral/trading info with an empty referral code |
| `getTradeGroupInfoBasedOnEntity(field, token, entity)` | Referral/trading info using the configured entity referral code |
| `setParamVal(param, value)` | Sets only static `clientType` or `status`; other names are ignored |

There is no current `getTradeGroupInfo(...)` method. Pagination is bounded to 100 pages of 10 records. Paginated lookup methods throw when no usable value is found within that limit. Unconditional list helpers inspect `content[0]` only; conditional helpers stop at the first matching record on each page even if its requested field is blank. Do not claim they examine every candidate or uniquely identify a record. The service does not provide a general typed response or schema-validation framework.

`ensureSuccess()` rejects null/non-2xx responses. JSON helpers expect a `response` object and, for lists, `response.content[]`; missing/null fields commonly become empty strings or arrays. HTTP success or console output alone is not a business assertion. Add explicit expected-value checks and distinguish absent data from an expected empty value.

Some request bodies interpolate strings directly; use proper JSON serialization for changed/new requests and check quoting and escaping. Error exceptions currently include response bodies; keep new diagnostics and review evidence free of tokens, credentials, and customer data.

## Environment routing

- Core-service base URL: `AbstractComponentsPW.getApiEndpointDomain(env)` maps `bauuat`, `egmuat`, `mt5sit`, and `mt5uat`; unrecognized values, including `bausit`, return an empty string.
- CRM base URL: `CoreService.getCrmDomain(entity, env)` maps `bauuat`/`mt5uat` for `EBL_MT5`, `EIEHK`, `EGM`, and `XPro`; unsupported combinations return an empty string.
- JDBC: `SQLConnection` maps only `bauuat` and `mt5uat`. Both currently point to the same BAU UAT CM database. Do not assume `-Denv=mt5uat` chooses a separate MT5 database.

Keep endpoint routing in these existing helpers. Require initialized, supported configuration and check the selected mapping rather than concatenating paths onto an empty base URL. The empty-route behavior applies to unsupported non-null values; null environment/entity values can fail in switches or dereferences. Connection credentials are currently embedded in source; do not reproduce them in docs or prompts.

## SQL contracts

`SQLDatabase` uses `PreparedStatement` for filter values, closes connection/statement/result set per query, and returns `Optional<String>`. Table and column identifiers are permitted only through exact `QueryShape` entries in `buildAllowedQueries()`; they are not arbitrary user-supplied SQL identifiers.

Allowed `(table, selected column, filter column)` combinations currently are:

- `person_email`, `profile_id`, `email_addr`
- `product_user`, `account_id`, `profile_id`
- `product_user`, `person_id`, `profile_id`
- `trade`, `status`, `account_id`
- `authentication`, `username`, `person_id`
- `trade`, `settlement_currency`, `account_id`
- `person_phone`, `phone_num`, `profile_id`

`retrieveValueFromDb()` prefixes tables with `cm.`, returns empty for a null filter value, and reads the first result. Email lookups order by descending creation date; other lookups do not establish ordering or uniqueness. `getPersonIdCount()` separately performs a parameterized count from `cm.person`.

`Optional.empty()` can mean a null filter, no row, SQL `NULL`, or an unsupported identity selector; the current contract cannot distinguish all these cases. A valid empty string is still present. `getValueBasedOnEmail()` accepts exactly `Profile ID`, `Account ID`, and `Person ID`; other text/casing returns empty. Do not turn absence into an expected empty value or a non-null sentinel to make an assertion pass.

Use `getPersonProfileId`, `getAccountId`, `getPersonId`, or `getValueBasedOnEmail` to resolve UI-derived identities. Add a reviewed query shape when a new assertion needs it; do not bypass the allowlist or embed SQL in step definitions. Preserve read-only validation unless the user explicitly requests data setup or modification.

## Reusing bindings

`BackendSteps` currently supports:

- `{string} retrieved from api endpoint` — uses a hardcoded AO UUID and prints a value; not a reusable assertion by itself.
- `the user extracts value {string} from the cm page api` — prints a CM field.
- `the parameter {string} is set to the value {string}` — sets the two supported filters.
- `value {string} is retrieved according to the param value {string} of param {string} from the ao page api` — stores the result through `setRetrievedData()`.
- `{string} is {string} in CM {string} database table where {string} retrieved by {string}` — resolves the current CM email and asserts a DB value.
- `{string} is updated to modified value in CM {string} database table where {string} retrieved by {string}` — compares DB data to the shared captured value.

`BackgroundSteps`' existing-person-ID step checks a count of four; it does not create four accounts. Reuse assertions only after checking their setup dependencies and missing-value behavior.

## Adding or changing a scenario

1. Establish product, environment/entity, business operation, authentication, identifying data, and expected values. For DB checks, establish the exact allowed query shape.
2. Reuse implemented bindings; put new requests and parsing in `CoreService` and transport concerns in `ApiClient`.
3. Keep setup mutations explicit. Do not use `getAccountId()` as though it were a read-only lookup.
4. Assert status and business results. Existing `Optional.orElse("")` and JSON empty-string defaults can hide missing records if used without a presence check.
5. Avoid extending static filters/captured values without reset planning; this layer is not scenario-isolated or safe for concurrent use.
6. Deliver the feature, implemented bindings/service methods, reference prompt, assumptions, and scoped command required by [AGENTS.md](AGENTS.md).

Example command for an existing CM subset, **only after explicit execution approval**:

```bash
mvn test -PWebTests -Dproduct=adminPortal -Denv=bauuat -Dentity=EBL_MT5 -Dbrowser=chrome \
  -Dcucumber.features=src/test/java/Features/AdminPortal/cm \
  -Dcucumber.filter.tags="@CM and @EBL_MT5"
```

Choose the actual feature path/tag expression for the requested case. Backend/API work does not waive the headed-web or explicit test-execution approval requirements. Do not change Qase behavior as part of endpoint work.
