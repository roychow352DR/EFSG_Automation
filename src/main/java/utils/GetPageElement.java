package utils;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPageElement {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public GetPageElement(AppiumDriver driver){
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
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
            rawValue = rawValue.replace(" Lots", "").replace(" lots", "").trim();
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
                "Close Position"
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
            return value.matches("(?i)\\d+(\\.\\d+)?(\\s*Lots?)?");
        }
        if ("Contract Value".equals(uiLabel) || "Initial Margin".equals(uiLabel)) {
            return value.matches("(?i).*\\d.*");
        }
        return true;
    }

    private String safeElementFingerprint(WebElement el) {
        return safeAttr(el, "className") + "|" + safeAttr(el, "bounds") + "|" + extractBestText(el);
    }

    private void printElementDetails(WebElement el, String prefix) {
        System.out.println(
                prefix
                        + " | class=\"" + safeAttr(el, "className") + "\""
                        + " | text=\"" + safeText(el) + "\""
                        + " | textAttr=\"" + safeAttr(el, "text") + "\""
                        + " | contentDesc=\"" + safeAttr(el, "contentDescription") + "\""
                        + " | hint=\"" + safeAttr(el, "hint") + "\""
                        + " | resourceId=\"" + safeAttr(el, "resourceId") + "\""
                        + " | clickable=\"" + safeAttr(el, "clickable") + "\""
                        + " | enabled=\"" + safeAttr(el, "enabled") + "\""
                        + " | selected=\"" + safeAttr(el, "selected") + "\""
                        + " | bounds=\"" + safeAttr(el, "bounds") + "\""
        );
    }

    private Rect parseBounds(String bounds) {
        if (bounds == null || bounds.isBlank()) {
            return null;
        }

        Pattern p = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]");
        Matcher m = p.matcher(bounds);
        if (!m.matches()) {
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

        int centerY() {
            return top + height() / 2;
        }
    }

    public String resolveLabelValue(String uiLabel) {
        includeUnimportantViews();
        waitForLabel(uiLabel);
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(20))
                    .pollingEvery(Duration.ofMillis(400))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> readLabelValueOnce(uiLabel));
        } catch (TimeoutException e) {
            logWarn("Could not resolve value for label [" + uiLabel + "] after waiting");
            return null;
        }
    }

    private String readLabelValueOnce(String uiLabel) {
        if ("Direction".equals(uiLabel) || "Side".equals(uiLabel)) {
            String direction = plausible(uiLabel, findDirectionValue());
            if (direction != null) {
                return direction;
            }
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
        for (String direction : Arrays.asList("BUY", "SELL")) {
            List<WebElement> matches = driver.findElements(By.xpath(
                    "//*[@text='" + direction + "' or @content-desc='" + direction + "']"
            ));
            for (WebElement match : matches) {
                try {
                    String text = firstUsableValue(extractBestText(match), direction);
                    if (text != null && isPlausibleValue("Direction", text)) {
                        logInfo("Found Direction from accessibility node: " + text);
                        return text.equalsIgnoreCase("SELL") ? "SELL" : "BUY";
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
        }

        String source;
        try {
            source = driver.getPageSource();
        } catch (Exception e) {
            return null;
        }
        if (source == null || source.isBlank()) {
            return null;
        }

        int from = source.indexOf("text=\"Direction\"");
        String window = from >= 0
                ? source.substring(from, Math.min(source.length(), from + 4000))
                : source;
        int volumeAt = window.indexOf("text=\"Volume\"");
        if (volumeAt > 0) {
            window = window.substring(0, volumeAt);
        }

        Matcher matcher = Pattern.compile(
                "(?:text|content-desc|contentDescription|name)=\"(BUY|SELL|Buy|Sell)\""
        ).matcher(window);
        if (matcher.find()) {
            String found = matcher.group(1).equalsIgnoreCase("SELL") ? "SELL" : "BUY";
            logInfo("Found Direction from page source: " + found);
            return found;
        }
        return null;
    }

    private String plausible(String uiLabel, String value) {
        return isPlausibleValue(uiLabel, value) ? value.trim() : null;
    }

    private void waitForLabel(String uiLabel) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(
                            By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]")
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
        List<WebElement> labels = driver.findElements(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]")
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
                "//android.widget.ScrollView//android.widget.TextView"
        ));
        if (candidates.isEmpty()) {
            candidates = driver.findElements(By.className("android.widget.TextView"));
        }

        for (WebElement el : candidates) {
            try {
                String text = extractBestText(el);
                if (text == null || text.isBlank() || uiLabel.equals(text) || isLikelyLabel(text)) {
                    continue;
                }

                Rect rect = parseBounds(safeAttr(el, "bounds"));
                if (rect == null) {
                    continue;
                }

                int verticalDelta = Math.abs(rect.centerY() - labelRect.centerY());
                boolean sameRow = verticalDelta <= Math.max(28, labelRect.height());
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

        String source;
        try {
            source = driver.getPageSource();
        } catch (Exception e) {
            logWarn("Could not read page source for label [" + uiLabel + "]: " + e.getMessage());
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

        Matcher textMatcher = Pattern.compile("(?:text|content-desc|hint)=\"([^\"]+)\"").matcher(windowMatcher.group(1));
        while (textMatcher.find()) {
            String found = textMatcher.group(1).trim();
            if (found.isBlank() || uiLabel.equals(found) || isLikelyLabel(found)) {
                continue;
            }
            logInfo("Found value for label [" + uiLabel + "] between adjacent labels: " + found);
            return found;
        }

        return null;
    }

    private String nextKnownLabel(String uiLabel) {
        String source;
        try {
            source = driver.getPageSource();
        } catch (Exception e) {
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
                "Target Price"
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
        String source;
        try {
            source = driver.getPageSource();
        } catch (Exception e) {
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
            if (uiLabel.equals(value) || isLikelyLabel(value)) {
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
        Matcher matcher = Pattern.compile(name + "=\"([^\"]*)\"").matcher(tag);
        if (!matcher.find()) {
            return "";
        }
        String value = matcher.group(1);
        return "null".equalsIgnoreCase(value) ? "" : value.trim();
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

        List<String> candidates;
        if ("Direction".equals(uiLabel)) {
            candidates = Arrays.asList("BUY", "SELL", "Buy", "Sell");
        } else if ("Volume".equals(uiLabel)) {
            candidates = new java.util.ArrayList<>();
            for (WebElement el : driver.findElements(By.className("android.widget.TextView"))) {
                String text = extractBestText(el);
                if (text != null && text.matches("(?i)\\d+(\\.\\d+)?(\\s*Lots?)?")) {
                    candidates.add(text);
                }
            }
        } else {
            return null;
        }

        for (String candidate : candidates) {
            List<WebElement> matches = driver.findElements(
                    By.xpath("//*[(@text=\"" + candidate + "\" or @content-desc=\"" + candidate + "\")]")
            );
            for (WebElement match : matches) {
                try {
                    Rect rect = parseBounds(safeAttr(match, "bounds"));
                    if (rect == null) {
                        continue;
                    }
                    int verticalDelta = Math.abs(rect.centerY() - labelRect.centerY());
                    boolean sameRow = verticalDelta <= Math.max(40, labelRect.height());
                    boolean toRight = rect.left >= labelRect.right - 10;
                    if (sameRow && toRight) {
                        String text = firstNonBlank(extractBestText(match), candidate);
                        logInfo("Found painted-row value for label [" + uiLabel + "]: " + text);
                        return text.trim();
                    }
                } catch (StaleElementReferenceException ignored) {
                    // Live quotes can invalidate one node; keep scanning the rest.
                }
            }
        }
        return null;
    }

    private Rect labelBounds(String uiLabel) {
        List<WebElement> labels = driver.findElements(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]")
        );
        if (labels.isEmpty()) {
            return null;
        }
        return parseBounds(safeAttr(labels.getFirst(), "bounds"));
    }

    public String findValueByFollowingSibling(String uiLabel) {
        List<By> locators = Arrays.asList(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]/following-sibling::android.widget.TextView"),
                By.xpath("//*[@text=\"" + uiLabel + "\"]/following-sibling::android.widget.TextView"),
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]/following::android.widget.TextView")
        );

        for (By locator : locators) {
            try {
                List<WebElement> matches = driver.findElements(locator);
                for (WebElement el : matches) {
                    String text = extractBestText(el);
                    printElementDetails(el, "followingSiblingMatch");
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
            String source = driver.getPageSource();
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
        } catch (StaleElementReferenceException e) {
            logWarn("Stale scoped sibling for label [" + uiLabel + "]");
        }
        return null;
    }
}
