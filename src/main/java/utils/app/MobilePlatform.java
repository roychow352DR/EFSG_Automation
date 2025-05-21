package utils.app;

import java.io.IOException;

import static utils.BaseTest.getProperty;
import static utils.BaseTest.getPropertyPath;

enum Platform {
    ANDROID,
    IOS;
}
public class MobilePlatform {
        public String getPlatform() throws IOException {

            String appPlatform = System.getProperty("platform") != null ? System.getProperty("platform") : getProperty(getPropertyPath("app"), "platform");
            return switch (Platform.valueOf(appPlatform)) {
                case ANDROID -> "ANDROID";
                case IOS -> "IOS";
            };
        }

}
