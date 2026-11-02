# APP-257 — Partial close of an open position

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Close Position page and the Positions tab on the trade view

## Feature File

`src/test/java/Features/NativeApp/trade/APP-257.feature`

## Scenario Title

User can partially close position successfully

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. Both entities are required. `@XPro` is an unresolved declaration with no Android package mapping. The tag is left in place. iOS is not implemented and is not claimed.

## Environment

- product=app
- env=mt5uat
- platform=ANDROID
- symbol=XAUUSD

Use the entity credentials already loaded by `the user logs in on the App login page`. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Direction: BUY, stored on `AppTradeView.selectedDirection` by the existing create-position flow
- Open lot chip: `TradeRecord` lot `0.5`, which the close-position field normalizes to 2 decimals (`0.50` when that is the live value)
- Lot step: `TradeSymbolConfig.getStepSize(entity)`. EIEHK is `0.01`. EBL_MT5 is `0.05`. The step is not hardcoded from the feature tag.
- Minimum lot: `TradeSymbolConfig.getMinLotSize(symbol)`. XAUUSD and XAGUSD are `0.01`.
- Edited close lot: live close-position lots minus the runtime step. When the live field is `0.50`, EIEHK submits `0.49` and EBL_MT5 submits `0.45`.
- Remaining open lot: original live lots minus the lot actually submitted. When the live field starts at `0.50`, EIEHK remains `0.01` and EBL_MT5 remains `0.05`.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. And the user creates a "BUY" position on the instrument details page
5. When the user taps "close" cta button on the app trade view
6. And the user edit the lot size which is smaller than the current lot size on the close position page
7. And the user taps button "Close Position" on the instrument details page
8. And the user taps button "Close Position" on the confirmation pop up
9. Then the user sees the message "Your position is closed" is displayed at the dialogue
10. And the user sees correct lot size of the partially closed position on the position tab of app trade view

Feature step text is unchanged.

## Expected Assertions

- The close-position lots field is the first `android.widget.EditText`. A missing or blank value fails.
- The edited value is the live field minus the runtime step, scale 2 HALF_UP. It must be strictly smaller than the live value, greater than zero, and at least the symbol minimum lot.
- The field is typed with `typeWithAndroidKeys` and re-read. `originalLots` and `partialCloseLots` are stored only when the re-read matches the edited value by `BigDecimal` compare. The edit step does not tap Close Position.
- The edit step fails when the re-read is blank, is not strictly smaller than `originalLots`, or does not equal `partialCloseLots`.
- The Positions assertion reads the displayed volume for the traded symbol and selected direction. Both the displayed volume and `remainingOpenLots()` must be non-null and non-blank before `Assert.assertEquals`.
- A missing row or volume throws `NoSuchElementException`. The expected remainder is never substituted as the displayed value.
- After that assertion passes, `AppTradeView.closePosition()` fully closes the remainder. An assertion failure happens before that cleanup.

## Entity coverage

### EBL_MT5

- Target: env `mt5uat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified beyond the package being on the guest Sign Up / Login screen.
- Lot step: `0.05`. Example from a live field of `0.50`: close `0.45`, remaining open lot `0.05`.
- UI path: `AppClosePositionPage.editLotSizeSmallerThanCurrent` and `remainingOpenLots`; `AppTradeView.openPositionVolume` on the Positions tab. EBL_MT5 position labels are read from text, and content-desc is read as well.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection of the close-position EditText, `TradeSymbolConfig.getStepSize`, and the portfolio-history pattern of volume beside BUY/SELL. The close-position field and the Positions-row volume were not observed live.
- Runtime result: not run.

### EIEHK

- Target: env `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261012.apk.zip`. Installed build was not verified beyond the package being on the guest Sign Up / Login screen.
- Lot step: `0.01`. Example from a live field of `0.50`: close `0.49`, remaining open lot `0.01`.
- UI path: the same Close Position editor and the same Positions reader. On the XAGUSD trade view the symbol stays in the header. Each Open Positions row is `BUY 0.01` on one line, then the open price and floating P/L. The reader checks text and content-desc. It uses a symbol-on-row match when that label exists, and otherwise the top direction row under Open Positions while the header shows the symbol.
- Status: implemented for Android. iOS is not implemented.
- Evidence: failure recording `app_videos/User can partially close position successfully.mp4` on emulator-5554, EIEHK, XAGUSD, BUY. Confirmation volume was `0.49 Lots`. The top Open Positions row then showed `BUY 0.01` at `2026-10-28 17:18:25`. An older `BUY 0.50` stayed below it. The first assertion failed because the reader required `XAGUSD` on the row.
- Runtime result: failed once on the row lookup. The header-plus-top-row reader has not been re-run.

### Unresolved

- `@XPro` has no supported Android package mapping. No `@XPro` behavior was added.

## Source evidence and device screens

- Source: `TradeRecord.createOpenPosition` fills the `0.5` lot chip. `AppClosePositionPage` reads the first lots EditText and normalizes to 2 decimals. `TradeSymbolConfig` selects the step from the runtime entity.
- Device: emulator-5554. The EIEHK failure recording shows the XAGUSD Positions list. EBL_MT5 Positions rows were not in that recording.
- EIEHK row: header `XAGUSD`; open-position line `BUY 0.01` with the lot immediately after the direction. The quote buttons at the bottom are excluded. A bare number is accepted only when it is greater than zero and at most 20, so the silver price is not used as the volume. The asserted row is the top matching direction row.

## Scoped commands

Not executed.

```bash
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-257.feature -Dcucumber.filter.tags="@Test"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-257.feature -Dcucumber.filter.tags="@Test"
```
