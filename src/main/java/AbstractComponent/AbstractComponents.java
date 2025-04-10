package AbstractComponent;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AbstractComponents {
    static WebDriver driver;

    public AbstractComponents(WebDriver driver) {
        this.driver = driver;
    }


    public Map<String, String> userinfoList() {
        int randomEmailSeed = (int) (Math.random() * 10001);
        int randomPhoneNo = (int) (Math.random() * 10000001);
        Map<String, String> info = new HashMap<String, String>();
         info.put("email", "qaauto" + randomEmailSeed + "@yopmail.com");
        info.put("phoneNumber", Integer.toString(randomPhoneNo));
        info.put("entity", "EIEHK");
        info.put("promoCode", "Test");
        info.put("referCode", "Test123");
        info.put("countryCode", "+852");
        info.put("lastName", "Peter");
        info.put("firstName", "Chu");
        info.put("country", "HKG");
        info.put("nationality", "HKG");
        info.put("gender", "Male");
        info.put("idType", "ID_CARD");
        info.put("dateOfBirthYear", "1990");
        info.put("dateOfBirthDay", "20");
        info.put("id", randomString(6));
        info.put("passwordNo", randomString(6));
        info.put("addressLine1", "Mong Kok");
        info.put("city", "Kowloon");
        info.put("employStatus", "EMPLOYED");
        info.put("industrial", "EDUCATION");
        info.put("annualIncome", "BETWEEN_200K_AND_500K");
        info.put("netWorth", "LESS_THAN_40K");
        info.put("tradeFunds", "EMPLOYMENT");
        info.put("taxCountry", "HKG");
        info.put("tradeEXP", "false");
        info.put("investEXP", "false");
        return info;

    }


    public void scrolling(WebElement scrollEle) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", scrollEle);
    }

    public void waitUtilElementFind(WebElement ele) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
        w.until(ExpectedConditions.visibilityOf(ele));
    }


    public static void clearField(WebElement ele, String text) {
        ele.sendKeys(Keys.chord(Keys.COMMAND, "a", Keys.DELETE));
        ele.sendKeys(Keys.DELETE);
        ele.sendKeys(text);
    }

    public void waitUtilElementClickable(WebElement ele) {
        WebDriverWait w2 = new WebDriverWait(driver, Duration.ofSeconds(5));
        w2.until(ExpectedConditions.elementToBeClickable(ele));
    }

    public String randomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public void selectDropdownItemsByAttribute(List<WebElement> itemList, String attribute, String selectItem) {
        for (WebElement ele : itemList) {
            if (Objects.requireNonNull(ele.getDomAttribute(attribute)).equalsIgnoreCase(selectItem)) {
                scrolling(ele);
                ele.click();
            }
        }
    }

    public void selectDropdownItemsByText(List<WebElement> itemList, String itemText) {
        for (WebElement ele : itemList) {
            if (ele.getText().equalsIgnoreCase(itemText)) {
                scrolling(ele);
                ele.click();
            }
        }
    }

    public void staleElementRefExceptionHandle(List<WebElement> eleList,String attribute,String item){
        int attempts = 0;
        while (attempts < 3) {
            try {
                if (!attribute.isEmpty()) {
                    selectDropdownItemsByAttribute(eleList, attribute, item);
                }
                else{
                    selectDropdownItemsByText(eleList, item);
                }
                break; // Exit loop if successful
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
    }



}
