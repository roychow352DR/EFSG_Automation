package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import io.cucumber.java.Scenario;
import io.qase.client.ApiClient;
import io.qase.client.Configuration;
import io.qase.client.api.AttachmentsApi;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class QaseApiClient {

    private static final String BASE_URL = "https://api.qase.io/v1/";
    private static String apiToken;
    private static String endpoint;
    private static String body;
    public String response;
    public String testPlanId;
    public String request;
    public String hash;
    public AttachmentsApi attachmentsApi;
    public ApiClient qaseApi = Configuration.getDefaultApiClient();

    //public static String tag;


    public QaseApiClient(String apiToken, String projectCode) {
        QaseApiClient.apiToken = apiToken;
        QaseApiClient.endpoint = BASE_URL + "run/" + projectCode;
        //   QaseApiClient.tag = tag;
        QaseApiClient.body = String.format("{\"title\":\"%s\"}", "Automated Test Run");
    }

    public String getCaseId(Scenario scenario, String projectCode) {
        String uri = scenario.getUri().toString(); // Get the URI of the scenario
        String[] caseId = uri.substring(uri.lastIndexOf("/") + 1).split(".feature");
        String[] actualCaseId = caseId[0].split(projectCode + "-");
        return actualCaseId[1];// Extract the file name
    }

    public int createTestRunByTestPlan(int planId, String runTitle,String browserType,String env) throws IOException {
        // Prepare the request payload
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String str = ft.format(new Date());

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("title", "["+browserType+"]" + "["+env+"]" + str + " - " + runTitle);
        requestBody.addProperty("plan_id", planId);

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

    public String getTestPlanTitle(int planId, String projectCode) throws IOException {
        String endpoint = BASE_URL + "plan/" + projectCode + "/" + planId;
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("code", projectCode);
        requestBody.addProperty("id", planId);

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
            testPlanId = System.getProperty("qase.regression.testPlanId");
        } else if (testtype.equalsIgnoreCase("smoke")) {
            testPlanId = System.getProperty("qase.regression.testPlanId");
        }

        return testPlanId;
    }

    public void createTestCaseResult(int testRunId, String projectCode, String hash, boolean status, String caseId,List<Map<String, Object>> steps) throws IOException {
        // Build the endpoint URL for Create test run result
        endpoint = BASE_URL + "result/" + projectCode + "/" + testRunId;

        // Create the full payload as a Map
        Map<String, Object> payload = new HashMap<>();

        if (!hash.isEmpty()) {
            payload.put("status", status ? "passed" : "failed");
            payload.put("case_id", caseId);
            payload.put("attachments", List.of(hash));
            payload.put("steps", steps);

        } else {

            payload.put("status", status ? "passed" : "failed");
            payload.put("case_id", caseId);
            payload.put("steps", steps);
        }

        // Serialize the payload to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(payload);
        //  System.out.println("Generated JSON Payload: " + jsonPayload);

        try {
            // Send the request using Apache HttpClient
            response = Request.post(endpoint)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("Token", apiToken) // Replace with your token
                    .bodyString(jsonPayload, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload Qase result, Error: " + e);
        }
    }


    public String uploadAttachment(String projectCode, String scenarioName,String path) throws IOException, InterruptedException {
        // Qase API endpoint
        String endpoint = BASE_URL + "attachment/" + projectCode;

        // Generate a random boundary for multipart encoding
        String boundary = "Boundary-" + System.currentTimeMillis();

        // Path to the video file
        Path filePath = Path.of(path + scenarioName);
        String fileName = filePath.getFileName().toString();


        // Verify the file exists and is complete
        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            throw new IOException("Attached file does not exist or is empty: " + filePath);
        }

        // Read the file content
        byte[] fileContent;
        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(filePath))) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[8192]; // Read in 8 KB chunks
            int bytesRead;
            while ((bytesRead = inputStream.read(chunk)) != -1) {
                buffer.write(chunk, 0, bytesRead);
            }
            fileContent = buffer.toByteArray();
        }


        // Construct the multipart body
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (path.contains("Video")) {
            try {
                // Add the boundary and headers for the file part
                outputStream.write(("--" + boundary + "\r\n").getBytes());
                outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n").getBytes());
                outputStream.write("Content-Type: video/mp4\r\n\r\n".getBytes());

                // Add the binary file content
                outputStream.write(fileContent);

                // Add the closing boundary
                outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error constructing multipart body", e);
            }
        }
        else if (path.contains("screenshots")){
            try {
                // Add the boundary and headers for the file part
                outputStream.write(("--" + boundary + "\r\n").getBytes());
                outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n").getBytes());
                outputStream.write("Content-Type: image/png\r\n\r\n".getBytes());

                // Add the binary file content
                outputStream.write(fileContent);

                // Add the closing boundary
                outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error constructing multipart body", e);
            }
        }
        // Create the HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("accept", "application/json")
                .header("content-type", "multipart/form-data; boundary=" + boundary)
                .header("Token", apiToken)
                .POST(HttpRequest.BodyPublishers.ofByteArray(outputStream.toByteArray()))
                .build();

        // Send the request and get the response
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        // Parse the response
        JsonObject rootObject = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray resultArray = rootObject.getAsJsonArray("result");
        if (!resultArray.isEmpty()) {
            // Extract the first object in the "result" array and get the "hash" value
            JsonObject firstResult = resultArray.get(0).getAsJsonObject();
            hash = firstResult.get("hash").getAsString();
        }

        return hash;

    }

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

    public String getCaseStepAction(String projectCode, int caseId, int stepPosition) throws IOException {
        endpoint = BASE_URL + "case/" + projectCode + "/" + caseId;

        // Make the API request and parse the response
        Map<Integer, String> stepsMap = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create the HTTP GET request
            HttpGet request = new HttpGet(endpoint);
            //  request.setHeader("Content-Type", "application/json");
            request.setHeader("Token", apiToken); // Qase API requires the API token in the headers

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the response as a String
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Parse the JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonResponse);

                // Navigate to the "steps" array
                JsonNode stepsNode = rootNode.path("result").path("steps");

                // Create a HashMap to store position and action
                stepsMap = new HashMap<>();

                // Iterate over the "steps" array
                for (JsonNode step : stepsNode) {
                    int position = step.path("position").asInt(); // Retrieve position
                    String action = step.path("action").asText(); // Retrieve action

                    // Add to the HashMap
                    stepsMap.put(position, action);
                }
                // Print the steps map
                //  System.out.println("Retrieved Steps:");
                stepsMap.forEach((position, action) -> {
                    //      System.out.println("Position: " + position + ", Action: " + action);
                });
                //   System.out.println(stepsMap.get(stepPosition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert stepsMap != null;
        return stepsMap.get(stepPosition);
    }

    }



