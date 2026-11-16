# APP-314 — Search a symbol from Markets

Create native Appium Java automation based on AGENTS.md and AGENTS-APP.md in this repository.

## Module / Page

Native App trade — Markets list search. The search controls live on the Markets page. `AppWatchlistDetailsPage` remains the symbol-detail position list and is not part of this search.

## Feature File

`src/test/java/Features/NativeApp/trade/APP-314.feature`

## Scenario Title

User can search symbol on the watchlist page

## Tags

`@App @Smoke @Regression @Trade @EIEHK @Test`

Effective entity tags: `@EIEHK` only. iOS is not implemented and is not claimed.

## Environment

- product=app
- env=mt5uat
- entity=EIEHK
- platform=ANDROID
- symbol=XAGUSD

Use the entity credentials already loaded by `the user logs in on the App login page`. Do not copy passwords into this prompt or other generated artifacts.

## Test Data

- Search query and expected row: `XAGUSD`
- Package: `com.efsg.eiehktrading.android`
- Artifact: `src/main/resources/com.efsg.eietrading.android_0.0.220-20261012.apk.zip`
- `bauuat` selects the same package and artifact as `mt5uat`

## Test Steps

1. Given the user launch the app
2. And the user logs in on the App login page
3. And the user taps button "Markets" on the app footer
4. When the user taps the search button on the markets page
5. And the user fills in the search field with value "XAGUSD" on the markets page
6. Then the user sees the symbol "XAGUSD" in the markets search results

## Expected Assertions

- The search field text is exactly `XAGUSD`. A placeholder of `Search` does not count as the query.
- A displayed node other than the search `EditText` has exact text or content-desc `XAGUSD`.
- The instrument ticket is not open. Visible `Lot Size`, `Buy at`, or `Sell at` fails the step.
- The step does not tap the result row, place an order, or change `AppMarketsPage.tradeSymbol`.
- Typing uses Android letter and digit keys. Uppercase letters include the Shift meta modifier so `XAGUSD` stays uppercase. The field is focused for 250ms before the first key. A failed attempt clears the field, including leftover characters, and types the whole query again.
- It does not call `typeWithAndroidKeys`, because that helper cannot type `X`, `G`, `S`, or `D` and presses Enter.
- Do not press Enter after the query. Enter can select a result and leave the search results.

## Entity coverage

### EIEHK

- Target: env `mt5uat`, platform `ANDROID`, package `com.efsg.eiehktrading.android`. Artifact `com.efsg.eietrading.android_0.0.220-20261012.apk.zip`. Installed build was not checked in this change.
- UI path: login, footer `Markets`, search button, search field, result row for `XAGUSD`.
- Search button locator, observed on the guest Markets watchlist: the first clickable `android.view.ViewGroup` sibling after the `Watchlist` title. Bounds were `[1076,438][1148,510]`. Text and content-desc were empty. The settings icon is the next clickable sibling, bounds `[1160,438][1232,510]`.
- Search field, observed after tapping that icon: `android.widget.EditText` with an empty hint and no content-desc. Bounds were `[204,183][1076,279]`.
- Status: implemented for Android. A missing search button, search field, or exact result row fails. iOS throws `UnsupportedOperationException`.
- Evidence: Appium inspection on emulator-5554, package `com.efsg.eiehktrading.android`, installed `versionName=0.0.220`, after the failed run had returned to the sign-up home. The Markets tab was opened and the icon tap opened the symbol search screen. This was not a logged-in run and did not type `XAGUSD`.
- Runtime result: not run.

## Scoped command

```bash
mvn test -PAppTests -Dproduct=app -Denv=mt5uat -Dentity=EIEHK -Dplatform=ANDROID -Dsymbol=XAGUSD \
  -Dcucumber.features=src/test/java/Features/NativeApp/trade/APP-314.feature \
  -Dcucumber.filter.tags="@App and @EIEHK"
```

This command was not executed.
