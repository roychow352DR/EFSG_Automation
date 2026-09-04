package utils;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPageElement {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private String pageSourceCache;

    public GetPageElement(AppiumDriver driver){
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
    }

    public void capturePageSource() {
        try {
            pageSourceCache = driver.getPageSource();
        } catch (Exception e) {
            pageSourceCache = null;
            logWarn("Could not capture page source: " + e.getMessage());
        }
    }

    public void waitAndCapture(By locator, int seconds) {
        includeUnimportantViews();
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        capturePageSource();
    }

    public void waitAndCapturePositionDetails() {
        waitAndCapture(By.xpath("//*[@text='Position Details']"), 10);
        waitForLabel("Direction");
        capturePageSource();
    }

    public void waitAndCaptureIfNeeded(By locator, int seconds) {
        if (pageSourceCache != null && !pageSourceCache.isBlank()) {
            return;
        }
        waitAndCapture(locator, seconds);
    }

    public void clearPageSourceCache() {
        pageSourceCache = null;
    }

    private String pageSource() {
        if (pageSourceCache != null && !pageSourceCache.isBlank()) {
            return pageSourceCache;
        }
        try {
            return driver.getPageSource();
        } catch (Exception e) {
            logWarn("Could not read page source: " + e.getMessage());
            return null;
        }
    }

    public String readLabelValueFast(String uiLabel) {
        boolean ownSnapshot = pageSourceCache == null;
        if (ownSnapshot) {
            capturePageSource();
        }
        try {
            String value = readLabelValueFromSnapshot(uiLabel);
            if (value == null && ("Direction".equals(uiLabel) || "Side".equals(uiLabel)
                    || "Volume".equals(uiLabel) || "Qty".equals(uiLabel))) {
                waitForLabel(uiLabel);
                capturePageSource();
                value = readLabelValueFromSnapshot(uiLabel);
            }
            return value;
        } finally {
            if (ownSnapshot) {
                clearPageSourceCache();
            }
        }
    }

    private String readLabelValueFromSnapshot(String uiLabel) {
        String value = null;
        if ("Direction".equals(uiLabel) || "Side".equals(uiLabel)) {
            value = plausible(uiLabel, findDirectionValueFromSource());
        } else if ("Volume".equals(uiLabel) || "Qty".equals(uiLabel)) {
            value = plausible(uiLabel, resolveVolumeValue());
        }
        if (value == null) {
            value = plausible(uiLabel, findValueOnSameRowFromSource(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueBetweenAdjacentLabels(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueFromPageSource(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueByFollowingSiblingScoped(uiLabel));
        }
        return value;
    }

    private boolean isSameElement(WebElement a, WebElement b) {
        String fa = safeElementFingerprint(a);
        String fb = safeElementFingerprint(b);
        return fa.equals(fb);
    }

    private String extractBestText(WebElement el) {
        return firstUsableValue(
                safeText(el),
                safeAttr(el, "text"),
                safeAttr(el, "contentDescription"),
                safeAttr(el, "content-desc")
        );
    }

    public String mapUiLabel(String label) {
        switch (label) {
            case "Side":
                return "Direction";
            case "Qty":
                return "Volume";
            default:
                return label;
        }
    }

    public String mapPositionDetailsLabel(String label) {
        switch (label) {
            case "Estimated Margin":
            case "Est. Margin":
                return "Initial Margin";
            default:
                return mapUiLabel(label);
        }
    }

    public String normalizeByLabel(String label, String rawValue, String symbolDecimal) {
        if (rawValue == null) {
            return null;
        }

        rawValue = rawValue.trim();

        if ("Qty".equals(label) || "Volume".equals(label)) {
            return canonicalizeVolume(rawValue);
        }

        if ("N/A".equalsIgnoreCase(rawValue)) {
            return rawValue;
        }

        return isPriceLabel(label)
                ? abs.normalizePriceToDecimals(rawValue, symbolDecimal)
                : abs.normalizeDialogueValue(label, rawValue);
    }

    private boolean isPriceLabel(String label) {
        if (label == null) {
            return false;
        }

        Set<String> priceLabels = new HashSet<>(Arrays.asList(
                "Open Price",
                "Current Price",
                "Take Profit Price",
                "Stop Loss Price",
                "Take Profit",
                "Stop Loss"
        ));

        return priceLabels.contains(label.trim());
    }

    private boolean isLikelyLabel(String text) {
        if (text == null) {
            return false;
        }

        Set<String> labels = new HashSet<>(Arrays.asList(
                "Product",
                "Product Name",
                "Account",
                "Status",
                "Direction",
                "Volume",
                "Contract Value",
                "Open Price",
                "Current Price",
                "Initial Margin",
                "Margin",
                "Floating P/L",
                "Take Profit Price",
                "Stop Loss Price",
                "Take Profit",
                "Stop Loss",
                "Interest",
                "Swap",
                "Commission",
                "Open Position Time",
                "Order Time",
                "Position ID",
                "Estimated Margin",
                "Est. Margin",
                "Position Details",
                "Edit Position",
                "Close Position",
                "Order Type",
                "Validity",
                "Target Price",
                "Product Name",
                "Modify Order"
        ));

        return labels.contains(text.trim());
    }

    private String safeText(WebElement el) {
        try {
            String t = el.getText();
            return t == null ? "" : t.trim();
        } catch (Exception e) {
            return "";
        }
    }

    private String safeAttr(WebElement el, String attr) {
        try {
            String value = el.getDomAttribute(attr);
            if (value == null || value.isBlank() || "null".equalsIgnoreCase(value.trim())) {
                value = el.getDomProperty(attr);
            }
            if (value == null || value.isBlank() || "null".equalsIgnoreCase(value.trim())) {
                value = el.getAttribute(attr);
            }
            if (value == null || "null".equalsIgnoreCase(value.trim())) {
                return "";
            }
            return value.trim();
        } catch (Exception e) {
            return "";
        }
    }

    private String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return null;
    }

    private String firstUsableValue(String... values) {
        for (String v : values) {
            if (!isNoiseValue(v)) {
                return v.trim();
            }
        }
        return null;
    }

    private boolean isNoiseValue(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String text = value.trim();
        return text.equalsIgnoreCase("true")
                || text.equalsIgnoreCase("false")
                || text.equalsIgnoreCase("null");
    }

    private boolean isPlausibleValue(String uiLabel, String value) {
        if (isNoiseValue(value) || isLikelyLabel(value) || uiLabel.equals(value)) {
            return false;
        }
        if ("Direction".equals(uiLabel) || "Side".equals(uiLabel)) {
            return value.equalsIgnoreCase("BUY") || value.equalsIgnoreCase("SELL");
        }
        if ("Volume".equals(uiLabel) || "Qty".equals(uiLabel)) {
            return extractVolumeToken(value) != null;
        }
        if (isMoneyLabel(uiLabel) || "Floating P/L".equals(uiLabel)) {
            return extractMoneyToken(value) != null;
        }
        if (isPriceLabel(uiLabel) || "Target Price".equals(uiLabel) || "Stop Order Price".equals(uiLabel)) {
            return "N/A".equalsIgnoreCase(value) || extractPriceToken(value) != null;
        }
        if ("Status".equals(uiLabel)) {
            return value.matches("(?i)Open|Pending|Filled|Cancelled|Canceled|Partial.*");
        }
        return true;
    }

    private boolean isMoneyLabel(String label) {
        return Arrays.asList(
                "Contract Value",
                "Initial Margin",
                "Estimated Margin",
                "Est. Margin",
                "Floating P/L",
                "Commission",
                "Interest",
                "Swap"
        ).contains(label);
    }

    private String extractPriceToken(String value) {
        if (isNoiseValue(value)) {
            return null;
        }
        String text = value.replace(",", "").replace('\n', ' ').trim();
        Matcher matcher = Pattern.compile("[-+]?\\d+(?:\\.\\d+)?").matcher(text);
        return matcher.find() ? matcher.group() : null;
    }

    private String extractMoneyToken(String value) {
        if (isNoiseValue(value) || isLikelyLabel(value)) {
            return null;
        }
        return value.matches("(?s).*\\d.*") ? value.trim() : null;
    }

    private String safeElementFingerprint(WebElement el) {
        return safeAttr(el, "className") + "|" + safeAttr(el, "bounds") + "|" + extractBestText(el);
    }

    private Rect parseBounds(String bounds) {
        if (bounds == null || bounds.isBlank()) {
            return null;
        }

        Pattern p = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]");
        Matcher m = p.matcher(bounds);
        if (!m.find()) {
            return null;
        }

        return new Rect(
                Integer.parseInt(m.group(1)),
                Integer.parseInt(m.group(2)),
                Integer.parseInt(m.group(3)),
                Integer.parseInt(m.group(4))
        );
    }

    public void logInfo(String msg) {
        System.out.println("[INFO] " + msg);
    }

    private void logWarn(String msg) {
        System.out.println("[WARN] " + msg);
    }

    private static class Rect {
        int left;
        int top;
        int right;
        int bottom;

        Rect(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        int height() {
            return bottom - top;
        }

        int width() {
            return right - left;
        }

        int centerX() {
            return left + width() / 2;
        }

        int centerY() {
            return top + height() / 2;
        }
    }

    private record DirectionHit(String direction, Rect rect) {
    }

    private record VolumeHit(String token, Rect rect) {
    }

    public String resolveLabelValue(String uiLabel) {
        includeUnimportantViews();
        if (pageSourceCache == null) {
            waitForLabel(uiLabel);
        }
        return readLabelValueFast(uiLabel);
    }

    private String readLabelValueOnce(String uiLabel) {
        if ("Direction".equals(uiLabel) || "Side".equals(uiLabel)) {
            return plausible(uiLabel, findDirectionValue());
        }
        if ("Volume".equals(uiLabel) || "Qty".equals(uiLabel)) {
            return plausible(uiLabel, findVolumeValue());
        }
        String value = plausible(uiLabel, findValueFromParentRow(uiLabel));
        if (value == null) {
            value = plausible(uiLabel, findValueBetweenAdjacentLabels(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueOnSameRowFromSource(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueOnSameRow(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueFromSiblings(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueByFollowingSibling(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueByFollowingSiblingScoped(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findValueFromPageSource(uiLabel));
        }
        if (value == null) {
            value = plausible(uiLabel, findPaintedRowValue(uiLabel));
        }
        return value;
    }

    private String findDirectionValue() {
        Rect labelRect = labelBounds("Direction");
        if (labelRect == null) {
            labelRect = labelBoundsFromPageSource("Direction");
        }
        String fromRow = directionBesideLabel(labelRect);
        if (fromRow != null) {
            logInfo("Found Direction from accessibility node: " + fromRow);
            return fromRow;
        }
        return null;
    }

    private String directionBesideLabel(Rect labelRect) {
        return pickDirectionBesideLabel(labelRect, allDirectionHits());
    }

    private String findDirectionValueFromSource() {
        String between = directionBetweenAdjacentLabels();
        if (between != null) {
            logInfo("Found Direction between adjacent labels: " + between);
            return between;
        }
        return pickDirectionBesideLabel(labelBoundsFromPageSource("Direction"), directionHitsFromPageSource());
    }

    private String directionBetweenAdjacentLabels() {
        String source = pageSource();
        if (source == null || source.isBlank()) {
            return null;
        }
        int headerAt = source.indexOf("text=\"Position Details\"");
        String section = headerAt >= 0 ? source.substring(headerAt) : source;
        String nextLabel = nextKnownLabelIn(section, "Direction");
        if (nextLabel == null) {
            return extractDirectionToken(sourceAfterLabel(section, "Direction"));
        }
        Matcher window = Pattern.compile(
                Pattern.quote("text=\"Direction\"") + "(.*?)" + Pattern.quote("text=\"" + nextLabel + "\""),
                Pattern.DOTALL
        ).matcher(section);
        if (window.find()) {
            String found = extractDirectionToken(window.group(1));
            if (found != null) {
                return found;
            }
        }
        return extractDirectionToken(sourceAfterLabel(section, "Direction"));
    }

    private String sourceAfterLabel(String source, String uiLabel) {
        String marker = "text=\"" + uiLabel + "\"";
        int from = source.indexOf(marker);
        if (from < 0) {
            return null;
        }
        int end = Math.min(source.length(), from + 2500);
        return source.substring(from, end);
    }

    private String extractDirectionToken(String xmlFragment) {
        if (xmlFragment == null || xmlFragment.isBlank()) {
            return null;
        }
        Matcher token = Pattern.compile(
                "(?:text|content-desc|contentDescription|name)=\"\\s*(BUY|SELL|Buy|Sell)\\s*\""
        ).matcher(xmlFragment);
        if (token.find()) {
            return normalizeDirection(token.group(1));
        }
        Matcher combined = Pattern.compile(
                "(?:text|content-desc|contentDescription|name)=\"[^\"]*\\b(BUY|SELL|Buy|Sell)\\b[^\"]*\""
        ).matcher(xmlFragment);
        if (combined.find()) {
            return normalizeDirection(combined.group(1));
        }
        return null;
    }

    private String pickDirectionBesideLabel(Rect labelRect, List<DirectionHit> hits) {
        if (labelRect == null) {
            return null;
        }
        String best = null;
        int bestRight = Integer.MIN_VALUE;
        int bestScore = Integer.MAX_VALUE;
        for (DirectionHit hit : hits) {
            if (isTicketToggleHit(hit, hits) || !isDirectionValueRect(labelRect, hit.rect())) {
                continue;
            }
            int score = Math.abs(hit.rect().centerY() - labelRect.centerY());
            if (hit.rect().right > bestRight || (hit.rect().right == bestRight && score < bestScore)) {
                bestRight = hit.rect().right;
                bestScore = score;
                best = hit.direction();
            }
        }
        return best;
    }

    private List<DirectionHit> allDirectionHits() {
        List<DirectionHit> hits = new ArrayList<>();
        for (String direction : Arrays.asList("BUY", "SELL", "Buy", "Sell")) {
            List<WebElement> matches = driver.findElements(By.xpath(
                    "//*[@text='" + direction + "' or @content-desc='" + direction + "']"
            ));
            for (WebElement match : matches) {
                try {
                    Rect rect = parseBounds(safeAttr(match, "bounds"));
                    if (rect != null) {
                        hits.add(new DirectionHit(normalizeDirection(direction), rect));
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
        }
        hits.addAll(directionHitsFromPageSource());
        return hits;
    }

    private List<DirectionHit> directionHitsFromPageSource() {
        List<DirectionHit> hits = new ArrayList<>();
        String source = pageSource();
        if (source == null || source.isBlank()) {
            return hits;
        }
        Matcher tag = Pattern.compile("<[^>]+>").matcher(source);
        while (tag.find()) {
            String node = tag.group();
            Matcher dir = Pattern.compile(
                    "(?:text|content-desc|contentDescription|name)=\"\\s*(BUY|SELL|Buy|Sell)\\s*\""
            ).matcher(node);
            Matcher bounds = Pattern.compile(
                    "bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\""
            ).matcher(node);
            if (dir.find() && bounds.find()) {
                hits.add(new DirectionHit(
                        normalizeDirection(dir.group(1)),
                        new Rect(
                                Integer.parseInt(bounds.group(1)),
                                Integer.parseInt(bounds.group(2)),
                                Integer.parseInt(bounds.group(3)),
                                Integer.parseInt(bounds.group(4))
                        )
                ));
            }
        }
        return hits;
    }

    private Rect labelBoundsFromPageSource(String uiLabel) {
        String source = pageSource();
        if (source == null || source.isBlank()) {
            return null;
        }
        Rect header = headerBoundsFromPageSource();
        Matcher tag = Pattern.compile("<[^>]+>").matcher(source);
        Rect best = null;
        int bestScore = Integer.MAX_VALUE;
        while (tag.find()) {
            String node = tag.group();
            if (!node.contains("text=\"" + uiLabel + "\"")) {
                continue;
            }
            Matcher bounds = Pattern.compile(
                    "bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\""
            ).matcher(node);
            if (!bounds.find()) {
                continue;
            }
            Rect rect = new Rect(
                    Integer.parseInt(bounds.group(1)),
                    Integer.parseInt(bounds.group(2)),
                    Integer.parseInt(bounds.group(3)),
                    Integer.parseInt(bounds.group(4))
            );
            if (!isUsableLabelRect(rect)) {
                continue;
            }
            if (header != null && rect.bottom < header.top) {
                continue;
            }
            int score = rect.top * 10 + Math.max(rect.height(), 0);
            if (header != null && rect.top >= header.bottom) {
                score -= 100000;
            }
            if (score < bestScore) {
                bestScore = score;
                best = rect;
            }
        }
        return best;
    }

    private Rect headerBoundsFromPageSource() {
        String source = pageSource();
        if (source == null || source.isBlank()) {
            return null;
        }
        for (String header : Arrays.asList(
                "Position Details",
                "Edit Position",
                "Pending Order Details",
                "Modify Order"
        )) {
            Matcher tag = Pattern.compile("<[^>]+>").matcher(source);
            while (tag.find()) {
                String node = tag.group();
                if (!node.contains("text=\"" + header + "\"")) {
                    continue;
                }
                Matcher bounds = Pattern.compile(
                        "bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\""
                ).matcher(node);
                if (!bounds.find()) {
                    continue;
                }
                return new Rect(
                        Integer.parseInt(bounds.group(1)),
                        Integer.parseInt(bounds.group(2)),
                        Integer.parseInt(bounds.group(3)),
                        Integer.parseInt(bounds.group(4))
                );
            }
        }
        return null;
    }

    private boolean isTicketToggleHit(DirectionHit hit, List<DirectionHit> all) {
        int mid = screenWidth() / 2;
        if (mid <= 0) {
            return false;
        }
        for (DirectionHit other : all) {
            if (other.direction().equals(hit.direction())) {
                continue;
            }
            if (Math.abs(other.rect().centerY() - hit.rect().centerY()) > 48) {
                continue;
            }
            boolean splitMid = (hit.rect().centerX() < mid) != (other.rect().centerX() < mid);
            if (splitMid) {
                return true;
            }
        }
        return false;
    }

    private boolean isDirectionValueRect(Rect labelRect, Rect rect) {
        if (rect == null || isLikelyTradeCta(rect)) {
            return false;
        }
        boolean toRight = rect.left >= labelRect.right - 24 || rect.centerX() > labelRect.centerX();
        boolean sameRow = isOnCompactRow(labelRect, rect);
        boolean justBelow = rect.top >= labelRect.top - 12
                && rect.top <= labelRect.bottom + 96
                && rect.centerX() > labelRect.left;
        return (toRight && sameRow) || justBelow;
    }

    private boolean isOnCompactRow(Rect labelRect, Rect rect) {
        int slop = Math.max(48, Math.max(24, labelRect.height()));
        return Math.abs(rect.centerY() - labelRect.centerY()) <= slop;
    }

    private boolean isLikelyTradeCta(Rect rect) {
        int screenWidth = screenWidth();
        if (screenWidth <= 0) {
            return false;
        }
        return rect.width() > (int) (screenWidth * 0.28) && rect.height() > 90;
    }

    private String normalizeDirection(String value) {
        return value.equalsIgnoreCase("SELL") ? "SELL" : "BUY";
    }

    private boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCompactLabelRect(Rect rect) {
        return isUsableLabelRect(rect);
    }

    private boolean isUsableLabelRect(Rect rect) {
        int height = rect.height();
        int width = rect.width();
        int screenWidth = screenWidth();
        boolean compactHeight = height <= 160;
        boolean compactWidth = screenWidth == 0 || width < (int) (screenWidth * 0.9);
        return compactHeight && compactWidth && (width > 0 || height > 0 || rect.top > 0);
    }

    private int screenWidth() {
        try {
            return driver.manage().window().getSize().getWidth();
        } catch (Exception e) {
            return 0;
        }
    }

    private int screenHeight() {
        try {
            return driver.manage().window().getSize().getHeight();
        } catch (Exception e) {
            return 0;
        }
    }

    private String extractVolumeToken(String value) {
        if (isNoiseValue(value) || isLikelyLabel(value)) {
            return null;
        }
        String text = value.replace('\n', ' ').replace('\u00A0', ' ').trim();
        Matcher withLots = Pattern.compile("(?i)(\\d+(?:[.,]\\d+)?)\\s*Lots?").matcher(text);
        if (withLots.find()) {
            return withLots.group(0).trim();
        }
        Matcher gluedLots = Pattern.compile("(?i)(\\d+(?:[.,]\\d+)?)Lots?").matcher(text);
        if (gluedLots.find()) {
            return gluedLots.group(0).trim();
        }
        Matcher numeric = Pattern.compile("^\\d+(?:[.,]\\d+)?$").matcher(text);
        return numeric.matches() ? text : null;
    }

    public String canonicalizeVolume(String rawValue) {
        if (rawValue == null) {
            return null;
        }
        String text = rawValue.replace('\u00A0', ' ').replaceAll("(?i)\\s*Lots?", "").trim();
        if (text.contains(",") && !text.contains(".")) {
            text = text.replace(',', '.');
        } else {
            text = text.replace(",", "");
        }
        try {
            return new BigDecimal(text).stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return text;
        }
    }

    private String findVolumeValue() {
        Rect labelRect = labelBounds("Volume");
        if (labelRect == null) {
            labelRect = labelBoundsFromPageSource("Volume");
        }
        String fromRow = volumeBesideLabel(labelRect);
        if (fromRow != null) {
            logInfo("Found Volume from accessibility node: " + fromRow);
            return fromRow;
        }
        return null;
    }

    private String volumeBesideLabel(Rect labelRect) {
        return pickVolumeBesideLabel(labelRect, allVolumeHits());
    }

    private String findVolumeValueFromSource() {
        return pickVolumeBesideLabel(labelBoundsFromPageSource("Volume"), volumeHitsFromPageSource());
    }

    private String resolveVolumeValue() {
        String value = findVolumeValueFromSource();
        if (value != null) {
            return value;
        }
        value = volumeBetweenAdjacentLabels();
        if (value != null) {
            return value;
        }
        capturePageSource();
        value = findVolumeValueFromSource();
        if (value != null) {
            return value;
        }
        return findVolumeValue();
    }

    private String volumeBetweenAdjacentLabels() {
        String nextLabel = nextKnownLabel("Volume");
        String source = pageSource();
        if (nextLabel == null || source == null || source.isBlank()) {
            return null;
        }
        Matcher window = Pattern.compile(
                Pattern.quote("text=\"Volume\"") + "(.*?)" + Pattern.quote("text=\"" + nextLabel + "\""),
                Pattern.DOTALL
        ).matcher(source);
        if (!window.find()) {
            return null;
        }
        Matcher token = Pattern.compile("(?:^|[\\s])(?:text|content-desc|contentDescription|name|hint)=\"([^\"]+)\"").matcher(window.group(1));
        while (token.find()) {
            String found = extractVolumeToken(token.group(1));
            if (found != null) {
                logInfo("Found Volume between adjacent labels: " + found);
                return found;
            }
        }
        return null;
    }

    private String pickVolumeBesideLabel(Rect labelRect, List<VolumeHit> hits) {
        if (labelRect == null) {
            return null;
        }
        String bestLots = null;
        String bestNumeric = null;
        int bestLotsRight = Integer.MIN_VALUE;
        int bestNumericRight = Integer.MIN_VALUE;
        for (VolumeHit hit : hits) {
            if (isLotChipHit(hit, hits) || !isVolumeValueRect(labelRect, hit)) {
                continue;
            }
            boolean hasLots = hit.token().toLowerCase().contains("lot");
            if (hasLots && hit.rect().right >= bestLotsRight) {
                bestLotsRight = hit.rect().right;
                bestLots = hit.token();
            } else if (!hasLots && hit.rect().right >= bestNumericRight) {
                bestNumericRight = hit.rect().right;
                bestNumeric = hit.token();
            }
        }
        return bestLots != null ? bestLots : bestNumeric;
    }

    private boolean isVolumeValueRect(Rect labelRect, VolumeHit hit) {
        Rect rect = hit.rect();
        if (rect == null) {
            return false;
        }
        int slop = Math.min(64, Math.max(32, labelRect.height() + 16));
        boolean sameRow = Math.abs(rect.centerY() - labelRect.centerY()) <= slop;
        if (!sameRow) {
            return false;
        }
        boolean toRight = rect.left >= labelRect.right - 20;
        boolean rowContainer = rect.height() <= 80
                && rect.left <= labelRect.left + 16
                && rect.right > labelRect.right
                && hit.token().toLowerCase().contains("lot");
        return toRight || rowContainer;
    }

    private boolean isLotChipHit(VolumeHit hit, List<VolumeHit> all) {
        if (hit.token().toLowerCase().contains("lot")) {
            return false;
        }
        int sameRow = 0;
        for (VolumeHit other : all) {
            if (other.token().toLowerCase().contains("lot")) {
                continue;
            }
            if (Math.abs(other.rect().centerY() - hit.rect().centerY()) <= 24) {
                sameRow++;
            }
        }
        return sameRow >= 3;
    }

    private List<VolumeHit> allVolumeHits() {
        List<VolumeHit> hits = new ArrayList<>();
        List<WebElement> matches = driver.findElements(By.xpath(
                "//*[@text or @content-desc or @hint]"
        ));
        for (WebElement match : matches) {
            try {
                String token = extractVolumeToken(firstNonBlank(
                        extractBestText(match),
                        safeAttr(match, "text"),
                        safeAttr(match, "hint")
                ));
                Rect rect = parseBounds(safeAttr(match, "bounds"));
                if (token != null && rect != null) {
                    hits.add(new VolumeHit(token, rect));
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        hits.addAll(volumeHitsFromPageSource());
        return hits;
    }

    private List<VolumeHit> volumeHitsFromPageSource() {
        List<VolumeHit> hits = new ArrayList<>();
        String source = pageSource();
        if (source == null || source.isBlank()) {
            return hits;
        }
        Matcher tag = Pattern.compile("<[^>]+>").matcher(source);
        while (tag.find()) {
            String node = tag.group();
            Matcher bounds = Pattern.compile(
                    "bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\""
            ).matcher(node);
            if (!bounds.find()) {
                continue;
            }
            Rect rect = new Rect(
                    Integer.parseInt(bounds.group(1)),
                    Integer.parseInt(bounds.group(2)),
                    Integer.parseInt(bounds.group(3)),
                    Integer.parseInt(bounds.group(4))
            );
            Matcher attr = Pattern.compile(
                    "(?:text|content-desc|contentDescription|name|hint)=\"([^\"]+)\""
            ).matcher(node);
            while (attr.find()) {
                String token = extractVolumeToken(attr.group(1));
                if (token != null) {
                    hits.add(new VolumeHit(token, rect));
                }
            }
        }
        return hits;
    }

    private String plausible(String uiLabel, String value) {
        return isPlausibleValue(uiLabel, value) ? value.trim() : null;
    }

    private void waitForLabel(String uiLabel) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(
                            By.xpath("//*[@text=\"" + uiLabel + "\"]")
                    ).isEmpty());
        } catch (TimeoutException e) {
            logWarn("Label was not visible: " + uiLabel);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void includeUnimportantViews() {
        if (driver instanceof AndroidDriver androidDriver) {
            try {
                androidDriver.setSetting("ignoreUnimportantViews", false);
                androidDriver.setSetting("allowInvisibleElements", true);
                androidDriver.setSetting("snapshotMaxDepth", 100);
                androidDriver.setSetting("shouldUseCompactResponses", false);
                androidDriver.setSetting("elementResponseAttributes", "name,text,contentDescription,class,bounds,hint,displayed");
            } catch (Exception e) {
                logWarn("Could not update accessibility dump settings: " + e.getMessage());
            }
        }
    }

    public String findValueOnSameRow(String uiLabel) {
        String fromSource = findValueOnSameRowFromSource(uiLabel);
        if (fromSource != null) {
            return fromSource;
        }

        List<WebElement> labels = driver.findElements(
                By.xpath("//*[@text=\"" + uiLabel + "\"]")
        );
        if (labels.isEmpty()) {
            return null;
        }

        Rect labelRect = parseBounds(safeAttr(labels.getFirst(), "bounds"));
        if (labelRect == null) {
            return null;
        }

        String bestValue = null;
        int bestScore = Integer.MAX_VALUE;
        List<WebElement> candidates = driver.findElements(By.xpath(
                "//*[@text=\"" + uiLabel + "\"]/following-sibling::*[@text or @content-desc]"
        ));

        for (WebElement el : candidates) {
            try {
                String text = extractBestText(el);
                if (!isPlausibleValue(uiLabel, text)) {
                    continue;
                }

                Rect rect = parseBounds(safeAttr(el, "bounds"));
                if (rect == null) {
                    continue;
                }

                int verticalDelta = Math.abs(rect.centerY() - labelRect.centerY());
                boolean sameRow = isOnCompactRow(labelRect, rect);
                boolean toRight = rect.left >= labelRect.right - 10;
                if (!sameRow || !toRight) {
                    continue;
                }

                int score = verticalDelta * 100 + Math.abs(rect.left - labelRect.right);
                if (score < bestScore) {
                    bestScore = score;
                    bestValue = text.trim();
                }
            } catch (StaleElementReferenceException ignored) {
                // Live P/L updates can invalidate a node mid-scan.
            }
        }

        if (bestValue != null) {
            logInfo("Found same-row value for label [" + uiLabel + "]: " + bestValue);
        }
        return bestValue;
    }

    public String findValueBetweenAdjacentLabels(String uiLabel) {
        String nextLabel = nextKnownLabel(uiLabel);
        if (nextLabel == null) {
            return null;
        }

        String source = pageSource();
        if (source == null || source.isBlank()) {
            logWarn("Could not read page source for label [" + uiLabel + "]");
            return null;
        }

        Pattern window = Pattern.compile(
                Pattern.quote("text=\"" + uiLabel + "\"") + "(.*?)" + Pattern.quote("text=\"" + nextLabel + "\""),
                Pattern.DOTALL
        );
        Matcher windowMatcher = window.matcher(source);
        if (!windowMatcher.find()) {
            return null;
        }

        Matcher textMatcher = Pattern.compile("(?:^|[\\s])(?:text|content-desc|contentDescription|name)=\"([^\"]+)\"").matcher(windowMatcher.group(1));
        while (textMatcher.find()) {
            String found = textMatcher.group(1).trim();
            if (found.isBlank() || uiLabel.equals(found) || isLikelyLabel(found) || !isPlausibleValue(uiLabel, found)) {
                continue;
            }
            logInfo("Found value for label [" + uiLabel + "] between adjacent labels: " + found);
            return found;
        }

        return null;
    }

    private String nextKnownLabel(String uiLabel) {
        return nextKnownLabelIn(pageSource(), uiLabel);
    }

    private String nextKnownLabelIn(String source, String uiLabel) {
        if (source == null || source.isBlank()) {
            return null;
        }

        String marker = "text=\"" + uiLabel + "\"";
        int from = source.indexOf(marker);
        if (from < 0) {
            return null;
        }

        int searchFrom = from + marker.length();
        String nearest = null;
        int nearestPos = Integer.MAX_VALUE;

        List<String> labels = Arrays.asList(
                "Product",
                "Product Name",
                "Account",
                "Status",
                "Direction",
                "Volume",
                "Contract Value",
                "Open Price",
                "Current Price",
                "Initial Margin",
                "Estimated Margin",
                "Floating P/L",
                "Take Profit Price",
                "Stop Loss Price",
                "Interest",
                "Open Position Time",
                "Order Time",
                "Position ID",
                "Validity",
                "Target Price",
                "Order Type",
                "Est. Margin"
        );

        for (String label : labels) {
            if (label.equals(uiLabel)) {
                continue;
            }
            int pos = source.indexOf("text=\"" + label + "\"", searchFrom);
            if (pos >= 0 && pos < nearestPos) {
                nearestPos = pos;
                nearest = label;
            }
        }
        return nearest;
    }

    public String findValueFromParentRow(String uiLabel) {
        List<WebElement> labels = driver.findElements(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]")
        );
        if (labels.isEmpty()) {
            return null;
        }

        WebElement label = labels.getFirst();
        WebElement current = label;
        for (int depth = 0; depth < 5; depth++) {
            try {
                current = current.findElement(By.xpath("./parent::*"));
            } catch (Exception e) {
                break;
            }

            String combined = firstNonBlank(
                    extractBestText(current),
                    safeAttr(current, "name")
            );
            String stripped = valueAfterStrippingLabels(combined, uiLabel);
            if (stripped != null) {
                logInfo("Found parent-row value for label [" + uiLabel + "]: " + stripped);
                return stripped;
            }

            try {
                for (WebElement child : current.findElements(By.xpath(".//*"))) {
                    String text = extractBestText(child);
                    if (isBlank(text) || uiLabel.equals(text) || isLikelyLabel(text)) {
                        continue;
                    }
                    Rect childRect = parseBounds(safeAttr(child, "bounds"));
                    Rect labelRect = parseBounds(safeAttr(label, "bounds"));
                    if (labelRect == null || childRect == null) {
                        continue;
                    }
                    int verticalDelta = Math.abs(childRect.centerY() - labelRect.centerY());
                    boolean sameRow = verticalDelta <= Math.max(28, labelRect.height());
                    boolean toRight = childRect.left >= labelRect.right - 10;
                    if (sameRow && toRight) {
                        logInfo("Found parent-child value for label [" + uiLabel + "]: " + text);
                        return text.trim();
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // Live quotes can invalidate a child; try the next parent.
            }
        }
        return null;
    }

    public String findValueFromSiblings(String uiLabel) {
        List<WebElement> siblings = driver.findElements(By.xpath(
                "//android.widget.TextView[@text=\"" + uiLabel + "\"]/following-sibling::*"
        ));
        Rect labelRect = null;
        List<WebElement> labels = driver.findElements(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]")
        );
        if (!labels.isEmpty()) {
            labelRect = parseBounds(safeAttr(labels.getFirst(), "bounds"));
        }

        for (WebElement sibling : siblings) {
            try {
                Rect rect = parseBounds(safeAttr(sibling, "bounds"));
                if (rect != null && rect.height() <= 8) {
                    continue;
                }
                if (labelRect != null && rect != null) {
                    int verticalDelta = Math.abs(rect.centerY() - labelRect.centerY());
                    boolean sameRow = verticalDelta <= Math.max(36, labelRect.height());
                    boolean toRight = rect.left >= labelRect.right - 10;
                    if (!sameRow || !toRight) {
                        String siblingText = extractBestText(sibling);
                        if (isLikelyLabel(siblingText)) {
                            break;
                        }
                        continue;
                    }
                }

                String text = extractBestText(sibling);
                if (isLikelyLabel(text)) {
                    break;
                }
                if (!isBlank(text) && !uiLabel.equals(text)) {
                    logInfo("Found sibling value for label [" + uiLabel + "]: " + text);
                    return text.trim();
                }

                for (WebElement child : sibling.findElements(By.xpath(".//*"))) {
                    String childText = extractBestText(child);
                    if (isBlank(childText) || uiLabel.equals(childText) || isLikelyLabel(childText)) {
                        continue;
                    }
                    logInfo("Found sibling-child value for label [" + uiLabel + "]: " + childText);
                    return childText.trim();
                }
            } catch (StaleElementReferenceException ignored) {
                return null;
            }
        }
        return null;
    }

    public String findValueOnSameRowFromSource(String uiLabel) {
        String source = pageSource();
        if (source == null || source.isBlank()) {
            return null;
        }

        Matcher nodeMatcher = Pattern.compile("<[^>]+bounds=\"\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]\"[^>]*>").matcher(source);
        Rect labelRect = null;
        String bestValue = null;
        int bestScore = Integer.MAX_VALUE;

        List<int[]> nodes = new java.util.ArrayList<>();
        List<String> values = new java.util.ArrayList<>();
        while (nodeMatcher.find()) {
            String tag = nodeMatcher.group(0);
            String value = firstNonBlank(
                    xmlAttr(tag, "text"),
                    xmlAttr(tag, "content-desc"),
                    xmlAttr(tag, "hint")
            );
            if (isBlank(value)) {
                continue;
            }
            int left = Integer.parseInt(nodeMatcher.group(1));
            int top = Integer.parseInt(nodeMatcher.group(2));
            int right = Integer.parseInt(nodeMatcher.group(3));
            int bottom = Integer.parseInt(nodeMatcher.group(4));
            if (uiLabel.equals(value)) {
                labelRect = new Rect(left, top, right, bottom);
            }
            nodes.add(new int[]{left, top, right, bottom});
            values.add(value);
        }

        if (labelRect == null) {
            return null;
        }

        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            if (uiLabel.equals(value) || isLikelyLabel(value) || !isPlausibleValue(uiLabel, value)) {
                continue;
            }
            int[] bounds = nodes.get(i);
            Rect rect = new Rect(bounds[0], bounds[1], bounds[2], bounds[3]);
            int verticalDelta = Math.abs(rect.centerY() - labelRect.centerY());
            boolean sameRow = verticalDelta <= Math.max(28, labelRect.height());
            boolean toRight = rect.left >= labelRect.right - 10;
            if (!sameRow || !toRight) {
                continue;
            }
            int score = verticalDelta * 100 + Math.abs(rect.left - labelRect.right);
            if (score < bestScore) {
                bestScore = score;
                bestValue = value.trim();
            }
        }

        if (bestValue != null) {
            logInfo("Found same-row XML value for label [" + uiLabel + "]: " + bestValue);
        }
        return bestValue;
    }

    private String xmlAttr(String tag, String name) {
        Matcher matcher = Pattern.compile("(?:^|[\\s])" + Pattern.quote(name) + "=\"([^\"]*)\"").matcher(tag);
        if (!matcher.find()) {
            return "";
        }
        String value = matcher.group(1);
        return isNoiseValue(value) ? "" : value.trim();
    }

    private String valueAfterStrippingLabels(String combined, String uiLabel) {
        if (isBlank(combined)) {
            return null;
        }
        String remaining = combined.replace('\n', ' ').replace('\u00A0', ' ').trim();
        if (remaining.startsWith(uiLabel)) {
            remaining = remaining.substring(uiLabel.length()).trim();
        }
        remaining = remaining.replace(uiLabel, " ");
        for (String label : Arrays.asList(
                "Product", "Product Name", "Account", "Status", "Direction", "Volume",
                "Contract Value", "Open Price", "Current Price", "Initial Margin",
                "Floating P/L", "Take Profit Price", "Stop Loss Price", "Interest",
                "Open Position Time", "Order Time", "Position ID"
        )) {
            if (!label.equals(uiLabel)) {
                remaining = remaining.replace(label, " ");
            }
        }
        remaining = remaining.replaceAll("[,|:]+", " ").trim();
        if (isBlank(remaining) || isLikelyLabel(remaining)) {
            return null;
        }
        for (String token : remaining.split("\\s+")) {
            if (!token.isBlank() && !isLikelyLabel(token)) {
                return token;
            }
        }
        return remaining;
    }

    public String findPaintedRowValue(String uiLabel) {
        Rect labelRect = labelBounds(uiLabel);
        if (labelRect == null) {
            return null;
        }

        String bestValue = null;
        int bestScore = Integer.MAX_VALUE;
        List<WebElement> matches = driver.findElements(By.xpath(
                "//android.widget.ScrollView//*[@text or @content-desc]"
        ));
        if (matches.isEmpty()) {
            matches = driver.findElements(By.xpath("//*[@text or @content-desc]"));
        }
        for (WebElement match : matches) {
            try {
                String text = extractBestText(match);
                if (!isPlausibleValue(uiLabel, text)) {
                    continue;
                }
                Rect rect = parseBounds(safeAttr(match, "bounds"));
                if (rect == null) {
                    continue;
                }
                int verticalDelta = Math.abs(rect.centerY() - labelRect.centerY());
                boolean sameRow = isOnCompactRow(labelRect, rect);
                boolean toRight = rect.left >= labelRect.right - 10;
                if (!sameRow || !toRight) {
                    continue;
                }
                int score = verticalDelta * 100 + Math.abs(rect.left - labelRect.right);
                if (score < bestScore) {
                    bestScore = score;
                    bestValue = text.trim();
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        if (bestValue != null) {
            logInfo("Found painted-row value for label [" + uiLabel + "]: " + bestValue);
        }
        return bestValue;
    }

    private Rect labelBounds(String uiLabel) {
        Rect header = detailsHeaderBounds();
        Rect best = null;
        int bestScore = Integer.MAX_VALUE;
        List<WebElement> labels = driver.findElements(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]")
        );
        if (labels.isEmpty()) {
            labels = driver.findElements(By.xpath("//*[@text=\"" + uiLabel + "\"]"));
        }
        for (WebElement label : labels) {
            try {
                if (!isElementDisplayed(label)) {
                    continue;
                }
                Rect rect = parseBounds(safeAttr(label, "bounds"));
                if (rect == null || !isCompactLabelRect(rect)) {
                    continue;
                }
                if (header != null && rect.bottom < header.top) {
                    continue;
                }
                int score = rect.top * 10 + rect.height();
                if (score < bestScore) {
                    bestScore = score;
                    best = rect;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private Rect detailsHeaderBounds() {
        int maxHeaderTop = (int) (screenHeight() * 0.32);
        for (String header : Arrays.asList(
                "Position Details",
                "Edit Position",
                "Pending Order Details",
                "Modify Order"
        )) {
            List<WebElement> headers = driver.findElements(By.xpath("//*[@text=\"" + header + "\"]"));
            for (WebElement element : headers) {
                try {
                    if (!isElementDisplayed(element)) {
                        continue;
                    }
                    Rect rect = parseBounds(safeAttr(element, "bounds"));
                    if (rect == null || !isCompactLabelRect(rect)) {
                        continue;
                    }
                    if (maxHeaderTop > 0 && rect.top > maxHeaderTop) {
                        continue;
                    }
                    return rect;
                } catch (StaleElementReferenceException ignored) {
                }
            }
        }
        return null;
    }

    public String findValueByFollowingSibling(String uiLabel) {
        List<By> locators = Arrays.asList(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]/following-sibling::android.widget.TextView"),
                By.xpath("//*[@text=\"" + uiLabel + "\"]/following-sibling::*[@text or @content-desc]")
        );

        for (By locator : locators) {
            try {
                List<WebElement> matches = driver.findElements(locator);
                for (WebElement el : matches) {
                    String text = extractBestText(el);
                    if (text == null || text.isBlank() || uiLabel.equals(text)) {
                        continue;
                    }
                    if (isLikelyLabel(text)) {
                        return null;
                    }
                    logInfo("Found value for label [" + uiLabel + "] using locator: " + locator);
                    return text.trim();
                }
            } catch (Exception e) {
                logWarn("Locator failed for label [" + uiLabel + "]: " + locator + " | " + e.getMessage());
            }
        }

        return null;
    }

    public String findValueFromPageSource(String uiLabel) {
        try {
            String source = pageSource();
            String value = extractValueForLabelFromXml(source, uiLabel);
            if (value != null && !value.isBlank()) {
                logInfo("Found value for label [" + uiLabel + "] from page source: " + value);
                return value.trim();
            }
        } catch (Exception e) {
            logWarn("Page source parsing failed for label [" + uiLabel + "]: " + e.getMessage());
        }
        return null;
    }

    private String extractValueForLabelFromXml(String xml, String uiLabel) {
        if (xml == null || xml.isBlank()) {
            return null;
        }

        String safeLabel = Pattern.quote(uiLabel);

        String pattern =
                "<android\\.widget\\.TextView[^>]*text=\"" + safeLabel + "\"[^>]*/>" +
                        "(?s).*?" +
                        "<android\\.widget\\.TextView[^>]*text=\"([^\"]+)\"[^>]*/>";

        Matcher matcher = Pattern.compile(pattern).matcher(xml);

        if (matcher.find()) {
            String found = matcher.group(1);
            if (found != null && !found.trim().equals(uiLabel) && !isLikelyLabel(found)) {
                return found.trim();
            }
        }

        return null;
    }

    public String findValueByFollowingSiblingScoped(String uiLabel) {
        String xpath =
                "//android.widget.ScrollView//android.widget.TextView[@text=\"" + uiLabel + "\"]" +
                        "/following-sibling::android.widget.TextView[1]";

        try {
            List<WebElement> matches = driver.findElements(By.xpath(xpath));
            for (WebElement el : matches) {
                String text = extractBestText(el);
                if (text == null || text.isBlank() || uiLabel.equals(text)) {
                    continue;
                }
                if (isLikelyLabel(text)) {
                    return null;
                }
                return text.trim();
            }
        } catch (Exception e) {
            logWarn("Scoped sibling failed for label [" + uiLabel + "]: " + e.getMessage());
        }
        return null;
    }
}
