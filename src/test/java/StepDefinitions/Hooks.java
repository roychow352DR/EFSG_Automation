package StepDefinitions;

import io.cucumber.java.*;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.WebDriver;
import utils.BaseTest;
import utils.QaseApiClient;
import utils.VideoRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Hooks extends BaseTest {

    public static QaseApiClient qaseApiClient;
    public static int runId;
    public static String runTitle;
    public static String projectCode;
    public static String testPlanId;
    public static String testType;
    public static String apiToken;
    public static String hash = "";
    public static String caseId;
   // public static BaseTest base;
    public static WebDriver driver;
    public static ScreenRecorder screenRecorder;
    //public static  VideoRecorder videoRecorder;
    public static File videoFile;
    private static String VIDEO_DIRECTORY;
    private static String SCREENSHOT_DIRECTORY;
    public static String qasePropertyPath = "//src//main//java//DataResources//qase-adminportal.properties";
    public static String filePropertyPath = "//src//main//java//DataResources//FileDirectory.properties";
    public static String globalPropertyPath = "//src//main//java//DataResources//GlobalData.properties";
    public static String videoName;
    public static boolean removeVideoFlag = true;
    public static boolean removeScreenShotFlag = true;
    public static int position;
    public static List<Map<String, Object>> steps = new ArrayList<>();

    public Hooks() throws IOException, AWTException, URISyntaxException {

        position = 0;
        VIDEO_DIRECTORY = getProperty(filePropertyPath, "video_directory");
        SCREENSHOT_DIRECTORY = getProperty(filePropertyPath, "screenshot_directory");


    }

    @BeforeAll
    public static void createQaseTestRun() throws IOException {
        browserType = System.getProperty("browser")!=null?System.getProperty("browser"):getProperty(globalPropertyPath,"browser");
        apiToken = getProperty(qasePropertyPath, "qase.api.token");
        projectCode = getProperty(qasePropertyPath, "qase.project.code");
        testType = System.getProperty("testtype") != null ? System.getProperty("testtype") : getProperty(qasePropertyPath, "testtype");
        try {
            testPlanId = BaseTest.getTestPlanId(testType, qasePropertyPath);

            //Initialize Qase API client
            qaseApiClient = new QaseApiClient(apiToken, projectCode);

            runTitle = qaseApiClient.getTestPlanTitle(Integer.parseInt(testPlanId), projectCode);

            // Create a test run in Qase
            runId = qaseApiClient.createTestRunByTestPlan(Integer.parseInt(testPlanId), runTitle,browserType);
        } catch (IOException e) {
            e.getStackTrace();
            System.out.println("Failed to create test run");
        }

    }

    @Before
    public void getCaseId(Scenario scenario) {
        caseId = qaseApiClient.getCaseId(scenario, projectCode);
    }


    @After
    public void logQaseTestResult(Scenario scenario) throws Exception {
        //wait until video creation fully complete
        Thread.sleep(5000);

        //   String path = "/Users/roychow/Desktop/Docker_Selenium_Grid/Video/";
        System.out.println(steps);
        boolean isPassed = !scenario.isFailed();
        if (BaseTest.actualVideoFileName(scenario.getName()).exists()) {
            hash = qaseApiClient.uploadAttachment(projectCode, BaseTest.actualVideoFileName(scenario.getName()).getName(), VIDEO_DIRECTORY);
        }

        try {
            qaseApiClient.uploadVideoToTestCaseResult(runId, projectCode, hash, isPassed, caseId, steps);
            //    qaseApiClient.updateStepsResult(projectCode,hash,Integer.parseInt(caseId),runId,isPassed,steps);
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
    public static void removeVideoFile() {
        if (removeVideoFlag) {
            VideoRecorder.deleteRecords(VIDEO_DIRECTORY);
        }
        if (removeScreenShotFlag) {
            emptyFolder("screenshots");
        }

    }

    @AfterStep
    public void prepareStepResult(Scenario scenario) throws IOException, InterruptedException {
        boolean isPassed = !scenario.isFailed();
        if (position > 0) {
        String stepAction =  qaseApiClient.getCaseStepAction(projectCode,Integer.parseInt(caseId),position);
        String screenShotName = stepAction + ".png";
        takeScreenshot(stepAction);
        try {
            hash = qaseApiClient.uploadAttachment(projectCode, screenShotName, SCREENSHOT_DIRECTORY);
            steps.add(BaseTest.stepsPayload(isPassed, position, stepAction, hash));
        }
        catch (Exception e){
            e.printStackTrace();
            }
        }
        position ++;
    }


}
