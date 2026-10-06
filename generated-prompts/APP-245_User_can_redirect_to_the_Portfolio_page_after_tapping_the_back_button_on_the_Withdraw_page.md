# APP-245 — Return from Withdraw

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio Withdraw action and Withdraw header back

## Feature File

`src/test/java/Features/NativeApp/trade/APP-245.feature`

## Scenario Title

User can redirect to the Portfolio page after tapping the back button on the Withdraw page

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

## Environment

- product=app
- env=mt5uat
- entity=EBL_MT5
- platform=ANDROID

Use the credentials already recorded in APP-245.feature. Do not copy passwords into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user login as username from the feature file on App login page
3. And the user taps button "Portfolio" on the app footer
4. When the user taps button "Withdraw" on the portfolio page
5. And the user taps back button on the withdraw page
6. Then the user sees header "Withdraw" on the page

## Expected Assertions

- Wait until the Withdraw header is visible before tapping back. The Portfolio action label `Withdraw` does not count as that header.
- Tap the Withdraw header back control through `AppWithdrawPage.tapBack()`.
- After back, the existing header step still expects the visible page header to equal `Withdraw`.
- Do not call `AppTradeView.leaveTradeView()` or `AppPortfolioPage.tapBack()`.
- Do not close a position or cancel an order after this assertion.

## Implementation Requirements

- Reuse `the user taps button {string} on the portfolio page` and `AppPortfolioPage.clickButton`.
- Add `the user taps back button on the withdraw page`, implemented by `AppWithdrawPage.tapBack()`.
- Register `AppWithdrawPage` on `AppPOManager`.
- Keep `AppDepositPage.tapBack()` as the Deposit-only entry used by APP-262.
- Leave `the user taps back button on the app trade view` and `the user taps back button on the portfolio filtering page` unchanged.
- Leave the feature Then step unchanged: `the user sees header "Withdraw" on the page`.
