# APP-262 — Return to Portfolio from Deposit

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio Deposit action, Deposit header back, and Portfolio landing

## Feature File

`src/test/java/Features/NativeApp/trade/APP-262.feature`

## Scenario Title

User can redirect to the Portfolio page after tapping the back button on the Deposit page

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

## Environment

- product=app
- env=mt5uat
- entity=EBL_MT5
- platform=ANDROID

Use the credentials already recorded in APP-262.feature. Do not copy passwords into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user login as username from the feature file on App login page
3. And the user taps button "Portfolio" on the app footer
4. When the user taps button "Deposit" on the portfolio page
5. And the user taps back button on the deposit page
6. Then the user lands on app portfolio page

## Expected Assertions

- Wait until the Deposit header is visible before tapping back. The Portfolio action label `Deposit` does not count as that header.
- Tap the Deposit header back control. Do not call `AppTradeView.leaveTradeView()`.
- After back, Portfolio chrome is visible: `Show all`, `History`, `Newest to Oldest`, or `Open Positions`.
- Do not close a position or cancel an order after this assertion.

## Implementation Requirements

- Reuse `the user taps button {string} on the portfolio page` and `AppPortfolioPage.clickButton`.
- Add `the user taps back button on the deposit page`, implemented by `AppDepositPage.tapBack()`.
- Add `the user lands on app portfolio page`, implemented by `AppPortfolioPage.waitUntilDisplayed()`.
- Leave `the user taps back button on the app trade view` unchanged.
