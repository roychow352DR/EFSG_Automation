package utils;

import PageObject.AdminPortal.ApplicationListPage;
import PageObject.AdminPortal.AdminLoginPage;
import PageObject.MIOadmin.MIOLoginPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.*;

public class BaseTest {
    public static WebDriver driver;
    public AdminLoginPage login;
    public MIOLoginPage mioLogin;
    public WebElement ctaButton;
    public DesiredCapabilities caps;
    public Scenario scenario;
    private static ScreenRecorder screenRecorder;
    public static File newFile, oldFile;
    public static String browserType;


    public WebDriver initializeDriver() throws IOException, InterruptedException {
        String path = "//src//main//java//DataResources//GlobalData.properties";
        browserType = System.getProperty("browser") != null ? System.getProperty("browser") : getProperty(path, "browser");
        driver = setBrowserDriver(browserType);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get(setDomain(getProperty(path, "env"),getProperty(path,"product")));
        return driver;
    }


    public AdminLoginPage launchApplication() throws IOException, InterruptedException {
        WebDriver driver = initializeDriver();
        login = new AdminLoginPage(driver);
        return login;
    }

    public MIOLoginPage launchMIOApplication() throws IOException, InterruptedException {
        WebDriver driver = initializeDriver();
        mioLogin = new MIOLoginPage(driver);
        return mioLogin;
    }

    public List<HashMap<String, String>> getJsonDataToMap() throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(System.getProperty("user.dir") + "//src//test//java//Data//Crendential.json"), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
        });

        return data;

    }

    public ApplicationListPage applicationPage() {
        ApplicationListPage ap = new ApplicationListPage(driver);
        return ap;
    }

    public boolean unclickableCTA(WebElement cta) {
        ctaButton = cta;
        return ctaButton.isEnabled();
    }

    public String setDomain(String env,String product) {
        if (product.equalsIgnoreCase("adminPortal")) {
            return switch (env) {
                case "bausit" -> "https://d13ckj22o5rgah.cloudfront.net/login";
                case "bauuat" -> "https://uat-aocm-ap.empfs.net/login";
                default -> "";
            };
        }
        else if (product.equalsIgnoreCase("mio"))
        {
            return switch (env) {
                case "bausit" -> "https://d27ekljjcs6mcs.cloudfront.net/login";
                case "bauuat" -> "https://d27ekljjcs6mcs.cloudfront.net/login";
                default -> "";
            };
        }
        return env;
    }

    public static String getProperty(String path, String propertyItem) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + path);
        prop.load(fis);

        return (System.getProperty(propertyItem) != null ? System.getProperty(propertyItem) : prop.getProperty(propertyItem));
    }

    public static File actualVideoFileName(String scenarioName) throws IOException {
        String path = "//src//main//java//DataResources//FileDirectory.properties";
        String VIDEO_DIRECTORY = getProperty(path, "video_directory");
        String actualFileName = "";
        newFile = new File(VIDEO_DIRECTORY, scenarioName + ".mp4");
        oldFile = new File(VIDEO_DIRECTORY, "Test.mp4");
        if (oldFile.exists()) {
            Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return newFile;
    }

    public File videoFile() {
        return newFile;
    }

    public static String getTestPlanId(String testType, String path) throws IOException {
        return switch (testType) {
            case "Regression" -> getProperty(path, "qase.regression.testPlanId");
            case "Smoke" -> getProperty(path, "qase.smoke.testPlanId");
            default -> "";
        };
    }

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


    public WebDriver setBrowserDriver(String browserName) {
        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");

            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
            }
            DesiredCapabilities chromeCaps = setBrowserCap(browserName);
            chromeCaps.setCapability(ChromeOptions.CAPABILITY, options);
            try {
               driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), chromeCaps);
            } catch (Exception e) {
                driver = new ChromeDriver(options);
                System.out.printf(String.valueOf(e));
            }
        } else if (browserName.contains("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");


            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
            }
            DesiredCapabilities firefoxCaps = setBrowserCap(browserName);
            firefoxCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
            try {
                driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), firefoxCaps);
            } catch (Exception e) {
                driver = new FirefoxDriver(options);
                System.out.printf(String.valueOf(e));
            }
        } else if (browserName.contains("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");


            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
            }
            DesiredCapabilities edgeCaps = setBrowserCap(browserName);
            edgeCaps.setCapability(EdgeOptions.CAPABILITY, options);
            try {
                driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), edgeCaps);
            } catch (Exception e) {
                driver = new EdgeDriver(options);
                System.out.printf(String.valueOf(e));
            }
        }
        return driver;
    }

    public static void takeScreenshot(String screenShotName) {
        File screenshotFile = createFolder("screenshots");
        String screenshotPath = screenshotFile.getAbsolutePath();
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(screenshotPath + "/"+ screenShotName + ".png");
            Files.copy(srcFile.toPath(), destFile.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> stepsPayload(boolean isPassed, int position, String stepAction, String hash) {
        Map<String, Object> step = new HashMap<>();
        if (!isPassed) {
            step.put("status", "failed");
            step.put("position", position);
            step.put("action", stepAction);
            step.put("attachments", List.of(hash));
        } else {
            step.put("status", "passed");
            step.put("position", position);
            step.put("action", stepAction);
        }
        return step;
    }

    public static void emptyFolder(String folderName) {
        File screenshotsFolder = new File(folderName);
        // Check if the folder exists
        if (screenshotsFolder.exists() && screenshotsFolder.isDirectory()) {
            // Get all files in the folder
            File[] files = screenshotsFolder.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Delete each file
                    if (file.isFile() && file.delete()) {
                        System.out.println("Deleted file: " + file.getName());
                    } else {
                        System.out.println("Failed to delete file: " + file.getName());
                    }
                }
            } else {
                System.out.println("No files found in the screenshots folder.");
            }
        } else {
            System.out.println("Screenshots folder does not exist.");
        }
    }

    public static String getPropertyPath(String product)
    {
        return switch (product) {
            case "adminPortal" -> "//src//main//java//DataResources//qase-adminportal.properties";
            case "mio" -> "//src//main//java//DataResources//qase-mioAdminPortal.properties";
            default -> "";
        };

    }

    public static File createFolder(String folderName)
    {
        String screenshotDir = System.getProperty("user.dir") + "/" + folderName;
        File directory = new File(screenshotDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
            System.out.println("Screenshot folder created: " + screenshotDir);
        }

        return directory;
    }

}
