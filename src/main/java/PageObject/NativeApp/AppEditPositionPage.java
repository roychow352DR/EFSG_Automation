package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppEditPositionPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;
    public static String stopLossPrice;
    public static String takeProfitPrice;

    public static void resetCapturedOrderValues() {
        stopLossPrice = null;
        takeProfitPrice = null;
    }

    public AppEditPositionPage(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(className = "android.widget.EditText")
    List<WebElement> inputFields;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]")
    WebElement stopLossPlusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]")
    WebElement stopLossMinusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[5]")
    WebElement takeProfitPlusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[4]")
    WebElement takeProfitMinusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[3]")
    WebElement stopLossClearBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[6]")
    WebElement takeProfitClearBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[1]")
    WebElement stopLossTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[2]")
    WebElement takeProfitTextFieldAos;

    @FindBy(className = "android.widget.TextView")
    List<WebElement> textMessages;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Edit Position\"])[1]")
    WebElement headerAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[13]")
    WebElement closeBtnAos;


    public String getDisplayedValue(String label, String symbolDecimal) {
        waitForEditPositionReady();
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if ((rawValue == null || rawValue.isBlank()) && isVolumeLabel(label)) {
            rawValue = getPageElement.readLabelValueFast("Lots");
            if (rawValue != null && !rawValue.isBlank()) {
                uiLabel = "Lots";
            }
        }
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value on Edit Position for label: " + uiLabel);
        }
        return getPageElement.normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }

    private boolean isVolumeLabel(String label) {
        return "Volume".equals(label) || "Qty".equals(label) || "Lots".equals(label);
    }

    private void waitForEditPositionReady() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(By.xpath(
                            "//*[@text='Edit Position' or @content-desc='Edit Position']"
                    )).isEmpty());
        } catch (TimeoutException ignored) {
        }
    }

    public String getInputFieldValue(String inputFieldName) {
        if (driver instanceof AndroidDriver) {
            return priceField(inputFieldName).getText();
        }
        return inputFieldName;
    }

    public void adjustPrice(String ctaBtn, String priceType) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        hideAndroidKeyboard();
        revealPriceRow(priceType);
        Point point = priceStepperPoint(priceType, ctaBtn);
        getPageElement.logInfo("Tapping " + ctaBtn + " on " + priceType + " at " + point.getX() + "," + point.getY());
        abs.tapAt(point.getX(), point.getY());
    }

    private Point priceStepperPoint(String fieldName, String ctaBtn) {
        WebElement field = priceField(fieldName);
        int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
        if (fieldBounds == null) {
            throw new NoSuchElementException("Could not read bounds for " + fieldName);
        }
        int fieldCenterY = (fieldBounds[1] + fieldBounds[3]) / 2;
        int fieldLeft = fieldBounds[0];
        int fieldRight = fieldBounds[2];
        // Edit Position: [minus] [field] [plus] [clear]. Some tickets: [minus] [field] [clear] [plus].
        List<StepperIcon> icons = compactControlsOnRow(fieldCenterY);
        StepperIcon minus = minusIcon(icons, fieldLeft);
        StepperIcon plus = plusIcon(icons, fieldRight);
        StepperIcon clear = clearIcon(icons, fieldRight, plus);
        String action = normalizeStepperAction(ctaBtn);
        if ("plus".equals(action)) {
            if (plus != null) {
                return insetControlCenter(plus.bounds, "plus");
            }
            Dimension window = driver.manage().window().getSize();
            return new Point(Math.min(window.getWidth() - 24, fieldRight + 70), fieldCenterY);
        }
        if ("minus".equals(action)) {
            if (minus != null) {
                return insetControlCenter(minus.bounds, "minus");
            }
            return new Point(Math.max(8, fieldLeft - 48), fieldCenterY);
        }
        if ("clear".equals(action)) {
            if (clear != null) {
                return insetControlCenter(clear.bounds, "clear");
            }
            return new Point(fieldRight + 28, fieldCenterY);
        }
        throw new IllegalArgumentException("Unsupported stepper button: " + ctaBtn);
    }

    private String normalizeStepperAction(String ctaBtn) {
        if (ctaBtn == null || ctaBtn.isBlank()) {
            throw new IllegalArgumentException("Stepper button was empty");
        }
        String text = ctaBtn.trim();
        if (text.equals("+") || text.equalsIgnoreCase("plus")) {
            return "plus";
        }
        if (text.equals("-") || text.equalsIgnoreCase("minus")) {
            return "minus";
        }
        if (text.equals("✕") || text.equals("×") || text.equalsIgnoreCase("x")
                || text.equals("?") || text.equalsIgnoreCase("clear")) {
            return "clear";
        }
        return text.toLowerCase(Locale.ROOT);
    }

    private List<StepperIcon> compactControlsOnRow(int rowCenterY) {
        List<StepperIcon> found = new ArrayList<>();
        List<By> locators = List.of(
                By.xpath("//android.widget.ScrollView//android.view.ViewGroup[@clickable='true']"),
                By.xpath("//android.view.ViewGroup[@clickable='true']")
        );
        for (By locator : locators) {
            for (WebElement el : driver.findElements(locator)) {
                try {
                    int[] bounds = parseBounds(elementAttribute(el, "bounds"));
                    if (bounds == null) {
                        continue;
                    }
                    int width = bounds[2] - bounds[0];
                    int height = bounds[3] - bounds[1];
                    if (width < 32 || width > 120 || height < 32 || height > 120) {
                        continue;
                    }
                    int centerY = (bounds[1] + bounds[3]) / 2;
                    if (Math.abs(centerY - rowCenterY) > 72) {
                        continue;
                    }
                    if (alreadyHasSimilarControl(found, bounds)) {
                        continue;
                    }
                    found.add(new StepperIcon(bounds, el));
                } catch (StaleElementReferenceException ignored) {
                }
            }
            if (!found.isEmpty()) {
                break;
            }
        }
        found.sort(Comparator.comparingInt(icon -> icon.bounds[0]));
        return found;
    }

    private StepperIcon minusIcon(List<StepperIcon> icons, int fieldLeft) {
        StepperIcon glyph = null;
        StepperIcon closest = null;
        for (StepperIcon icon : icons) {
            if (icon.centerX() >= fieldLeft) {
                continue;
            }
            if ("minus".equals(icon.glyphKind())) {
                glyph = icon;
            }
            if (closest == null || icon.centerX() > closest.centerX()) {
                closest = icon;
            }
        }
        return glyph != null ? glyph : closest;
    }

    private StepperIcon plusIcon(List<StepperIcon> icons, int fieldRight) {
        List<StepperIcon> right = iconsRightOf(icons, fieldRight);
        for (StepperIcon icon : right) {
            if ("plus".equals(icon.glyphKind())) {
                return icon;
            }
        }
        if (right.size() >= 2) {
            return largerIcon(right);
        }
        return right.isEmpty() ? null : right.getFirst();
    }

    private StepperIcon clearIcon(List<StepperIcon> icons, int fieldRight, StepperIcon plus) {
        List<StepperIcon> right = iconsRightOf(icons, fieldRight);
        for (StepperIcon icon : right) {
            if ("clear".equals(icon.glyphKind())) {
                return icon;
            }
        }
        if (right.size() >= 2) {
            StepperIcon smaller = smallerIcon(right);
            if (plus == null || smaller.centerX() != plus.centerX()) {
                return smaller;
            }
        }
        return null;
    }

    private List<StepperIcon> iconsRightOf(List<StepperIcon> icons, int fieldRight) {
        List<StepperIcon> right = new ArrayList<>();
        for (StepperIcon icon : icons) {
            if (icon.centerX() > fieldRight) {
                right.add(icon);
            }
        }
        return right;
    }

    private StepperIcon largerIcon(List<StepperIcon> icons) {
        StepperIcon best = icons.getFirst();
        for (StepperIcon icon : icons) {
            if (icon.area() > best.area()) {
                best = icon;
            }
        }
        return best;
    }

    private StepperIcon smallerIcon(List<StepperIcon> icons) {
        StepperIcon best = icons.getFirst();
        for (StepperIcon icon : icons) {
            if (icon.area() < best.area()) {
                best = icon;
            }
        }
        return best;
    }

    private Point insetControlCenter(int[] bounds, String action) {
        int centerY = (bounds[1] + bounds[3]) / 2;
        int width = bounds[2] - bounds[0];
        int x = switch (action) {
            case "plus" -> bounds[0] + Math.max((width * 2) / 3, width / 2);
            case "minus" -> bounds[0] + Math.min(width / 3, Math.max(12, width / 2));
            default -> (bounds[0] + bounds[2]) / 2;
        };
        return new Point(x, centerY);
    }

    private void revealPriceRow(String fieldName) {
        Dimension window = driver.manage().window().getSize();
        int minY = (int) (window.getHeight() * 0.18);
        int maxY = (int) (window.getHeight() * 0.72);
        for (int swipe = 0; swipe < 3; swipe++) {
            try {
                WebElement label = priceLabel(fieldName);
                int y = label.getLocation().getY();
                if (y >= minY && y <= maxY) {
                    return;
                }
                if (y > maxY) {
                    abs.swipeUp(driver);
                }
            } catch (RuntimeException e) {
                abs.swipeUp(driver);
            }
        }
    }

    private void hideAndroidKeyboard() {
        if (!(driver instanceof AndroidDriver androidDriver)) {
            return;
        }
        try {
            if (androidDriver.isKeyboardShown()) {
                androidDriver.hideKeyboard();
            }
        } catch (RuntimeException ignored) {
        }
    }

    private boolean alreadyHasSimilarControl(List<StepperIcon> found, int[] bounds) {
        int centerX = (bounds[0] + bounds[2]) / 2;
        int centerY = (bounds[1] + bounds[3]) / 2;
        for (StepperIcon existing : found) {
            if (Math.abs(existing.centerX() - centerX) <= 12
                    && Math.abs(existing.centerY() - centerY) <= 12) {
                return true;
            }
        }
        return false;
    }

    private static final class StepperIcon {
        private final int[] bounds;
        private final WebElement element;

        private StepperIcon(int[] bounds, WebElement element) {
            this.bounds = bounds;
            this.element = element;
        }

        private int centerX() {
            return (bounds[0] + bounds[2]) / 2;
        }

        private int centerY() {
            return (bounds[1] + bounds[3]) / 2;
        }

        private int area() {
            return Math.max(0, bounds[2] - bounds[0]) * Math.max(0, bounds[3] - bounds[1]);
        }

        private String glyphKind() {
            int[] inner = smallestInnerPath(element, bounds);
            if (inner == null) {
                return null;
            }
            int width = inner[2] - inner[0];
            int height = inner[3] - inner[1];
            if (height <= 12 && width >= 20) {
                return "minus";
            }
            if (width <= 28 && height <= 28) {
                return "clear";
            }
            if (Math.abs(width - height) <= 12 && width >= 30) {
                return "plus";
            }
            return null;
        }

        private static int[] smallestInnerPath(WebElement icon, int[] iconBounds) {
            int[] best = null;
            int bestArea = Integer.MAX_VALUE;
            int iconArea = Math.max(1, (iconBounds[2] - iconBounds[0]) * (iconBounds[3] - iconBounds[1]));
            try {
                for (WebElement path : icon.findElements(By.className("com.horcrux.svg.PathView"))) {
                    int[] box = parseBoundsStatic(elementBounds(path));
                    if (box == null) {
                        continue;
                    }
                    int area = Math.max(0, box[2] - box[0]) * Math.max(0, box[3] - box[1]);
                    if (area < 16 || area >= iconArea * 0.92) {
                        continue;
                    }
                    if (area < bestArea) {
                        bestArea = area;
                        best = box;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
            return best;
        }

        private static String elementBounds(WebElement element) {
            try {
                String value = element.getAttribute("bounds");
                if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
                    return null;
                }
                return value;
            } catch (RuntimeException e) {
                return null;
            }
        }

        private static int[] parseBoundsStatic(String bounds) {
            if (bounds == null || bounds.isBlank()) {
                return null;
            }
            Matcher matcher = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]").matcher(bounds);
            if (!matcher.find()) {
                return null;
            }
            return new int[]{
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4))
            };
        }
    }

    public void fillInTextField(String textFieldName, String direction, String decimal, int priceDifVal) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        String enterPrice;
        switch (textFieldName) {
            case "Stop Loss" -> {
                if (direction.equalsIgnoreCase("BUY")) {
                    enterPrice = String.valueOf(Float.parseFloat(getStopLossPrice(direction, decimal)) - priceDifVal);
                } else {
                    enterPrice = String.valueOf(Float.parseFloat(getStopLossPrice(direction, decimal)) + priceDifVal);
                }
                stopLossPrice = abs.normalizePriceToDecimals(enterPrice, decimal);
            }
            case "Take Profit" -> {
                if (direction.equalsIgnoreCase("BUY")) {
                    enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) + priceDifVal);
                } else {
                    enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) - priceDifVal);
                }
                takeProfitPrice = abs.normalizePriceToDecimals(enterPrice, decimal);
            }
            default -> throw new IllegalStateException("Unexpected value: " + textFieldName);
        }
        typeIntoPriceField(textFieldName, enterPrice);
    }

    private void typeIntoPriceField(String textFieldName, String enterPrice) {
        WebElement field = priceField(textFieldName);
        try {
            field.clear();
        } catch (Exception ignored) {
            abs.tapElement(field);
        }
        field = priceField(textFieldName);
        abs.typeWithAndroidKeys((AndroidDriver) driver, field, enterPrice);
    }

    private WebElement priceField(String priceType) {
        abs.waitUntilElementVisible(By.xpath("//*[@text='Edit Position' or @content-desc='Edit Position']"));
        WebElement label = priceLabel(priceType);
        int[] labelBounds = parseBounds(elementAttribute(label, "bounds"));

        WebElement closest = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement field : driver.findElements(By.className("android.widget.EditText"))) {
            int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
            if (labelBounds == null || fieldBounds == null) {
                continue;
            }
            int labelCenterY = (labelBounds[1] + labelBounds[3]) / 2;
            int fieldCenterY = (fieldBounds[1] + fieldBounds[3]) / 2;
            int verticalGap = fieldCenterY - labelCenterY;
            if (verticalGap < -20) {
                continue;
            }
            int score = Math.abs(verticalGap) * 100 + Math.abs(fieldBounds[0] - labelBounds[0]);
            if (score < bestScore) {
                bestScore = score;
                closest = field;
            }
        }

        if (closest == null) {
            throw new NoSuchElementException("Could not find EditText for price type: " + priceType);
        }
        return closest;
    }

    private WebElement priceLabel(String priceType) {
        String labelToken = switch (priceType) {
            case "Stop Loss" -> "Stop Loss";
            case "Take Profit" -> "Take Profit";
            default -> throw new IllegalStateException("Unexpected value: " + priceType);
        };

        abs.waitUntilElementVisible(
                By.xpath("//android.widget.TextView[contains(@text,\"" + labelToken + "\")]")
        );

        WebElement fieldLabel = null;
        for (WebElement el : driver.findElements(
                By.xpath("//android.widget.TextView[contains(@text,\"" + labelToken + "\")]"))) {
            String text = el.getText() == null ? "" : el.getText().trim();
            if (text.contains("&") || text.toLowerCase().contains(" and ")) {
                continue;
            }
            fieldLabel = el;
        }

        if (fieldLabel == null) {
            throw new NoSuchElementException("Could not find field label for price type: " + priceType);
        }
        return fieldLabel;
    }

    private String elementAttribute(WebElement element, String name) {
        try {
            String value = element.getAttribute(name);
            if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
                return null;
            }
            return value;
        } catch (RuntimeException e) {
            return null;
        }
    }

    private int[] parseBounds(String bounds) {
        if (bounds == null || bounds.isBlank()) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]").matcher(bounds);
        if (!matcher.find()) {
            return null;
        }
        return new int[]{
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
        };
    }

    public String getStopLossPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        switch (direction) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]));
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]));
            }
        }
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        stopLossPrice = formattedPrice;
        return formattedPrice;
    }

    public String getTakeProfitPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        switch (direction) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]));
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]));
            }
        }
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        takeProfitPrice = formattedPrice;
        return formattedPrice;
    }

    public String getValidationValue(String label) {
        return switch (label) {
            case "Stop Loss Price", "Stop Loss" -> stopLossPrice;
            case "Take Profit Price", "Take Profit" -> takeProfitPrice;
            case "Direction" -> AppTradeView.selectedDirection;
            default -> null;
        };
    }

    public void tapsButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Cancel Order")) {
                WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup"));
                abs.waitUntilElementFind(button);
                button.click();
            } else {
                driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
            }
        }
    }

    public boolean getTextMessage(String messageContent) {
        if (driver instanceof AndroidDriver) {
            for (WebElement ele : textMessages) {
                if (ele.getText().equalsIgnoreCase(messageContent)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean getHeader() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(headerAos);
            return headerAos.isDisplayed();
        }
        return false;
    }

    public String getHeaderText() {
        WebElement header = waitForEditPositionHeader();
        if (header == null) {
            return "";
        }
        String text = header.getText();
        if (text == null || text.trim().isEmpty()) {
            text = elementAttribute(header, "text");
        }
        if (text == null || text.trim().isEmpty()) {
            text = elementAttribute(header, "content-desc");
        }
        return text == null ? "" : text.trim();
    }

    private WebElement waitForEditPositionHeader() {
        if (!(driver instanceof AndroidDriver)) {
            return null;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.ignoring(StaleElementReferenceException.class);
        return wait.until(d -> visibleEditPositionHeader());
    }

    private WebElement visibleEditPositionHeader() {
        return firstPageTitle("Edit Position");
    }

    private WebElement firstPageTitle(String title) {
        Dimension window = driver.manage().window().getSize();
        int maxHeaderY = (int) (window.getHeight() * 0.45);
        int maxHeaderWidth = (int) (window.getWidth() * 0.75);
        WebElement compact = null;
        WebElement any = null;
        int bestCompactY = Integer.MAX_VALUE;
        int bestAnyY = Integer.MAX_VALUE;
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@text='" + title + "' or @content-desc='" + title + "' or contains(@text,'" + title + "')]"
        ))) {
            try {
                String text = firstNonBlank(
                        el.getText(),
                        elementAttribute(el, "text"),
                        elementAttribute(el, "content-desc")
                );
                if (text == null || !text.trim().equals(title)) {
                    continue;
                }
                int[] box = parseBounds(elementAttribute(el, "bounds"));
                int top = box == null ? Integer.MAX_VALUE / 2 : box[1];
                int width = box == null ? 0 : box[2] - box[0];
                int height = box == null ? 0 : box[3] - box[1];
                if (top < bestAnyY) {
                    bestAnyY = top;
                    any = el;
                }
                if (box != null && top <= maxHeaderY && width <= maxHeaderWidth && height <= 140) {
                    if (top < bestCompactY) {
                        bestCompactY = top;
                        compact = el;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return compact != null ? compact : any;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    public void tapButtonOnDialogue(String btnName) {
        if (driver instanceof AndroidDriver) {
            switch (btnName) {
                case "x" -> {
                    abs.waitUntilElementClickable(closeBtnAos);
                    closeBtnAos.click();
                }
            }
        }
    }

}
