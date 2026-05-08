package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public class AppPortfolioPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppPortfolioPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        //    PageFactory.initElements(driver, this);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout//android.view.ViewGroup[3]//android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]")
    WebElement applicationButtonAos;

    @FindBy(className = "android.view.ViewGroup")
    List<WebElement> buttons;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]" +
            "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    WebElement titleAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup")
    List<WebElement> products;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    WebElement checkedIconAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup" +
            "/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]" +
            "/android.view.ViewGroup/android.view.ViewGroup")
    WebElement backButtonAos;

    @AndroidFindBy(accessibility = "Open\n" +
            "Positions")
    WebElement positionTabAos;

    @AndroidFindBy(accessibility = "Pending\n" +
            "Orders")
    WebElement pendingOrderTabAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement arrowBtnAos;


    @AndroidFindBy(accessibility = "History")
    WebElement historyTabAos;

    public void tapButtonOnPortfolioPage(String buttonName) {
        if (buttonName.equals("Open a Live Trading Accounts")) {
            abs.waitUtilElementFind(applicationButtonAos);
            applicationButtonAos.click();
        }
    }

    public void clickButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup")).click();
        }
    }

    public String getTitleAos() {
        abs.waitUtilElementFind(titleAos);
        return titleAos.getText();
    }

    public String getCheckedProduct() {
        if (driver instanceof AndroidDriver) {
            return driver.findElement(By.xpath("//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup" +
                    "/parent::android.view.ViewGroup/android.widget.TextView")).getText();
        }
        return "No checked product found";
    }

    public void tapBack() {
        if (driver instanceof AndroidDriver) {
            backButtonAos.click();
        }
    }

    public boolean tabIsSelected(String tabName) {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement tab = switch (tabName) {
                    case "Open Positions" -> positionTabAos;
                    case "Pending Orders" -> pendingOrderTabAos;
                    case "History" -> historyTabAos;
                    default -> null;
                };

                if (tab == null) {
                    return false;
                }

                String selected = tab.getDomAttribute("selected");
                if (selected == null) {
                    selected = tab.getDomProperty("selected");
                }

                return "true".equalsIgnoreCase(selected);
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element when checking tab selection for: " + tabName);
            }
        }

        return false;
    }

    public void tapTab(String tabName) {
        if (driver instanceof AndroidDriver) {
            WebElement tab = switch (tabName) {
                case "Open Positions" -> positionTabAos;
                case "Pending Orders" -> pendingOrderTabAos;
                case "History" -> historyTabAos;
                default -> null;
            };

            if (tab != null) {
                abs.waitUtilElementFind(tab);
                tab.click();
            }
        }
    }

    public void tapButtonOnRow(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "arrow" -> {
                    abs.waitUtilElementFind(arrowBtnAos);
                    arrowBtnAos.click();
                }
            }
        }
    }
}
