# APP-2834 — Volume minus stepper on instrument details

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — instrument details Lots / Lot Size stepper

## Feature File

`src/test/java/Features/NativeApp/trade/APP-2834.feature`

## Scenario Title

The volume input value is reduced by step size when the minus button is tapped

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

## Environment

- product=app
- env=mt5uat
- entity=EBL_MT5
- platform=ANDROID
- symbol=XAUUSD

Use the credentials already recorded in APP-2834.feature. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Direction: BUY
- Lot chip: 0.5
- Observed Android UI label: `Lots` (feature text remains `Lot Size`)
- Volume step: `TradeSymbolConfig.getStepSize()` (`0.05` for the inspected XAUUSD ticket)
- Expected after one minus tap: `0.50 - 0.05 = 0.45`

## Test Steps

1. Given the user launch the app
2. And the user login as username from the feature file on App login page
3. And the user taps button "Markets" on the app footer
4. When the user taps symbol on the app markets page
5. And the user selects direction "BUY" on the app trade view
6. And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
7. When the user taps button "-" of the "Lot Size" input text field on the instrument details page
8. Then the user sees the value "Lot Size" is decreased by the step size on the instrument details page

## Expected Assertions

- After the 0.5 chip is selected, capture the Lots EditText value from the UI.
- After tapping the minus control on the same row, the Lots EditText equals captured value minus `TradeSymbolConfig.getStepSize()`.
- Missing or blank displayed/captured values must fail. Do not treat two nulls as success.
- Do not place, confirm, or cancel an order.

## Implementation Requirements

- Reuse `AppPOManager.getAppInstrumentDetailsPage()`.
- Reuse the existing tap-stepper binding (`the user taps button {string} of the {string} input text field on the instrument details page`).
- Extend `adjustPrice` so Lot Size / Lots uses the Lots EditText row, not TP/SL labels.
- Locate minus/plus as compact clickable ViewGroups on the Lots row (icons have no `+`/`-` text). Prefer accessibility id / text / row geometry over full hierarchy XPath.
- Add the missing Then binding. Lot Size plus/minus uses `TradeSymbolConfig.getStepSize()`, not a price tick.
- Keep unrelated tradeSteps bindings, including the pending-order-with-validity step.

## Appium MCP evidence (Android emulator, EBL_MT5 package `com.emperorfs.ebltrading.android`)

- Screen: Market Order ticket after Markets -> XAUUSD -> BUY quote chip
- Lots label: `//android.widget.TextView[@text='Lots']`
- Lots field: `android.widget.EditText` beside that label (values `0.10` default, `0.50` after chip `0.5`)
- 0.5 chip: accessibility id `0.5`
- Minus/plus: clickable `android.view.ViewGroup` immediately left/right of the EditText (72x72, no text / no content-desc)
- Observed delta: `0.50` -> `0.45` after one minus tap
- Submit CTA `BUY` was not tapped

## Output

1. Feature kept at APP-2834.feature
2. Step + page-object support for Lot Size minus
3. Assumptions
4. Scoped Maven command (not executed in this task)
