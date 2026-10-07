# APP-1445 — Limit-order lot size above the maximum

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — EIEHK instrument details pending-order lot validation

## Feature File

`src/test/java/Features/NativeApp/trade/APP-1445.feature`

## Scenario Title

It prompted error message when the inputted lot size of the limit order is more than maximum lot

## Tags

`@App @Smoke @Regression @Trade @EIEHK`

## Environment

- product=app
- env=mt5uat
- entity=EIEHK
- platform=ANDROID
- symbol=XAUUSD

Use the credentials already recorded for the runtime entity. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Login identity: `AppCredential` for `EIEHK`.
- App package: `com.efsg.eiehktrading.android` from `AppConfig`.
- Direction: BUY
- Pending order control: `android.widget.Switch` following the `Pending Order` label. The label text includes a trailing space. The switch has no resource-id or content-desc.
- Field label: `Lot` under the value. The feature step still says `Lot Size`.
- Maximum lot: `TradeSymbolConfig.getMaxLotSize()` for the runtime symbol and entity. `EIEHK` `XAUUSD` is `10.00`.
- Entered lot: that maximum plus `0.01`, formatted to two decimal places. `XAUUSD` enters `10.01`.
- Expected message: `Invalid lot size`

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. When the user taps symbol on the app markets page
5. And the user selects direction "BUY" on the app trade view
6. And the user toggles on pending order on the instrument details page
7. And the user fills in the text field "Lot Size" with the value more than maximum on the instrument details page
8. Then the user sees an error message "Invalid lot size" is displayed on the instrument details page

## Expected Assertions

- The instrument details page shows text or content-desc containing `Invalid lot size`.
- A missing message fails the step.
- Do not place, confirm, or cancel an order.

## Implementation Requirements

- Reuse `AppPOManager.getAppInstrumentDetailsPage()`.
- Turn on the EIEHK Pending Order switch. Do not use the EBL_MT5 `Limit / Stop Order` dropdown.
- Leave the switch alone when it is already checked, then wait until `checked=true`.
- Resolve the lot EditText from the `Lot` / `Lots` label. Do not type into the first EditText on the page.
- Format the out-of-range lot with two decimal places before Android key entry.
- Keep unrelated tradeSteps bindings.

## Output

1. Feature kept at APP-1445.feature
2. Pending Order switch binding shared with APP-1444
3. Lot entry through the label-resolved Lots field
4. Assumptions
5. Scoped Maven command (not executed in this task)
