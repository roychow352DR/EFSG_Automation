# APP-119 — Lv1 registration gate after BUY

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Markets symbol, trade-view BUY quote, and the Lv1 registration gate

## Feature File

`src/test/java/Features/NativeApp/trade/APP-119.feature`

## Scenario Title

Lv1 user sees a page with This service is for registered user only after tapping on Sell/Buy button

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @Test`

## Environment

- product=app
- env=mt5uat
- entity=EBL_MT5
- platform=ANDROID

Lv1 is the app in an empty login state. Do not log in.

## Test Steps

1. Given the user launch the app
2. And the user taps button "Markets" on the app footer
3. When the user taps symbol on the app markets page
4. And the user taps direction "BUY" on the app trade view
5. Then the user sees a page with a content "This service is for registered user only."

## Expected Assertions

- After BUY is tapped, wait until the registration gate is visible.
- The visible page content equals `This service is for registered user only.` exactly, including the trailing period.
- A Markets symbol label does not satisfy the check. The text or content-desc on a displayed element must equal that sentence.
- Do not tap Back, close a position, or cancel an order after this assertion.

## Implementation Requirements

- Reuse `the user launch the app`, `the user taps button {string} on the app footer`, and `the user taps symbol on the app markets page`.
- Leave the app in the empty login state. Do not add a login step.
- Tap BUY through `AppPOManager.getAppTradeView().tapDirectionQuote(direction)`.
- `tapDirectionQuote` is Android only. It reuses the existing quote locators (`content-desc` starting with `BUY,` and the parent clickable groups), uses `tapBottomMost` when the order ticket is closed and `tapVisible` when it is open, and returns after the first successful tap.
- Do not call `selectDirection` or `waitForOrderTicket`. Those wait for `Market Order`, `Limit / Stop Order`, or `Lots`, which an Lv1 gate does not show.
- Read the gate through `getVisiblePageContent(expectedContent)`. Wait up to 20 seconds for a displayed Android element whose text or content-desc equals the expected sentence. Return that observed value. A missing sentence fails the wait. Do not click the element.
