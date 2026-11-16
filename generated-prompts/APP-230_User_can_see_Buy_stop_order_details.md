# APP-230 — Buy Stop pending order details

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Pending Order Details

## Feature File

`src/test/java/Features/NativeApp/trade/APP-230.feature`

## Scenario Title

User can see Buy stop order details

## Tags

`@App @Smoke @Regression @Trade @EIEHK @Test`

Effective entity tag: `@EIEHK` only. `@EBL_MT5` and `@XPro` are not on this scenario. iOS is out of scope.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- symbol=XAGUSD

Login uses the existing `the user logs in on the App login page` step. Do not copy credentials into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. And the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
5. When the user taps "detail" cta button on the app trade view
6. Then the user sees limit order values are displayed correctly with the user input value on the pending order details page

## Expected Assertions

`placePendingOrder` stores direction `BUY`, order type `Buy Stop`, the lot size read back after the `0.5` chip, and `AppInstrumentDetailsPage.stopOrderPrice`. It does not tap a validity chip. The pending order details page must show the app default validity `Today`.

The step compares these labels on `AppPendingOrderDetailsPage`:

- Direction = captured `BUY`
- Order Type = `Stop` (the second word of `Buy Stop`)
- Volume = captured lot size, both sides canonicalized to two decimal places
- Target Price = `stopOrderPrice`, both sides normalized to the `TradeSymbolConfig` decimal places for the symbol after commas are removed. The step passes that scale into the page because `TradeSymbolConfig` is test source.
- Validity = `Today`

Each displayed value and each expected value must be present and non-blank before the equality check. A missing value fails. Validity `Today` is supplied by this step only. `getValidationValue("Validity")` still returns the stored selection, so an explicit GTC case does not inherit this default.

Take profit and stop loss are not part of this placement and are not asserted.

After the checks pass, `cancelPendingOrderInDetail` cancels that pending order.

## Entity coverage

### EIEHK

- Target: env `bauuat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261012.apk.zip`. Symbol `XAGUSD`.
- UI path: `TradeRecord.placePendingOrder`, `AppTradeView.tapCtaButton("detail")`, `AppPendingOrderDetailsPage.getDetailValue` / `comparableUserInput`, then `AppTradeView.cancelPendingOrderInDetail`.
- Validity: unselected during placement; expected details value is the app default `Today`.
- Status: implemented for Android from existing page-object readers. No device hierarchy was captured for this case.
- Runtime result: not run.
- Approved limitation: iOS login and iOS pending-order details are outside this scenario.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAGUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-230.feature \
  -Dcucumber.filter.tags="@Test"
```
