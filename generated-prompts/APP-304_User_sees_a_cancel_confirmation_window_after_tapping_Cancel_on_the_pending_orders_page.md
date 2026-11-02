# APP-304 — Cancel confirmation on the pending orders page

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Pending Orders list on the instrument trade view, and the cancel confirmation overlay

## Feature File

`src/test/java/Features/NativeApp/trade/APP-304.feature`

## Scenario Title

User sees a cancel confirmation window after tapping Cancel on the pending orders page

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest @Test`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. Both entities are required. `@XPro` is an unresolved declaration with no Android package mapping. The tag is left in place. iOS is not implemented and is not claimed.

## Environment

- product=app
- env=mt5uat
- platform=ANDROID
- symbol=XAUUSD

Use the entity credentials already loaded by `the user logs in on the App login page`. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Direction: BUY
- Order type: Buy Limit, displayed on the pending-order row as `Limit`
- Target price: the price captured by `AppInstrumentDetailsPage.stopOrderPrice` while placing the order
- Dialogue title: `Confirmation`
- Confirm button: `Cancel Order`

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Me" on the app footer
4. And the user toggles on trade confirmation on the app setting page
5. And the user taps button "Markets" on the app footer
6. And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
7. When the user taps "cancel" cta button on the app trade view
8. Then the user sees a dialogue with title "Confirmation" is prompted on the app trade view

## Expected Assertions

- The cancel tap targets the pending-order row for the created direction and captured target price. EBL also matches the displayed type `Limit` or `Stop`. EIEHK rows omit that type label, so direction plus price identifies the row.
- The leftmost of the three row icons is the cancel control. It has no text or content-desc on the observed EBL row.
- The dialogue is `resource-id='RNE__Overlay'`. Its top TextView must be non-blank and equal `Confirmation`, and `Cancel Order` must be visible in the same overlay.
- A missing overlay, a blank title, or a different title fails before cleanup.
- After the assertion passes, the step taps `Cancel Order` and removes the pending order created by this scenario.

## Entity coverage

### EBL_MT5

- Target: env `mt5uat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, installed `versionName=0.0.303` on emulator-5554. Artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`.
- UI path: instrument XAUUSD, Positions, Pending Orders. Cancel is the left icon of the row cluster. The overlay title text is `Confirmation`. The confirm control content-desc is `Cancel Order`.
- Status: implemented for Android. iOS is not implemented.
- Evidence: Appium inspection of an existing BUY Limit row. The cancel icon bounds were `[892,897][995,999]`. The overlay first TextView was `Confirmation`. The dialogue was dismissed with the close icon. That existing order was not cancelled.
- Runtime result: not run.

### EIEHK

- Target: env `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, installed `versionName=0.0.220` on emulator-5554. Artifact `com.efsg.eietrading.android_0.0.220-20261012.apk.zip`.
- UI path: instrument XAGUSD, Positions, Pending Orders. The row shows `BUY` with the lot, the order time, and the target price. It does not show `Limit`. Cancel is the left icon of the row cluster. The place-order overlay title is `Confirmation`.
- Status: implemented for Android. The cancel-row lookup now accepts a row that has the direction and target price when the type label is absent. iOS is not implemented.
- Evidence: failure recording `app_videos/User sees a cancel confirmation window after tapping Cancel on the pending orders page.mp4` on emulator-5554, EIEHK, XAGUSD. The Pending Orders row was `BUY 0.50` at `27.792`, timestamp `2026-10-29 18:28:25`. The lookup required a `Limit` text and timed out.
- Runtime result: failed on the cancel tap. The direction-and-price lookup has not been re-run.

### Unresolved

- `@XPro` has no supported Android package mapping. No `@XPro` behavior was added.

## Scoped commands

Not executed.

```bash
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-304.feature -Dcucumber.filter.tags="@Test"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-304.feature -Dcucumber.filter.tags="@Test"
```
