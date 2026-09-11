package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
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

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppModifyOrderPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static String editPrice;
    public static boolean SCROLLED = false;

    public static void resetCapturedOrderValues() {
        editPrice = null;
        SCROLLED = false;
    }

    public AppModifyOrderPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[15]")
    WebElement closeBtnAos;


    public String getEditPrice(String direction, String decimal, String priceType, int value) {
        String price = "";
        if (driver instanceof AndroidDriver) {
            float current = Float.parseFloat(priceField(priceType).getText());
            if (priceType.equalsIgnoreCase("Stop Limit")) {
                price = Float.toString(direction.equalsIgnoreCase("BUY") ? current + value : current - value);
            } else if (priceType.equalsIgnoreCase("Stop Loss")) {
                price = Float.toString(direction.equalsIgnoreCase("BUY") ? current - value : current + value);
            } else {
                price = Float.toString(direction.equalsIgnoreCase("BUY") ? current + value : current - value);
            }
        }
        editPrice = abs.normalizePriceToDecimals(price, decimal);
        return price;
    }

    public void editTextField(String priceType, String direction, String decimal, int value) {
        if (driver instanceof AndroidDriver) {
            String editedPrice = getEditPrice(direction, decimal, priceType, value);
            WebElement field = priceField(priceType);
            field.clear();
            abs.typeWithAndroidKeys((AndroidDriver) driver, field, editedPrice);
        }
    }

    private WebElement priceField(String priceType) {
        WebElement label = priceLabel(priceType);
        int[] labelBounds = parseBounds(label.getAttribute("bounds"));

        WebElement closest = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement field : driver.findElements(By.className("android.widget.EditText"))) {
            int[] fieldBounds = parseBounds(field.getAttribute("bounds"));
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
            throw new NoSuchElementException(
                    "Could not find EditText for price type: " + priceType
            );
        }
        return closest;
    }

    private WebElement priceLabel(String priceType) {
        String labelToken = switch (priceType) {
            case "Stop Loss" -> "Stop Loss";
            case "Take Profit" -> "Take Profit";
            default -> "Price (";
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

    private int[] parseBounds(String bounds) {
        if (bounds == null || bounds.isBlank()) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]").matcher(bounds);
        if (!matcher.matches()) {
            return null;
        }
        return new int[]{
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
        };
    }

    public void tapsButton(String buttonName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        abs.tapBottomMost(By.xpath("//*[@text='" + buttonName + "']"), 10);
    }

    public void scrollDown() {
        SCROLLED = true;
        abs.swipeUpUntilEnd(driver);
    }

    public boolean getTextMessage(String messageContent) {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        By error = By.xpath(
                "//*[@text='" + messageContent + "' or @content-desc='" + messageContent + "'"
                        + " or contains(@text,'" + messageContent + "')"
                        + " or contains(@content-desc,'" + messageContent + "')]");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> visibleMessage(d.findElements(error), messageContent));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean visibleMessage(List<WebElement> elements, String messageContent) {
        for (WebElement ele : elements) {
            try {
                if (!ele.isDisplayed()) {
                    continue;
                }
                String text = ele.getText();
                if (text != null && text.toLowerCase().contains(messageContent.toLowerCase())) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    public void tapBack() {
        new AppTradeView(driver).tapBack();
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
    public boolean getHeader() {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> compactModifyOrderHeader() != null);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebElement compactModifyOrderHeader() {
        try {
            Dimension window = driver.manage().window().getSize();
            int maxHeaderY = (int) (window.getHeight() * 0.40);
            WebElement best = null;
            int bestY = Integer.MAX_VALUE;
            for (WebElement el : driver.findElements(By.xpath(
                    "//*[@text='Modify Order' or @content-desc='Modify Order']"))) {
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (location.getY() > maxHeaderY || size.getHeight() > 160) {
                    continue;
                }
                if (location.getY() < bestY) {
                    bestY = location.getY();
                    best = el;
                }
            }
            return best;
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }

}
