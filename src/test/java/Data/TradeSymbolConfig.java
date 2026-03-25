package Data;

public class TradeSymbolConfig {

    public String getDecimalPlace(String symbol){
       return switch (symbol) {
            case "XAUUSD" -> "2";
            case "XAGUSD" -> "3";
            case "RKGCNH" -> "2";
            case "HKGHKD" -> "0";
            default -> "";
        };
    }

//    public String getMinLotSize(String symbol){
//
//    }
}
