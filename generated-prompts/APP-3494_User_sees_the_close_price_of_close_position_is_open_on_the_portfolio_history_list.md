# APP-3494 — Close price on the portfolio history list

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History list

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3494.feature`

## Scenario Title

User sees the close position price is displayed on the portfolio history list

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. Both entities are required. `@XPro` is unresolved and has no Android package mapping. This scenario does not have `@Test`, so the default `AppTests` `@Test` filter does not select it. The scoped commands use `@App` together with the feature path.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- symbol=XAUUSD

Use the entity credentials already loaded by `the user logs in on the App login page`. Do not copy passwords into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. And the user creates a "BUY" position on the instrument details page
5. When the user taps "close" cta button on the app trade view
6. And the user taps button "Close Position" on the instrument details page
7. And the user taps button "Close Position" on the confirmation pop up
8. And the user taps back button on the app trade view
9. And the user taps button "Portfolio" on the app footer
10. And the user selects tab "History" on the portfolio page
11. Then the user sees transaction record item "Close Price" on the portfolio history list

## Expected Assertions

`historyTransactionItemDisplayed("Close Price")` waits for the latest Close record for the traded symbol and captured volume. Before the Close Position confirmation is tapped, `captureCloseConfirmationPrice()` stores only the overlay `Current Price` in `AppInstrumentDetailsPage.closeExecutedPrice`. It does not read `Open Price` and does not reuse the open-order `executedPrice`. Prices are compared as numbers after commas are removed. A missing expected price, a missing displayed price, or unequal numbers fails. Two nulls are not a success. A Current Price quote that is not the history fill still fails the comparison. EBL close rows use the opposite side. EIEHK keeps the original side. A non-Android driver returns false.

## Entity coverage

### EBL_MT5

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified in this change.
- UI path: `AppPortfolioPage.historyTransactionItemDisplayed("Close Price")` reads the plain decimal on the `Price` line of the latest Close row. A signed P/L value is ignored. The close row direction is the opposite of the opened BUY.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection, plus the existing portfolio history-list videos in `app_videos` (open/close records, open and close action, open and close direction, product code, and product name). No new device inspection in this change.
- Runtime result: not run.

### EIEHK

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261007.apk.zip`. Installed build was not verified in this change.
- UI path: the price on the `Close:` line of the latest matching card, between the label and the timestamp. Direction stays BUY.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection, plus the existing portfolio history-list videos in `app_videos`. No new device inspection in this change.
- Runtime result: not run.

### Unresolved

- `@XPro` has no verified entity mapping. No command is provided for it.

## Scoped commands

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3494.feature \
  -Dcucumber.filter.tags="@App"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3494.feature \
  -Dcucumber.filter.tags="@App"
```
