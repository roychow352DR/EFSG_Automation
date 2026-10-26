# APP-3516 — Product code on the close position details page

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Position Details for close position history

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3516.feature`

## Scenario Title

User sees product code on the position details page of close position history

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @AppTest @Test`

Effective entity tag: `@EBL_MT5` only. Do not add `@EIEHK` or `@XPro`. iOS is out of scope.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- symbol=XAUUSD

Login uses the existing `the user logs in on the App login page` step. Do not copy credentials into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. And the user creates a "BUY" position on the instrument details page
5. And the user taps "close" cta button on the app trade view
6. And the user taps button "Close Position" on the instrument details page
7. And the user taps button "Close Position" on the confirmation pop up
8. And the user taps back button on the app trade view
9. When the user taps button "Portfolio" on the app footer
10. And the user selects tab "History" on the portfolio page
11. And the user taps "arrow" button of the record row on the portfolio page
12. Then the user sees correct value "Product" on the close position details page
13. And the user sees correct value "Action" on the close position details page

## Expected Assertions

`AppPositionDetailsPage.getCloseHistoryValidationValue("Product")` returns `AppMarketsPage.tradeSymbol`. `getCloseHistoryValidationValue("Action")` returns the literal `Close`. Any other label, including a null label, returns null.

The close position details step reads the displayed value with `AppPositionDetailsPage.getDetailValue`. Displayed and expected values must be non-null and non-blank before equality. A missing or blank displayed value fails. A missing or blank expected value fails. Product must equal `AppMarketsPage.tradeSymbol`. Action must equal `Close`.

The step does not call `appPoManager.getAppTradeView().closePositionInDetails()`. Open-position callers keep `getValidationValue`, whose Action and Status value remains `Open`.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified in this change.
- UI path: `AppPositionDetailsPage.getDetailValue` plus `AppPositionDetailsPage.getCloseHistoryValidationValue`.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3516.feature \
  -Dcucumber.filter.tags="@Test"
```
