# APP-1441 — Lots minus stepper on instrument details

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — instrument details Lots stepper

## Feature File

`src/test/java/Features/NativeApp/trade/APP-1441.feature`

## Scenario Title

The lot value can be adjust by tapping minus button on the instrument details page

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

## Environment

- product=app
- env=mt5uat
- entity comes from `-Dentity` or `GlobalData.properties`: `EIEHK` or `EBL_MT5`
- platform=ANDROID
- symbol=XAUUSD

Use the credentials already recorded for the runtime entity. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Login identity: `AppCredential` for the runtime entity. `EBL_MT5` uses its username mapping. `EIEHK` uses its email mapping.
- App package: `AppConfig` for the runtime entity.
- Direction: BUY
- Lot chip: 0.5. `EBL_MT5` shows `0.5`. `EIEHK` shows `0.5 Lot`.
- Field label: `EBL_MT5` uses `Lots`. `EIEHK` uses `Lot` under the value. The feature step still says `Lots`.
- Volume step: `TradeSymbolConfig.getStepSize()` for the runtime entity. `EBL_MT5` is `0.05`. `EIEHK` is `0.01`.
- Expected after one minus tap: captured lot value minus that step. `0.50` becomes `0.45` on `EBL_MT5` and `0.49` on `EIEHK`.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. When the user taps symbol on the app markets page
5. And the user selects direction "BUY" on the app trade view
6. And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
7. And the user taps button "-" of the "Lots" input text field on the instrument details page
8. Then the user sees the lots value is decreased by default step size on the instrument details page

## Expected Assertions

- Capture the Lots EditText from the UI immediately before the minus tap.
- After the tap, the Lots EditText equals that captured value minus `TradeSymbolConfig.getStepSize()`.
- Missing or blank displayed/captured values must fail. Do not treat two nulls as success.
- Do not place, confirm, or cancel an order.

## Implementation Requirements

- Reuse `AppPOManager.getAppInstrumentDetailsPage()`.
- Reuse the existing tap-stepper binding. The feature names the field `Lots`, not `Lot Size`.
- Recognize both field labels. `EBL_MT5` uses `Lots` above the value. `EIEHK` uses `Lot` under the value.
- Lot chips follow the runtime entity. `EBL_MT5` taps `0.5`. `EIEHK` taps `0.5 Lot`.
- The minus control is the 72px icon on the left edge of the lot value. On `EIEHK` that icon sits just outside an EditText whose reported bounds cover the whole row. Tap the icon center.
- Step size follows the runtime entity: `0.05` for `EBL_MT5`, `0.01` for `EIEHK`.
- Stop Loss, Take Profit, and Price keep the existing row: filled `[minus] [field] [clear] [plus]`, empty row without clear. Their minus stays the rightmost compact control left of the field.
- Dismiss the Android keyboard before reading stepper bounds so the row has not shifted.
- Keep unrelated tradeSteps bindings.

## Output

1. Feature kept at APP-1441.feature
2. Then binding for the default step-size decrease on the instrument details page
3. Field-specific minus position for Lots versus price rows
4. Assumptions
5. Scoped Maven command (not executed in this task)
