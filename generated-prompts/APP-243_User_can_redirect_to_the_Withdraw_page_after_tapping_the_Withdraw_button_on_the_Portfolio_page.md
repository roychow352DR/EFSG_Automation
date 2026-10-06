# APP-243 — Redirect to Withdraw from Portfolio

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio Withdraw action and Withdraw page header

## Feature File

`src/test/java/Features/NativeApp/trade/APP-243.feature`

## Scenario Title

User can redirect to the Withdraw page after tapping the "Withdraw" button on the Portfolio page

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

## Environment

- product=app
- env=mt5uat
- entity=EBL_MT5
- platform=ANDROID

Use the credentials already recorded in APP-243.feature. Do not copy passwords into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user login as username from the feature file on App login page
3. And the user taps button "Portfolio" on the app footer
4. When the user taps button "Withdraw" on the portfolio page
5. Then the user sees header "Withdraw" on the page

## Expected Assertions

- After the Portfolio Withdraw action is tapped, wait until the Withdraw screen has finished loading.
- The visible page header equals `Withdraw`.
- The Portfolio action row label `Withdraw` does not satisfy the check. The header must sit in the top band, and Portfolio chrome (`Open Positions` or `Newest to Oldest`) must be gone.
- Do not tap back or close a position after this assertion.

## Implementation Requirements

- Reuse `the user taps button {string} on the portfolio page` and `AppPortfolioPage.clickButton`.
- Read the header through `AppPOManager.getAppDepositPage().getHeader(expectedHeader)`.
- The same method serves Deposit (APP-261), Withdraw (APP-243), and Records (APP-246).
- Wait up to 60 seconds for the native header. A missing header fails the step.
- Leave the existing Position Details, Pending Order Details, Close Position, and Edit Position header behavior unchanged.
