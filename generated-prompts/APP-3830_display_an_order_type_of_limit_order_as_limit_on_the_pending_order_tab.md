# APP-3830 — Limit order type on the portfolio pending order tab

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio Pending Orders tab

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3830.feature`

## Scenario Title

Display an order type of buy limit order as limit on the portfolio pending order tab

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @AppTest @Test`

Effective entity tag: `@EBL_MT5` only. iOS is out of scope.

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
4. When the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
5. And the user taps back button on the app trade view
6. And the user taps button "Portfolio" on the app footer
7. And the user selects tab "Pending Orders" on the portfolio page
8. Then the user sees order type "Limit" of pending order is displayed on the pending order tab of portfolio page

## Expected Assertions

`AppPortfolioPage.createdPendingOrderTypeDisplayed("Limit")` finds the created pending card on the portfolio Pending Orders tab. The card must show the step text `Limit`, `AppTradeView.selectedDirection`, `AppMarketsPage.tradeSymbol`, and `AppInstrumentDetailsPage.stopOrderPrice`.

The expected type, symbol, direction, and price must be present before a match. A missing or blank value fails. `Limit` must be the card's own text. `Buy Limit` does not satisfy it. The price comparison ignores thousands separators and trailing-zero differences.

After the assertion passes, `cancelCreatedPendingOrder` cancels that same card. This scenario does not open a filter. A blank stored filter means the list is unfiltered. A stored filter that is neither `All` nor the traded symbol still fails before a tap.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`.
- Device evidence: emulator-5554, density 480, display 1280x2856, package in the foreground. Portfolio Pending Orders tab text is `Pending\nOrders`. One card showed `Gold`, `XAUUSD`, type `Limit` at about `[581,1530][877,1581]`, `BUY`, volume `0.50`, price `3966.55`, and timestamp `2026-10-26 10:10:39`. The type node is `android.widget.TextView` with no resource-id. The close icon is the leftmost icon on the right of the card.
- UI path: `AppPortfolioPage.createdPendingOrderTypeDisplayed` and `AppPortfolioPage.cancelCreatedPendingOrder`.
- Status: implemented for Android.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3830.feature \
  -Dcucumber.filter.tags="@Test"
```
