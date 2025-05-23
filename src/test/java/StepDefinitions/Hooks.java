package StepDefinitions;

import Data.QASEConfig;
import Data.GlobalConfig;
import io.cucumber.java.*;
import utils.BaseTest;
import utils.VideoRecorder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Hooks extends BaseTest {


    public static int runId;
    public static String runTitle;
    public static String projectCode;
    public static String testPlanId;
    public static String apiToken;
    public static String hash ;
    public static String caseId;
    //public static  VideoRecorder videoRecorder;
    public static File videoFile;
    public static boolean removeVideoFlag = true;
    public static boolean removeScreenShotFlag = true;
    public static int position ;
    public static List<Map<String, Object>> steps = new ArrayList<>();
    public static GlobalConfig globalConfig;
    public static QASEConfig qaseConfig;
    public static String product;

    @BeforeAll
    public static void createQaseTestRun() throws IOException {
        initializeConfigurations();
        setupQaseTestRun();
    }

    /**
     * Initializes all necessary configurations
     */
    private static void initializeConfigurations() throws IOException {
        globalConfig = new GlobalConfig();
        product = GlobalConfig.getProperty(GlobalConfig.getGlobalPropertyPath("globalPropertyPath"), "product");
        qaseConfig = new QASEConfig(product);
        apiToken = qaseConfig.getQaseConfig().get("apiToken");
        projectCode = qaseConfig.getQaseConfig().get("projectCode");
    }

    /**
     * Sets up QASE test run based on product type
     */
    private static void setupQaseTestRun() {
        try {
            testPlanId = qaseConfig.getQaseConfig().get("testPlanId");
            runTitle = qaseConfig.getQaseConfig().get("runTitle");
            String runType = product.equalsIgnoreCase("app") ? "platform" : "browser";
            runId = qaseConfig.getTestRunId(runType, testPlanId, runTitle);
        } catch (IOException e) {
            System.err.println("Failed to create test run: " + e.getMessage());
        }
    }

    /**
     * Initializes test case and gets case ID
     */

    @Before
    public void initializeTestCase(Scenario scenario) throws IOException {
        caseId = qaseConfig.getCaseId(scenario);
        steps.clear();
        position = 0;

    }

    /**
     * Handles test cleanup and reporting
     */

    @After
    public void cleanupAndReport(Scenario scenario) throws Exception {
        handleVideoRecording(scenario);
        cleanupDriver();
        waitForVideoProcessing();
        reportTestResult(scenario);
        cleanupMediaFiles();
    }
    /**
     * Waits for video processing to complete
     */
    private void waitForVideoProcessing() throws InterruptedException {
        Thread.sleep(5000);
    }

    /**
     * Handles video recording based on product type
     */
    private void handleVideoRecording(Scenario scenario) throws IOException {
        if (product.equalsIgnoreCase("app")) {
            videoFile = videoFileCreation(scenario.getName(), driver);
        }
    }

    /**
     * Cleans up the driver instance
     */
    private void cleanupDriver() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Failed to cleanup driver: " + e.getMessage());
        }
    }

    /**
     * Reports test result to QASE
     */
    private void reportTestResult(Scenario scenario) throws IOException, InterruptedException {
        boolean isPassed = !scenario.isFailed();
        String videoFileName;
        if (actualVideoFileName(scenario.getName()).exists() && ! product.equalsIgnoreCase("app"))
        {
            videoFileName = actualVideoFileName(scenario.getName()).getName();
        }
        else if (product.equalsIgnoreCase("app"))
        {
            videoFileName = videoFile.getName();
        }
        else {
            videoFileName = "";
        }
        String videoDirectory = actualVideoFileName(scenario.getName()).exists()
                ? globalConfig.getDirectory().get("VIDEO_DIRECTORY")
                : globalConfig.getDirectory().get("APP_VIDEO_DIRECTORY");

        hash = videoFileName.isEmpty() ? "" : qaseConfig.createHash(videoFileName, videoDirectory) ;
        try {
            qaseConfig.createTestCaseResult(runId, projectCode, hash, isPassed, caseId, steps);
        } catch (Exception e) {
            System.err.println("Failed to report test result: " + e.getMessage());
        }

    }

    /**
     * Cleans up media files (screenshots and videos)
     */
    private void cleanupMediaFiles() {
        if (removeScreenShotFlag) {
            try {
                emptyFolder("screenshots");
            } catch (Exception e) {
                System.err.println("Failed to delete screenshots: " + e.getMessage());
            }
        }
        if (removeVideoFlag) {
            try {
                VideoRecorder.deleteRecords(globalConfig.getDirectory().get("VIDEO_DIRECTORY"));
                VideoRecorder.deleteRecords(globalConfig.getDirectory().get("APP_VIDEO_DIRECTORY"));
            } catch (Exception e) {
                System.err.println("Failed to delete videos: " + e.getMessage());
            }
            }
    }


    @AfterStep
    public void recordStepResult(Scenario scenario) throws IOException, InterruptedException {
        if (position > 0) {
            try {
                String stepAction = qaseConfig.getCaseStepAction(projectCode, Integer.parseInt(caseId), position);
                boolean isPassed = !scenario.isFailed();

                if (!isPassed) {
                    captureScreenshot(stepAction);
                }

                recordStepDetails(isPassed, position, stepAction);
            } catch (Exception e) {
                System.err.println("Failed to record step result: " + e.getMessage());
            }

        }
            position++;
    }

    /**
     * Captures screenshot for failed steps
     */
    private void captureScreenshot(String stepAction) throws IOException, InterruptedException {
        try {
            String screenShotName = stepAction + ".png";
            takeScreenshot(stepAction);
            hash = qaseConfig.createHash(screenShotName, globalConfig.getDirectory().get("SCREENSHOT_DIRECTORY"));
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Records step details for reporting
     */
    private void recordStepDetails(boolean isPassed, int position, String stepAction) {
        try {
            steps.add(stepsPayload(isPassed, position, stepAction, hash));
        } catch (Exception e) {
            System.err.println("Failed to record step details: " + e.getMessage());
        }
    }
}

