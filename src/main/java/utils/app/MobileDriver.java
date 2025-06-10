package utils.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScreenOrientation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * MobileDriver class handles the initialization and configuration of mobile automation drivers.
 * It supports both Android and iOS platforms with configurable options and capabilities.
 */
public class MobileDriver {
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private static final String APPIUM_JS_PATH = "//usr//local//lib//node_modules//appium//build//lib//main.js";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private static final Duration EXPLICIT_WAIT = Duration.ofSeconds(30);
    private static final Duration WDA_LAUNCH_TIMEOUT = Duration.ofSeconds(20);
    private static final String IOS_BUNDLE_ID = "com.efsg.eiehktrading.ios.sit";
    private static final String IOS_DEEPLINK = "eunify.eiehk.uat://app/tabDirectory?screen=Tab_Me";

    // Loading indicator locators
    private static final String IOS_LOADING_INDICATOR = "//XCUIElementTypeActivityIndicator";
    private static final String ANDROID_LOADING_INDICATOR = "//android.widget.ProgressBar";

    public static AppiumDriver driver;
    public AppiumDriverLocalService service;
    public static UiAutomator2Options aosOptions;
    public static XCUITestOptions iosOptions;

    /**
     * Waits for the loading indicator to disappear
     * @param driver AppiumDriver instance
     * @param timeout Duration to wait
     * @return true if loading completed, false if timeout
     */
    private boolean waitForLoadingToComplete(AppiumDriver driver, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        String loadingIndicator = driver instanceof IOSDriver ? IOS_LOADING_INDICATOR : ANDROID_LOADING_INDICATOR;
        
        try {
            // Wait for loading indicator to be visible
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loadingIndicator)));
            
            // Then wait for it to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loadingIndicator)));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            // If loading indicator is not found, app might be already loaded
            return true;
        }
    }

    /**
     * Waits for the app to be ready for interaction
     * @param driver AppiumDriver instance
     */
    private void waitForAppReady(AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT);
        
        try {
            // Wait for any loading indicators to disappear
            waitForLoadingToComplete(driver, EXPLICIT_WAIT);
            
            // Additional wait for app to be interactive
            wait.until(d -> {
                try {
                    return d.findElement(By.xpath("//*[@clickable='true' or @enabled='true']")).isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            System.err.println("Warning: App did not become ready within timeout period");
        }
    }

    /**
     * Handles deeplink navigation for iOS
     */
    private void handleIOSDeeplink(IOSDriver driver) {
        try {
            // First ensure app is fully launched and ready
            waitForAppReady(driver);
            
            // Wait a bit to ensure app is fully initialized
            Thread.sleep(3000);

            // Using URL scheme
            try {
                // Activate the app
                driver.executeScript("mobile: activateApp", new HashMap<String, Object>() {{
                    put("bundleId", IOS_BUNDLE_ID);
                }});

                // Wait for app to be active
                Thread.sleep(2000);
                
                // Try to navigate using URL scheme
                driver.get(IOS_DEEPLINK);
                
            } catch (Exception e) {
                System.out.println("Deeplink redirection failed: " + e.getMessage());
            }
            
            // Verify if we're on the correct page
          //  verifyDeeplinkNavigation(driver);

        } catch (Exception e) {
            System.err.println("Failed to handle deeplink: " + e.getMessage());
        }
    }

    /**
     * Initializes and configures the Android driver with UiAutomator2
     */
    public AndroidDriver initializeAndroidDriver() throws MalformedURLException {
        try {
            // Start Appium server
            startAppiumServer();

            // Configure Android options
            aosOptions = new UiAutomator2Options();
            configureAndroidOptions();

            // Initialize Android driver
            driver = new AndroidDriver(new URL(APPIUM_SERVER_URL), aosOptions);
            driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);

            // Wait for app to be ready
            waitForAppReady(driver);
            
            return (AndroidDriver) driver;
        } catch (Exception e) {
            System.err.println("Error initializing Android driver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Android driver", e);
        }
    }

    /**
     * Initializes and configures the iOS driver with XCUITest
     */
    public IOSDriver initializeiOSDriver() throws MalformedURLException {
        try {
            // Start Appium server
            startAppiumServer();

            // Configure iOS options
            iosOptions = new XCUITestOptions();
            configureIOSOptions();

            // Initialize iOS driver
            driver = new IOSDriver(new URL(APPIUM_SERVER_URL), iosOptions);
            driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);

            // Wait for app to be ready
            waitForAppReady(driver);
            
            return (IOSDriver) driver;
        } catch (Exception e) {
            System.err.println("Error initializing iOS driver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize iOS driver", e);
        }
    }

    /**
     * Starts the local Appium server
     */
    private void startAppiumServer() {
        try {
            service = new AppiumServiceBuilder()
                    .withAppiumJS(new File(APPIUM_JS_PATH))
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .build();
            service.start();
        } catch (Exception e) {
            System.err.println("Failed to start Appium server: " + e.getMessage());
            throw new RuntimeException("Appium server failed to start", e);
        }
    }

    /**
     * Configures Android-specific options
     */
    private void configureAndroidOptions() {
        if (aosOptions == null) {
            throw new IllegalStateException("Options object is null!");
        }

        // Set basic capabilities
        aosOptions.setDeviceName("Pixel 2 XL API 33"); // emulator
        aosOptions.setPlatformName("Android");
        aosOptions.setAutomationName("UiAutomator2");
        
        // Set app path
        String appPath = System.getProperty("user.dir") + "/src/main/resources/com.efsg.eiehktrading.android_uat-0.0.214-0805.apk";
        aosOptions.setApp(appPath);

        aosOptions.setCapability("autoGrantPermissions", true);

        // Add additional capabilities if needed
//        options.setNoReset(true);
//        options.setNewCommandTimeout(Duration.ofSeconds(300));
    }

    /**
     * Configures iOS-specific options
     */
    private void configureIOSOptions() {
        if (iosOptions == null) {
            throw new IllegalStateException("Options object is null!");
        }

        // Set basic capabilities
        iosOptions.setDeviceName("iPhone 14 Pro");
        iosOptions.setPlatformName("iOS");
        iosOptions.setWdaLaunchTimeout(WDA_LAUNCH_TIMEOUT);
        iosOptions.setAutomationName("XCUITest");

        // Set bundle ID
      //  iosOptions.setBundleId(IOS_BUNDLE_ID);

        // Set app path
        String appPath = System.getProperty("user.dir") + "/src/main/resources/CopyMaster.app";
        iosOptions.setApp(appPath);
        
        // Add capabilities for better deeplink handling
        iosOptions.setCapability("autoAcceptAlerts", true);
        iosOptions.setCapability("autoDismissAlerts", true);
        iosOptions.setCapability("nativeWebTap", true);
        iosOptions.setCapability("iosSetValueByPaste", true);
        
        // Add URL scheme handling capability
        //iosOptions.setCapability("urlScheme", "eunify.eiehk.uat");
        
        // Add additional capabilities for better app handling
        iosOptions.setCapability("newCommandTimeout", 300);
        iosOptions.setCapability("wdaStartupRetries", 4);
        iosOptions.setCapability("wdaStartupRetryInterval", 20000);
        iosOptions.setCapability("useNewWDA", true);
        iosOptions.setCapability("showIOSLog", true);
    }
}
