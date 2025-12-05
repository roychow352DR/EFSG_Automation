package API;

import AbstractComponent.AbstractComponentsPW;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Optimized version of CoreService with improved:
 * - Resource management (ApiClient with AutoCloseable)
 * - Error handling and validation
 * - Code reusability and maintainability
 * - Better null safety
 * - Consistent pagination handling
 */
public class CoreServiceOptimized {

    private static final Gson GSON = new Gson();
    private static final String DEFAULT_ACCOUNT_ID = "46be06ef-ab11-4d3b-978b-49f1ce80265e";
    private static final int MAX_PAGE_LOOKUP = 100;
    private final AbstractComponentsPW abs;
    private final String entity;
    public final String env;
    public static String clientType;
    public static String status;
    private final String domain;

    public CoreServiceOptimized(Page page, String productEnv) throws IOException {
        abs = new AbstractComponentsPW(page);
        entity = abs.userinfoList().get("entity");
        env = productEnv;
        this.domain = abs.getApiEndpointDomain(env);
    }

    /**
     * Safely extracts the response object from JSON response body
     */
    private JsonObject getResponseObject(String responseBody) {
        JsonObject root = GSON.fromJson(responseBody, JsonObject.class);
        if (root == null || !root.has("response") || root.get("response").isJsonNull()) {
            return new JsonObject();
        }
        return root.getAsJsonObject("response");
    }

    /**
     * Safely extracts the content array from response body
     */
    private JsonArray getContentArray(String responseBody) {
        return getContentArray(getResponseObject(responseBody));
    }

    /**
     * Safely extracts the content array from response object
     */
    private JsonArray getContentArray(JsonObject responseObj) {
        if (responseObj == null || !responseObj.has("content") || responseObj.get("content").isJsonNull()) {
            return new JsonArray();
        }
        return responseObj.getAsJsonArray("content");
    }

    /**
     * Safely extracts a string value from a JsonObject
     */
    private String getString(JsonObject source, String key) {
        if (source == null || key == null || !source.has(key) || source.get(key).isJsonNull()) {
            return "";
        }
        return source.get(key).getAsString();
    }

    /**
     * Converts null to empty string
     */
    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * Validates API response and throws exception if request failed
     */
    private APIResponse ensureSuccess(APIResponse response, String endpoint) {
        if (response == null) {
            throw new IllegalStateException("No response returned from endpoint: " + endpoint);
        }
        if (!response.ok()) {
            throw new IllegalStateException(
                    String.format("Request to %s failed with status %d and body %s",
                            endpoint, response.status(), response.text()));
        }
        return response;
    }

    public void getAccountStatus(String token) {
        String endpoint = domain + "account-opening/" + DEFAULT_ACCOUNT_ID + "/status";
        try (ApiClient apiClient = new ApiClient()) {
            APIResponse response = ensureSuccess(apiClient.get(endpoint, token), endpoint);
            System.out.println(parseJson(response.text(), "status"));
        }
    }

    public String parseJson(String responseBody, String value) {
        JsonObject responseObj = getResponseObject(responseBody);
        return getString(responseObj, value);
    }

    public String getValFromJsonArray(String responseBody, String value) {
        JsonArray contentArray = getContentArray(responseBody);
        if (contentArray.isEmpty()) {
            return "";
        }
        JsonObject firstItem = contentArray.get(0).getAsJsonObject();
        return getString(firstItem, value);
    }

    public String getValFromJsonArray(String responseBody, String extractVal, String conditionVal, String conditionParam) {
        JsonArray contentArray = getContentArray(responseBody);
        if (contentArray.isEmpty()) {
            return "";
        }
        for (JsonElement element : contentArray) {
            JsonObject item = element.getAsJsonObject();
            if (getString(item, conditionVal).equalsIgnoreCase(conditionParam)) {
                return getString(item, extractVal);
            }
        }
        return "";
    }

    public String getClientFromJsonArray(String responseBody, String extractVal, String conditionVal, String conditionParam, String conditionVal2, String conditionParam2, String entity, String entityVal,String createType) {
        JsonArray contentArray = getContentArray(responseBody);
        if (contentArray.isEmpty()) {
            return "";
        }
        String createdParam = "createdBy";
        for (JsonElement element : contentArray) {
            JsonObject firstItem = element.getAsJsonObject();
            boolean matchesPrimary = getString(firstItem, conditionVal).equalsIgnoreCase(conditionParam);
            boolean matchesSecondary = getString(firstItem, conditionVal2).equalsIgnoreCase(conditionParam2);
            boolean matchesEntity = getString(firstItem, entity).equalsIgnoreCase(entityVal);
            boolean matchesCreator = getString(firstItem, createdParam).equalsIgnoreCase(createType);
            if (matchesPrimary && matchesSecondary && matchesEntity && matchesCreator) {
                return getString(firstItem, extractVal);
            }
        }
        return "";
    }

    public void getAccountId(String token) {
        String endpoint = domain + "account-opening/init-customer/LEVEL_3_INDIVIDUAL";
        try (ApiClient apiClient = new ApiClient()) {
            APIResponse response = ensureSuccess(apiClient.post(endpoint, token, null), endpoint);
            System.out.println("uuid: " + parseJson(response.text(), "id"));
        }
    }

    public void getAoAccountDetail(String uuid, String token, String value) {
        String endpoint = domain + "account-opening/" + uuid;
        try (ApiClient apiClient = new ApiClient()) {
            APIResponse response = ensureSuccess(apiClient.get(endpoint, token), endpoint);
            System.out.println(value + ":" + parseJson(response.text(), value));
        }
    }

    public void getCmList(String token, String extractVal) {
        String endpoint = domain + "customer-management/page";
        try (ApiClient apiClient = new ApiClient()) {
            for (int pageNum = 0; pageNum < MAX_PAGE_LOOKUP; pageNum++) {
                String payload = String.format(
                        "{\"filter\":{\"clientType\":\"%s\",\"entity\":[\"%s\"],\"status\":\"%s\"},\"page\":%d,\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}",
                        nullToEmpty(clientType),
                        entity,
                        nullToEmpty(status),
                        pageNum);
                APIResponse response = ensureSuccess(apiClient.post(endpoint, token, payload), endpoint);
                String value = getValFromJsonArray(response.text(), extractVal);
                if (value != null && !value.isEmpty()) {
                    System.out.println("Matched customer-management value: " + value);
                    return;
                }
            }
        }
        throw new IllegalStateException("Unable to find value for " + extractVal + " within " + MAX_PAGE_LOOKUP + " pages.");
    }

    public String getAoList(String token, String extractVal) {
        String endpoint = domain + "account-opening/page";
        try (ApiClient apiClient = new ApiClient()) {
            for (int pageNum = 0; pageNum < MAX_PAGE_LOOKUP; pageNum++) {
                String payload = String.format("{\"filter\":{},\"page\":%d,\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}", pageNum);
                APIResponse response = ensureSuccess(apiClient.post(endpoint, token, payload), endpoint);
                String value = getValFromJsonArray(response.text(), extractVal);
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            }
        }
        throw new IllegalStateException("Unable to find value for " + extractVal + " within " + MAX_PAGE_LOOKUP + " pages.");
    }

    public String getAoListItem(String token, String extractVal, String conditionVal, String conditionParam) {
        String endpoint = domain + "account-opening/page";
        try (ApiClient apiClient = new ApiClient()) {
            for (int pageNum = 0; pageNum < MAX_PAGE_LOOKUP; pageNum++) {
                String payload = String.format("{\"filter\":{},\"page\":%d,\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}", pageNum);
                APIResponse response = ensureSuccess(apiClient.post(endpoint, token, payload), endpoint);
                String value = getValFromJsonArray(response.text(), extractVal, conditionVal, conditionParam);
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            }
        }
        throw new IllegalStateException("Unable to find value for " + extractVal + " within " + MAX_PAGE_LOOKUP + " pages.");
    }

    public String getAoClient(String token, String extractVal, String conditionVal, String conditionParam, String createType, String clientType ) throws IOException {
        String createdBy = createType.equalsIgnoreCase("app") ? "Customer" : "qaauto";
        String endpoint = domain + "account-opening/page";
        String entityCode = abs.userinfoList().get("entity");
        try (ApiClient apiClient = new ApiClient()) {
            for (int pageNum = 0; pageNum < MAX_PAGE_LOOKUP; pageNum++) {
                String payload = String.format("{\"filter\":{},\"page\":%d,\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}", pageNum);
                APIResponse response = ensureSuccess(apiClient.post(endpoint, token, payload), endpoint);
                String value = getClientFromJsonArray(response.text(), extractVal, conditionVal, conditionParam, "clientType", clientType, "entity", entityCode, createdBy);
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            }
        }
        throw new IllegalStateException("Unable to find client for " + extractVal + " within " + MAX_PAGE_LOOKUP + " pages.");
    }

    public void setParamVal(String param, String value) {
        switch (param) {
            case "clientType":
                clientType = value;
                break;
            case "status":
                status = value;
                break;
            default:
                break;
        }
    }

    public String getCrmDomain(String entity, String env) {
        if (env.equalsIgnoreCase("mt5uat")) {
            return switch (entity) {
                case "EBL_MT5" -> "https://uat-bcrm.empfs.net/";
                case "EIEHK" -> "https://uat-mhcrm.empfs.net/";
                case "XPro" -> "https://uat-mecrm.emxpro.com/";
                default -> "";
            };
        }
        return "";
    }

    public String getTradeGroupInfo(String extractVal, String token) {
        String endpoint = getCrmDomain(entity, env) + "admin-portal/api/user/referral-code/trading-group";
        Map<String, String> headers = new HashMap<>();
        headers.put("Key", "YXBpZ2F0ZXdheTpwYXNzd29yZA==");
        try (ApiClient apiClient = new ApiClient()) {
            APIResponse response = ensureSuccess(apiClient.post(endpoint, token, "{\"code\":\"\"}", headers), endpoint);
            return parseJson(response.text(), extractVal);
        }
    }

    public String getTradeGroupInfoBasedOnEntity(String extractVal, String token, String entity) {
        String endpoint = getCrmDomain(entity, env) + "admin-portal/api/user/referral-code/trading-group";
        Map<String, String> headers = new HashMap<>();
        headers.put("Key", "YXBpZ2F0ZXdheTpwYXNzd29yZA==");
        String payload = "{\"code\":\"" + abs.setIBCode(entity) + "\"}";
        try (ApiClient apiClient = new ApiClient()) {
            APIResponse response = ensureSuccess(apiClient.post(endpoint, token, payload, headers), endpoint);
            return parseJson(response.text(), extractVal);
        }
    }


}

