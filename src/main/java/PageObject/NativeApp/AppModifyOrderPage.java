package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    @FindBy(className = "android.widget.TextView")
    List<WebElement> textMessagesAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout" +
            "/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup" +
            "/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement backBtnAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[15]")
    WebElement closeBtnAos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Modify Order\"])[1]")
    WebElement headerAos;


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
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
        }
    }

    public void scrollDown() {
        SCROLLED = true;
        abs.swipeUpUntilEnd(driver);
    }

    public boolean getTextMessage(String messageContent) {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(textMessagesAos.getFirst());
            for (WebElement ele : textMessagesAos) {
                if (ele.getText().equalsIgnoreCase(messageContent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void tapBack() {
        if (driver instanceof AndroidDriver) {
            backBtnAos.click();
        }
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
    public boolean getHeader(){
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(headerAos);
            return headerAos.isDisplayed();
        }
        return false;
    }

}
