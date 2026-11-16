# APP-2969 — Take Profit and Stop Loss toggle on the modify order page

Complete the modify-order Take Profit and Stop Loss toggle for the existing APP-2969 scenario. Follow AGENTS.md and AGENTS-APP.md.

## Module / Page

Native App trade — Portfolio pending orders, then Modify Order

## Feature File

`src/test/java/Features/NativeApp/trade/APP-2969.feature`

## Scenario Title

Take Profit & Stop Loss toggle on successfully on the modify order page

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. `@XPro` is an unresolved declaration with no Android package mapping. The tag is left in place. iOS is not implemented and is not claimed.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- symbol=XAUUSD

Run once for `entity=EBL_MT5` and once for `entity=EIEHK`. Use the credentials already recorded for the App login binding. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Direction: BUY
- Order type: Buy Limit
- Symbol: XAUUSD, from `TradeSymbol.properties` unless `-Dsymbol` overrides it
- Lot size: `TradeRecord.LOT_SIZE` = 0.5
- The placed pending order does not include Take Profit or Stop Loss

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
5. And the user taps back button on the app trade view
6. When the user taps button "Portfolio" on the app footer
7. And the user selects tab "Pending Orders" on the portfolio page
8. And the user taps "edit" button of the record row on the portfolio page
9. And the user switches on take profit and stop loss on the modify order page
10. Then the user sees that the Take Profit and Stop Loss toggles are turned "on" on the modify order page

## Expected Assertions

- The Modify Order header is visible before the switch is tapped.
- The switch is the `android.widget.Switch` on the same row as the label that names both Take Profit or Target Profit and Stop Loss. `Set Target Profit` is also accepted. Price fields that contain `(` are not the switch row.
- Do not tap the first Switch on the page. Pending Order sits on a different row.
- Do not treat a Stop Loss field label as proof that the switch is on.
- Visibility uses UiAutomator `bounds` before `getLocation()`, because React Native often reports location `0,0`.
- The action waits until that switch `checked` attribute is true. The assertion reads the same attribute. A missing switch or a missing `checked` value fails the step.
- Do not tap Modify Order, and do not cancel the pending order. The order remains on the account.

## Implementation Requirements

- Reuse `AppPOManager.getAppModifyOrderPage()`.
- Keep the new step text on the modify order page. Do not bind these steps to the instrument details page.
- Portfolio edit accepts a compact Modify Order header as a successful destination, in addition to Edit Position.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`. Artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. `mt5uat` selects the same package and artifact. Installed versionName was not read.
- UI path: Markets, Buy Limit pending order, back, Portfolio, Pending Orders, row edit, Modify Order Take Profit and Stop Loss switch.
- Status: Android path selects the Switch on the same row as the Take Profit / Stop Loss label, using bounds. The screen was not inspected on a device for this change.
- Evidence: the previous following-sibling locator timed out on Modify Order. No device hierarchy was captured for the same-row lookup.
- Runtime result: not run for the same-row lookup.

### EIEHK

- Target: env `bauuat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`. Artifact `com.efsg.eietrading.android_0.0.220-20261012.apk.zip`. `mt5uat` selects the same package and artifact. Installed versionName was not read.
- UI path: same portfolio route. The order ticket uses a Switch beside `Set Target Profit`, under Pending Order. Modify Order selects the Switch on the Take Profit / Stop Loss row by bounds.
- Status: Android path implemented. The Modify Order screen was not inspected on a device for this change. iOS is not implemented.
- Evidence: the previous following-sibling locator timed out on Modify Order. No device hierarchy was captured for the same-row lookup.
- Runtime result: not run for the same-row lookup.

### Unresolved

- `@XPro` has no supported Android package mapping. No `@XPro` behavior was added.

## Scoped commands

Not executed.

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-2969.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"

mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-2969.feature \
  -Dcucumber.filter.tags="@Test and @EIEHK"
```
