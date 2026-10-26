# APP-3656 — The default filtering option of history is all

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History filtering page

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3656.feature`

## Scenario Title

The default filtering option of history is all

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @AppTest @Test`

Effective entity tag: `@EBL_MT5` only. Do not add EIEHK. iOS is out of scope.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- entity=EBL_MT5

The scenario logs in with the username and password already written in `APP-3656.feature`. Do not copy those credential values into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user login as the username and password in APP-3656.feature on App login page
3. And the user taps button "Portfolio" on the app footer
4. And the user selects tab "History" on the portfolio page
5. When the user taps filtering button on the portfolio page
6. Then the user sees option "All" under "Product" session on the filtering page
7. And the user sees option "All" is selected under "Product" session on the filtering page

The feature word `session` is the section label parameter. Keep that wording so the step matches the feature.

## Expected Assertions

`AppPortfolioPage.optionLabelsUnderSection("Product")` still expects the Product label list to equal exactly `["All"]` on this account.

`AppPortfolioPage.selectedOptionUnderSection("Product")` must equal `All`. Android reports `checked` and `selected` as false on these rows. The selection marker is a descendant whose tag name contains `SvgView` (observed class `com.horcrux.svg.SvgView`) inside the option ViewGroup. The walk starts after the named section heading and stops at the next section heading, so Action All does not satisfy the Product check. `getCheckedProduct()` is not used.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, installed `versionName` `0.0.303`. Device `emulator-5554`.
- UI path: `AppPortfolioPage.tapFilteringButton()` then `AppPortfolioPage.optionLabelsUnderSection("Product")` and `AppPortfolioPage.selectedOptionUnderSection("Product")`.
- Status: implemented for Android. A non-Android driver throws and does not pass. iOS is not implemented.
- Evidence: Appium inspection on emulator-5554. History for the feature account was empty. The empty-state message was `Let's fund and trade now!`. The visible trading account reference was `GLOBAL-660003533309`. The filter page heading was `Show`. Action options were All, Open, and Close, and Action All had an SvgView checkmark. The Product section listed only All, also with an SvgView checkmark. Neither All row had `checked` or `selected` set to true.
- Coverage limitation: the Qase precondition of open/closed XAUUSD and HKGHKD positions was not present. Multi-product lists were not observed.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3656.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"
```
