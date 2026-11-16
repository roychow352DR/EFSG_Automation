# APP-2844 — Contract value after lot size change

Create the EIEHK instrument-details contract value assertion for the existing APP-2844 scenario. Follow AGENTS.md and AGENTS-APP.md.

## Module / Page

Native App trade — instrument details market-order ticket

## Feature File

`src/test/java/Features/NativeApp/trade/APP-2844.feature`

## Scenario Title

The contract value is displayed correctly after the lot size changed

## Tags

`@App @Smoke @Regression @Trade @EIEHK @XPro @Test`

Effective entity tag: `@EIEHK`. `@XPro` is an unresolved declaration with no Android package mapping. The tag is left in place. iOS is not implemented and is not claimed.

## Environment

- product=app
- env=bauuat
- entity=EIEHK
- platform=ANDROID
- symbol=XAUUSD

Use the credentials already recorded for the App login binding. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Direction: BUY
- Symbol: XAUUSD
- Lot chip: 0.5
- Contract size: `TradeSymbolConfig.getContractSize("XAUUSD")` = 100
- Expected contract value: visible BUY price text × Lots field × contract size, half-up to 2 decimal places

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. When the user taps symbol "XAUUSD" on the app markets page
5. And the user selects direction "BUY" on the app trade view
6. And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
7. Then the user sees the "Contract Value" value is displayed correctly on the instrument details page

## Expected Assertions

- Capture one ticket page-source snapshot.
- Read Contract Value from that snapshot and normalize it with the existing Contract Value normalizer.
- Read the selected direction price from visible `text` in the same snapshot. The text is `BUY` followed by `3922.75`, or by `3922` and `.75`. Do not read quote content-desc for this assertion.
- Read the current Lots field and normalize it to 2 decimal places.
- Expected value is `quote × lots × contract size`, scaled to 2 decimal places with half-up rounding.
- Displayed and expected values must both be present and non-blank before they are compared.
- Do not place, confirm, or cancel an order.

## Implementation Requirements

- Reuse `AppPOManager.getAppInstrumentDetailsPage()`.
- Keep the existing feature step text.
- Branch only the Contract Value label in `the user sees the {string} value is displayed correctly on the instrument details page`.
- Leave the Est. Margin and Lots paths on that step unchanged.
- Reset `expectedContractValue` with the other captured ticket values.

## Entity coverage

### EIEHK

- Target: env `bauuat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`. Artifact `com.efsg.eietrading.android_0.0.220-20261012.apk.zip`. `mt5uat` selects the same package and artifact. Installed versionName was not read for this change.
- UI path: Markets, XAUUSD, BUY quote, 0.5 lot chip, instrument-details Contract Value.
- Status: Android assertion implemented. iOS is not implemented.
- Evidence: one assertion run compared content-desc quote `3922.92` with displayed Contract Value `196137.50` (`expected [196146.00] but found [196137.50]`). Contract value validation now reads the visible price text beside `BUY` instead of that content-desc.
- Runtime result: not run for the visible-text price calculation.

### Unresolved

- `@XPro` has no supported Android package mapping. No `@XPro` behavior was added.

## Scoped command

Not executed.

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-2844.feature \
  -Dcucumber.filter.tags="@Test and @EIEHK"
```
