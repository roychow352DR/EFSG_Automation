package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AppLoginPage {
    private final AppiumDriver driver;
    public BiometricsPage biometricsPage;
    private final MobileAbstractComponents abs;

    private static final By SIGN_UP_LOGIN = By.xpath("//*[@text='Sign Up / Login']");
    private static final By HAVE_AN_ACCOUNT = By.xpath("//android.widget.TextView[contains(@text,'Have an account')]");
    private static final By LOGIN_TEXT = By.xpath("//*[@text='Login']");
    private static final By EDIT_TEXT = By.className("android.widget.EditText");
    private static final By ME_TAB = By.xpath("//android.widget.TextView[@text='Me']");

    public AppLoginPage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[5]")
    WebElement loginButtonAndroid;

    @FindBy(xpath = "(//XCUIElementTypeOther[@name=\"Login\"])[10]")
    WebElement loginButtonIos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.EditText")
    WebElement emailFieldAndroid;

    @FindBy(xpath = "//XCUIElementTypeTextField")
    WebElement emailFieldIos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText")
    WebElement passwordFieldAndroid;

    @FindBy(xpath = "//XCUIElementTypeSecureTextField")
    WebElement passwordFieldIos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Login\"])[2]")
    WebElement loginTitleAndroid;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='Login']")
    WebElement loginTitleIos;

    public void loginAs(String username, String password) throws InterruptedException {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        if (!openLoginPage()) {
            System.out.println("Already logged in; skipping login");
            return;
        }
        fillCredential(username, password);
        clickLogin();
    }

    private boolean openLoginPage() {
        if (isLoginFormVisible(2)) {
            return true;
        }
        if (tapIfPresent(SIGN_UP_LOGIN, 5)) {
            tapIfPresent(HAVE_AN_ACCOUNT, 10);
            return isLoginFormVisible(10);
        }
        if (tapIfPresent(HAVE_AN_ACCOUNT, 2)) {
            return isLoginFormVisible(10);
        }
        if (tapIfPresent(ME_TAB, 5) && tapIfPresent(SIGN_UP_LOGIN, 8)) {
            tapIfPresent(HAVE_AN_ACCOUNT, 10);
            return isLoginFormVisible(10);
        }
        return false;
    }

    private boolean isLoginFormVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d -> {
                List<WebElement> fields = d.findElements(EDIT_TEXT);
                return fields.size() >= 2 && !d.findElements(LOGIN_TEXT).isEmpty();
            });
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean tapIfPresent(By locator, int seconds) {
        try {
            abs.tapVisible(locator, seconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public BiometricsPage clickLogin() {
        if (driver instanceof AndroidDriver) {
            abs.tapBottomMost(LOGIN_TEXT, 15);
        } else {
            loginButtonIos.click();
        }
        biometricsPage = new BiometricsPage(driver);
        return biometricsPage;
    }

    public void fillCredential(String email, String password) throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            List<WebElement> fields = wait.until(d -> {
                List<WebElement> found = d.findElements(EDIT_TEXT);
                return found.size() >= 2 ? found : null;
            });
            fields.get(0).clear();
            fields.get(0).sendKeys(email);
            fields.get(1).clear();
            fields.get(1).sendKeys(password);
        } else {
            for (char c : email.toCharArray()) {
                emailFieldIos.sendKeys(String.valueOf(c));
            }
            passwordFieldIos.sendKeys(password);
            passwordFieldIos.sendKeys(Keys.RETURN);
        }
    }

    public boolean loginPageValidation() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementVisible(LOGIN_TEXT);
            return true;
        }
        abs.waitUntilElementFind(loginTitleIos);
        return loginTitleIos.isDisplayed();
    }
}
