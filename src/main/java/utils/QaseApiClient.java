package utils;

import com.google.gson.*;
import io.cucumber.java.Scenario;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.client5.http.fluent.Request;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Properties;

public class QaseApiClient {

    private static final String BASE_URL = "https://api.qase.io/v1/";
    private static String apiToken;
    private static String endpoint;
    private static String body;
    public String response;
    public String testPlanId;
    //public static String tag;


    public QaseApiClient(String apiToken,String projectCode) {
        QaseApiClient.apiToken = apiToken;
        QaseApiClient.endpoint = BASE_URL + "run/" + projectCode;
     //   QaseApiClient.tag = tag;
        QaseApiClient.body = String.format("{\"title\":\"%s\"}", "Automated Test Run");
    }

    public String createTestRun() throws IOException {

        // Prepare the request payload
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("title", "Automated Test Run");
       // requestBody.addProperty("tag",tag);

        response = Request.post(endpoint)
                .addHeader("Content-Type", "application/json")
                .addHeader("Token", apiToken)
                .bodyString(requestBody.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);

        return response;
    }

    public int getTestRunID() throws IOException {

        // Send POST request to create the test run

        // Parse the JSON response to retrieve the test run ID
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        if (jsonResponse.get("status").getAsBoolean()) {
            return jsonResponse.getAsJsonObject("result").get("id").getAsInt();
        } else {
            throw new RuntimeException("Failed to create a test run: " + response);
        }
    }

    public String logTestResult(String projectCode, int runId, String caseId, boolean success) throws IOException {
        String endpoint = BASE_URL + "result/" + projectCode + "/" + runId;
        String body = String.format("{\"case_id\":%s,\"status\":\"%s\"}", caseId, success ? "passed" : "failed");

        return Request.post(endpoint)
                .addHeader("Content-Type", "application/json")
                .addHeader("Token", apiToken)
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);
    }

    public String getAllTestCases(String projectCode) throws IOException {
        HashMap<String,String> testCases = new HashMap<String,String>();
        String endpoint = BASE_URL + "case/" + projectCode;
        response = Request.get(endpoint)
                .addHeader("accept", "application/json")
                .addHeader("Token", apiToken)
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);
            // Map Cucumber scenario names or tags to Qase case IDs
            testCases.put("Test Scenario 1", "1");
            testCases.put("Test Scenario 2", "2");
            testCases.put("@Smoke", "3");

            return response;


    }

    public Map<Integer, JsonNull> getTestCasesFromPlan(String projectCode, int planId) throws IOException {
        String endpoint = BASE_URL + "plan/" + projectCode + "/" + planId;

        // Make the API request
        String response = Request.get(endpoint)
                .addHeader("Content-Type", "application/json")
                .addHeader("Token", apiToken)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);

        // Parse the JSON response
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        Map<Integer, JsonNull> testCaseMap = new HashMap<>();

        if (jsonResponse.get("status").getAsBoolean()) {
            JsonArray cases = jsonResponse.getAsJsonObject("result").getAsJsonArray("cases");

            // Iterate over the cases and populate the HashMap
            for (JsonElement testCaseElement : cases) {
                JsonObject testCase = testCaseElement.getAsJsonObject();
                int caseId = testCase.get("case_id").getAsInt();
                JsonNull caseTitle = testCase.get("assignee_id").getAsJsonNull();

                // Add to the HashMap
                testCaseMap.put(caseId, caseTitle);
            }
        } else {
            throw new RuntimeException("Failed to fetch test plan details: " + response);
        }

        return testCaseMap;
    }

    public String getFeatureFileName(Scenario scenario,String projectCode)
    {
        String uri = scenario.getUri().toString(); // Get the URI of the scenario
        String[] caseId =  uri.substring(uri.lastIndexOf("/") + 1).split(".feature");
        String[] actualCaseId = caseId[0].split(projectCode+"-");
        return actualCaseId[1];// Extract the file name
    }

    public int createTestRunByTestPlan(int planId,String runTitle) throws IOException {
        // Prepare the request payload
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String str = ft.format(new Date());

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("title", str + " - " + runTitle);
        requestBody.addProperty("plan_id",planId);

        response = Request.post(endpoint)
                .addHeader("Content-Type", "application/json")
                .addHeader("Token", apiToken)
                .bodyString(requestBody.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        return jsonResponse.getAsJsonObject("result").get("id").getAsInt();
    }

    public String getTestPlanTitle(int planId,String projectCode) throws IOException {
        String endpoint = BASE_URL + "plan/" + projectCode + "/" + planId;
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("code", projectCode);
        requestBody.addProperty("id",planId);

        response = Request.get(endpoint)
                .addHeader("Content-Type", "application/json")
                .addHeader("Token", apiToken)
                .bodyString(requestBody.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString(StandardCharsets.UTF_8);

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        return jsonResponse.getAsJsonObject("result").get("title").getAsString();
    }
    public String getTestPlanId() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//main//java//DataResources//qase-adminportal.properties");
        prop.load(fis);
        String testtype = System.getProperty("testtype") != null ? System.getProperty("testtype") : prop.getProperty("testtype");
        if (testtype.equalsIgnoreCase("regression")) {
            testPlanId =System.getProperty("qase.regression.testPlanId");
        } else if (testtype.equalsIgnoreCase("smoke")) {
            testPlanId = System.getProperty("qase.regression.testPlanId");
        }

        return testPlanId;
    }

    }


