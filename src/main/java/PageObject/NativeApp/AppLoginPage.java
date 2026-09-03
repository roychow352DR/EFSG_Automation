package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
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

    private static final By SIGNUP_LOGIN_LINK = By.xpath(
            "//android.widget.TextView[@text='Have an account? Log In']"
    );
    private static final By LOGIN_TEXT = By.xpath("//*[@text='Login' or @text='Log In' or @text='Log in']");
    private static final By SIGNUP_TITLE = By.xpath("//*[@text='Signup']");
    private static final By EDIT_TEXT = By.className("android.widget.EditText");

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
        openLoginPage();
        fillCredential(username, password);
        clickLogin();
    }

    private void openLoginPage() {
        if (isLoginFormVisible(2)) {
            return;
        }
        try {
            new AppHomePage(driver).navigateToSignupPage();
        } catch (TimeoutException e) {
            throw new TimeoutException("Login page was not visible", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TimeoutException("Login page was not visible", e);
        }
        if (isLoginFormVisible(2)) {
            return;
        }
        new AppSignupPage(driver).navigateToLoginPage();
        if (!isLoginFormVisible(15)) {
            System.out.println("Login form not visible after Signup -> Log In. onScreenEditTexts="
                    + onScreenCount(EDIT_TEXT)
                    + " loginTitle=" + isOnScreen(LOGIN_TEXT)
                    + " signupTitle=" + isOnScreen(SIGNUP_TITLE)
                    + " haveAccountLink=" + isOnScreen(SIGNUP_LOGIN_LINK));
            throw new TimeoutException("Login page was not visible");
        }
    }

    private boolean isLoginFormVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d -> {
                int fields = onScreenCount(EDIT_TEXT);
                boolean loginTitle = isOnScreen(LOGIN_TEXT);
                boolean signupLink = isOnScreen(SIGNUP_LOGIN_LINK);
                // Signup stays in the RN tree after Log In; only reject it when the link is still on screen.
                return fields >= 2 && loginTitle && !signupLink;
            });
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private int onScreenCount(By locator) {
        int count = 0;
        for (WebElement element : driver.findElements(locator)) {
            if (isOnScreen(element)) {
                count++;
            }
        }
        return count;
    }

    private boolean isOnScreen(By locator) {
        for (WebElement element : driver.findElements(locator)) {
            if (isOnScreen(element)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnScreen(WebElement element) {
        try {
            if (!element.isDisplayed()) {
                return false;
            }
            Point location = element.getLocation();
            Dimension size = element.getSize();
            Dimension window = driver.manage().window().getSize();
            if (size.getWidth() <= 0 || size.getHeight() <= 0) {
                return false;
            }
            int right = location.getX() + size.getWidth();
            int bottom = location.getY() + size.getHeight();
            return right > 0 && location.getX() < window.getWidth()
                    && bottom > 0 && location.getY() < window.getHeight();
        } catch (StaleElementReferenceException e) {
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
                List<WebElement> found = d.findElements(EDIT_TEXT).stream()
                        .filter(this::isOnScreen)
                        .toList();
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
