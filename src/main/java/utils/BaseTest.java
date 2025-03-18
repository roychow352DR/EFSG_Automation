package utils;

import PageObject.ApplicationPage;
import PageObject.LoginPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.*;

public class BaseTest {
    public static WebDriver driver;
    public LoginPage login;
    public WebElement ctaButton;
    public DesiredCapabilities caps;
    public Scenario scenario;
    private static ScreenRecorder screenRecorder;
    public static File newFile,oldFile;
    public static String browserType;


    public WebDriver initializeDriver() throws IOException, URISyntaxException {
        String path = "//src//main//java//DataResources//GlobalData.properties";
        browserType = System.getProperty("browser")!=null?System.getProperty("browser"):getProperty(path,"browser");
        driver = setBrowserDriver(browserType);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get(setDomain(getProperty(path,"env")));
        return driver;
    }


    public LoginPage launchApplication() throws IOException, URISyntaxException {
        WebDriver driver = initializeDriver();
        login = new LoginPage(driver);
        //   login.landOnLoginPage();
        return login;
    }

    public List<HashMap<String, String>> getJsonDataToMap() throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(System.getProperty("user.dir") + "//src//test//java//Data//Crendential.json"), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
        });

        return data;

    }

    public ApplicationPage applicationPage() {
        ApplicationPage ap = new ApplicationPage(driver);
        return ap;
    }

    public boolean unclickableCTA(WebElement cta) {
        ctaButton = cta;
        return ctaButton.isEnabled();
    }

    public String setDomain(String env)
    {
        return switch (env) {
            case "mf4sit" -> "https://d3lyp6p86bdjbb.cloudfront.net/login";
            case "mf4uat" -> "https://mf4-uat-aocm-ap.empfs.net/login";
            case "bausit" -> "https://mf4-uat-aocm-ap.empfs.net/login";
            case "bauuat" -> "https://mf4-uat-aocm-ap.empfs.net/login";
            default -> "";
        };
    }

/*    public Properties getPropertiesFile(String path) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + path);
        prop.load(fis);

        return prop;
    }*/

    public static String getProperty(String path, String propertyItem) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + path);
        prop.load(fis);

        return prop.getProperty(propertyItem);
    }

    public static File actualVideoFileName(String scenarioName) throws IOException {
        String path = "//src//main//java//DataResources//FileDirectory.properties";
        String VIDEO_DIRECTORY = getProperty(path,"video_directory");
        String actualFileName = "";
        newFile = new File(VIDEO_DIRECTORY, scenarioName + ".mp4");
        oldFile = new File(VIDEO_DIRECTORY, "Test.mp4");
        if (oldFile.exists()) {
            Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return newFile;
    }

    public File videoFile()
    {
        return newFile;
    }

    public static String getTestPlanId(String testType, String path) throws IOException {
        return switch (testType) {
            case "Regression" -> getProperty(path,"qase.regression.testPlanId");
            case "Smoke" -> getProperty(path,"qase.regression.testPlanId");
            default -> "";
        };
    }

    public DesiredCapabilities setBrowserCap(String browserName)
    {
            caps = new DesiredCapabilities();
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
            setBrowserCap(browserName).setCapability(ChromeOptions.CAPABILITY, options);
            // driver = new ChromeDriver(options);//global max timeout
            try {
                driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), setBrowserCap(browserName));
            } catch (Exception e) {
                driver = new ChromeDriver(options);
                System.out.printf(String.valueOf(e));
            }
        }
        else if (browserName.contains("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");


            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
            }
            setBrowserCap(browserName).setCapability(ChromeOptions.CAPABILITY, options);
            // driver = new ChromeDriver(options);//global max timeout
            try {
                driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), setBrowserCap(browserName));
            } catch (Exception e) {
                driver = new FirefoxDriver(options);
                System.out.printf(String.valueOf(e));
            }
        }
        else if (browserName.contains("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");


            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
            }
            setBrowserCap(browserName).setCapability(ChromeOptions.CAPABILITY, options);
            // driver = new ChromeDriver(options);//global max timeout
            try {
                driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), setBrowserCap(browserName));
            } catch (Exception e) {
                driver = new EdgeDriver(options);
                System.out.printf(String.valueOf(e));
            }
        }
        return driver;
    }

    public static void takeScreenshot(String screenShotName)
    {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        //Copy the file to a location and use try catch block to handle exception
        try {
            FileUtils.copyFile(screenshot, new File("./screenshots/" + screenShotName + ".png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Map<String, Object> stepsPayload(boolean isPassed, int position, String stepAction, String hash )
    {
        Map<String, Object> step = new HashMap<>();
        step.put("status", isPassed ? "passed" : "failed");
        step.put("position", position);
        step.put("action", stepAction);
        step.put("attachments",List.of(hash));
        return step;
    }

    public static void emptyFolder(String folderName)
    {
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

}
