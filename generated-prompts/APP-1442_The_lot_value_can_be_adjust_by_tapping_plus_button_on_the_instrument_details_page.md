# APP-1442 — Lots plus stepper on instrument details

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — instrument details Lots stepper

## Feature File

`src/test/java/Features/NativeApp/trade/APP-1442.feature`

## Scenario Title

The lot value can be adjust by tapping plus button on the instrument details page

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

## Environment

- product=app
- env=mt5uat
- entity=EBL_MT5
- platform=ANDROID
- symbol=XAUUSD

Use the credentials already recorded in APP-1442.feature. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Login identity: `AppCredential` for the runtime entity. `EBL_MT5` uses its username mapping. `EIEHK` uses its email mapping.
- Direction: BUY
- Lot chip: 0.5
- Feature field name for the stepper: `Lots` (the fill step still says `Lot Size`)
- Volume step: `TradeSymbolConfig.getStepSize()` (`0.05`)
- Expected after one plus tap: captured Lots value plus `0.05` (0.50 becomes 0.55)

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. When the user taps symbol on the app markets page
5. And the user selects direction "BUY" on the app trade view
6. And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
7. And the user taps button "+" of the "Lots" input text field on the instrument details page
8. Then the user sees the lots value is increased by default step size on the instrument details page

## Expected Assertions

- Capture the Lots EditText from the UI immediately before the plus tap.
- After the tap, the Lots EditText equals that captured value plus `TradeSymbolConfig.getStepSize()`.
- Missing or blank displayed/captured values must fail. Do not treat two nulls as success.
- Do not place, confirm, or cancel an order.

## Implementation Requirements

- Reuse `AppPOManager.getAppInstrumentDetailsPage()`.
- Reuse the existing tap-stepper binding. The feature names the field `Lots`, not `Lot Size`.
- Lots plus is the square control immediately right of the Lots box (about 72px), including when that icon overlaps the EditText's right edge. Tap its center.
- Stop Loss, Take Profit, and Price keep the existing row: filled `[minus] [field] [clear] [plus]`, empty row without clear.
- Dismiss the Android keyboard and capture Lots before reading stepper bounds.
- Keep the APP-1441 Lots minus path and unrelated tradeSteps bindings.

## Output

1. Feature kept at APP-1442.feature
2. Then binding for the default step-size increase on the instrument details page
3. Lots plus position beside the field
4. Assumptions
5. Scoped Maven command (not executed in this task)
