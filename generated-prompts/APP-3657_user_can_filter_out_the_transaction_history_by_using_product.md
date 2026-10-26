# APP-3657 — User can filter out the transaction history by using product

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio History filtering page

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3657.feature`

## Scenario Title

User can filter out the transaction history by using product

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
7. And the user taps button "Done" on the filtering page
8. Then the user sees the transaction history matched with the selected option on the portfolio page

The feature word `session` is the section label parameter. Keep that wording so the step matches the feature.

## Expected Assertions

`AppPortfolioPage.tapOptionUnderSection("Product", "XAUUSD")` taps the clickable following-sibling ViewGroup whose content-desc is `XAUUSD`. Android reports `checked` and `selected` as false. The selection marker is a descendant whose tag name contains `SvgView`. The method stores `XAUUSD` only after `selectedOptionUnderSection("Product")` returns `XAUUSD`.

`AppPortfolioPage.tapFilteringButtonNamed("Done")` taps the clickable ViewGroup with content-desc `Done`, then waits until the filtering heading `Show` is gone.

`AppPortfolioPage.assertVisibleHistoryMatchesSelectedOption()` reads only the history rows currently inside the list area. It does not scroll. A visible product code is a six-letter symbol that `MobileAbstractComponents.getProductName` maps (`XAUUSD` to `Gold`, `XAGUSD` to `Silver`, `HKGHKD` to `Hong Kong Gold`, `RKGCNH` to `RMB Kilobar Gold`). Every visible product code must equal the stored option. A complete row also shows that mapped product name, plus action, direction, volume, and timestamp. At least one complete row is required. The product name identifies the row and is not compared with the selected option. A clipped row that has no product code is skipped. Stopping on the first screen does not mean the account history from `2026-07-23` was fully read.

`optionLabelsUnderSection`, `selectedOptionUnderSection`, `tapFilteringButton`, and `tapBack` stay unchanged for APP-3524 and APP-3656.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, installed `versionName` `0.0.303`. Device `emulator-5554`.
- UI path: `AppPortfolioPage.tapFilteringButton()`, `tapOptionUnderSection("Product", "XAUUSD")`, `tapFilteringButtonNamed("Done")`, then `assertVisibleHistoryMatchesSelectedOption()`.
- Status: implemented for Android. A non-Android driver throws and does not pass. iOS is not implemented.
- Evidence: Appium inspection on emulator-5554. Portfolio account reference `GLOBAL-66000288081`. Product options were All, HKGHKD, RKGCNH, XAGUSD, and XAUUSD. XAUUSD was a clickable ViewGroup with content-desc `XAUUSD`. Done was a clickable ViewGroup with content-desc `Done`. After Done, the chip content-desc was `Show Custom Filter`. The first screen showed history rows whose product code text was `XAUUSD` and whose product name text was `Gold`.
- Coverage limitation: the assertion covers the rows visible after Done. It does not enumerate older rows back to the filter start date `2026-07-23`.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3657.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"
```
