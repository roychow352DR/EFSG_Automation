package utils.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

/**
 * MobileDriver class handles the initialization and configuration of mobile automation drivers.
 * It supports both Android and iOS platforms with configurable options and capabilities.
 */
public class MobileDriver {
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private static final String APPIUM_JS_PATH = "//usr//local//lib//node_modules//appium//build//lib//main.js";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private static final Duration WDA_LAUNCH_TIMEOUT = Duration.ofSeconds(20);

    public static AppiumDriver driver;
    public AppiumDriverLocalService service;
    public static UiAutomator2Options aosOptions;
    public static XCUITestOptions iosOptions;

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

        // Add additional capabilities if needed
//        options.setNoReset(true);
//        options.setNewCommandTimeout(Duration.ofSeconds(300));
    }

    /**
     * Configures iOS-specific options
     */
    private void configureIOSOptions() {
        // Set basic capabilities
        iosOptions.setDeviceName("iPhone 12 Pro");
        iosOptions.setPlatformName("16.4");
        iosOptions.setWdaLaunchTimeout(WDA_LAUNCH_TIMEOUT);

        // Set app path
        String appPath = System.getProperty("user.dir") + "/src/main/resources/.app";
        iosOptions.setApp(appPath);

        // Uncomment and configure these for real device testing
        /*
        options.setCapability("xcodeOrgId", "xxxxxxxx"); // team
        options.setCapability("xcodeSigningId", "iPhone Developer");
        options.setCapability("udid", "xxxxxxxx"); // udid is tied up with real device
        options.setCapability("updateWDABundleId", "xxxxxxx"); // team
        */
    }
}
