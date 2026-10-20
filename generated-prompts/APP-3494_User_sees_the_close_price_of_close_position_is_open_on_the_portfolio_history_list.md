# APP-3494 — Close price on the portfolio history list

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History list

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3494.feature`

## Scenario Title

User sees the close price of close position is open on the portfolio history list

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @AppTest @Test`

Effective entity tags: `@EBL_MT5` and `@EIEHK`. Both entities are required. The scenario has `@Test`, so the scoped commands keep that tag.

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

## Expected Assertions

The scenario opens a BUY position, closes it, and opens Portfolio History. It does not assert the close price. The confirmation Current Price and the history fill price differ, so this scenario does not compare them.

## Entity coverage

### EBL_MT5

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, artifact `com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip`. Installed build was not verified in this change.
- UI path: create a BUY position on the instrument details page, close it from the trade view, then open Portfolio and select the History tab. The scenario stops there and does not read a close price from the history list.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection, plus the existing portfolio history-list videos in `app_videos` (open/close records, open and close action, open and close direction, product code, and product name). No new device inspection in this change.
- Runtime result: not run.

### EIEHK

- Target: env `bauuat` or `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`, artifact `com.efsg.eietrading.android_0.0.220-20261007.apk.zip`. Installed build was not verified in this change.
- UI path: create a BUY position on the instrument details page, close it from the trade view, then open Portfolio and select the History tab. The scenario stops there and does not read a close price from the history card.
- Status: implemented for Android. iOS is not implemented.
- Evidence: source inspection, plus the existing portfolio history-list videos in `app_videos`. No new device inspection in this change.
- Runtime result: not run.

## Scoped commands

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3494.feature \
  -Dcucumber.filter.tags="@Test"
```

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3494.feature \
  -Dcucumber.filter.tags="@Test"
```
