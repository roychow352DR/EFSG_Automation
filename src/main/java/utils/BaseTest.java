package utils;

import PageObject.AdminPortal.ApplicationListPage;
import PageObject.AdminPortal.AdminLoginPage;
import PageObject.MIOadmin.MIOLoginPage;
import PageObject.NativeApp.WelcomePage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.app.MobileDriver;
import utils.app.MobilePlatform;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.*;

/**
 * BaseTest class serves as the foundation for all test automation.
 * It provides core functionality for:
 * - Web and mobile driver initialization
 * - Browser configuration
 * - Screenshot and video recording
 * - File management
 * - Test data handling
 */
public class BaseTest {
    // WebDriver instance for browser automation
    public static WebDriver driver;
    
    // Page Object instances
    public AdminLoginPage login;
    public MIOLoginPage mioLogin;
    public WebElement ctaButton;
    
    // Configuration and capabilities
    public DesiredCapabilities caps;
    public Scenario scenario;
    private static ScreenRecorder screenRecorder;
    public static File newFile, oldFile;
    public static String browserType, productType;
    public AppiumDriverLocalService service;
    public static UiAutomator2Options options;
    public MobilePlatform mobilePlatform;
    public MobileDriver mobileDriver;

    /**
     * Initializes the appropriate driver based on the product type and platform
     * @return WebDriver instance
     */
    public WebDriver initializeDriver() throws IOException, InterruptedException {
        mobileDriver = new MobileDriver();
        mobilePlatform = new MobilePlatform();
        String path = "//src//main//java//DataResources//GlobalData.properties";
        productType = System.getProperty("product") != null ? 
            System.getProperty("product") : getProperty(path, "product");

        if (!productType.equalsIgnoreCase("app")) {
            // Initialize web browser driver
            browserType = System.getProperty("browser") != null ? 
                System.getProperty("browser") : getProperty(path, "browser");
            driver = setBrowserDriver(browserType);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            driver.get(setDomain(getProperty(path, "env"), getProperty(path, "product")));
        } else if (mobilePlatform.getPlatform().equalsIgnoreCase("ANDROID")) {
            // Initialize Android driver
            driver = mobileDriver.initializeAndroidDriver();
            ((CanRecordScreen) driver).startRecordingScreen();
        } else if (mobilePlatform.getPlatform().equalsIgnoreCase("IOS")) {
            // Initialize iOS driver
            driver = mobileDriver.initializeiOSDriver();
        } else {
            throw new RuntimeException("Invalid Platform");
        }
        return driver;
    }

    /**
     * Launches the admin portal application
     * @return AdminLoginPage instance
     */
    public AdminLoginPage launchApplication() throws IOException, InterruptedException {
        WebDriver driver = initializeDriver();
        login = new AdminLoginPage(driver);
        return login;
    }

    /**
     * Launches the MIO application
     * @return MIOLoginPage instance
     */
    public MIOLoginPage launchMIOApplication() throws IOException, InterruptedException {
        WebDriver driver = initializeDriver();
        mioLogin = new MIOLoginPage(driver);
        return mioLogin;
    }

    /**
     * Reads and parses JSON test data
     * @return List of HashMaps containing test data
     */
    public List<HashMap<String, String>> getJsonDataToMap() throws IOException {
        String jsonContent = FileUtils.readFileToString(
            new File(System.getProperty("user.dir") + "//src//test//java//Data//Crendential.json"), 
            StandardCharsets.UTF_8
        );
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }

    /**
     * Creates and returns an ApplicationListPage instance
     */
    public ApplicationListPage applicationPage() {
        return new ApplicationListPage(driver);
    }

    /**
     * Checks if a CTA button is clickable
     * @param cta WebElement to check
     * @return boolean indicating if the element is enabled
     */
    public boolean unclickableCTA(WebElement cta) {
        ctaButton = cta;
        return ctaButton.isEnabled();
    }

    /**
     * Sets the domain URL based on environment and product
     * @param env Environment name
     * @param product Product name
     * @return Domain URL
     */
    public String setDomain(String env, String product) {
        if (product.equalsIgnoreCase("adminPortal")) {
            return switch (env) {
                case "bausit" -> "https://d13ckj22o5rgah.cloudfront.net/login";
                case "bauuat" -> "https://uat-aocm-ap.empfs.net/login";
                default -> "";
            };
        } else if (product.equalsIgnoreCase("mio")) {
            return switch (env) {
                case "bausit" -> "https://d27ekljjcs6mcs.cloudfront.net/login";
                case "bauuat" -> "https://d27ekljjcs6mcs.cloudfront.net/login";
                default -> "";
            };
        }
        return env;
    }

    /**
     * Reads property value from configuration file
     * @param path Path to property file
     * @param propertyItem Property key
     * @return Property value
     */
    public static String getProperty(String path, String propertyItem) throws IOException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + path)) {
            prop.load(fis);
            return System.getProperty(propertyItem) != null ? 
                System.getProperty(propertyItem) : prop.getProperty(propertyItem);
        }
    }

    /**
     * Creates and manages video file for test recording
     * @param scenarioName Name of the test scenario
     * @return File object for the video
     */
    public static File actualVideoFileName(String scenarioName) throws IOException {
        String VIDEO_DIRECTORY = getProperty(getPropertyPath("filePropertyPath"), "video_directory");
        newFile = new File(VIDEO_DIRECTORY, scenarioName + ".mp4");
        oldFile = new File(VIDEO_DIRECTORY, "Test.mp4");
        if (oldFile.exists()) {
            Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return newFile;
    }

    /**
     * Sets browser capabilities based on browser type
     * @param browserName Name of the browser
     * @return DesiredCapabilities object
     */
    public DesiredCapabilities setBrowserCap(String browserName) {
        caps = new DesiredCapabilities();
        if (browserName.contains("headless")) {
            caps.setCapability("se:options", "--headless");
        }

        caps.setCapability(CapabilityType.BROWSER_NAME, browserName);
        //caps.setCapability(CapabilityType.PLATFORM_NAME, Platform.MAC);
        caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        caps.setCapability("se:recordVideo", true);
        caps.setCapability("se:name", "Test");

        return caps;
    }

    /**
     * Initializes and configures the appropriate browser driver
     * @param browserName Name of the browser
     * @return WebDriver instance
     */
    public WebDriver setBrowserDriver(String browserName) {
        try {
            if (browserName.contains("chrome")) {
                return initializeChromeDriver(browserName);
            } else if (browserName.contains("firefox")) {
                return initializeFirefoxDriver(browserName);
            } else if (browserName.contains("edge")) {
                return initializeEdgeDriver(browserName);
            }
        } catch (Exception e) {
            System.err.println("Error initializing browser driver: " + e.getMessage());
        }
        throw new RuntimeException("Unsupported browser type: " + browserName);
    }

    private WebDriver initializeChromeDriver(String browserName) throws Exception {
        ChromeOptions options = new ChromeOptions();
        configureChromeBrowserOptions(options, browserName);
        DesiredCapabilities chromeCaps = setBrowserCap(browserName);
        chromeCaps.setCapability(ChromeOptions.CAPABILITY, options);
        return createRemoteOrLocalChromeDriver(options, chromeCaps);
    }

    private WebDriver initializeFirefoxDriver(String browserName) throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        configureFirefoxBrowserOptions(options, browserName);
        DesiredCapabilities firefoxCaps = setBrowserCap(browserName);
        firefoxCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
        return createRemoteOrLocalFirefoxDriver(options, firefoxCaps);
    }

    private WebDriver initializeEdgeDriver(String browserName) throws Exception {
        EdgeOptions options = new EdgeOptions();
        configureEdgeBrowserOptions(options, browserName);
        DesiredCapabilities edgeCaps = setBrowserCap(browserName);
        edgeCaps.setCapability(EdgeOptions.CAPABILITY, options);
        return createRemoteOrLocalEdgeDriver(options, edgeCaps);
    }

    private void configureChromeBrowserOptions(ChromeOptions options, String browserName) {
        options.addArguments(
            "--disable-dev-shm-usage",
            "--no-sandbox",
            "--disable-gpu",
            "--start-maximized"
        );
        if (browserName.contains("headless")) {
            options.addArguments("--headless=new");
        }
    }

    private void configureFirefoxBrowserOptions(FirefoxOptions options, String browserName) {
        options.addArguments(
            "--disable-dev-shm-usage",
            "--no-sandbox",
            "--disable-gpu",
            "--start-maximized"
        );
        if (browserName.contains("headless")) {
            options.addArguments("--headless=new");
        }
    }

    private void configureEdgeBrowserOptions(EdgeOptions options, String browserName) {
        options.addArguments(
            "--disable-dev-shm-usage",
            "--no-sandbox",
            "--disable-gpu",
            "--start-maximized"
        );
        if (browserName.contains("headless")) {
            options.addArguments("--headless=new");
        }
    }

    private WebDriver createRemoteOrLocalChromeDriver(ChromeOptions options, DesiredCapabilities caps) throws Exception {
        try {
            return new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), caps);
        } catch (Exception e) {
            return new ChromeDriver(options);
        }
    }

    private WebDriver createRemoteOrLocalFirefoxDriver(FirefoxOptions options, DesiredCapabilities caps) throws Exception {
        try {
            return new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), caps);
        } catch (Exception e) {
            return new FirefoxDriver(options);
        }
    }

    private WebDriver createRemoteOrLocalEdgeDriver(EdgeOptions options, DesiredCapabilities caps) throws Exception {
        try {
            return new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), caps);
        } catch (Exception e) {
            return new EdgeDriver(options);
        }
    }

    /**
     * Takes a screenshot and saves it to the screenshots folder
     * @param screenShotName Name for the screenshot file
     */
    public static void takeScreenshot(String screenShotName) {
        File screenshotFile = createFolder("screenshots");
        String screenshotPath = screenshotFile.getAbsolutePath();
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(screenshotPath + "/" + screenShotName + ".png");
            Files.copy(srcFile.toPath(), destFile.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and saves a video recording of the test
     * @param appVideoName Name for the video file
     * @param driver WebDriver instance
     * @return File object for the video
     */
    public static File videoFileCreation(String appVideoName, WebDriver driver) throws IOException {
        File appVideoRecordingFileDir = createFolder("app_Video");
        File videoFile = new File(appVideoRecordingFileDir, appVideoName + ".mp4");

        String base64Video = ((CanRecordScreen)driver).stopRecordingScreen();
        byte[] data = Base64.getDecoder().decode(base64Video);
        try (FileOutputStream stream = new FileOutputStream(videoFile)) {
            stream.write(data);
        }
        System.out.println("Video saved: " + videoFile.getAbsolutePath());

        return videoFile;
    }

    /**
     * Creates a step payload for test reporting
     * @param isPassed Whether the step passed
     * @param position Step position
     * @param stepAction Step action description
     * @param hash Attachment hash
     * @return Map containing step information
     */
    public static Map<String, Object> stepsPayload(boolean isPassed, int position, String stepAction, String hash) {
        Map<String, Object> step = new HashMap<>();
        step.put("status", isPassed ? "passed" : "failed");
        step.put("position", position);
        step.put("action", stepAction);
        if (!isPassed) {
            step.put("attachments", List.of(hash));
        }
        return step;
    }

    /**
     * Clears all files from a specified folder
     * @param folderName Name of the folder to clear
     */
    public static void emptyFolder(String folderName) {
        File folder = new File(folderName);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.delete()) {
                        System.out.println("Deleted file: " + file.getName());
                    } else {
                        System.out.println("Failed to delete file: " + file.getName());
                    }
                }
            }
        }
    }

    /**
     * Gets the property file path based on product type
     * @param product Product name
     * @return Property file path
     */
    public static String getPropertyPath(String product) {
        return switch (product) {
            case "adminPortal" -> "//src//main//java//DataResources//qase-adminportal.properties";
            case "mio" -> "//src//main//java//DataResources//qase-mioAdminPortal.properties";
            case "app" -> "//src//main//java//DataResources//qase-nativeApp.properties";
            case "filePropertyPath" -> "//src//main//java//DataResources//FileDirectory.properties";
            case "globalPropertyPath" -> "//src//main//java//DataResources//GlobalData.properties";
            default -> "";
        };
    }

    /**
     * Creates a folder if it doesn't exist
     * @param folderName Name of the folder to create
     * @return File object for the created folder
     */
    public static File createFolder(String folderName) {
        String folderPath = System.getProperty("user.dir") + "/" + folderName;
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Folder created: " + folderPath);
        }
        return directory;
    }

    /**
     * Launches the mobile application
     * @return WelcomePage instance
     */
    public WelcomePage launchApp() throws IOException, InterruptedException {
        AppiumDriver driver = (AndroidDriver) initializeDriver();
        return new WelcomePage(driver);
    }
}
