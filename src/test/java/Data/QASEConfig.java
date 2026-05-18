package Data;

import io.cucumber.java.Scenario;
import utils.QaseApiClient;
import utils.QaseApiClientOptimized;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QASEConfig extends GlobalConfig {

    public static QaseApiClient qaseApiClient;
    public static QaseApiClientOptimized qaseApiClientOptimized;
    private final String product;
    private final String productEntity;
    public String path;

    public QASEConfig(String product,String productEntity)
    {
        this.product = product;
        this.productEntity = productEntity;
    }


    public Map<String, String> getQaseConfig() throws IOException, InterruptedException {
        Map<String,String> qaseConfig = new HashMap<>();
        path = getQasePropertyPath(product);
        qaseApiClientOptimized = new QaseApiClientOptimized(getProperty(path, "qase.api.token"), getProperty(path, "qase.project.code"));
        qaseConfig.put("qasePropertyPath",path);
        qaseConfig.put("apiToken",getProperty(path,"qase.api.token"));
        qaseConfig.put("projectCode",getProperty(path,"qase.project.code"));
        qaseConfig.put("testPlanId",getTestPlanId(getProperty(path, "testtype"),path,productEntity));
        qaseConfig.put("runTitle",qaseApiClientOptimized.getTestPlanTitle(Integer.parseInt(getTestPlanId(getProperty(path, "testtype"), path,productEntity))));
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
    public static String getTestPlanId(String testType, String path,String entity) throws IOException {
        if (entity.equalsIgnoreCase("EIEHK")) {
            return switch (testType) {
                case "Regression" -> getProperty(path, "qase.regression.testPlanId");
                case "Smoke" -> getProperty(path, "qase.smoke.testPlanId");
                default -> "";
            };
        }
        else if (entity.equalsIgnoreCase("EBL_MT5")) {
            return switch (testType) {
                case "Regression" -> getProperty(path, "qase.ebl.regression.testPlanId");
                case "Smoke" -> getProperty(path, "qase.smoke.testPlanId");
                default -> "";
            };
        }
        else if (entity.equalsIgnoreCase("XPro")) {
            return switch (testType) {
                case "Regression" -> getProperty(path, "qase.xpro.regression.testPlanId");
                case "Smoke" -> getProperty(path, "qase.smoke.testPlanId");
                default -> "";
            };
        }

        return testType;
    }

    public int getTestRunId(String property,String testPlanId,String runTitle,String product) throws IOException, InterruptedException {
        if (product.equalsIgnoreCase("app"))
        {
            return qaseApiClientOptimized.createTestRunByTestPlan(Integer.parseInt(testPlanId),
                    runTitle, getProperty(getPropertyPath("app"), property),
                    getProperty(getGlobalPropertyPath("globalPropertyPath"), "env"),getProperty(getGlobalPropertyPath("globalPropertyPath"), "entity"),product);
        }
        else {
            return qaseApiClientOptimized.createTestRunByTestPlan(Integer.parseInt(testPlanId),
                    runTitle, getProperty(getGlobalPropertyPath("globalPropertyPath"), property),
                    getProperty(getGlobalPropertyPath("globalPropertyPath"), "env"),getProperty(getGlobalPropertyPath("globalPropertyPath"), "entity"),product);
        }
    }

    public String getCaseId(Scenario scenario) throws IOException, InterruptedException {
        return qaseApiClientOptimized.getCaseId(scenario, getQaseConfig().get("projectCode"));
    }

    public String createHash(String scenarioName,String directory) throws IOException, InterruptedException {
       // return qaseApiClient.uploadAttachment(getQaseConfig().get("projectCode"), scenarioName, directory);
        return qaseApiClientOptimized.uploadAttachment(scenarioName, directory);
    }

    public void createTestCaseResult(int runId, String projectCode, String hash, boolean isPassed, String caseId, List<Map<String, Object>> steps) throws IOException, InterruptedException {
        //qaseApiClient.createTestCaseResult(runId, projectCode, hash, isPassed, caseId, steps);
        qaseApiClientOptimized.createTestCaseResult(runId, hash, isPassed, caseId, steps);
    }

    public String getCaseStepAction(String projectCode, int caseId, int position) throws IOException, InterruptedException {
        //return qaseApiClient.getCaseStepAction(projectCode, caseId, position);
        return qaseApiClientOptimized.getCaseStepAction(caseId, position);
    }




}
