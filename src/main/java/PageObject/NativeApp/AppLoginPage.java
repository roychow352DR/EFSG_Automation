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

    private static final By SIGN_UP_LOGIN = By.xpath(
            "//*[contains(@text,'Sign Up') and contains(@text,'Login') or contains(@text,'Sign up') and contains(@text,'Log')]"
    );
    private static final By HAVE_AN_ACCOUNT = By.xpath(
            "//*[contains(@text,'Have an account') or contains(@text,'Log In') and contains(@text,'account')]"
    );
    private static final By LOGIN_TEXT = By.xpath("//*[@text='Login' or @text='Log In' or @text='Log in']");
    private static final By SIGNUP_TITLE = By.xpath("//*[@text='Signup' or @text='Sign Up']");
    private static final By EDIT_TEXT = By.className("android.widget.EditText");
    private static final By ME_TAB = By.xpath("//*[@text='Me']");
    private static final By LOGOUT = By.xpath(
            "//*[@text='Logout' or @text='Log Out' or @text='Log out' or @text='Sign Out' or @text='Sign out']"
    );
    private static final By CONFIRM = By.xpath(
            "//*[@text='Confirm' or @text='OK' or @text='Yes' or @text='Logout' or @text='Log Out']"
    );

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
        if (!isGuestEntryVisible(3)) {
            logoutLeftoverSession();
        }
        if (isLoginFormVisible(5) || openLoginFromGuestHome() || openLoginFromMeTab()) {
            return;
        }
        throw new TimeoutException("Login page was not visible");
    }

    private boolean isGuestEntryVisible(int seconds) {
        return isDisplayed(SIGN_UP_LOGIN, seconds) || isDisplayed(HAVE_AN_ACCOUNT, 1);
    }

    private boolean openLoginFromGuestHome() {
        if (!tapIfPresent(SIGN_UP_LOGIN, 8)) {
            return tapHaveAnAccountAndWait();
        }
        if (isLoginFormVisible(5)) {
            return true;
        }
        return tapHaveAnAccountAndWait();
    }

    private boolean openLoginFromMeTab() {
        tapMeTab();
        if (!tapIfPresent(SIGN_UP_LOGIN, 8)) {
            return false;
        }
        if (isLoginFormVisible(5)) {
            return true;
        }
        return tapHaveAnAccountAndWait();
    }

    private boolean tapHaveAnAccountAndWait() {
        for (int swipe = 0; swipe < 3; swipe++) {
            if (tapIfPresent(HAVE_AN_ACCOUNT, 5)) {
                return isLoginFormVisible(10);
            }
            abs.swipeUp(driver);
        }
        return isLoginFormVisible(5);
    }

    private void logoutLeftoverSession() {
        tapMeTab();
        for (int swipe = 0; swipe < 4 && !tapIfPresent(LOGOUT, 2); swipe++) {
            abs.swipeUp(driver);
        }
        tapIfPresent(LOGOUT, 2);
        tapIfPresent(CONFIRM, 5);
        isGuestEntryVisible(8);
    }

    private void tapMeTab() {
        try {
            new AppFooter(driver).tapFooterButton("Me");
        } catch (TimeoutException e) {
            tapIfPresent(ME_TAB, 5);
        }
    }

    private boolean isLoginFormVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d -> {
                List<WebElement> fields = d.findElements(EDIT_TEXT);
                boolean hasLogin = !d.findElements(LOGIN_TEXT).isEmpty();
                boolean onSignup = !d.findElements(SIGNUP_TITLE).isEmpty();
                return fields.size() >= 2 && hasLogin && !onSignup;
            });
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isDisplayed(By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d ->
                    d.findElements(locator).stream().anyMatch(WebElement::isDisplayed));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean tapIfPresent(By locator, int seconds) {
        try {
            abs.tapBottomMost(locator, seconds);
            return true;
        } catch (TimeoutException e) {
            try {
                abs.tapVisible(locator, Math.min(seconds, 4));
                return true;
            } catch (TimeoutException ignored) {
                return false;
            }
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
