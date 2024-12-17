package AbstractComponent;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AbstractComponents {
    static WebDriver driver;
    public AbstractComponents(WebDriver driver)
    {
        this.driver = driver;
    }


    public Map<String, String> userinfoList()
    {
        int emailCount = 34;
        Map<String,String> info = new HashMap<String,String>();
        info.put("email","autoeblsit11@yopmail.com");
        info.put("phoneNumber","1234567890010");
        info.put("promoCode","Test");
        info.put("referCode","Test123");
        info.put("countryCode","+852");
        info.put("lastName","Chow");
        info.put("firstName","Roy");
        info.put("country","HKG");
        return info;

    }


    public void scrolling(WebElement scrollEle){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();",scrollEle);
    }

    public void waitUtilElementFind (WebElement ele)
    {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOf(ele));
    }


    public static void clearField(WebElement ele, String text){
        ele.sendKeys(Keys.chord(Keys.COMMAND, "a", Keys.DELETE));
        ele.sendKeys(Keys.DELETE);
        ele.sendKeys(text);
    }



}
