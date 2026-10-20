# APP-3691 — Product code on the portfolio history list

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History list

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3691.feature`

## Scenario Title

User sees the product code is displayed on the portfolio history list

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest @Test`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. `@XPro` is an unresolved declaration with no Android package mapping.

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
11. Then the user sees transaction record item "Product Code" on the portfolio history list

## Expected Assertions

`Product Code` is `AppMarketsPage.tradeSymbol`. A blank code, an unknown item name, or a missing code is a failed assertion. The friendly product name, such as Gold for XAUUSD, does not satisfy this check. An older same-day record that still shows the code does not satisfy it either.

- EBL_MT5: choose the latest Open row and the latest Close row by their `yyyy-MM-dd HH:mm:ss` timestamps. Both rows must show the product code, together with the expected direction, captured lot size, and today's date. The Close timestamp is not earlier than the Open timestamp. Closing a BUY position is shown as Close SELL.
- EIEHK: choose the latest card that contains both an `Open:` line and a `Close:` line for the original direction and captured lot size. That same card shows the product code once. Both lines use today's date, and the Close timestamp is not earlier than the Open timestamp. Open from one card and Close from another do not satisfy the check.

## Entity coverage

### EBL_MT5

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified in this change.
- UI path: `AppPortfolioPage.historyTransactionItemDisplayed`, using the latest EBL Open and Close rows by timestamp. APP-3684 still calls `historyPositionRecordVisible`.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection of the existing EBL history row geometry. No new device inspection in this change.
- Runtime result: not run.

### EIEHK

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261007.apk.zip`. Installed build was not verified in this change.
- UI path: `AppPortfolioPage.historyTransactionItemDisplayed`, using the latest EIEHK card that contains both Open and Close. APP-3684 still calls `historyPositionRecordVisible`.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection of the existing EIEHK history card geometry. No new device inspection in this change.
- Runtime result: not run.

### Unresolved

- `@XPro` has no supported Android package mapping.

## Scoped commands

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3691.feature \
  -Dcucumber.filter.tags="@Test"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3691.feature \
  -Dcucumber.filter.tags="@Test"
```
