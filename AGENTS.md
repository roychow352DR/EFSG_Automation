# AGENTS.md

Repository-wide instructions for maintaining and generating EFSG automation. Reviewed against the local working tree on **2026-09-21**. For implementation details and known limitations, see [ARCHITECTURE.md](ARCHITECTURE.md).

## Orchestration and delegation

The primary agent acts as the EFSG orchestrator and owns the complete user request. Follow [AGENTS-ORCHESTRATOR.md](AGENTS-ORCHESTRATOR.md) to plan work, assign specialists, integrate changes, and report validation.

- For work that can be split into independent, bounded tasks, delegate to the relevant `efsg-web`, `efsg-app`, or `efsg-api` specialist. Use only the specialists needed for the request; handle small, tightly coupled changes directly.
- Assign one active writer per file and serialize shared-framework changes. Parallel code inspection does not authorize parallel test/device execution.
- Pass repository rules, exact file ownership, dependencies, acceptance criteria, and existing execution authorization to each specialist. Specialists return evidence and unresolved issues to the orchestrator.
- The orchestrator reviews every result and provides one consolidated response. Delegation does not expand permission to run tests, modify Qase behavior, or change unrelated files.

Named agent definitions live in [.codex/agents](.codex/agents): `efsg-orchestrator`, `efsg-web`, `efsg-app`, and `efsg-api`. If the client cannot select custom agents, pass the appropriate guide and task brief to its available subagent tool. If delegation is unavailable, follow the same workflow sequentially and disclose that no separate agents ran.

## Choose the applicable guide

- [AGENTS-ORCHESTRATOR.md](AGENTS-ORCHESTRATOR.md): coordination, delegation contracts, shared-file ownership, and final review.
- [AGENTS-WEB.md](AGENTS-WEB.md): Admin Portal and MIO web automation with Playwright Java.
- [AGENTS-APP.md](AGENTS-APP.md): native Android/iOS automation with Appium Java; Appium MCP inspection.
- [AGENTS-API.md](AGENTS-API.md): REST calls, JSON validation, and MySQL checks.

For mixed scenarios, apply the UI guide to features/page objects and the API guide to backend steps. These documents define agent responsibilities; Maven/Cucumber remain the test execution system.

## Project baseline

- Java 21, Maven, Cucumber 7.20.1, and TestNG 7.10.2.
- Playwright 1.53.0 is the current web stack. Preserve legacy Selenium flows; do not extend them for new web tests.
- Appium Java client 9.4.0 drives native tests. Java runners are the primary execution path; root WebdriverIO files are auxiliary scaffolding.
- Features live in `src/test/java/Features`; all four Cucumber runners use `glue = "StepDefinitions"`.
- Page objects live in `src/main/java/PageObject`; steps in `src/test/java/StepDefinitions`; configuration in `src/main/java/DataResources`.

## Runners and selection

| Maven profile | Runner | Feature source | Annotation tag filter |
|---|---|---|---|
| `WebTests` | `WebTestRunner` | `src/test/java/Features` | `@Test` |
| `AppTests` | `AppTestRunner` | `src/test/java/Features` | `@Test` |
| `WebFailedTests` | `WebFailedTestRunner` | `@target/web_failed_scenarios.txt` | None |
| `AppFailedTests` | `AppFailedTestRunner` | `@target/app_failed_scenarios.txt` | None |

`Regression` points to legacy `testng.xml`; it is not a Cucumber tag selector. Its configured `Onboarding.loginSuccess` class is absent from the current source tree.

**A profile or `-Dproduct` does not restrict feature discovery to that product.** Scope normal runs with `-Dcucumber.features=...` and an appropriate `-Dcucumber.filter.tags=...` expression. Supply `@Test` for new scenarios intended for the default runners. Existing scenarios may lack it; use an explicit filter when selecting those cases. Do not run `clean` before a rerun: it removes the failure list.

## Configuration

`BaseTest.getProperty()` gives Java system properties precedence over property-file values. This applies to keys actually read by the code, not arbitrary Appium capabilities.

| Setting | File/default source | Supported selection |
|---|---|---|
| `product`, `env`, `entity`, `browser` | `GlobalData.properties` | Product-specific routing in `BaseTest` |
| `platform` | `qase-nativeApp.properties` | Exact uppercase `ANDROID` or `IOS` |
| `symbol` | `TradeSymbol.properties` | Native trading symbol; `TradeSymbolConfig` holds its metadata |
| `qase.*`, `testtype` | Product-specific `qase-*.properties` | Used when Qase integration is enabled |

Use explicit overrides in commands; local defaults change between tasks. Entity tags do not guarantee that an Android package, CRM route, or Qase plan exists for that entity. Check the relevant mapping before generating a scenario.

## Shared implementation rules

1. Inspect the existing binding, page object, helper, and feature before extending them. A matching step annotation does not guarantee a complete assertion or a side-effect-free method.
2. Keep UI mechanics in page objects and access them from steps through `AOPOManager`, `MIOPOManager`, or `AppPOManager`. Register new pages in the corresponding manager.
3. Reuse shared helpers and data builders. Keep methods small, imports clean, and every generated step implemented; no placeholder bodies, TODO steps, or assertions that compare missing values as a success.
4. Use `<QaseProject>-<CaseId>.feature`: `AP`, `MIO`, or `APP`. Preserve existing IDs; use the supplied case ID and check for collisions.
5. Preserve unrelated scenarios, bindings, application behavior, and user changes, including staged files and generated evidence. Do not reformat or revert unrelated work.
6. Do not copy repository credentials, API tokens, or connection passwords into documentation, generated prompts, or new fixtures. Use the existing approved data source or user-supplied test data.
7. Shared drivers, managers, API filters, and trading values are largely static. Do not add parallel execution or claim scenario isolation without addressing that state and the shared output paths.

## Lifecycle and Qase guardrails

In the current `Hooks.java`, only `@After tearDown` is active. Qase `@BeforeAll`, `@Before`, `@AfterStep`, step synchronization, and result submission are commented out. There is no implemented `qase.enabled` switch.

When integration is explicitly enabled:

- Case IDs come from feature filenames, not case tags.
- Feature-file scenario steps are the primary source for result actions; the code falls back to Qase for a missing position.
- Replace Qase case steps only when they differ and the scenario passed. Failed scenarios must not replace them.
- Preserve native captured-state resets and existing teardown/cleanup behavior.

Do not modify or enable Qase hook behavior unless explicitly requested. See [ARCHITECTURE.md](ARCHITECTURE.md) for parser, configuration, and media-cleanup limitations.

## Execution policy

- **Do not execute Maven or test commands automatically, including compilation or dry runs. Ask for explicit user approval before starting them.** Provide the exact command and its intended scope. Existing explicit authorization applies only to the approved scope.
- Web test execution must be **headed**: use `chrome`, `firefox`, `edge`, or `webkit`, never `*-headless`.
- A request to inspect code or update documentation permits static inspection; it does not authorize a test run.
- For an explicitly requested Appium MCP launch/inspection, use the selected device and inspect the requested screen. Do not treat that as approval to run Maven tests or execute trading workflows.

## Deliverables for a generated test

- Executable feature, implemented step definitions, and page-object/API changes as needed.
- Reference prompt: `generated-prompts/<featureId>_<scenarioTitleSafe>.md`, with filesystem-safe title and no secrets. Create the directory if needed; the framework does not generate these files automatically.
- Runnable, scoped Maven command with the correct profile and explicit product/environment/entity/browser or platform overrides.
- Assumptions for missing test data and a clear account of validation actually performed. Do not describe an unexecuted test as passing.

[STANDARD_PROMPT_TEMPLATE.md](STANDARD_PROMPT_TEMPLATE.md) is a web-oriented request template. Use these guides and current source code to resolve outdated examples.
