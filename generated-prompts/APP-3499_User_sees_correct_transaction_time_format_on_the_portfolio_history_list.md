# APP-3499 — Transaction time format on the portfolio history list

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History list

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3499.feature`

## Scenario Title

User sees correct transaction time format on the portfolio history list

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @AppTest`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. This scenario has no `@Test` tag. The scoped command uses `@AppTest`.

## Environment

- product=app
- env=bauuat
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
11. Then the user sees the transaction time format is correct on the portfolio history list

## Expected Assertions

The scenario opens a BUY position, closes it, and opens Portfolio History. Open and Close transaction times for `AppMarketsPage.tradeSymbol` and the captured lot volume must both be today's local date. The full trimmed displayed text must pass `MobileAbstractComponents.dateValidator` (strict `uuuu-MM-dd HH:mm:ss`). Extra characters, a slash date, or a missing seconds field fail. `historyTimestamp` taking `substring(0, 19)` is not a pass. Close must not be earlier than Open. A missing symbol, volume, row/card, or timestamp fails. Two unread times are not a success.

EBL Open and Close stay separate rows. Among those rows, a strict today timestamp is chosen by the latest parsed time, then the smaller top. EIEHK selects one card by the latest strict today Open time (smaller top on a tie) and then checks that card's Open and Close. Close must not be before Open on that card. A date-like Close that is not a strict today timestamp is included in the detail. When matching rows exist but none of those times are strict and today, the failure detail includes the action and the actual displayed text, preferring the smallest top.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified.
- UI path: one row per deal. Open keeps the traded side. Close uses the opposite side through `expectedHistoryDirection`. The timestamp is the nearest date-like text below the action, with `0 < delta <= px(56)`, that contains `-` or `/` and `:`.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection only.
- Runtime result: not run.

### EIEHK

- Target: env `bauuat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261007.apk.zip`. Installed build was not verified.
- UI path: one card. The check selects one card by the latest strict today Open time (smaller top on a tie) and then checks that card's Open and Close. `Open:` and `Close:` share the original side and volume. The timestamp is the date-like text to the right of the caption, with the node left to the right of the caption and the vertical center within `px(12)`. Text nodes are searched first, then `visibleHistoryLabels`, using the same caption window as `eiehkActionTimestamp`.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection only.
- Runtime result: not run.

## Scoped commands

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3499.feature \
  -Dcucumber.filter.tags="@AppTest"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3499.feature \
  -Dcucumber.filter.tags="@AppTest"
```
