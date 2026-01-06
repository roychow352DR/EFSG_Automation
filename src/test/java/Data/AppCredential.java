package Data;

public class AppCredential {

    private final String entity;

    public AppCredential(String entity) {
        this.entity = entity;
    }

    public String getLoginCredential() {
        return switch (entity) {
            case "EBL_MT5" -> "rc60";
            case "EIEK" -> "eieuat564@yopmail.com";
            default -> throw new IllegalArgumentException("Invalid entity: " + entity);
        };
    }

    public String getLoginPassword() {
        return switch (entity) {
            case "EBL_MT5" -> "Test1234@";
            case "EIEK" -> "Test1234@";
            default -> throw new IllegalArgumentException("Invalid entity: " + entity);
        };
    }
}
