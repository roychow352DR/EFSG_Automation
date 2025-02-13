package StepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.jsoup.Connection;
import utils.BaseTest;
import utils.QaseApiClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Hooks extends BaseTest {

    public static QaseApiClient qaseApiClient;
    public static int runId;
    public static String runTitle;
    public static String projectCode;
    public static String testPlanId;
    public static String testType;
    public static String apiToken;

    @BeforeAll
    public static void createQaseTestRun() throws IOException {

        try {
            String path = "//src//main//java//DataResources//qase-adminportal.properties";
            BaseTest base = new BaseTest();
            apiToken = base.getProperty(path,"qase.api.token");
            projectCode = base.getProperty(path,"qase.project.code");;
            testType = System.getProperty("testtype") != null ? System.getProperty("testtype") : base.getProperty(path,"testtype");;
            testPlanId = getTestPlanId(testType,path);

            //Initialize Qase API client
            qaseApiClient = new QaseApiClient(apiToken, projectCode);

            runTitle = qaseApiClient.getTestPlanTitle(Integer.parseInt(testPlanId), projectCode);

            // Create a test run in Qase
            runId = qaseApiClient.createTestRunByTestPlan(Integer.parseInt(testPlanId), runTitle);
            System.out.println("Test Run Created: " + runTitle + " " + runId);
            System.out.println("Tag: " + testPlanId );
        }
        catch(IOException e) {
            e.getStackTrace();
            System.out.println("Failed to create test run");
        }

       //runId = qaseApiClient.getTestRunID();
       // System.out.println("Test Run Created for Tag " + tag + " with ID: " + runId);
        // Extract runId from API response (you can use a JSON parser here)
        //  runId = 1; // Replace with actual extraction logic

//        String response2 = qaseApiClient.getAllTestCases(projectCode);
//        System.out.println(response2);
//        try {
//      //   Fetch all test cases from the test plan
//        Map<Integer, JsonNull> testCaseMap = qaseApiClient.getTestCasesFromPlan(projectCode, testPlanId);
//
//        // Print the mapped test cases
//        System.out.println("Test Case Mapping:");
//        for (Map.Entry<Integer, JsonNull> entry : testCaseMap.entrySet()) {
//            System.out.println("Title: " + entry.getKey() + ", ID: " + entry.getValue());
//        }
//
//        // Example: Assert that a specific test case exists
//        Assert.assertTrue(testCaseMap.containsKey("Sample Test Case Title"), "Test case not found!");
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }


    }

    @After
    public void logQaseTestResult(Scenario scenario) throws IOException {
        // Extract test case metadata
        String caseId = qaseApiClient. getFeatureFileName(scenario,projectCode);
        boolean isPassed = !scenario.isFailed();
        // Log result into Qase// Replace with your Qase case ID
        qaseApiClient.logTestResult(projectCode, runId, caseId, isPassed);
    }

    public static String getTestPlanId(String testType, String path) throws IOException {
        BaseTest base = new BaseTest();
        return switch (testType) {
            case "regression" -> base.getProperty(path,"qase.regression.testPlanId");
            case "smoke" -> base.getProperty(path,"qase.regression.testPlanId");
            default -> "";
        };
    }

}
