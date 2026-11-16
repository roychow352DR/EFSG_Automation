# APP-3667 — The filtering options under action are displayed correctly

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History filtering page

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3667.feature`

## Scenario Title

The filtering options under action are displayed correctly

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @AppTest @Test`

Effective entity tag: `@EBL_MT5` only. Do not add EIEHK. iOS is out of scope.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- entity=EBL_MT5

The scenario logs in with the username and password already written in `APP-3667.feature`. Do not copy those credential values into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user login as the username and password in APP-3667.feature on App login page
3. And the user taps button "Portfolio" on the app footer
4. And the user selects tab "History" on the portfolio page
5. When the user taps filtering button on the portfolio page
6. Then the user sees option "All" under "Action" session on the filtering page
7. And the user sees option "Open" under "Action" session on the filtering page
8. And the user sees option "Close" under "Action" session on the filtering page

The feature word `session` is the section label parameter. Keep that wording so the step matches the feature.

## Expected Assertions

`AppPortfolioPage.optionLabelsUnderSection("Action")` reads the filtering page hierarchy. It collects option labels on the siblings that follow the Action heading and stops at the next section heading. A row label is its content description, or the text of its child when the description is blank. The step copies those labels into an `ArrayList`, loops the list, and passes when the named option is present. Each of All, Open, and Close is checked by the same step. The list is not required to contain only that one option. Product rows are after the Action heading, so Product All does not satisfy the Action check. `getCheckedProduct()` is not used.

The same step is shared with APP-3524. APP-3524 passes when Product contains All. It no longer fails when Product also lists other symbols.

## Entity coverage

### EBL_MT5

- Target: env `bauuat` (the same package is also mapped for `mt5uat`), platform `ANDROID`, package `com.emperorfs.ebltrading.android`, installed `versionName` `0.0.303`. Device evidence from the same empty-account filter page: `emulator-5554`.
- UI path: `AppPortfolioPage.tapFilteringButton()` then `AppPortfolioPage.optionLabelsUnderSection("Action")`.
- Status: implemented for Android. A non-Android driver throws and does not pass. iOS is not implemented.
- Evidence: prior Appium inspection of the History filter page. The heading was `Show`. Action options were All, Open, and Close on both an empty account and an account that already had history. This task did not open a new device session. The scenario uses the credentials written in the feature file.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3667.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"
```
