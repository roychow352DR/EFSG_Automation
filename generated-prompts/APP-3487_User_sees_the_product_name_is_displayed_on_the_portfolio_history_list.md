# APP-3487 — Product name on the portfolio history list

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History list

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3487.feature`

## Scenario Title

User sees the product name is displayed on the portfolio history list

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. `@XPro` is an unresolved declaration with no Android package mapping. This scenario has no `@Test` tag, so the scoped command overrides the runner tag with `@App`.

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
11. Then the user sees transaction record item "Product Name" on the portfolio history list

## Expected Assertions

`Product Name` comes only from `MobileAbstractComponents.getProductName(AppMarketsPage.tradeSymbol)`, for example Gold for XAUUSD. A blank name, `symbol not found`, or the product code itself does not satisfy this check. The latest Open and Close records are the ones whose identifier is the product code, chosen by timestamp. Each of those records must show the product name in its text or content description. The code may remain in the identifier slot. The Close timestamp is not earlier than the Open timestamp.

## Entity coverage

### EBL_MT5

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified in this change.
- UI path: `AppPortfolioPage.historyTransactionItemDisplayed("Product Name")`.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection. Earlier history evidence showed the product code in the identifier slot, so this case fails if the row does not also show the product name.
- Runtime result: not run.

### EIEHK

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261007.apk.zip`. Installed build was not verified in this change.
- UI path: the latest EIEHK card that contains both `Open:` and `Close:` must show the product name.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection. No new device inspection in this change.
- Runtime result: not run.

### Unresolved

- `@XPro` has no supported Android package mapping.

## Scoped commands

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3487.feature \
  -Dcucumber.filter.tags="@App"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3487.feature \
  -Dcucumber.filter.tags="@App"
```
