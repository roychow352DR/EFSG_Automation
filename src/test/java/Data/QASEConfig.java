package Data;

import io.cucumber.java.Scenario;
import utils.QaseApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QASEConfig extends GlobalConfig {

    public static QaseApiClient qaseApiClient;
    public String product;
    public String path;

    public QASEConfig(String product)
    {
        this.product = product;
    }


    public Map<String, String> getQaseConfig() throws IOException {
        Map<String,String> qaseConfig = new HashMap<>();
        path = getQasePropertyPath(product);
        qaseApiClient = new QaseApiClient(getProperty(path,"qase.api.token"),getProperty(path,"qase.project.code"));
        qaseConfig.put("qasePropertyPath",path);
        qaseConfig.put("apiToken",getProperty(path,"qase.api.token"));
        qaseConfig.put("projectCode",getProperty(path,"qase.project.code"));
        qaseConfig.put("testPlanId",getTestPlanId(getProperty(path, "testType"), path));
        qaseConfig.put("runTitle",qaseApiClient.getTestPlanTitle(Integer.parseInt(getTestPlanId(getProperty(path, "testType"), path)), getProperty(path,"qase.project.code")));
        return qaseConfig;

    }

    public static String getQasePropertyPath(String product)
    {
        return switch (product) {
            case "adminPortal" -> "//src//main//java//DataResources//qase-adminportal.properties";
            case "mio" -> "//src//main//java//DataResources//qase-mioAdminPortal.properties";
            case "app" -> "//src//main//java//DataResources//qase-nativeApp.properties";
            default -> "";
        };

    }
    public static String getTestPlanId(String testType, String path) throws IOException {
        return switch (testType) {
            case "Regression" -> getProperty(path, "qase.regression.testPlanId");
            case "Smoke" -> getProperty(path, "qase.smoke.testPlanId");
            default -> "";
        };
    }

    public int getTestRunId(String property,String testPlanId,String runTitle) throws IOException {
        if (product.equalsIgnoreCase("app"))
        {
            return qaseApiClient.createTestRunByTestPlan(Integer.parseInt(testPlanId),
                    runTitle, getProperty(getPropertyPath("app"), property),
                    getProperty(getGlobalPropertyPath("globalPropertyPath"), "env"));
        }
        else {
            return qaseApiClient.createTestRunByTestPlan(Integer.parseInt(testPlanId),
                    runTitle, getProperty(getGlobalPropertyPath("globalPropertyPath"), property),
                    getProperty(getGlobalPropertyPath("globalPropertyPath"), "env"));
        }
    }

    public String getCaseId(Scenario scenario) throws IOException {
        return qaseApiClient.getCaseId(scenario, getQaseConfig().get("projectCode"));
    }

    public String createHash(String scenarioName,String directory) throws IOException, InterruptedException {
        return qaseApiClient.uploadAttachment(getQaseConfig().get("projectCode"), scenarioName, directory);
    }

    public void createTestCaseResult(int runId, String projectCode, String hash, boolean isPassed, String caseId, List<Map<String, Object>> steps) throws IOException, InterruptedException {
        qaseApiClient.createTestCaseResult(runId, projectCode, hash, isPassed, caseId, steps);
    }

    public String getCaseStepAction(String projectCode, int caseId, int position) throws IOException {
        return qaseApiClient.getCaseStepAction(projectCode, caseId, position);
    }




}
