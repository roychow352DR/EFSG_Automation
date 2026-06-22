# AGENTS.md

## Purpose

This repository uses **three specialized agent guides** for test automation. Each guide defines how the coding agent should create scripts when test steps are provided by the user.

For full system design, see [`ARCHITECTURE.md`](ARCHITECTURE.md).

---

## Agent Guides

| Guide | File | Use when |
|-------|------|----------|
| **Web automation** | [`AGENTS-WEB.md`](AGENTS-WEB.md) | Admin Portal or MIO Admin UI tests (Playwright) |
| **App automation** | [`AGENTS-APP.md`](AGENTS-APP.md) | Native mobile tests (Appium, Android/iOS) |
| **API automation** | [`AGENTS-API.md`](AGENTS-API.md) | REST API calls, JSON validation, SQL backend checks |

Select the guide that matches the task. For mixed UI + API scenarios, follow **AGENTS-WEB** (or **AGENTS-APP**) for the feature and page objects, and **AGENTS-API** for backend steps.

---

## Shared Project Context

| Aspect | Detail |
|--------|--------|
| **Language** | Java 21 |
| **Build** | Maven |
| **BDD** | Cucumber 7 + Gherkin |
| **Test runner** | TestNG via `AbstractTestNGCucumberTests` |
| **TMS** | Qase.io REST API (optional, via `Hooks`) |

### Runners and profiles

| Runner | Profile | Product scope |
|--------|---------|---------------|
| `WebTestRunner` | `WebTests` | Admin Portal, MIO |
| `WebFailedTestRunner` | `WebFailedTests` | Rerun failed web |
| `AppTestRunner` | `AppTests` | Native App |
| `AppFailedTestRunner` | — | Rerun failed app |

All runners: `features = "src/test/java/Features"`, `glue = "StepDefinitions"`, `tags = "@Test"`.

### Configuration

System properties (`-Dkey=value`) override `GlobalData.properties`.

| Parameter | Example values |
|-----------|----------------|
| `product` | `adminPortal`, `mio`, `app` |
| `env` | `bauuat`, `mt5uat`, `bausit` |
| `entity` | `EBL_MT5`, `EIEHK`, `XPro`, `EGM` |
| `browser` | `chrome`, `firefox`, `edge`, `webkit` |
| `platform` | `ANDROID`, `IOS` |

---

## Shared Quality Guardrails

- Do not break existing web, app, or API automation flows.
- Do not remove unrelated scenarios or step definitions.
- Keep methods small and intention-revealing.
- Ensure imports are clean and compile-ready.
- Feature filenames must follow `<QaseProject>-<CaseId>.feature` for Qase case ID resolution.
- Every generated test must be fully implemented — no placeholder stubs or TODO steps.

---

## Qase Integration Notes

When Qase hooks are enabled in `Hooks.java`:

- Case ID is parsed from the feature filename (e.g. `AP-141.feature` → case `141`).
- Feature-file steps are the source of truth for step result payloads.
- Case step sync updates Qase only when steps differ **and the scenario passed**.
- Failed scenarios do **not** replace Qase case steps.

Do not modify Qase hook behavior unless explicitly requested.

---

## Execution Policy (All Agents)

- Web tests must run in **headed mode** (no `*-headless` browser values).
- The agent must **not** execute Maven/test commands automatically.
- Before starting any execution, the agent must ask for explicit user approval.

---

## Shared Output Expectations

For every generated test case:

- Executable feature + step definitions (+ page objects or API methods as applicable)
- Reference prompt file: `generated-prompts/<featureId>_<scenarioTitleSafe>.md`
- Runnable Maven command with correct profile and `-D` overrides
- Assumptions listed when test data was not provided by the user

---

## Related Documentation

| Document | Purpose |
|----------|---------|
| [`ARCHITECTURE.md`](ARCHITECTURE.md) | Full system architecture |
| [`AGENTS-WEB.md`](AGENTS-WEB.md) | Web UI automation rules |
| [`AGENTS-APP.md`](AGENTS-APP.md) | Mobile app automation rules |
| [`AGENTS-API.md`](AGENTS-API.md) | API and backend validation rules |
| [`STANDARD_PROMPT_TEMPLATE.md`](STANDARD_PROMPT_TEMPLATE.md) | Prompt template for new test cases |
