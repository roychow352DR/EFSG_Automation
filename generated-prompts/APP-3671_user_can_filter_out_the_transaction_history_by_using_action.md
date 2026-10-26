# APP-3671 — User can filter out the transaction history by using action

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History filtering page

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3671.feature`

## Scenario Title

User can filter out the transaction history by using action

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
6. And the user taps option "Open" under "Action" session on the filtering page
7. And the user taps button "Done" on the filtering page
8. Then the user sees the transaction history matched with the selected option on the portfolio page

The feature word `session` is the section label parameter. Keep that wording so the step matches the feature.

## Expected Assertions

`AppPortfolioPage.tapOptionUnderSection("Action", "Open")` taps the clickable following-sibling ViewGroup whose content-desc is `Open`. Android reports `checked` and `selected` as false. The selection marker is a descendant whose tag name contains `SvgView`. The method stores section `Action` and option `Open` only after `selectedOptionUnderSection("Action")` returns `Open`.

`AppPortfolioPage.tapFilteringButtonNamed("Done")` taps the clickable ViewGroup with content-desc `Done`, then waits until the filtering heading `Show` is gone.

`AppPortfolioPage.assertVisibleHistoryMatchesSelectedOption()` reads only the history rows currently inside the list area. It does not scroll. The stored section decides the compared field:

- `Product` compares each visible product code with the stored option. A product code is a six-letter symbol that `MobileAbstractComponents.getProductName` maps (`XAUUSD` to `Gold`, `XAGUSD` to `Silver`, `HKGHKD` to `Hong Kong Gold`, `RKGCNH` to `RMB Kilobar Gold`). APP-3657 uses this path with `XAUUSD`.
- `Action` compares the nearest `Open` or `Close` label below that product code and above the next row. APP-3671 uses this path with `Open`. Product remains `All` in this scenario, so other mapped product codes on the same screen are accepted.
- `All` in either section accepts every value of that field.
- Any other section throws `AssertionError`.

A complete row shows the mapped product name, product code, action, direction, volume, and timestamp. At least one complete row is required. A visible value that does not match the selected option fails the step, including a visible `Close` when `Open` is selected. A clipped row with no product code is skipped. An Action row with no readable `Open` or `Close` label is skipped and does not count as complete. Stopping on the first screen does not mean the account history from `2026-07-23` was fully read.

The same step text is shared with APP-3657. `optionLabelsUnderSection`, `selectedOptionUnderSection`, `tapFilteringButton`, and `tapBack` stay unchanged for APP-3524 and APP-3656.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`. The action recording metadata reports Android 16. Installed `versionName` and device id were not read from that recording.
- UI path: `AppPortfolioPage.tapFilteringButton()`, `tapOptionUnderSection("Action", "Open")`, `tapFilteringButtonNamed("Done")`, then `assertVisibleHistoryMatchesSelectedOption()`.
- Status: implemented for Android EBL_MT5 history labels `Open` and `Close`. A non-Android driver throws and does not pass. iOS is not implemented. EIEHK history captions such as `Open:` are outside this case because the feature has no `@EIEHK` tag.
- Evidence: recording `app_videos/User can filter out the transaction history by using action.mp4`. Portfolio account reference `GLOBAL-66000288081`. Action options were All, Open, and Close. Product options were All, HKGHKD, RKGCNH, XAGUSD, and XAUUSD. After selecting Action `Open` and Done, the chip read `Show Custom Filter`. The first screen showed history rows whose action label was `Open` and whose product code text was `XAUUSD`. Before the filter, the same screen also showed `Close` rows.
- Coverage limitation: the assertion covers the rows visible after Done. It does not enumerate older rows back to the filter start date `2026-07-23`. It checks the tapped section only. Product stays `All` and is not required to be a single code.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3671.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"
```
