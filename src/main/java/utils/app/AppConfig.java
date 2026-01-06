package utils.app;

public class AppConfig {

    private final String entity;
    private final String env;

    public AppConfig(String entity, String env) {

        this.entity = entity;
        this.env = env;
    }

    public String getAndroidAppPath() {
        return switch (entity) {
            case "EBL_MT5" -> switch (env) {
                case "mt5uat" ->
                        System.getProperty("user.dir") + "/src/main/resources/com.emperorfs.ebltrading.android_uat-0.0.301-0102.apk";
                case "bauuat" ->
                        System.getProperty("user.dir") + "/src/main/resources/com.emperorfs.ebltrading.android_uat-0.0.301-1222.apk";
                default -> "";
            };
            case "EIEHK" -> switch (env) {
                case "mt5uat" ->
                        System.getProperty("user.dir") + "/src/main/resources/com.efsg.eiehktrading.android_uat-0.0.214-0805.apk";
                case "bauuat" ->
                        System.getProperty("user.dir") + "/src/main/resources/com.efsg.eiehktrading.android_uat-0.0.214-0805.apk";
                default -> "";
            };
            default -> throw new IllegalArgumentException("Invalid app path");
        };
    }

    public String getAndroidPackage() {
        return switch (entity) {
            case "EBL_MT5" -> switch (env) {
                case "mt5uat" -> "com.emperorfs.ebltrading.android";
                case "bauuat" -> "com.emperorfs.ebltrading.android";
                default -> "";
            };
            case "EIEHK" -> switch (env) {
                case "mt5uat" -> "com.efsg.eiehktrading.android_uat";
                case "bauuat" -> "com.efsg.eiehktrading.android_uat";
                default -> "";
            };
            default -> throw new IllegalArgumentException("Invalid app package");
        };
        }


}
