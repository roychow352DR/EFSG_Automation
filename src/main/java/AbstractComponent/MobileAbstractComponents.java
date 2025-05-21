package AbstractComponent;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
public class MobileAbstractComponents {
    static AppiumDriver driver;

    public MobileAbstractComponents(AppiumDriver driver) {
        this.driver = driver;
    }

    public void swipeAction(WebElement ele,String direction,int swipeCount) throws InterruptedException {
        	//boolean canSwipeMore;
        int canSwipe = 0;

        	do {

                ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId",((RemoteWebElement)ele).getId(),
                "direction", direction,
                "percent", 0.75
        ));
                canSwipe++;
        } while(canSwipe < swipeCount);
    }

    public void waitUtilElementFind(WebElement ele) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
        w.until(ExpectedConditions.visibilityOf(ele));
    }

}
