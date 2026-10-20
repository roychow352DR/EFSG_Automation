# APP-3684 — Portfolio history open and close records

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History list

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3684.feature`

## Scenario Title

User sees open and close position histories on the portfolio history list

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest @Test`

## Environment

- product=app
- env=bauuat
- entity=EBL_MT5
- platform=ANDROID
- symbol=XAUUSD

Use the entity credentials already loaded by `the user logs in on the App login page`. Do not copy passwords into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. And the user creates a "BUY" position on the instrument details page
5. When the user taps "close" cta button on the app trade view
6. And the user taps button "Close Position" on the instrument details page
7. And the user taps button "Close Position" on the confirmation pop up
8. And the user taps back button on the app trade view
9. And the user taps button "Portfolio" on the app footer
10. And the user selects tab "History" on the portfolio page
11. Then the user sees an open position record on the portfolio history list
12. And the user sees a close position record on the portfolio history list

## Expected Assertions

- EBL_MT5: the History list shows a separate Open row for the traded symbol, original direction, captured lot size, and today's date, plus a Close row on the opposite side.
- EIEHK: one history card shows the symbol, the original direction, and the captured lot size. The same card has an `Open:` line and a `Close:` line, each with today's date. The close line keeps the original direction.
- A missing symbol, direction, volume, or date is a failed assertion.

## Device evidence

Android emulator `emulator-5554`.

- EBL package `com.emperorfs.ebltrading.android`: separate text nodes for the symbol, `Open` or `Close`, direction, volume, and `yyyy-MM-dd HH:mm:ss`. Closing a BUY position is shown as Close SELL.
- EIEHK package `com.efsg.eiehktrading.android`: one card with symbol, original direction, volume, then `Open:` price/time and `Close:` price/time. Closing a BUY position stays BUY.
