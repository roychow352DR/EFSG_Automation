package Data;

import utils.BaseTest;

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
            case "XAUUSD" -> 0.01;
            case "XAGUSD" -> 0.01;
            case "RKGCNH" -> 0.01;
            case "HKGHKD" -> 0.01;
            default -> 0.0;
        };
    }

    public Double getMaxLotSize(String symbol,String entity) {
        if (entity.equalsIgnoreCase("EBL_MT5")) {
            return switch (symbol) {
                case "XAUUSD" -> 5.00;
                case "XAGUSD" -> 5.00;
                case "RKGCNH" -> 5.00;
                case "HKGHKD" -> 5.00;
                default -> 0.0;
            };
        }
        else if (entity.equalsIgnoreCase("EIEHK")){
            return switch (symbol) {
                case "XAUUSD" -> 10.00;
                case "XAGUSD" -> 10.00;
                case "RKGCNH" -> 10.00;
                case "HKGHKD" -> 10.00;
                default -> 0.0;
            };
        }
        return 0.0;
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
        return getStepSize(BaseTest.productEntity);
    }

    public String getStepSize(String entity) {
        // EIEHK lot stepper moves 0.01. EBL_MT5 moves 0.05.
        if (entity != null && entity.equalsIgnoreCase("EIEHK")) {
            return "0.01";
        }
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

    public String getDefaultLotSize(String entity)
    {
        return switch (entity) {
            case "EBL_UAT" -> "1.0";
            default -> "0.10";
        };
    }

}
