# APP-3674 — The filtering option can be reset to default

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History filtering page

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3674.feature`

The feature file is not edited by this change.

## Scenario Title

The filtering option can be reset to default

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @AppTest @Test`

Effective entity tag: `@EBL_MT5` only. Do not add EIEHK. iOS is out of scope.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- entity=EBL_MT5

The scenario logs in through `the user logs in on the App login page`, which reads the EBL_MT5 entry from `AppCredential`. Do not copy that username or password into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Portfolio" on the app footer
4. And the user selects tab "History" on the portfolio page
5. When the user taps filtering button on the portfolio page
6. And the user taps option "XAUUSD" under "Product" session on the filtering page
7. And the user taps option "Open" under "Action" session on the filtering page
8. And the user taps button "Default" on the filtering page
9. Then the user sees option "All" is selected under "Product" session on the filtering page
10. And the user sees option "All" is selected under "Action" session on the filtering page

The feature word `session` is the section label parameter. Keep that wording so the step matches the feature.

## Expected Assertions

The existing step `the user taps button {string} on the filtering page` calls `AppPortfolioPage.tapFilteringButtonNamed`. `Done` is unchanged: it taps the clickable ViewGroup with content-desc `Done`, then waits until the filtering heading `Show` is gone.

`Default` taps the clickable ViewGroup with content-desc `Default`. The filtering page stays open. The step returns only after the heading `Show` is still present and `selectedOptionFromHierarchy` reports `All` for both `Product` and `Action`. Android reports `checked` and `selected` as false. The selection marker is a descendant whose tag name contains `SvgView`. The walk for each section starts after that section heading and stops at the next heading, so Action All does not satisfy the Product check.

After the tap, `selectedFilteringSection` and `selectedFilteringOption` are cleared. The taps before Default store Action `Open`. A later history comparison must not reuse that stored option. This scenario does not assert history rows or the From/To dates.

The following steps reuse the existing selected-option binding. Each calls `selectedOptionUnderSection` and expects `All`.

A non-Android driver throws `TimeoutException`. A missing Default control throws from the visible tap. If both sections are not `All` within 10 seconds, the step throws `TimeoutException`. Any other button name still throws `NoSuchElementException`.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`. The reset recording metadata reports Android 16. Installed `versionName` and device id were not read from that recording.
- UI path: `AppPortfolioPage.tapFilteringButton()`, `tapOptionUnderSection("Product", "XAUUSD")`, `tapOptionUnderSection("Action", "Open")`, `tapFilteringButtonNamed("Default")`, then `selectedOptionUnderSection` for Product and Action.
- Status: implemented for Android EBL_MT5 history labels. A non-Android driver throws and does not pass. iOS is not implemented. EIEHK is outside this case because the feature has no `@EIEHK` tag.
- Evidence: recording `app_videos/The filtering option can be reset to default.mp4`. Portfolio account reference `GLOBAL-66000288081`. The recording shows the white Default control above the yellow Done control while Action `Open` and Product `XAUUSD` are selected. It ends before the reset frame. An earlier hierarchy of the same package shows Default as a clickable ViewGroup with content-desc `Default`, beside Done. On that filter page, Action `All` and Product `All` each carry an SvgView while the Show page stays open. Product options observed on the recording include HKGHKD, RKGCNH, XAGUSD, and XAUUSD.
- Coverage limitation: the scenario checks the selected Product and Action options after Default. It does not check the restored From/To range, and it does not read history rows. Pending-order filtering is not a caller of `Default`.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3674.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"
```
