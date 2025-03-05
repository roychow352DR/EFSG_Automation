package StepDefinitions;

import io.cucumber.java.*;
import org.monte.screenrecorder.ScreenRecorder;
import utils.BaseTest;
import utils.QaseApiClient;
import utils.VideoRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Hooks extends BaseTest {

    public static QaseApiClient qaseApiClient;
    public static int runId;
    public static String runTitle;
    public static String projectCode;
    public static String testPlanId;
    public static String testType;
    public static String apiToken;
    public static BaseTest base;
    public static ScreenRecorder screenRecorder;
    //public static  VideoRecorder videoRecorder;
    public static File videoFile;
    private static String VIDEO_DIRECTORY;
    public static String qasePropertyPath;
    public static String videoName;
    public static boolean removeVideoFlag = true;

    public Hooks() throws IOException, AWTException {

    }

    @BeforeAll
    public static void createQaseTestRun() throws IOException {

        try {
            qasePropertyPath = "//src//main//java//DataResources//qase-adminportal.properties";
            VIDEO_DIRECTORY = "/Users/roychow/Desktop/Docker_Selenium_Grid/Video";
          //  base = new BaseTest();
            apiToken = BaseTest.getProperty(qasePropertyPath,"qase.api.token");
            projectCode = BaseTest.getProperty(qasePropertyPath,"qase.project.code");;
            testType = System.getProperty("testtype") != null ? System.getProperty("testtype") : BaseTest.getProperty(qasePropertyPath,"testtype");;
            testPlanId = BaseTest.getTestPlanId(testType,qasePropertyPath);

            //Initialize Qase API client
            qaseApiClient = new QaseApiClient(apiToken, projectCode);

            runTitle = qaseApiClient.getTestPlanTitle(Integer.parseInt(testPlanId), projectCode);

            // Create a test run in Qase
            runId = qaseApiClient.createTestRunByTestPlan(Integer.parseInt(testPlanId), runTitle);
        }
        catch(IOException e) {
            e.getStackTrace();
            System.out.println("Failed to create test run");
        }

    }

    @After
    public void logQaseTestResult(Scenario scenario) throws Exception {
        //wait until video creation fully complete
        Thread.sleep(5000);
        String caseId = qaseApiClient.getCaseId(scenario,projectCode);
        boolean isPassed = !scenario.isFailed();
        try {
            String hash = qaseApiClient.uploadVideo(projectCode, BaseTest.actualVideoFileName(scenario.getName()));
            qaseApiClient.uploadVideoToTestCaseResult(runId,projectCode,hash,isPassed,caseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Before
    public void startVideoRecording(Scenario scenario) {
        try {
            // Start recording with the scenario name
            screenRecorder = VideoRecorder.startRecording(scenario.getName());
            System.out.println("Test case "+scenario.getName() + " is recording");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @AfterAll
    public static void removeVideoFile()
    {
        if (removeVideoFlag) {
            VideoRecorder.deleteRecords(VIDEO_DIRECTORY);
        }
    }


}
