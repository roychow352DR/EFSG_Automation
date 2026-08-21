package utils;

import AbstractComponent.MobileAbstractComponents;
import PageObject.NativeApp.AppPOManager;
import PageObject.NativeApp.AppTradeView;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
        return firstNonBlank(
                safeText(el),
                safeAttr(el, "text"),
                safeAttr(el, "contentDescription"),
                safeAttr(el, "content-desc"),
                safeAttr(el, "hint")
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
                "Position ID"
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
            String value = el.getAttribute(attr);
            if (value == null) {
                return "";
            }
            if ("null".equalsIgnoreCase(value.trim())) {
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

    public String findValueByFollowingSibling(String uiLabel) {
        List<By> locators = Arrays.asList(
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]/following-sibling::android.widget.TextView[1]"),
                By.xpath("//*[@text=\"" + uiLabel + "\"]/following-sibling::android.widget.TextView[1]"),
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]/following::android.widget.TextView[1]")
        );

        for (By locator : locators) {
            try {
                List<WebElement> matches = driver.findElements(locator);
                for (WebElement el : matches) {
                    String text = extractBestText(el);
                    printElementDetails(el, "followingSiblingMatch");
                    if (text != null && !text.isBlank()) {
                        logInfo("Found value for label [" + uiLabel + "] using locator: " + locator);
                        return text.trim();
                    }
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
            if (found != null && !found.trim().equals(uiLabel)) {
                return found.trim();
            }
        }

        return null;
    }

    public String findValueByFollowingSiblingScoped(String uiLabel) {
        String xpath =
                "//android.widget.ScrollView//android.widget.TextView[@text=\"" + uiLabel + "\"]" +
                        "/following-sibling::android.widget.TextView[1]";

        List<WebElement> matches = driver.findElements(By.xpath(xpath));
        for (WebElement el : matches) {
            String text = extractBestText(el);
            printElementDetails(el, "scopedFollowingSiblingMatch");
            if (text != null && !text.isBlank()) {
                return text.trim();
            }
        }
        return null;
    }
}
