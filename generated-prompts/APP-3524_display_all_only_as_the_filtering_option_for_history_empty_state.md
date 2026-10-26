# APP-3524 — All is the only History filter product option

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History empty-state filtering page

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3524.feature`

## Scenario Title

display all only as the filtering option for history empty state

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @AppTest @Test`

Effective entity tag: `@EBL_MT5` only. Do not add EIEHK. iOS is out of scope.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- entity=EBL_MT5

The scenario logs in with the username and password already written in `APP-3524.feature`. Do not copy those credential values into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user login as the username and password in APP-3524.feature on App login page
3. And the user taps button "Portfolio" on the app footer
4. And the user selects tab "History" on the portfolio page
5. When the user taps filtering button on the portfolio page
6. Then the user sees option "All" under "Product" session on the filtering page

The feature word `session` is the section label parameter. Keep that wording so the step matches the feature.

## Expected Assertions

`AppPortfolioPage.optionLabelsUnderSection("Product")` reads the filtering page hierarchy. It collects option labels on the siblings that follow the Product heading and stops at the next section heading. A row label is its content description, or the text of its child when the description is blank. That list must equal exactly `["All"]`. Rows under Action are before the Product heading, so Action's All does not satisfy this check. `getCheckedProduct()` is not used.

## Entity coverage

### EBL_MT5

- Target: env `bauuat` (the same package is also mapped for `mt5uat`), platform `ANDROID`, package `com.emperorfs.ebltrading.android`, installed `versionName` `0.0.303`. Device `emulator-5554`.
- UI path: `AppPortfolioPage.tapFilteringButton()` then `AppPortfolioPage.optionLabelsUnderSection("Product")`.
- Status: implemented for Android. A non-Android driver throws and does not pass. iOS is not implemented.
- Evidence: Appium inspection on emulator-5554. History was selected on account `GLOBAL-660003533309` with the empty-state message. The filter control was a clickable ViewGroup with content-desc `Show last 90 days` (Open Positions uses `Show all` for the same kind of control). The filter page heading was `Show`. Action options were All, Open, and Close. The Product section listed only All, with a checkmark drawn as an SvgView. Neither All row had `checked` or `selected` set to true. No resource-id was present on these controls.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3524.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"
```
