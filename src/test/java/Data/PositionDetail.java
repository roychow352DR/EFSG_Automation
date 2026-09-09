package Data;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class PositionDetail {



private final AppiumDriver driver;
private final MobileAbstractComponents abs;

    public PositionDetail(AppiumDriver driver){
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
    }


    public String getPositionValueByLabel(String label, String symbolDecimal) {
        String uiLabel = mapUiLabel(label);

        WebElement labelElement = waitForVisibleLabel(uiLabel);

        String rawValue;
        if (requiresOcrFallback(uiLabel)) {
            logInfo("Using OCR fallback for label: " + uiLabel);
            rawValue = extractValueUsingOcr(labelElement, uiLabel);
        } else {
            rawValue = findAccessibleValueOnRight(labelElement, uiLabel);

            if (rawValue == null || rawValue.isBlank()) {
                logWarn("Accessible extraction failed for label: " + uiLabel + ", trying OCR fallback.");
                rawValue = extractValueUsingOcr(labelElement, uiLabel);
            }
        }

        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Value not found for label: " + uiLabel);
        }

        return normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }

    private boolean requiresOcrFallback(String uiLabel) {
        return "Direction".equals(uiLabel) || "Volume".equals(uiLabel);
    }

    private WebElement waitForVisibleLabel(String uiLabel) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<By> locators = Arrays.asList(
                AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.widget.TextView\").text(\"" + uiLabel + "\")"
                ),
                By.xpath("//*[@text=\"" + uiLabel + "\"]"),
                By.xpath("//android.widget.TextView[@text=\"" + uiLabel + "\"]")
        );

        for (By locator : locators) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (TimeoutException ignored) {
            }
        }

        throw new NoSuchElementException("Could not locate visible label: " + uiLabel);
    }

    private String findAccessibleValueOnRight(WebElement labelElement, String uiLabel) {
        Rect labelRect = parseBounds(safeAttr(labelElement, "bounds"));
        if (labelRect == null) {
            return null;
        }

        List<WebElement> textViews = driver.findElements(By.xpath("//android.widget.TextView"));
        WebElement best = null;
        int bestScore = Integer.MAX_VALUE;

        for (WebElement el : textViews) {
            String text = extractBestText(el);
            if (text == null || text.isBlank()) {
                continue;
            }

            text = text.trim();

            if (uiLabel.equals(text) || isLikelyLabel(text)) {
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

            int horizontalGap = rect.left - labelRect.right;
            int score = verticalDelta * 100 + Math.abs(horizontalGap);

            if (score < bestScore) {
                bestScore = score;
                best = el;
            }
        }

        if (best != null) {
            logInfo("Accessible value found for " + uiLabel + ": " + extractBestText(best));
            printElementDetails(best, "bestAccessibleValue");
        }

        return best == null ? null : extractBestText(best);
    }

    private String extractValueUsingOcr(WebElement labelElement, String uiLabel) {
        Rect labelRect = parseBounds(safeAttr(labelElement, "bounds"));
        if (labelRect == null) {
            return null;
        }

        try {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            BufferedImage fullImg = ImageIO.read(screenshot);

            if (fullImg == null) {
                return null;
            }

            Dimension viewport = driver.manage().window().getSize();
            double scaleX = (double) fullImg.getWidth() / viewport.getWidth();
            double scaleY = (double) fullImg.getHeight() / viewport.getHeight();

            Rect scaledLabelRect = labelRect.scale(scaleX, scaleY);

            int cropLeft = clamp(scaledLabelRect.right + 8, 0, fullImg.getWidth() - 1);
            int cropTop = clamp(scaledLabelRect.top - 12, 0, fullImg.getHeight() - 1);
            int cropRight = clamp(fullImg.getWidth() - 1, 0, fullImg.getWidth() - 1);
            int cropBottom = clamp(scaledLabelRect.bottom + 12, 0, fullImg.getHeight() - 1);

            if (cropRight <= cropLeft || cropBottom <= cropTop) {
                return null;
            }

            BufferedImage rowImage = fullImg.getSubimage(
                    cropLeft,
                    cropTop,
                    cropRight - cropLeft,
                    cropBottom - cropTop
            );

            BufferedImage processed = preprocessForOcr(rowImage);

            logDebug("OCR crop for " + uiLabel
                    + " | viewport=" + viewport.width + "x" + viewport.height
                    + " | screenshot=" + fullImg.getWidth() + "x" + fullImg.getHeight()
                    + " | scaleX=" + scaleX
                    + " | scaleY=" + scaleY
                    + " | crop=[" + cropLeft + "," + cropTop + "][" + cropRight + "," + cropBottom + "]");

            String ocrText = runOcr(processed);

            logDebug("Raw OCR text for " + uiLabel + ": " + String.valueOf(ocrText));

            return postProcessOcrValue(uiLabel, ocrText);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read screenshot for OCR", e);
        }
    }

    private BufferedImage preprocessForOcr(BufferedImage input) {
        BufferedImage gray = new BufferedImage(
                input.getWidth(),
                input.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );

        Graphics2D g = gray.createGraphics();
        g.drawImage(input, 0, 0, null);
        g.dispose();

        BufferedImage enlarged = new BufferedImage(
                gray.getWidth() * 2,
                gray.getHeight() * 2,
                BufferedImage.TYPE_BYTE_GRAY
        );

        Graphics2D g2 = enlarged.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(gray, 0, 0, enlarged.getWidth(), enlarged.getHeight(), null);
        g2.dispose();

        return enlarged;
    }

    private String runOcr(BufferedImage image) {
        try {
            return getTesseract().doOCR(image);
        } catch (TesseractException e) {
            throw new RuntimeException("OCR failed", e);
        }
    }

    private String postProcessOcrValue(String uiLabel, String raw) {
        if (raw == null) {
            return null;
        }

        String cleaned = raw
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("\\s+", " ")
                .trim();

        if (cleaned.isBlank()) {
            return cleaned;
        }

        if ("Direction".equals(uiLabel)) {
            String upper = cleaned.toUpperCase(Locale.ROOT);

            if (upper.contains("BUY")) return "BUY";
            if (upper.contains("SELL")) return "SELL";
            if (upper.contains("LONG")) return "BUY";
            if (upper.contains("SHORT")) return "SELL";
        }

        if ("Volume".equals(uiLabel)) {
            cleaned = cleaned.replaceAll("(?i)lots", "").trim();
            Matcher m = Pattern.compile("[-+]?\\d+(?:[.,]\\d+)?").matcher(cleaned);
            if (m.find()) {
                return m.group().replace(",", "");
            }
        }

        return cleaned;
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

    private String mapUiLabel(String label) {
        switch (label) {
            case "Side":
                return "Direction";
            case "Qty":
                return "Volume";
            default:
                return label;
        }
    }

    private String normalizeByLabel(String label, String rawValue, String symbolDecimal) {
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

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private ITesseract getTesseract() {
        Tesseract t = new Tesseract();

        // If needed, uncomment and set your tessdata path:
        // t.setDatapath("absolute/path/to/tessdata");

        t.setLanguage("eng");
        t.setPageSegMode(7);
        t.setOcrEngineMode(1);
        return t;
    }

    private void logInfo(String msg) {
        System.out.println("[INFO] " + msg);
    }

    private void logWarn(String msg) {
        System.out.println("[WARN] " + msg);
    }

    private void logDebug(String msg) {
        System.out.println("[DEBUG] " + msg);
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

        int width() {
            return right - left;
        }

        int height() {
            return bottom - top;
        }

        int centerY() {
            return top + height() / 2;
        }

        Rect scale(double scaleX, double scaleY) {
            return new Rect(
                    (int) Math.round(left * scaleX),
                    (int) Math.round(top * scaleY),
                    (int) Math.round(right * scaleX),
                    (int) Math.round(bottom * scaleY)
            );
        }
    }
}
