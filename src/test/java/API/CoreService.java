package API;

import AbstractComponent.AbstractComponentsPW;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.io.IOException;

public class CoreService {

    private final AbstractComponentsPW abs;
    private final String entity;
    public final String env;
    public static String clientType;
    public static String status;
    private final String domain;

    public CoreService(Page page, String productEnv) throws IOException {
        abs = new AbstractComponentsPW(page);
        entity = abs.userinfoList().get("entity");
        env = productEnv;
        this.domain = abs.getApiEndpointDomain(env);
    }

    public void getAccountStatus(String token) {
        String id = "46be06ef-ab11-4d3b-978b-49f1ce80265e";
        String authToken = "Bearer " + token;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.get(domain + "account-opening/" + id + "/status",
                RequestOptions.create().setHeader("Authorization", authToken));
        System.out.println(parseJson(response.text(), "status"));
    }

    public String parseJson(String responseBody, String value) {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = json.getAsJsonObject("response");
        return responseObj.get(value).getAsString();
    }

    public String getValFromJsonArray(String responseBody, String value) {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = json.getAsJsonObject("response");
        JsonArray contentArray = responseObj.getAsJsonArray("content");
        JsonObject firstItem = contentArray.get(0).getAsJsonObject();
        return firstItem.get(value).getAsString();
    }

    public String getValFromJsonArray(String responseBody, String extractVal, String conditionVal, String conditionParam) {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = json.getAsJsonObject("response");
        JsonArray contentArray = responseObj.getAsJsonArray("content");
        String retrieveValue = "";
        try {
            for (int i = 0; i < contentArray.size(); i++) {
                JsonObject firstItem = contentArray.get(i).getAsJsonObject();
                if (firstItem.get(conditionVal).getAsString().equalsIgnoreCase(conditionParam)) {
                    retrieveValue = firstItem.get(extractVal).getAsString();
                    return retrieveValue;
                }
            }
        } catch (Exception e) {
            System.err.println("Matched Record not found :" + e.getMessage());
        }
        return retrieveValue;
    }

    public String getAppClientFromJsonArray(String responseBody, String extractVal, String conditionVal, String conditionParam, String conditionVal2, String conditionParam2, String entity, String entityVal) {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = json.getAsJsonObject("response");
        JsonArray contentArray = responseObj.getAsJsonArray("content");
        String retrieveValue = "";
        try {
            for (int i = 0; i < contentArray.size(); i++) {
                JsonObject firstItem = contentArray.get(i).getAsJsonObject();
                if (firstItem.get(conditionVal).getAsString().equalsIgnoreCase(conditionParam) &&
                        firstItem.get(conditionVal2).getAsString().equalsIgnoreCase(conditionParam2) &&
                        firstItem.get(entity).getAsString().equalsIgnoreCase(entityVal)) {
                    retrieveValue = firstItem.get(extractVal).getAsString();
                    return retrieveValue;
                }
            }
        } catch (Exception e) {
            System.err.println("Matched Record not found :" + e.getMessage());
        }
        return retrieveValue;
    }

    public void getAccountId(String token) {
        String authToken = "Bearer " + token;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.post(domain + "account-opening/init-customer/LEVEL_3_INDIVIDUAL",
                RequestOptions.create().setHeader("Authorization", authToken));
        System.out.println("uuid: " + parseJson(response.text(), "id"));

    }

    public void getAoAccountDetail(String uuid, String token, String value) {
        String authToken = "Bearer " + token;
        String endPoint = domain + "account-opening/" + uuid;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.get(endPoint,
                RequestOptions.create().setHeader("Authorization", authToken));
        System.out.println(value + ":" + parseJson(response.text(), value));

    }

    public void getCmList(String token, String extractVal) {
        int pageNum = 0;
        String jsonBody;
        String endPoint = domain + "customer-management/page";
        String authToken = "Bearer " + token;
        String value;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response;
        do {
            jsonBody = "{\"filter\":{\"clientType\": \"" + clientType + "\",\"entity\":[\"" + entity + "\"],\"status\": \"" + status + "\"},\"page\":" + pageNum + ",\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}";
            System.out.println(jsonBody);
            response = request.post(
                    endPoint,
                    RequestOptions.create()
                            .setHeader("Authorization", authToken)
                            .setHeader("Content-Type", "application/json")
                            .setData(jsonBody)
            );
            value = getValFromJsonArray(response.text(), extractVal);
            if (value == null) {
                pageNum++;
            }
        } while (value == null);
    }

    public String getAoList(String token, String extractVal) {
        int pageNum = 0;
        String jsonBody;
        String endPoint = domain + "account-opening/page";
        String authToken = "Bearer " + token;
        String value;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response;
        do {
            jsonBody = "{\"filter\":{},\"page\":" + pageNum + ",\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}";
            // System.out.println(jsonBody);
            response = request.post(
                    endPoint,
                    RequestOptions.create()
                            .setHeader("Authorization", authToken)
                            .setHeader("Content-Type", "application/json")
                            .setData(jsonBody)
            );
            System.out.println(response.text());
            value = getValFromJsonArray(response.text(), extractVal);
            if (value == null) {
                pageNum++;
            }
        } while (value == null);
        return value;
    }

    public String getAoListItem(String token, String extractVal, String conditionVal, String conditionParam) {
        int pageNum = 0;
        String jsonBody;
        String endPoint = domain + "account-opening/page";
        String authToken = "Bearer " + token;
        String value;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response;
        do {
            jsonBody = "{\"filter\":{},\"page\":" + pageNum + ",\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}";
            // System.out.println(jsonBody);
            response = request.post(
                    endPoint,
                    RequestOptions.create()
                            .setHeader("Authorization", authToken)
                            .setHeader("Content-Type", "application/json")
                            .setData(jsonBody)
            );
            value = getValFromJsonArray(response.text(), extractVal, conditionVal, conditionParam);
            if (value.isEmpty()) {
                pageNum++;
            }
        } while (value.isEmpty());
        return value;
    }

    public String getAoAppClient(String token, String extractVal, String conditionVal, String conditionParam, String createType) throws IOException {
        int pageNum = 0;
        String appParam = "createdBy";
        String createdBy = createType.equalsIgnoreCase("app") ? "Customer" : "Admin";
        String jsonBody;
        String endPoint = domain + "account-opening/page";
        String authToken = "Bearer " + token;
        String value;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response;
        do {
            jsonBody = "{\"filter\":{},\"page\":" + pageNum + ",\"size\":10,\"sort\":[{\"by\":\"createdDate\",\"asc\":false}]}";
            // System.out.println(jsonBody);
            response = request.post(
                    endPoint,
                    RequestOptions.create()
                            .setHeader("Authorization", authToken)
                            .setHeader("Content-Type", "application/json")
                            .setData(jsonBody)
            );
            value = getAppClientFromJsonArray(response.text(), extractVal, conditionVal, conditionParam, appParam, createdBy, "entity", abs.userinfoList().get("entity"));
            if (value.isEmpty()) {
                pageNum++;
            }
        } while (value.isEmpty());
        return value;
    }

    public void setParamVal(String param, String value) {
        switch (param) {
            case "clientType":
                clientType = value;
            case "status":
                status = value;
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
        return null;
    }

    public String getTradeGroupInfo(String extractVal, String token) {
        String endPoint = getCrmDomain(entity, env) + "admin-portal/api/user/referral-code/trading-group";
        String authToken = "Bearer " + token;
        String jsonBody;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response;
        jsonBody = "{\"code\":\"\"}";
        // System.out.println(jsonBody);
        response = request.post(
                endPoint,
                RequestOptions.create()
                        .setHeader("Authorization", authToken)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Key", "YXBpZ2F0ZXdheTpwYXNzd29yZA==")
                        .setData(jsonBody)
        );
        System.out.println(response.text());
        return parseJson(response.text(), extractVal);
    }

}
