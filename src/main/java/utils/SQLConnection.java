package utils;

public class SQLConnection {

    private String env;

    public SQLConnection(String env) {
        this.env = env;
    }

    public String getUrl() {
        return switch (env) {
            case "bauuat" ->
                    "jdbc:mysql://cm-bau-uat-db.cg8qhq5ec18a.ap-southeast-1.rds.amazonaws.com:3306/cm?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            case "mt5uat" ->
                    "jdbc:mysql://cm-bau-uat-db.cg8qhq5ec18a.ap-southeast-1.rds.amazonaws.com:3306/cm?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            default -> "";
        };
    }

    public String getUsername() {
        return switch (env) {
            case "bauuat" -> "cm_readonly";
            case "mt5uat" -> "cm_readonly";
            default -> "";
        };
    }

    public String getPassword() {
        return switch (env) {
            case "bauuat" -> "WI4R3n)caXi:?B><";
            case "mt5uat" -> "WI4R3n)caXi:?B><";
            default -> "";
        };
    }
}
