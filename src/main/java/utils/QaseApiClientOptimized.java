package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.cucumber.java.Scenario;
import org.apache.hc.core5.http.ContentType;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Comparator;

/**
 * Optimized Qase API client with consistent HTTP usage, try-with-resources, and
 * clearer method contracts. The original QaseApiClient is kept for comparison.
 */
public class QaseApiClientOptimized {

    private static final String BASE_URL = "https://api.qase.io/v1/";
    private final String apiToken;
    private final String projectCode;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new optimized client.
     *
     * @param apiToken    Qase API token
     * @param projectCode default project code for run/plan requests
     */
    public QaseApiClientOptimized(String apiToken, String projectCode) {
        this.apiToken = apiToken;
        this.projectCode = projectCode;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Extracts the case ID from a scenario URI using the provided project code.
     *
     * @param scenario    Cucumber scenario
     * @param projectCode project code prefix in the file name
     * @return extracted case ID portion
     */
    public String getCaseId(Scenario scenario, String projectCode) {
        String uri = scenario.getUri().toString();
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        String featureName = fileName.endsWith(".feature")
                ? fileName.substring(0, fileName.length() - ".feature".length())
                : fileName;
        String prefix = projectCode + "-";
        if (!featureName.startsWith(prefix)) {
            throw new IllegalArgumentException("Feature file name does not follow '<projectCode>-<caseId>.feature': " + fileName);
        }
        return featureName.substring(prefix.length());
    }

    /**
     * Creates a test run by plan ID and returns the created run ID.
     *
     * @param planId   plan identifier
     * @param runTitle run title
     * @param platform platform label
     * @param env      environment label
     * @param entity   entity label
     * @param product  product name that influences the title
     * @return created run ID
     * @throws IOException          on network or serialization errors
     * @throws InterruptedException when the HTTP call is interrupted
     */
    public int createTestRunByTestPlan(int planId, String runTitle, String platform, String env, String entity, String product) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "run/" + projectCode;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = ft.format(new Date());

        JsonObject requestBody = new JsonObject();
        if (product.contains("adminPortal")) {
            requestBody.addProperty("title", "[" + entity + "][" + platform + "][" + env + "]" + dateStr + " - " + runTitle);
        } else {
            requestBody.addProperty("title", "[" + platform + "][" + env + "]" + dateStr + " - " + runTitle);
        }
        requestBody.addProperty("plan_id", planId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("Token", apiToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                .build();

        String response = send(request);
        JsonNode root = objectMapper.readTree(response);
        return root.path("result").path("id").asInt();
    }

    /**
     * Retrieves the title of a test plan.
     *
     * @param planId plan identifier
     * @return plan title
     * @throws IOException          on network or parsing errors
     * @throws InterruptedException when the HTTP call is interrupted
     */
    public String getTestPlanTitle(int planId) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "plan/" + projectCode + "/" + planId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("Token", apiToken)
                .GET()
                .build();
        String response = send(request);
        JsonNode root = objectMapper.readTree(response);
        return root.path("result").path("title").asText();
    }

    /**
     * Resolves a test plan ID from system properties or a properties file.
     *
     * @return test plan ID or null if not resolved
     * @throws IOException on file read errors
     */
    public String getTestPlanId() throws IOException {
        Properties prop = new Properties();
        Path propPath = Path.of(System.getProperty("user.dir"), "src", "main", "java", "DataResources", "qase-adminportal.properties");
        try (InputStream fis = Files.newInputStream(propPath)) {
            prop.load(fis);
        }
        String testType = System.getProperty("testtype") != null ? System.getProperty("testtype") : prop.getProperty("testtype");
        if ("regression".equalsIgnoreCase(testType)) {
            return System.getProperty("qase.regression.testPlanId");
        } else if ("smoke".equalsIgnoreCase(testType)) {
            return System.getProperty("qase.regression.testPlanId");
        }
        return null;
    }

    /**
     * Creates a test case result in a run.
     *
     * @param testRunId   run ID
     * @param hash        optional attachment hash
     * @param status      pass/fail status
     * @param caseId      case identifier
     * @param steps       list of step maps
     * @return raw response string
     * @throws IOException          on serialization or network errors
     * @throws InterruptedException when the HTTP call is interrupted
     */
    public void createTestCaseResult(int testRunId, String hash, boolean status, String caseId, List<Map<String, Object>> steps) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "result/" + projectCode + "/" + testRunId;

        Map<String, Object> payload = new HashMap<>();
        payload.put("status", status ? "passed" : "failed");
        payload.put("case_id", caseId);
        payload.put("steps", steps);
        if (hash != null && !hash.isEmpty()) {
            payload.put("attachments", List.of(hash));
        }

        String jsonPayload = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("Token", apiToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        send(request);
    }

    /**
     * Uploads an attachment (video or screenshot) to Qase and returns the hash.
     *
     * @param scenarioName file name to upload
     * @param path         directory path prefix
     * @return attachment hash
     * @throws IOException          on IO failures
     * @throws InterruptedException when the HTTP call is interrupted
     */
    public String uploadAttachment(String scenarioName, String path) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "attachment/" + projectCode;
        Path filePath = Path.of(path + scenarioName);
        String fileName = filePath.getFileName().toString();

        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            throw new IOException("Attached file does not exist or is empty: " + filePath);
        }

        byte[] fileContent;
        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(filePath))) {
            fileContent = inputStream.readAllBytes();
        }

        String boundary = "Boundary-" + System.currentTimeMillis();
        String contentType = path.contains("videos") ? ContentType.DEFAULT_BINARY.toString() : "image/png";

        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileName).append("\"\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n\r\n");

        byte[] prefix = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] suffix = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);
        byte[] body = joinByteArrays(prefix, fileContent, suffix);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("accept", "application/json")
                .header("content-type", "multipart/form-data; boundary=" + boundary)
                .header("Token", apiToken)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();

        String response = send(request);
        JsonNode root = objectMapper.readTree(response);
        JsonNode resultArray = root.path("result");
        if (resultArray.isArray() && resultArray.size() > 0) {
            return resultArray.get(0).path("hash").asText();
        }
        return null;
    }

    /**
     * Retrieves a step action from a test case at a given position.
     *
     * @param caseId       case identifier
     * @param stepPosition position of the step
     * @return action text for the step or null if missing
     * @throws IOException          on network or parsing errors
     * @throws InterruptedException when the HTTP call is interrupted
     */
    public String getCaseStepAction(int caseId, int stepPosition) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "case/" + projectCode + "/" + caseId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Token", apiToken)
                .GET()
                .build();
        String response = send(request);

        JsonNode root = objectMapper.readTree(response);
        JsonNode stepsNode = root.path("result").path("steps");
        if (stepsNode.isArray()) {
            for (JsonNode step : stepsNode) {
                int position = step.path("position").asInt();
                if (position == stepPosition) {
                    return step.path("action").asText();
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all step actions from a Qase test case in order.
     *
     * @param caseId case identifier
     * @return ordered list of step actions
     * @throws IOException on network or parsing errors
     * @throws InterruptedException when the HTTP call is interrupted
     */
    public List<String> getCaseStepActions(int caseId) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "case/" + projectCode + "/" + caseId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Token", apiToken)
                .GET()
                .build();
        String response = send(request);

        JsonNode root = objectMapper.readTree(response);
        JsonNode stepsNode = root.path("result").path("steps");
        List<String> stepActions = new ArrayList<>();
        if (stepsNode.isArray()) {
            List<JsonNode> sortedSteps = new ArrayList<>();
            stepsNode.forEach(sortedSteps::add);
            sortedSteps.sort(Comparator.comparingInt(step -> step.path("position").asInt(0)));
            for (JsonNode step : sortedSteps) {
                stepActions.add(step.path("action").asText("").trim());
            }
        }
        return stepActions;
    }

    /**
     * Updates case steps in Qase when feature steps differ from existing steps.
     *
     * @param caseId       case identifier
     * @param featureSteps steps parsed from feature file
     * @return true when update call was sent, false when no change is needed
     * @throws IOException on serialization or network failures
     * @throws InterruptedException when the HTTP call is interrupted
     */
    public boolean replaceCaseStepsIfDifferent(int caseId, List<String> featureSteps) throws IOException, InterruptedException {
        List<String> normalizedFeatureSteps = new ArrayList<>();
        for (String step : featureSteps) {
            normalizedFeatureSteps.add(step == null ? "" : step.trim());
        }

        List<String> currentSteps = getCaseStepActions(caseId);
        if (Objects.equals(currentSteps, normalizedFeatureSteps)) {
            return false;
        }

        List<Map<String, Object>> qaseSteps = new ArrayList<>();
        int position = 1;
        for (String step : normalizedFeatureSteps) {
            Map<String, Object> stepBody = new HashMap<>();
            stepBody.put("action", step);
            stepBody.put("expected_result", "");
            stepBody.put("data", "");
            stepBody.put("position", position++);
            qaseSteps.add(stepBody);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", getCaseTitle(caseId));
        payload.put("steps_type", "classic");
        payload.put("steps", qaseSteps);
        String jsonPayload = objectMapper.writeValueAsString(payload);

        String endpoint = BASE_URL + "case/" + projectCode + "/" + caseId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("Token", apiToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        send(request);
        return true;
    }

    private String getCaseTitle(int caseId) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "case/" + projectCode + "/" + caseId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Token", apiToken)
                .GET()
                .build();
        String response = send(request);
        JsonNode root = objectMapper.readTree(response);
        return root.path("result").path("title").asText("");
    }

    /**
     * Parses a response JSON and retrieves a top-level field under "result".
     *
     * @param response      raw JSON string
     * @param retrieveParam field name under "result"
     * @return value as string
     * @throws IOException on parsing errors
     */
    public String parseJsonObject(String response, String retrieveParam) throws IOException {
        JsonNode root = objectMapper.readTree(response);
        return root.path("result").path(retrieveParam).asText();
    }

    /**
     * Sends an HTTP request using the shared HttpClient and returns the response body.
     */
    private String send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Qase API request failed (" + response.statusCode() + ") for "
                    + request.method() + " " + request.uri() + " with response: " + response.body());
        }
        return response.body();
    }

    /**
     * Concatenates multiple byte arrays (used for multipart payload assembly).
     */
    private static byte[] joinByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }
        return result;
    }
}





