package Data;

import utils.BaseTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GlobalConfig extends BaseTest {

    public static String getGlobalPropertyPath(String property)
    {
        return switch (property){
            case "filePropertyPath" -> "//src//main//java//DataResources//FileDirectory.properties";
            case "globalPropertyPath" -> "//src//main//java//DataResources//GlobalData.properties";
            default -> "";
        };
    }

    public Map<String, String> getDirectory() throws IOException {
        Map<String,String> globalDirectory = new HashMap<>();
        globalDirectory.put("VIDEO_DIRECTORY",getProperty(getGlobalPropertyPath("filePropertyPath"), "video_directory"));
        globalDirectory.put("SCREENSHOT_DIRECTORY",System.getProperty("user.dir") + "/screenshots/");
        globalDirectory.put("APP_VIDEO_DIRECTORY",System.getProperty("user.dir") + "/app_Video/");
        return globalDirectory;
    }


}
