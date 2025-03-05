package utils;

import PageObject.ApplicationPage;
import PageObject.LoginPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
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

import static org.jsoup.nodes.Entities.EscapeMode.base;

public class BaseTest {
    public WebDriver driver;
    public LoginPage login;
    public WebElement ctaButton;
    public DesiredCapabilities caps;
    public Scenario scenario;
    public static String sessionId;
    private static ScreenRecorder screenRecorder;
    public static File newFile,oldFile;

    public WebDriver initializeDriver() throws IOException, URISyntaxException {
        String domain;

        Properties prop = getPropertiesFile("//src//main//java//DataResources//GlobalData.properties");

        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : prop.getProperty("browser");

        String env = System.getProperty("env") != null ? System.getProperty("env") : prop.getProperty("env");
        domain = setDomain(env);

        if (browserName.contains("chrome")) {
            Map<String, Object> selenoidOptions2 = new HashMap<>();
            selenoidOptions2.put("enableVNC", true);
            selenoidOptions2.put("enableVideo", true);
            caps = new DesiredCapabilities();
            caps.setCapability(CapabilityType.BROWSER_NAME, "chrome");
           // caps.setCapability(CapabilityType.PLATFORM_NAME, Platform.MAC);
            caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");
            caps.setCapability("selenoid:options", selenoidOptions2);


            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
            }
            caps.setCapability(ChromeOptions.CAPABILITY, options);
            // driver = new ChromeDriver(options);//global max timeout
            try {
                driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), caps);
            } catch (Exception e) {
                driver = new ChromeDriver(options);
                System.out.printf(String.valueOf(e));
            }
        } else if (browserName.equalsIgnoreCase("firefox")) {
            Map<String, Object> selenoidOptions = new HashMap<>();
            /*selenoidOptions.put("enableVNC", true);
            selenoidOptions.put("enableVideo", true);*/
            caps = new DesiredCapabilities();
            caps.setCapability(CapabilityType.BROWSER_NAME, "firefox");
            //caps.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
            caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            caps.setCapability("se:recordVideo", true);
            caps.setCapability("se:name", "Test");
           // caps.setCapability("selenoid:options", selenoidOptions);

            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--start-maximized");
            // System.setProperty("webdriver.gecko.driver","/Users/roychow/Downloads/geckodriver 2");
            caps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);

            try {
                driver = new RemoteWebDriver(new URI("http://localhost:4444/wd/hub").toURL(), caps);

            } catch (Exception e) {
              //  driver = new FirefoxDriver(options);
                System.out.printf(String.valueOf(e));
            }
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get(domain);
        sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
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

    public Properties getPropertiesFile(String path) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + path);
        prop.load(fis);

        return prop;
    }

    public static String getProperty(String path, String propertyItem) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + path);
        prop.load(fis);

        return prop.getProperty(propertyItem);
    }

    public String getSessionId() throws IOException, URISyntaxException {
        //return ((RemoteWebDriver) driver).getSessionId().toString();
        return sessionId;
    }

    public static String actualVideoFileName(String scenarioName) throws IOException {
        String path = "//src//main//java//DataResources//videoDirectory.properties";
        String VIDEO_DIRECTORY = getProperty(path,"video_directory");
        newFile = new File(VIDEO_DIRECTORY, scenarioName + ".mp4");
        oldFile = new File(VIDEO_DIRECTORY, "Test.mp4");
        Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return newFile.getName();
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

}
