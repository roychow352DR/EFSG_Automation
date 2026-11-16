# APP-2970 — Take Profit and Stop Loss toggle off on the modify order page

Complete the modify-order Take Profit and Stop Loss toggle-off path for the existing APP-2970 scenario. Follow AGENTS.md and AGENTS-APP.md.

## Module / Page

Native App trade — instrument details pending order, trade view edit, then Modify Order

## Feature File

`src/test/java/Features/NativeApp/trade/APP-2970.feature`

## Scenario Title

Take Profit & Stop Loss toggle off successfully on the modify order page

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
5. When the user taps "edit" cta button on the app trade view
6. And the user switches on take profit and stop loss on the modify order page
7. And the user switches off take profit and stop loss on the modify order page
8. Then the user sees that the Take Profit and Stop Loss toggles are turned "off" on the modify order page

## Expected Assertions

- The Modify Order header is visible before the switch is tapped.
- The switch is the `android.widget.Switch` on the same row as the label that names both Take Profit or Target Profit and Stop Loss. `Set Target Profit` is also accepted. Price fields that contain `(` are not the switch row.
- Do not tap the first Switch on the page. Pending Order sits on a different row.
- Switch on waits until that switch `checked` attribute is true. Switch off taps the same switch and waits until `checked` is false. A switch that is already in the requested state is left untouched.
- The assertion reads the same `checked` attribute and expects false. A missing switch or a missing `checked` value fails the step.
- After the assertion, the existing step taps back and cancels the pending order created by this scenario.

## Implementation Requirements

- Reuse `AppPOManager.getAppModifyOrderPage()`.
- Keep the new step text on the modify order page. Do not bind this step to the instrument details page.
- Android only. An iOS driver throws `UnsupportedOperationException`.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`. Artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. `mt5uat` selects the same package and artifact. Installed versionName was not read.
- UI path: Markets, Buy Limit pending order, trade-view edit, Modify Order Take Profit and Stop Loss switch on, then off.
- Status: Android path selects the Switch on the same row as the Take Profit / Stop Loss label, using bounds, then turns it off. The screen was not inspected on a device for this change.
- Evidence: source inspection of `AppModifyOrderPage.switchOffTakeProfitAndStopLoss()` and the existing same-row switch lookup. No device hierarchy was captured for this case.
- Runtime result: not run.

### EIEHK

- Target: env `bauuat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`. Artifact `com.efsg.eietrading.android_0.0.220-20261012.apk.zip`. `mt5uat` selects the same package and artifact. Installed versionName was not read.
- UI path: same trade-view route. The order ticket uses a Switch beside `Set Target Profit`, under Pending Order. Modify Order selects the Switch on the Take Profit / Stop Loss row by bounds, then turns it off.
- Status: Android path implemented. The Modify Order screen was not inspected on a device for this change. iOS is not implemented.
- Evidence: source inspection of the same-row switch lookup. No device hierarchy was captured for this case.
- Runtime result: not run.

### Unresolved

- `@XPro` has no supported Android package mapping. No `@XPro` behavior was added.

## Scoped commands

Not executed.

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-2970.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"

mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-2970.feature \
  -Dcucumber.filter.tags="@Test and @EIEHK"
```
