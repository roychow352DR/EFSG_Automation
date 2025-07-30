package AbstractComponent;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseTest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AbstractComponentsPW {
    Page page;

    public AbstractComponentsPW(Page page) {
        this.page = page;
    }


    public Map<String, String> userinfoList() throws IOException {
        LocalDate localDate = LocalDate.now();
        String path = "//src//main//java//DataResources//GlobalData.properties";
        int randomEmailSeed = (int) (Math.random() * 10001);
        int randomPhoneNo = (int) (Math.random() * 10000001);
        Map<String, String> info = new HashMap<String, String>();
        info.put("email", "qaauto" + randomEmailSeed + "@yopmail.com");
        info.put("existedEmail", "eieuatapproved@yopmail.com");
        info.put("phoneNumber", Integer.toString(randomPhoneNo));
        info.put("existedPhoneNumber", "96553209");
        info.put("entity", BaseTest.getProperty(path, "entity"));
        info.put("promoCode", "Test");
        info.put("referCode", "Test123");
        info.put("countryCode", "+852");
        info.put("lastName", "Peter");
        info.put("firstName", "Chu");
        info.put("country", "Hong Kong, China");
        info.put("nationality", "Hong Kong, China");
        info.put("gender", "Male");
        info.put("idType", "ID Card");
        info.put("dateOfBirthYear", "1990");
        info.put("dateOfBirthDay", "20");
        info.put("dateOfBirthYearBelow18", Integer.toString(localDate.getYear() - 15));
        info.put("id", randomString(6));
        info.put("passwordNo", randomString(6));
        info.put("addressLine1", "Mong Kok");
        info.put("city", "Kowloon");
        info.put("employStatus", "Employed ");
        info.put("industrial", "Education");
        info.put("annualIncome", "Less than $200,000 (Appx. USD 25,000)");
        info.put("netWorth", "Less than $40,000 (Appx. USD 5,000)");
        info.put("tradeFunds", "Employment");
        info.put("taxCountry", "Hong Kong, China");
        info.put("tradeEXP", "No");
        info.put("investEXP", "No");
        info.put("expiredYear", Integer.toString(localDate.getYear()));
        info.put("expiryDay", Integer.toString(localDate.get(ChronoField.DAY_OF_MONTH)));
        info.put("validExpiryYear", Integer.toString(localDate.getYear() + 2));
        return info;

    }


//    public void clearField(WebElement ele) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        ele.click();
//        if (System.getProperty("os.name").contains("Mac")) {
//            ele.sendKeys(Keys.COMMAND + "a");
//        } else if (System.getProperty("os.name").contains("Windows")) {
//            ele.sendKeys(Keys.CONTROL + "a");
//        }
//        ele.sendKeys(Keys.DELETE);
//        wait.until(driver -> Objects.requireNonNull(ele.getDomAttribute("value")).isEmpty());
//    }


    public String randomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public void waitForLocatorVisible(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }


}
