package Data;

public class AppCredential {

    private final String entity;

    public AppCredential(String entity) {
        this.entity = entity;
    }

    public String getLoginCredential() {
        return switch (entity) {
            case "EBL_MT5" -> "autol3";
            case "EIEHK" -> "eieuat004@yopmail.com";
            default -> throw new IllegalArgumentException("Invalid entity: " + entity);
        };
    }

    public String getLoginPassword() {
        return switch (entity) {
            case "EBL_MT5" -> "Test1234@";
            case "EIEHK" -> "Test1234@";
            default -> throw new IllegalArgumentException("Invalid entity: " + entity);
        };
    }

    public String getL2LoginCredential() {
        return switch (entity) {
            case "EBL_MT5" -> "autol2";
            case "EIEHK" -> "eieautol2@yopmail.com";
            default -> throw new IllegalArgumentException("Invalid entity: " + entity);
        };
    }
}
