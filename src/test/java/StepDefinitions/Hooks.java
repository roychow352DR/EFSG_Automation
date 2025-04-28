package StepDefinitions;

import io.cucumber.java.*;
import org.jsoup.Connection;
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
import java.util.concurrent.ExecutionException;

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
    public static ScreenRecorder screenRecorder;
    //public static  VideoRecorder videoRecorder;
    public static File videoFile;
    private static String VIDEO_DIRECTORY;
    private static String SCREENSHOT_DIRECTORY;
    public static String qasePropertyPath ;
    public static String filePropertyPath = "//src//main//java//DataResources//FileDirectory.properties";
    public static String globalPropertyPath = "//src//main//java//DataResources//GlobalData.properties";
    public static String videoName;
    public static boolean removeVideoFlag = true;
    public static boolean removeScreenShotFlag = true;
    public static int position;
    public static List<Map<String, Object>> steps = new ArrayList<>();

    public Hooks() throws IOException{

        position = 0;
        VIDEO_DIRECTORY = getProperty(filePropertyPath, "video_directory");
        SCREENSHOT_DIRECTORY = System.getProperty("user.dir") + "/screenshots/";

    }

    @BeforeAll
    public static void createQaseTestRun() throws IOException {
        String product = System.getProperty("product") != null ? System.getProperty("product") : getProperty(globalPropertyPath, "product");
        qasePropertyPath = getPropertyPath(product);
        apiToken = getProperty(qasePropertyPath, "qase.api.token");
        projectCode = getProperty(qasePropertyPath, "qase.project.code");
        try {
            testPlanId = BaseTest.getTestPlanId(getProperty(qasePropertyPath, "testtype"), qasePropertyPath);

            //Initialize Qase API client
            qaseApiClient = new QaseApiClient(apiToken, projectCode);

            runTitle = qaseApiClient.getTestPlanTitle(Integer.parseInt(testPlanId), projectCode);

            // Create a test run in Qase
            runId = qaseApiClient.createTestRunByTestPlan(Integer.parseInt(testPlanId),
                    runTitle, getProperty(globalPropertyPath, "browser"),
                    getProperty(globalPropertyPath, "env"));
        } catch (IOException e) {
            e.getStackTrace();
            System.out.println("Failed to create test run");
        }

    }

    @Before
    public void getCaseId(Scenario scenario) {
        caseId = qaseApiClient.getCaseId(scenario, projectCode);
        steps.clear();

    }


    @After
    public void logQaseTestResult(Scenario scenario) throws Exception {
        driver.quit();
        //wait until video creation fully complete
        Thread.sleep(5000);

        boolean isPassed = !scenario.isFailed();

        if (BaseTest.actualVideoFileName(scenario.getName()).exists()) {
            hash = qaseApiClient.uploadAttachment(projectCode, BaseTest.actualVideoFileName(scenario.getName()).getName(), VIDEO_DIRECTORY);
        }

        try {
            qaseApiClient.createTestCaseResult(runId, projectCode, hash, isPassed, caseId, steps);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (removeScreenShotFlag) {
            try {
                emptyFolder("screenshots");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("No screenshots is deleted");
            }
        }
        if (removeVideoFlag) {
            try {
                VideoRecorder.deleteRecords(VIDEO_DIRECTORY);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("No video is deleted");
            }
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

    @AfterStep
    public void prepareStepResult(Scenario scenario) throws IOException, InterruptedException {
        boolean isPassed = !scenario.isFailed();
        if (position > 0) {
            String stepAction = qaseApiClient.getCaseStepAction(projectCode, Integer.parseInt(caseId), position);
            String screenShotName = stepAction + ".png";
            if (!isPassed) {
                takeScreenshot(stepAction);
                hash = qaseApiClient.uploadAttachment(projectCode, screenShotName, SCREENSHOT_DIRECTORY);
            }
            try {
                steps.add(BaseTest.stepsPayload(isPassed, position, stepAction, hash));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        position++;
    }

/*    @After
    public void teardown() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }*/

}
