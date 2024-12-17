package utils;

import PageObject.ApplicationPage;
import PageObject.LoginPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class BaseTest {
    public WebDriver driver;
    public LoginPage login;
    public WebElement ctaButton;

    public WebDriver initializeDriver() throws IOException {

       Properties prop = new Properties();
       FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//DataResources//GlobalData.properties");
       prop.load(fis);


       String browserName = System.getProperty("browser")!=null? System.getProperty("browser") : prop.getProperty("browser");
       if (browserName.contains("chrome")) {
           ChromeOptions options = new ChromeOptions();

           if (browserName.contains("headless")) {
               options.addArguments("headless");
           }
           driver = new ChromeDriver(options);//global max timeout
       }
       else if(browserName.equalsIgnoreCase("firefox"))
       {
        //  System.setProperty("webdriver.gecko.driver","/Users/roychow/Downloads/geckodriver 2");
           driver = new FirefoxDriver();
       }
       driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
       driver.get("https://d3lyp6p86bdjbb.cloudfront.net/login");
       return driver;
    }

    @AfterTest
    public void afterTest()
    {
        driver.quit();
    }

    @BeforeTest
    public LoginPage launchApplication() throws IOException {
        WebDriver driver = initializeDriver();
        login = new LoginPage(driver);
        login.landOnLoginPage();
        return login;
    }

    public List<HashMap<String, String>> getJsonDataToMap() throws IOException {
       String jsonContent = FileUtils.readFileToString(new File(System.getProperty("user.dir") + "//src//test//java//Data//Crendential.json"), StandardCharsets.UTF_8);
       ObjectMapper mapper = new ObjectMapper();
       List<HashMap<String,String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
       });

       return data;

    }

    public ApplicationPage applicationPage(){
        ApplicationPage ap = new ApplicationPage(driver);
        return ap;
    }

    public boolean unclickableCTA(WebElement cta)
    {
        ctaButton = cta;
        return ctaButton.isEnabled();
    }

}
