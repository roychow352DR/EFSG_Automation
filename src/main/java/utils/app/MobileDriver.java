package utils.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class MobileDriver {
    public static AppiumDriver driver;
    public AppiumDriverLocalService service;
    private static Properties props = new Properties();
    public static UiAutomator2Options options;


    public AndroidDriver initializeAndroidDriver() throws MalformedURLException {

        try {
            service = new AppiumServiceBuilder().withAppiumJS(new File("//usr//local//lib//node_modules//appium//build//lib//main.js"))
                    .withIPAddress("127.0.0.1").usingPort(4723).build();
            service.start();

            options = new UiAutomator2Options();
            options.setDeviceName("Pixel 2 XL API 33"); //emulator
            //options.setDeviceName("Android Phone"); // real device
            // options.setChromedriverExecutable("//Users//roychow//Downloads//chromedriver");
            options.setApp(System.getProperty("user.dir") + "/src/main/resources/com.efsg.eiehktrading.android_uat-0.0.214-0805.apk");
        } catch (Exception e) {
            e.printStackTrace();
        }

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return (AndroidDriver) driver;

    }

    public IOSDriver initializeiOSDriver() throws MalformedURLException {

        try {
            service = new AppiumServiceBuilder().withAppiumJS(new File("//usr//local//lib//node_modules//appium//build//lib//main.js"))
                    .withIPAddress("127.0.0.1").usingPort(4723).build();
            service.start();

            XCUITestOptions options = new XCUITestOptions();
            options.setDeviceName("iPhone 12 Pro");
           // options.setApp("/Users/roychow/eclipse-workspace/Appium/src/test/java/resources/TestApp 3.app");
            options.setPlatformName("16.4");
            // Appium - Webdriver Agent -> iOS apps
            options.setWdaLaunchTimeout(Duration.ofSeconds(20));

		/*	d.setCapability("xcodeOrgId","xxxxxxxx"); //team
			d.setCapability("xcodeSigningId","iPhone Developer");
			d.setCapability("udid","xxxxxxxx"); //udid is tied up with real device
			d.setCapability("updateWDABundleId","xxxxxxx"); //team */

        } catch (Exception e) {
            e.printStackTrace();
        }

        driver = new IOSDriver(new URL("http://127.0.0.1:4723"),options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return (IOSDriver) driver;

    }

//    public String videoRecording()
//    {
//        CanRecordScreen recorder = (CanRecordScreen) driver;
//        recorder.startRecordingScreen();
//    }
}
