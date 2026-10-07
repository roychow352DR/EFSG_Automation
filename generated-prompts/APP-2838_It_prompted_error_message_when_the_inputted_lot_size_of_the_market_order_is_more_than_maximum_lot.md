# APP-2838 — Lot size above the maximum on instrument details

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — instrument details market-order lot validation

## Feature File

`src/test/java/Features/NativeApp/trade/APP-2838.feature`

## Scenario Title

It prompted error message when the inputted lot size of the market order is more than maximum lot

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro`

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
- Field label: `EBL_MT5` uses `Lots`. `EIEHK` uses `Lot` under the value. The feature step still says `Lot Size`.
- Maximum lot: `TradeSymbolConfig.getMaxLotSize()` for the runtime symbol. `XAUUSD` is `5.00`.
- Entered lot: that maximum plus `0.01`, formatted to two decimal places. `XAUUSD` enters `5.01`.
- Expected message: `Invalid lot size`

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. When the user taps symbol on the app markets page
5. And the user selects direction "BUY" on the app trade view
6. And the user fills in the text field "Lot Size" with the value more than maximum on the instrument details page
7. Then the user sees an error message "Invalid lot size" is displayed on the instrument details page

## Expected Assertions

- The instrument details page shows text or content-desc containing `Invalid lot size`.
- A missing message fails the step.
- Do not place, confirm, or cancel an order.

## Implementation Requirements

- Reuse `AppPOManager.getAppInstrumentDetailsPage()`.
- Resolve the lot EditText from the `Lot` / `Lots` label. Do not type into the first EditText on the page.
- Format the out-of-range lot with two decimal places before Android key entry.
- Keep Stop Loss, Take Profit, and Price entry on their existing fields.
- Keep unrelated tradeSteps bindings.

## Output

1. Feature kept at APP-2838.feature
2. Entity login through the existing App login step
3. Lot entry through the label-resolved Lots field
4. Assumptions
5. Scoped Maven command (not executed in this task)
