package utils;

import java.util.HashMap;
import java.util.Map;

public class CaseMapping {

    private static final Map<String, String> CASE_MAP = new HashMap<>();

    static {
        // Map Cucumber scenario names or tags to Qase case IDs
        CASE_MAP.put("Test Scenario 1", "1");
        CASE_MAP.put("Test Scenario 2", "2");
        CASE_MAP.put("@Smoke", "3");
    }

    public static String getCaseId(String scenarioNameOrTag) {
        return CASE_MAP.getOrDefault(scenarioNameOrTag, "0"); // Default to "0" if not found
    }
}