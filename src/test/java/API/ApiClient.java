package API;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.util.Map;
import java.util.Objects;

/**
 * Lightweight Playwright API client wrapper that manages request context lifecycle
 * and simplifies authenticated GET/POST interactions.
 */
public class ApiClient implements AutoCloseable {

    private final Playwright playwright;
    private final APIRequestContext requestContext;

    public ApiClient() {
        this.playwright = Playwright.create();
        this.requestContext = playwright.request().newContext();
    }

    public APIResponse get(String url, String token) {
        return requestContext.get(url, defaultOptions(token));
    }

    public APIResponse post(String url, String token, String body) {
        return post(url, token, body, null);
    }

    public APIResponse post(String url, String token, String body, Map<String, String> extraHeaders) {
        RequestOptions options = defaultOptions(token);
        boolean hasCustomContentType = extraHeaders != null && extraHeaders.keySet().stream()
                .anyMatch(key -> "Content-Type".equalsIgnoreCase(key));
        if (!hasCustomContentType) {
            options.setHeader("Content-Type", "application/json");
        }
        if (extraHeaders != null) {
            extraHeaders.forEach(options::setHeader);
        }
        if (body != null && !body.isEmpty()) {
            options.setData(body);
        }
        return requestContext.post(url, options);
    }

    private RequestOptions defaultOptions(String token) {
        return RequestOptions.create().setHeader("Authorization", formatToken(token));
    }

    private String formatToken(String token) {
        String trimmed = Objects.requireNonNull(token, "token cannot be null").trim();
        return trimmed.startsWith("Bearer ") ? trimmed : "Bearer " + trimmed;
    }

    @Override
    public void close() {
        requestContext.dispose(); //tears down the HTTP request context.
        playwright.close();
    }
}




