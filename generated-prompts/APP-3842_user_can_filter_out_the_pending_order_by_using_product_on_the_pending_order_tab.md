# APP-3842 — User can filter out the pending order by using product on the pending order tab

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Portfolio Pending Orders filtering

## Feature File

`src/test/java/Features/NativeApp/trade/APP-3842.feature`

The feature file is not edited by this change.

## Scenario Title

User can filter out the pending order by using product on the pending order tab

## Tags

`@App @Smoke @Regression @Trade @EBL_MT5 @AppTest @Test`

Effective entity tag: `@EBL_MT5` only. Do not add EIEHK. iOS is out of scope.

## Environment

- product=app
- env=bauuat
- platform=ANDROID
- entity=EBL_MT5
- symbol override=`XAUUSD`

`TradeSymbol.properties` currently has `symbol=XAGUSD` and is not changed. The scoped run must pass `-Dsymbol=XAUUSD`.

The scenario logs in through `the user logs in on the App login page`, which reads the EBL_MT5 entry from `AppCredential`. Do not copy that username or password into this prompt or other generated artifacts.

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
5. And the user taps back button on the app trade view
6. When the user taps button "Portfolio" on the app footer
7. And the user selects tab "Pending Orders" on the portfolio page
8. And the user taps filtering button on the portfolio pending order tab
9. And the user taps option "XAUUSD" under "Product" session on the filtering page
10. Then the user sees the transaction history matched with the selected option on the portfolio pending order tab

The feature word `session` is the section label parameter. There is no Done step. Tapping XAUUSD closes the filter page.

## Expected Assertions

`the user taps filtering button on the portfolio pending order tab` calls the existing `AppPortfolioPage.tapFilteringButton()`. That locator taps a clickable ViewGroup whose content-desc starts with `Show`.

`AppPortfolioPage.tapOptionUnderSection("Product", "XAUUSD")` still taps the clickable following-sibling ViewGroup. After the tap it waits until one of these is true:

- The exact TextView text `Show` is gone and a clickable portfolio filter chip is visible with content-desc `Show all` or `Show Custom Filter`.
- The filter page is still open and `selectedOptionFromHierarchy` reports the tapped option. That report requires the SvgView checkmark. `selectedOptionUnderSection` is not called inside the wait, because it waits for the Show heading and throws after the page has closed.

On success the method stores section `Product` and option `XAUUSD`. If neither condition happens it throws the existing not-selected error and does not store the option.

`assertVisiblePendingOrdersMatchSelectedOption()` is Android only. A non-Android driver throws and does not pass. It requires a stored Product section and option. Other sections throw. It reads only rows whose text centers are inside `listAreaTopY()` and `footerTopY()`. It does not scroll. A product code is a six-letter symbol that `MobileAbstractComponents.getProductName` maps, using `historyProductCode`. Every visible product code must equal the stored option. `All` accepts every mapped code. A complete row also shows the mapped product name above the code, a BUY or SELL direction, a volume, a numeric price, and a timestamp whose full text matches strict `uuuu-MM-dd HH:mm:ss`. At least one complete row is required. A clipped row with no product code is skipped. A foreign product code fails. A missing field does not pass, and two nulls are not treated as a match. This does not call `assertVisibleHistoryMatchesSelectedOption`.

`cancelCreatedPendingOrder()` runs only after that assertion returns. Identity is `AppMarketsPage.tradeSymbol`, `AppTradeView.selectedDirection`, and `AppInstrumentDetailsPage.stopOrderPrice`. A blank identity throws before any tap. The displayed order type is `Stop` when `AppInstrumentDetailsPage.stopOrderType` contains `stop`, otherwise `Limit` when it contains `limit`. When the stored filter option is not `All`, it must equal the traded symbol; otherwise the method throws and does not cancel another product's order. It taps only the leftmost icon-sized clickable on the topmost visible row whose product code, direction, order-type label, and price match. Prices are compared with `BigDecimal` after commas are removed. It then calls the existing private `confirmCancelOrder()`. A missing row or cancel icon throws. It does not call `cancelPendingOrder()` and does not use an estimated coordinate.

## Shared-helper impact

`tapOptionUnderSection` is shared with APP-3657 (Product) and APP-3671 (Action). Those history filters stay open until Done. While the exact TextView `Show` is present, the option is stored only after `selectedOptionFromHierarchy` sees the SvgView checkmark on that option. The pending-order path stores the option when the Show heading is gone and the `Show all` or `Show Custom Filter` chip is visible. `selectedOptionUnderSection`, `tapFilteringButton`, `tapFilteringButtonNamed`, and `assertVisibleHistoryMatchesSelectedOption` are unchanged.

`listAreaTopY` and `portfolioListChromeVisible` are reused and not edited. After a product filter the Show chip content-desc becomes `Show Custom Filter`. Both helpers still match the `Newest to Oldest` sort chip, so the list area remains below that chip.

## Entity coverage

### EBL_MT5

- Target: env `bauuat`, platform `ANDROID`, package `com.emperorfs.ebltrading.android`, installed `versionName` `0.0.303`. Device `emulator-5554`.
- UI path: place a BUY Buy Limit pending order for the overridden symbol `XAUUSD`, open Portfolio Pending Orders, `tapFilteringButton()`, `tapOptionUnderSection("Product", "XAUUSD")`, `assertVisiblePendingOrdersMatchSelectedOption()`, then `cancelCreatedPendingOrder()`.
- Status: implemented for Android. A non-Android driver throws and does not pass. iOS is not implemented.
- Evidence: Appium inspection on emulator-5554. Portfolio account reference `GLOBAL-66000288081`. The Pending Orders chip is a clickable ViewGroup with content-desc `Show all`. After a product filter it becomes `Show Custom Filter`. The sort chip is `Newest to Oldest`. The filter heading is a TextView whose exact text is `Show`. There is no Done button. Product options are clickable ViewGroups with content-desc `All`, `XAGUSD`, and `XAUUSD`, following the Product TextView. The selected option contains an SvgView. Dividers between options are non-clickable ViewGroups. Tapping `XAUUSD` closes the filter page immediately. A pending row shows the product name above a six-letter product code, a timestamp under the code (`uuuu-MM-dd HH:mm:ss`), order type `Limit` or `Stop`, direction `BUY` or `SELL`, volume, and price. Three clickable icons sit on the right: cancel is the leftmost, then edit, then the detail arrow. Coordinates from the 1280-wide device are not hardcoded.
- Coverage limitation: the assertion and cancel use only the rows visible without scrolling. The created order is expected near the top because the sort chip is `Newest to Oldest`.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=bauuat -Dentity=EBL_MT5 -Dplatform=ANDROID -Dsymbol=XAUUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-3842.feature \
  -Dcucumber.filter.tags="@Test and @EBL_MT5"
```
