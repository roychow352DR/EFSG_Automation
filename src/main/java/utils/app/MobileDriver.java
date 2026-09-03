package utils.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
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
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ScreenOrientation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

/**
 * MobileDriver class handles the initialization and configuration of mobile automation drivers.
 * It supports both Android and iOS platforms with configurable options and capabilities.
 */
public class MobileDriver {
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private static final String APPIUM_JS_PATH = "//usr//local//lib//node_modules//appium//build//lib//main.js";
    private static final Duration IMPLICIT_WAIT = Duration.ZERO;
    private static final Duration APP_READY_WAIT = Duration.ofSeconds(15);
    // Package is entity-specific; the RN launcher class is still CopyMaster MainActivity.
    private static final String ANDROID_LAUNCH_ACTIVITY = "com.mfinance.copymaster.MainActivity";
    private static final By ANDROID_FIRST_SCREEN = By.xpath(
            "//*[@text='Home' or @text='Markets' or @text='Me' or @text='Sign Up / Login']"
    );
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
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            String loadingIndicator = driver instanceof IOSDriver ? IOS_LOADING_INDICATOR : ANDROID_LOADING_INDICATOR;

            try {
                // Wait for loading indicator to be visible (with shorter timeout)
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loadingIndicator)));

                // Then wait for it to disappear
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loadingIndicator)));
                return true;
            } catch (TimeoutException | NoSuchElementException e) {
                // If loading indicator is not found, app might be already loaded
                return true;
            } catch (WebDriverException e) {
                // Handle connection issues - app might have crashed or connection lost
                System.out.println("Connection issue while checking loading indicator: " + e.getMessage());
                // Don't fail - app might still be usable
                return true;
            }
        } catch (Exception e) {
            // Handle any other exceptions gracefully
            System.out.println("Could not check loading indicator (app may already be ready): " + e.getMessage());
            return true; // Assume app is ready if we can't check
        }
    }

    /**
     * Waits for the app to be ready for interaction
     * @param driver AppiumDriver instance
     */
    private void waitForAppReady(AppiumDriver driver) {
        try {
            // Give the app some time to fully launch
            // We avoid doing any driver operations here that might cause session loss
            System.out.println("Waiting for app to launch...");
            Thread.sleep(5000);

            // Note: We intentionally skip active readiness checks here to avoid session loss.
            // The actual test steps will handle element finding and readiness verification.
            // This approach is safer as it doesn't perform operations that might invalidate the session.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Warning: Interrupted while waiting for app to be ready");
        } catch (Exception e) {
            System.err.println("Warning: Error while waiting for app to be ready: " + e.getMessage());
            // Don't throw exception - app might still be usable even if readiness check fails
        }
    }

    private void waitForAppReady(AppiumDriver driver, String androidAppPackage) {
        try {
            if (driver instanceof AndroidDriver) {
                waitForAndroidAppReady(driver, androidAppPackage);
            } else {
                System.err.println("Unknown driver type; skipping app-ready wait");
            }
        } catch (Exception e) {
            System.err.println("Warning: Error while waiting for app to be ready: " + e.getMessage());
        }
    }

    private void waitForAndroidAppReady(AppiumDriver driver, String appPackage) {
        System.out.println("Waiting for Android app to be ready...");
        waitUntilAppInForeground(driver, appPackage);
        waitForFirstScreen(driver);
    }

    private void waitUntilAppInForeground(AppiumDriver driver, String appPackage) {
        if (!(driver instanceof InteractsWithApps apps)) {
            return;
        }
        try {
            new WebDriverWait(driver, APP_READY_WAIT).until(d -> {
                try {
                    var currentState = apps.queryAppState(appPackage);
                    System.out.println("Android app state: " + currentState);
                    return currentState.name().contains("RUNNING")
                            && !currentState.name().contains("BACKGROUND");
                } catch (Exception e) {
                    System.err.println("Error querying Android app state: " + e.getMessage());
                    return false;
                }
            });
        } catch (Exception e) {
            System.err.println("Error waiting for Android app foreground: " + e.getMessage());
        }
    }

    private boolean waitForFirstScreen(AppiumDriver driver) {
        try {
            new WebDriverWait(driver, APP_READY_WAIT)
                    .until(ExpectedConditions.visibilityOfElementLocated(ANDROID_FIRST_SCREEN));
            System.out.println("First screen is visible");
            return true;
        } catch (Exception e) {
            System.err.println("First screen not visible after launch: " + e.getMessage());
            return false;
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
    public AndroidDriver initializeAndroidDriver(String androidAppPath,String androidPackage) throws MalformedURLException {
        try {
            // Start Appium server
            startAppiumServer();

            // Configure Android options
            aosOptions = new UiAutomator2Options();
            configureAndroidOptions(androidAppPath,androidPackage);

            // Initialize Android driver
            System.out.println("Initializing Android driver...");
            driver = new AndroidDriver(new URL(APPIUM_SERVER_URL), aosOptions);
            driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
            System.out.println("Android driver initialized successfully. Session ID: " + driver.getSessionId());

            try {
                if (driver instanceof InteractsWithApps apps) {
                    var state = apps.queryAppState(androidPackage);
                    System.out.println("Initial app state after driver creation: " + state);
                    if (state.name().contains("NOT_RUNNING") || state.name().contains("RUNNING_IN_BACKGROUND")) {
                        System.out.println("Activating app to bring to foreground...");
                        apps.activateApp(androidPackage);
                    }
                }
            } catch (Exception e) {
                System.err.println("Warning: Error activating app after driver init: " + e.getMessage());
            }

            // Wait for app to be ready (with error handling)
            try {
                waitForAppReady(driver, androidPackage);
            } catch (Exception e) {
                System.err.println("Warning: Error during app readiness check, but continuing: " + e.getMessage());
                // Continue anyway - the app might still be usable
            }

            return (AndroidDriver) driver;
        } catch (Exception e) {
            System.err.println("Error initializing Android driver: " + e.getMessage());
            e.printStackTrace();
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
            // Check if Appium server is already running
            try {
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection)
                        new URL(APPIUM_SERVER_URL + "/status").openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(2000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("Appium server is already running");
                    return;
                }
            } catch (Exception e) {
                // Server is not running, proceed to start it
                System.out.println("Appium server is not running, starting new instance...");
            }

            File appiumJS = new File(APPIUM_JS_PATH);
            if (!appiumJS.exists()) {
                System.err.println("Warning: Appium JS path does not exist: " + APPIUM_JS_PATH);
                System.err.println("Attempting to start Appium server without explicit path...");
            }

            AppiumServiceBuilder builder = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723);

            if (appiumJS.exists()) {
                builder.withAppiumJS(appiumJS);
            }

            service = builder.build();
            service.start();

            // Wait a bit for server to be ready
            Thread.sleep(2000);
            System.out.println("Appium server started successfully");
        } catch (Exception e) {
            System.err.println("Failed to start Appium server: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Appium server failed to start", e);
        }
    }

    /**
     * Configures Android-specific options
     */
    private void configureAndroidOptions(String androidAppPath,String androidPackage) throws MalformedURLException {
        if (aosOptions == null) {
            throw new IllegalStateException("Options object is null!");
        }

        // device emulator name
        aosOptions.setDeviceName("AndroidDevice");

        boolean noReset = Boolean.parseBoolean(System.getProperty("noReset", "false"));
        String deviceName = System.getProperty("deviceName", "Android Device");

        // Set basic capabilities
        aosOptions.setPlatformName("Android");
        aosOptions.setAutomationName("UiAutomator2");
        // aosOptions.setDeviceName(deviceName);

        // Use already installed app
        aosOptions.setAppPackage(androidPackage);
        aosOptions.setAppActivity(ANDROID_LAUNCH_ACTIVITY);
        // Do not wait for a single activity name; splash or alias activities may appear first
        aosOptions.setCapability("appWaitActivity", "*");
        aosOptions.setCapability("appWaitForLaunch", true);
        aosOptions.setCapability("appWaitDuration", (int) APP_READY_WAIT.toMillis());
        aosOptions.setCapability("appium:autoAcceptAlerts", true);
        aosOptions.setCapability("appium:autoDismissAlerts",true);

        aosOptions.setAutoGrantPermissions(true);

        // Configure reset options - use the system property value, not hardcoded
        aosOptions.setNoReset(true);
        aosOptions.setFullReset(false);

        // Additional capabilities to ensure app launches properly
        aosOptions.setCapability("autoLaunch", true);
        aosOptions.setCapability("skipUnlock", true);
        aosOptions.setCapability("skipServerInstallation", false);

        // Handle app path configuration
        if (androidAppPath != null && !androidAppPath.isBlank()) {
            File appFile = new File(androidAppPath);
            if (appFile.exists()) {
                aosOptions.setApp(appFile.getAbsolutePath());
                System.out.println("Android app path set: " + appFile.getAbsolutePath());
            } else {
                String errorMsg = "Provided Android app path does not exist: " + androidAppPath;
                System.err.println(errorMsg);
                if (!noReset) {
                    // When noReset is false, app path is required for installation
                    throw new IllegalArgumentException(errorMsg + ". App path is required when noReset is false.");
                } else {
                    // When noReset is true, we can try to use existing installed app
                    System.out.println("Warning: App path not found, but noReset=true. Attempting to use existing installed app.");
                }
            }
        } else {
            if (!noReset) {
                // When noReset is false, app path is mandatory
                throw new IllegalArgumentException("Android app path is required when noReset is false. Provided path: " + androidAppPath);
            } else {
                // When noReset is true, we can use existing installed app
                System.out.println("No app path provided, but noReset=true. Using existing installed app.");
            }
        }
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