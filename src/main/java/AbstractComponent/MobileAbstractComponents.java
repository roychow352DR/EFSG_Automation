package AbstractComponent;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MobileAbstractComponents {
    static AppiumDriver driver;
    private static final Duration EXPLICIT_WAIT = Duration.ofSeconds(30);

    public MobileAbstractComponents(AppiumDriver driver) {
        this.driver = driver;
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
    }


