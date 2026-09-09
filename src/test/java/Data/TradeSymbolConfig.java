package Data;

public class TradeSymbolConfig {

    public static boolean isInitialMarginZero = false;

    public String getDecimalPlace(String symbol) {
        return switch (symbol) {
            case "XAUUSD" -> "2";
            case "XAGUSD" -> "3";
            case "RKGCNH" -> "2";
            case "HKGHKD" -> "0";
            default -> "";
        };
    }

    public Double getMinLotSize(String symbol) {
        return switch (symbol) {
            case "XAUUSD" -> 0.05;
            case "XAGUSD" -> 0.05;
            case "RKGCNH" -> 0.05;
            case "HKGHKD" -> 0.05;
            default -> 0.0;
        };
    }

    public Double getMaxLotSize(String symbol) {
        return switch (symbol) {
            case "XAUUSD" -> 5.00;
            case "XAGUSD" -> 5.00;
            case "RKGCNH" -> 5.00;
            case "HKGHKD" -> 5.00;
            default -> 0.0;
        };
    }

    public Integer getInitialMargin(String symbol) {
        if (isInitialMarginZero) return 0;
        else {
            return switch (symbol) {
                case "XAUUSD" -> 3000;
                case "XAGUSD" -> 10000;
                case "RKGCNH" -> 1300;
                case "HKGHKD" -> 4000;
                default -> 0;
            };
        }
    }

    public String getStepSize() {
        return "0.05";
    }

    public Integer getContractSize(String symbol){
        return switch (symbol) {
            case "XAUUSD" -> 100;
            case "XAGUSD" -> 5000;
            case "RKGCNH" -> 1000;
            case "HKGHKD" -> 100;
            default -> 0;
        };
    }

}
