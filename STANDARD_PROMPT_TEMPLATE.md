# Standard Prompt Template (AGENTS.md)

Use this template whenever you want the agent to generate web automation scripts using the guidance in `AGENTS.md`.

## Environment to Framework Mapping

- Page objects root: `src/main/java/PageObject`
- `product=adminPortal` -> use `PageObject/AdminPortalPW` (and `AOPOManager` where applicable)
- `product=mio` -> use `PageObject/MIOadmin`
- `env` + `product` determine web domain resolution in framework runtime (`BaseTest`)

## Standard Prompt Template

```text
Create Playwright Java web automation based on AGENTS.md in this repository.

Module/Page:
<login | customer management | user management | ...>

Feature File:
<path or new feature name>

Scenario Title:
<scenario title>

Tags:
@<tag1> @<tag2> ...

Environment:
product=<adminPortal|mio>
env=<bauuat|bausit|mt5uat|mt5sit>
entity=<EBL_MT5|EIEHK|...>
browser=<chrome|edge|firefox>  # headed mode required

Test Data:
- <key>: <value>
- <key>: <value>

Test Steps:
1) Given ...
2) When ...
3) Then ...
4) And ...

Expected Assertions:
- <assertion 1>
- <assertion 2>

Implementation Requirements:
- Reuse existing page objects and the product-appropriate PO manager where possible
- Add/update feature + step definitions + page object methods
- Select page object package by product:
  - `adminPortal` -> `PageObject/AdminPortalPW`
  - `mio` -> `PageObject/MIOadmin`
- Select the product-appropriate PO manager:
  - `adminPortal` -> `AOPOManager`
  - `mio` -> `MIOPOManager`
- Keep locators robust and Playwright-first
- Avoid changing unrelated files

Output Format:
1) Files created/updated
2) Code changes
3) Assumptions (if any)
4) Maven run command
```

## Example 1: Login Validation

```text
Create Playwright Java web automation based on AGENTS.md in this repository.

Module/Page:
Admin Portal Login

Feature File:
src/test/java/Features/AdminPortal/login/AP-999.feature

Scenario Title:
User sees invalid credential error on Admin Portal login

Tags:
@Test @AP-999 @web @adminPortal

Environment:
product=adminPortal
env=bauuat
entity=EBL_MT5
browser=chrome

Test Data:
- username: invalid_user
- password: invalid_pass

Test Steps:
1) Given the user lands on Admin Portal login page
2) And the user fills in with username "invalid_user" and password "invalid_pass"
3) When the user clicks Sign In button
4) Then the user sees "Invalid username or password." message pop up

Expected Assertions:
- Error message is visible
- User remains on login page

Implementation Requirements:
- Reuse existing login step style and AdminLoginPagePW
- Keep @Test tag compatible with WebTestRunner
- Do not modify unrelated scenarios

Output Format:
1) Files created/updated
2) Code changes
3) Assumptions (if any)
4) Maven run command
```

## Example 2: Customer Management Search

```text
Create Playwright Java web automation based on AGENTS.md in this repository.

Module/Page:
Customer Management

Feature File:
src/test/java/Features/AdminPortal/cm/AP-1001.feature

Scenario Title:
AO user can search customer by email

Tags:
@Test @AP-1001 @web @cm

Environment:
product=adminPortal
env=mt5uat
entity=EIEHK
browser=edge

Test Data:
- aoUsername: ao_user_01
- aoPassword: Password123!
- customerEmail: uatapproved@yopmail.com

Test Steps:
1) Given the user lands on Admin Portal login page
2) And the user fills in with username "ao_user_01" and password "Password123!"
3) When the user clicks Sign In button
4) Then the user sees Menu display on the screen
5) When the user navigates to Customer Management page
6) And the user searches customer with email "uatapproved@yopmail.com"
7) Then the user sees searched customer record displayed

Expected Assertions:
- Search result table contains the target email
- No empty-state message is shown

Implementation Requirements:
- Add/update page object methods in AdminPortalPW classes
- Add/update step defs under StepDefinitions/AdminPortal
- Keep method names reusable and generic

Output Format:
1) Files created/updated
2) Code changes
3) Assumptions (if any)
4) Maven run command
```

## Example 3: Minimal Fast Prompt

```text
Use AGENTS.md and generate full Playwright Java automation for these steps.

Tags: @Test @AP-2002
Env: product=adminPortal, env=bauuat, entity=EBL_MT5, browser=chrome
Feature: AP-2002 - Reset password link validation

Steps:
1) Given user is on Admin Portal login page
2) When user clicks "Forgot Password"
3) Then reset password page is displayed
4) And user sees email input and submit button

Expected:
- Reset password URL is correct
- Required controls are visible

Please create/update feature file, step definitions, page objects, and AOPOManager if needed.
Return changed files + maven run command.
```

