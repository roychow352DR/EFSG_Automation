package API;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class CoreService {

    public String domain = "https://zmtezs56l2.execute-api.ap-southeast-1.amazonaws.com/uat/core-service/";

    public void getAccountStatus(String token)
    {
        String domain = "https://zmtezs56l2.execute-api.ap-southeast-1.amazonaws.com/uat/core-service/";
        String id = "46be06ef-ab11-4d3b-978b-49f1ce80265e";
        String authToken = "Bearer " + token;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.get(domain + "account-opening/"+ id + "/status",
                RequestOptions.create().setHeader("Authorization",authToken));
        System.out.println(parseJson(response.text(),"status"));
    }

    public String parseJson(String responseBody, String value)
    {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        JsonObject responseObj = json.getAsJsonObject("response");
        return responseObj.get(value).getAsString();
    }

    public void getAccountId(String token)
    {
        String domain = "https://zmtezs56l2.execute-api.ap-southeast-1.amazonaws.com/uat/core-service/";
        String authToken = "Bearer " + token;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.post(domain + "account-opening/init-customer/LEVEL_3_INDIVIDUAL",
                RequestOptions.create().setHeader("Authorization",authToken));
        System.out.println("uuid: " + parseJson(response.text(),"id"));

    }

    public void getAoAccountDetail(String uuid,String token,String value){
        String authToken = "Bearer " + token;
        String endPoint = domain + "account-opening/" + uuid;
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.get(endPoint,
                RequestOptions.create().setHeader("Authorization",authToken));
        System.out.println( value + ":" + parseJson(response.text(),value));

    }
}
