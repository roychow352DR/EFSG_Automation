# APP-3493 — Open price on the portfolio history list

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History list

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3493.feature`

## Scenario Title

User sees the open price of open position is open on the portfolio history list

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @AppTest @Test`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. Both entities are required. The scenario has `@Test`, so the scoped commands keep that tag.

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
5. When the user taps back button on the app trade view
6. And the user taps button "Portfolio" on the app footer
7. And the user selects tab "History" on the portfolio page
8. Then the user sees transaction record item "Open Price" on the portfolio history list

## Expected Assertions

The scenario does not close the position. `historyTransactionItemDisplayed("Open Price")` waits for the latest today Open record for the traded symbol and captured volume. The expected price is `AppInstrumentDetailsPage.executedPrice`, captured from the order confirmation before the market order is confirmed. Prices are compared as numbers after commas are removed. A missing expected price, a missing displayed price, or unequal numbers fails. Two nulls are not a success. Older Close rows for other deals may remain; the check selects the latest Open for this symbol and volume. EBL close rows use the opposite side. EIEHK keeps the original side. A non-Android driver returns false.

## Entity coverage

### EBL_MT5

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified in this change.
- UI path: `AppPortfolioPage.historyTransactionItemDisplayed("Open Price")` reads the plain decimal under the `Price` label on the latest Open row. A signed P/L value is ignored.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection, plus the existing portfolio history-list videos in `app_videos` (open/close records, open and close action, open and close direction, product code, and product name). No new device inspection in this change.
- Runtime result: not run.

### EIEHK

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261007.apk.zip`. Installed build was not verified in this change.
- UI path: the price on the `Open:` line of the latest matching card, between the label and the timestamp. A `Close:` line is not required.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection, plus the existing portfolio history-list videos in `app_videos`. No new device inspection in this change.
- Runtime result: not run.

## Scoped commands

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3493.feature \
  -Dcucumber.filter.tags="@Test"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3493.feature \
  -Dcucumber.filter.tags="@Test"
```
