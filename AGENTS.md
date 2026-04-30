# AGENTS.md

## Purpose
This file defines how the coding agent should create **web automation scripts using Playwright Java** in this repository when test steps are provided by the user.

## Project Context
- Framework: Java + Maven + Cucumber + TestNG + Playwright
- Web runner: `src/test/java/CucumberRunner/WebTestRunner.java`
- Step definitions root: `src/test/java/StepDefinitions`
- Web page objects root: `src/main/java/PageObject`
- Product-based PO package mapping:
  - `product=adminPortal` -> `src/main/java/PageObject/AdminPortalPW`
  - `product=mio` -> `src/main/java/PageObject/MIOadmin`
- Product-based PO manager mapping:
  - `product=adminPortal` -> `src/main/java/PageObject/AdminPortalPW/AOPOManager`
  - `product=mio` -> `src/main/java/PageObject/MIOadmin/MIOPOManager`
- Base utilities: `src/main/java/utils/BaseTest.java`

## Working Rules For Agent
1. Reuse existing framework patterns before creating new structures.
2. Keep implementation Playwright-first for web (`com.microsoft.playwright.Page`, `Locator`, Playwright assertions).
3. Follow existing naming and package style:
   - Step definitions in `StepDefinitions/...`
   - Page objects in product-mapped package:
     - `PageObject/AdminPortalPW/...` when `product=adminPortal`
     - `PageObject/MIOadmin/...` when `product=mio`
   - Feature files in `src/test/java/Features/...`
4. Use `BaseTest.initializePage()` for page initialization in web steps.
5. Use the product-appropriate PO manager to access page objects from step definitions.
   - `product=adminPortal` -> `AOPOManager`
   - `product=mio` -> `MIOPOManager`
   - If a manager does not exist for the product/module, create one following existing patterns.
6. Prefer robust locators (`getByRole`, `getByLabel`, `getByTestId`, stable text) over fragile CSS selectors.
7. Add explicit waits only when needed; prefer Playwright auto-wait behavior.
8. Keep steps reusable and concise; avoid scenario-specific hardcoding unless requested.
9. Every generated test case script must include an implemented, executable automated test (feature scenario + bound step definitions), not placeholder-only code.

## When User Provides Test Steps
The agent should:
1. Convert steps into/extend a `.feature` file under `src/test/java/Features`.
2. Implement or update matching step definitions under `src/test/java/StepDefinitions`.
3. Screen existing step definitions to see whether or not they match user-provided steps exactly:
   - If a user-provided step text matches an existing step binding exactly, reuse that step definition.
   - If a user-provided step is exactly unmatched by existing bindings, create a new step definition for it.
4. Add/update page object methods in the product-mapped package:
   - `src/main/java/PageObject/AdminPortalPW` when `product=adminPortal`
   - `src/main/java/PageObject/MIOadmin` when `product=mio`
5. Update the relevant PO manager if a new page object class is introduced (for example `AOPOManager` for `adminPortal`, `MIOPOManager` for `mio`).
6. Keep compatibility with existing `WebTestRunner` tags and glue config.

## Input Checklist (Ask If Missing)
Before generating scripts, collect:
- Target module/page (for example: login, user management, customer management)
- Scenario name and expected tag (for example: `@Test`, `@APP-1234`)
- Test data values (username/password, search text, etc.)
- Expected results/assertions
- Environment or entity constraints (if any)

## Output Expectations
For each request, the agent should provide:
- Updated/created feature file
- Updated/created step definition class
- Updated/created page object class/methods
- Any required product PO manager updates (for example `AOPOManager` or `MIOPOManager`)
- Confirmation that each generated test case has a runnable automated test implementation
- Create a reference prompt file for the generated test case script:
  - Folder: `generated-prompts/`
  - Filename: `<featureId>_<scenarioTitleSafe>.md` (scenario title sanitized to be filename-safe)
- A short run command suggestion, for example:
  - `mvn test -PWebTests -Dproduct=adminPortal -Denv=bauuat -Dentity=EBL_MT5 -Dbrowser=chrome`

## Execution Policy (Approval Required)
- Tests must be executed in **headed mode** (do not use `*-headless` browser values).
- The agent must **not** execute Maven/test commands automatically.
- Before starting any execution, the agent must ask for explicit user approval and wait for your response.

## Quality Guardrails
- Do not break existing app/mobile automation flows.
- Do not break existing web automation flows
- Do not remove unrelated scenarios or step definitions.
- Keep methods small and intention-revealing.
- Reuse existing helper methods from `BaseTest`/`AbstractComponentsPW` where appropriate.
- Ensure imports are clean and compile-ready.

## Definition of Done (Strict)
A generated test case is complete only when all conditions below are met:
1. Feature is implemented with at least one executable scenario and appropriate tags.
2. Every scenario step has a matching implemented step definition (no undefined/pending steps).
3. Step definitions call implemented page object methods (no empty methods, stubs, or placeholder TODO/FIXME).
4. Required page object methods and locators are implemented and used through the product-appropriate PO manager when applicable (`AOPOManager` for `adminPortal`, `MIOPOManager` for `mio`).
5. Assertion coverage exists for the expected result(s), not only action steps.
6. Changes are compile-ready with clean imports and no obvious syntax errors.
7. A runnable Maven command is provided for the created test (for example using `-PWebTests` with `product/env/entity`).
   - The command must be compatible with **headed mode** (for example `-Dbrowser=chrome` / `-Dbrowser=edge` / `-Dbrowser=firefox`).
8. Assumptions and required test data are explicitly listed when not provided by the user.
9. A corresponding reference prompt file is created under `generated-prompts/` using the agreed naming convention.

## Default Behavior
If user only gives plain language steps, the agent should still:
1. Propose sensible scenario title and tags.
2. Generate complete framework-aligned automation files.
3. Highlight any assumptions clearly at the end.
