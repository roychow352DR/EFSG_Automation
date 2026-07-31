package AbstractComponent;

import PageObject.NativeApp.AppMarketsPage;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

public class MobileAbstractComponents {
    static AppiumDriver driver;
    private static final Duration EXPLICIT_WAIT = Duration.ofSeconds(30);

    public MobileAbstractComponents(AppiumDriver driver) {
        this.driver = driver;
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public void swipeAction(WebElement ele, String direction, int swipeCount) throws InterruptedException {
        int canSwipe = 1;

        do {
            if (driver instanceof IOSDriver) {
                // iOS swipe implementation
                Map<String, Object> params = new HashMap<>();
                params.put("direction", direction.toLowerCase());
                params.put("element", ((RemoteWebElement) ele).getId());

                // iOS specific parameters
                Map<String, Object> options = new HashMap<>();
                options.put("duration", 0.5); // Duration in seconds
                options.put("velocity", 1000); // Velocity in points per second
                params.put("options", options);

                ((JavascriptExecutor) driver).executeScript("mobile: swipe", params);
            } else {
                // Android swipe implementation
                ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                        "elementId", ((RemoteWebElement) ele).getId(),
                        "direction", direction,
                        "percent", 0.75
                ));
            }

            canSwipe++;
            Thread.sleep(500); // Small delay between swipes
        } while (canSwipe < swipeCount);
    }

    /**
     * Alternative swipe method using W3C actions for iOS
     */
    public void swipeActionIOS(WebElement ele, String direction, int swipeCount) throws InterruptedException {
        int canSwipe = 0;

        do {
            // Get element location and size
            Point location = ele.getLocation();
            Dimension size = ele.getSize();

            // Calculate start and end points based on direction
            int startX, startY, endX, endY;

            switch (direction.toLowerCase()) {
                case "up":
                    startX = location.getX() + size.getWidth() / 2;
                    startY = location.getY() + size.getHeight() * 3 / 4;
                    endX = startX;
                    endY = location.getY() + size.getHeight() / 4;
                    break;
                case "down":
                    startX = location.getX() + size.getWidth() / 2;
                    startY = location.getY() + size.getHeight() / 4;
                    endX = startX;
                    endY = location.getY() + size.getHeight() * 3 / 4;
                    break;
                case "left":
                    startX = location.getX() + size.getWidth() * 3 / 4;
                    startY = location.getY() + size.getHeight() / 2;
                    endX = location.getX() + size.getWidth() / 4;
                    endY = startY;
                    break;
                case "right":
                    startX = location.getX() + size.getWidth() / 4;
                    startY = location.getY() + size.getHeight() / 2;
                    endX = location.getX() + size.getWidth() * 3 / 4;
                    endY = startY;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction: " + direction);
            }

            // Create and perform the swipe action
            Actions actions = new Actions(driver);
            actions.moveToElement(ele, startX - location.getX(), startY - location.getY())
                    .clickAndHold()
                    .moveByOffset(endX - startX, endY - startY)
                    .release()
                    .perform();

            canSwipe++;
            Thread.sleep(500); // Small delay between swipes
        } while (canSwipe < swipeCount);
    }

    public void waitUtilElementFind(WebElement ele) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(60));
        w.until(ExpectedConditions.visibilityOf(ele));
    }

    public void waitUtilAllElementFind(List<WebElement> ele) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(60));

        for (int attempt = 1; attempt <= 5; attempt++) {
            try {
                w.until(ExpectedConditions.invisibilityOfAllElements(ele));
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("Attempt " + attempt + ": stale element, retrying...");
            }
        }

        throw new StaleElementReferenceException("Element remained stale after 3 attempts: " + ele);

    }

    public void waitUtilElementClickable(WebElement ele) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(ele));
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("Attempt " + attempt + ": stale element, retrying...");
            }
        }

        throw new StaleElementReferenceException("Element remained stale after 3 attempts: " + ele);
    }

    public void waitUtilElementVisible(WebElement ele) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        for (int attempt = 1; attempt <= 10; attempt++) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOf(ele));
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("Attempt " + attempt + ": stale element, retrying...");
            }
        }

        throw new StaleElementReferenceException("Element remained stale after 10 attempts: " + ele);
    }

    /**
     * Verifies if the deeplink navigation was successful
     *
     * @return
     */
    public boolean verifyDeeplinkNavigation(AppiumDriver driver, WebElement ele) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT);

            // Wait until the target element is visible
            boolean elementDisplayed = wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    try {
                        // Replace the XPath with your actual locator or use `ele.isDisplayed()` if passing the element is correct
                        return d.findElement(By.xpath("(//XCUIElementTypeOther[@name='Login'])[6]")).isDisplayed();
                    } catch (NoSuchElementException e) {
                        return false;
                    }
                }
            });

            if (elementDisplayed) {
                System.out.println("Successfully navigated to the deeplink page");
                return true;
            }
        } catch (TimeoutException e) {
            System.err.println("Warning: Could not verify deeplink navigation (timeout)");
        } catch (Exception e) {
            System.err.println("Warning: Could not verify deeplink navigation - " + e.getMessage());
        }
        return false;
    }

    public void swipeUp(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.80);
        int endY = (int) (size.getHeight() * 0.20);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(new Pause(finger, Duration.ofMillis(150)));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600),
                PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    public void swipeUpUntilEnd(AppiumDriver driver) {
        final int MAX_SWIPES = 2; // safety limit

        String previousPageSource = "";
        int swipes = 0;

        while (swipes < MAX_SWIPES) {
            String currentPageSource = driver.getPageSource();

            // If page source didn't change, we've probably reached the end
            if (currentPageSource.equals(previousPageSource)) {
                break;
            }

            previousPageSource = currentPageSource;
            swipeUp(driver);
            swipes++;
        }
    }

    public void swipeUntilElementVisible(AppiumDriver driver, WebElement element, int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            try {
                if (element.isDisplayed() && element.isEnabled()) {
                    return;
                }
            } catch (Exception e) {
                // element not yet present / stale etc.
            }
            swipeUp(driver);
        }
        throw new RuntimeException("Element not visible after " + maxSwipes + " swipes");
    }

    public HashMap<String, String> userInfo() {
        int randomPhoneNo = (int) (Math.random() * 10000001);
        int randomEmailSeed = (int) (Math.random() * 10001);
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("username", randomString(10));
        userInfo.put("password", "Test1234@");
        userInfo.put("phone", Integer.toString(randomPhoneNo));
        userInfo.put("email", "qaautoapp" + "_" + BaseTest.productEntity + "_" + randomEmailSeed + "@yopmail.com");
        return userInfo;
    }

    public String randomString(int length) {
        char letter = RandomStringUtils.randomAlphabetic(1).charAt(0);
        char digit = RandomStringUtils.randomNumeric(1).charAt(0);
        String combinedString = "" + letter + digit + RandomStringUtils.randomAlphanumeric(length - 2);

        List<Character> characters = new ArrayList<>();
        for (char c : combinedString.toCharArray()) {
            characters.add(c);
        }

        Collections.shuffle(characters);

        StringBuilder shuffledString = new StringBuilder();
        for (char c : characters) {
            shuffledString.append(c);
        }

        return shuffledString.toString();
    }

    private static AndroidKey mapCharToAndroidKey(char c) {
        return switch (c) {
            case 'a', 'A' -> AndroidKey.A;
            case 'u', 'U' -> AndroidKey.U;
            case 't', 'T' -> AndroidKey.T;
            case 'o', 'O' -> AndroidKey.O;
            case 'l', 'L' -> AndroidKey.L;
            case '0' -> AndroidKey.DIGIT_0;
            case '1' -> AndroidKey.DIGIT_1;
            case '2' -> AndroidKey.DIGIT_2;
            case '3' -> AndroidKey.DIGIT_3;
            case '4' -> AndroidKey.DIGIT_4;
            case '5' -> AndroidKey.DIGIT_5;
            case '6' -> AndroidKey.DIGIT_6;
            case '7' -> AndroidKey.DIGIT_7;
            case '8' -> AndroidKey.DIGIT_8;
            case '9' -> AndroidKey.DIGIT_9;
            case '.' -> AndroidKey.PERIOD;
            default -> throw new IllegalArgumentException("Unsupported character for typing: " + c);
        };
    }

    public void typeWithAndroidKeys(AndroidDriver driver, WebElement element, String text) {

        waitUtilElementClickable(element);
        // Focus the field first
        element.click();

        for (char c : text.toCharArray()) {
            AndroidKey key = mapCharToAndroidKey(c);
            driver.pressKey(new KeyEvent(key));
        }
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));
    }

    public void tapEmptySpace(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int x = size.getWidth() / 2;
        int y = (int) (size.getHeight() * 0.8);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                x,
                y
        ));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerMove(
                Duration.ofMillis(100),
                PointerInput.Origin.viewport(),
                x,
                y
        ));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    public void swipe(AppiumDriver driver, Direction direction, int durationMs) {
        Dimension size = driver.manage().window().getSize();

        int startX, startY, endX, endY;

        switch (direction) {
            case UP:
                // swipe from bottom to top
                startX = size.getWidth() / 2;
                startY = (int) (size.getHeight() * 0.8);
                endX = startX;
                endY = (int) (size.getHeight() * 0.2);
                break;

            case DOWN:
                // swipe from top to bottom
                startX = size.getWidth() / 2;
                startY = (int) (size.getHeight() * 0.2);
                endX = startX;
                endY = (int) (size.getHeight() * 0.8);
                break;

            case LEFT:
                // swipe from right to left
                startY = size.getHeight() / 2;
                startX = (int) (size.getWidth() * 0.8);
                endX = (int) (size.getWidth() * 0.2);
                endY = startY;
                break;

            case RIGHT:
                // swipe from left to right
                startY = size.getHeight() / 2;
                startX = (int) (size.getWidth() * 0.2);
                endX = (int) (size.getWidth() * 0.8);
                endY = startY;
                break;

            default:
                throw new IllegalArgumentException("Unsupported direction: " + direction);
        }

        performSwipe(driver, startX, startY, endX, endY, durationMs);
    }

    private static void performSwipe(AppiumDriver driver,
                                     int startX, int startY,
                                     int endX, int endY,
                                     int durationMs) {

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // Move finger to the start position
        swipe.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                startX,
                startY
        ));

        // Finger down
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Move to end position over the given duration
        swipe.addAction(finger.createPointerMove(
                Duration.ofMillis(durationMs),
                PointerInput.Origin.viewport(),
                endX,
                endY
        ));

        // Finger up
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    public String normalizePriceToDecimals(String priceStr, String symbolDecimal) {
        String pattern = "%." + symbolDecimal + "f";
        if (priceStr == null) {
            return null;
        }
        if (getDecimalPlaces(priceStr) != Integer.parseInt(symbolDecimal)) {
            double value = Double.parseDouble(priceStr);
            return String.format(pattern, value);
        }

        return priceStr;
    }

    public int getDecimalPlaces(String numberStr) {
        if (numberStr == null) {
            return 0;
        }

        numberStr = numberStr.trim();

        int dotIndex = numberStr.indexOf('.');
        if (dotIndex < 0) {
            return 0;
        }

        String fractionalPart = numberStr.substring(dotIndex + 1);

        return fractionalPart.length();
    }

    public String getLabelValue(String label) {
        List<WebElement> text = driver.findElements(By.className("android.widget.TextView"));
        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).getText().equalsIgnoreCase(label)) {
                return text.get(i + 1).getText().split(" ")[1].replace(",", "");
            }
        }
        return null;
    }

    public String getQuoteCurrency(String symbol) {
        if (symbol == null || symbol.length() < 3) {
            throw new IllegalArgumentException("Invalid symbol: " + symbol);
        }
        return symbol.substring(symbol.length() - 3);
    }

    public String getDialogueValue(String label) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);

        try {
            return wait.until(d -> {
                List<WebElement> elements = d.findElements(By.className("android.widget.TextView"));
                List<String> texts = new ArrayList<>();

                for (WebElement element : elements) {
                    texts.add(element.getText());
                }

                for (int i = 0; i < texts.size() - 1; i++) {
                    String currentLabel = texts.get(i);

                    if (currentLabel != null && currentLabel.equalsIgnoreCase("Status")) {
                        i+=1;
                    }

                    if (currentLabel != null && currentLabel.equalsIgnoreCase(label)) {
                        return normalizeDialogueValue(label, texts.get(i + 1));
                    }
                }

                return null;
            });
        } catch (TimeoutException e) {
            return null;
        }
    }

    public String normalizeDialogueValue(String label, String rawValue) {
        if (rawValue == null) {
            return null;
        }

        rawValue = rawValue.trim();

        if (label.equalsIgnoreCase("Volume")) {
            return rawValue.replace("Lots", "").trim();
        }

        if (label.equalsIgnoreCase("Initial Margin") || label.equalsIgnoreCase("Estimated Margin")) {
            String[] parts = rawValue.split("USD");
            return parts.length > 1 ? parts[1].trim().replace(",", "") : rawValue.replace(",", "");
        }

        if (label.equalsIgnoreCase("Contract Value")) {
            String currency = getQuoteCurrency(AppMarketsPage.tradeSymbol);
            String[] parts = rawValue.split(currency);
            return parts.length > 1 ? parts[1].trim().replace(",", "") : rawValue.replace(",", "");
        }

        if (label.equalsIgnoreCase("Floating P/L")) {
            return rawValue.split(" ")[0].trim();
        }

        return rawValue;
    }

    public boolean dateValidator(String input) {
        DateTimeFormatter FORMATTER =
                DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
                        .withResolverStyle(ResolverStyle.STRICT);
        if (input == null) return false;
        try {
            LocalDateTime.parse(input, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public String getProductName(String symbol){
        return switch (symbol) {
            case "XAUUSD" -> "Gold";
            case "XAGUSD" -> "Silver";
            case "HKGHKD" -> "Hong Kong Gold";
            case "RKGCNH" -> "RMB Kilobar Gold";
            default -> "symbol not found";
        };
    }

}


