package utils;

public class SetCondition {

    public static boolean isExistedEmail;
    public static boolean isExistedPhoneNumber;
    public static boolean isBelow18;
    public static boolean isExpired ;
    public static boolean isEdd;
    public static boolean isExpiredBeforeCurrent;
    public static boolean isCrossEntity;

    public SetCondition(boolean isExistedEmail, boolean isExistedPhoneNumber, boolean isBelow18, boolean isExpired, boolean isEdd, boolean isExpiredBeforeCurrent, boolean isCrossEntity) {
        SetCondition.isExistedEmail = isExistedEmail;
        SetCondition.isExistedPhoneNumber = isExistedPhoneNumber;
        SetCondition.isBelow18 = isBelow18;
        SetCondition.isExpired = isExpired;
        SetCondition.isEdd = isEdd;
        SetCondition.isExpiredBeforeCurrent = isExpiredBeforeCurrent;
        SetCondition.isCrossEntity = isCrossEntity;
    }

    public static boolean isExistedEmail() {
        return isExistedEmail;
    }

    public static boolean isExistedPhoneNumber() {
        return isExistedPhoneNumber;
    }

    public static boolean isBelow18() {
        return isBelow18;
    }

    public static boolean isExpired() {
        return isExpired;
    }

    public static boolean isEdd() {
        return isEdd;
    }

    public static boolean isExpiredBeforeCurrent() {
        return isExpiredBeforeCurrent;
    }
    public static boolean isCrossEntity() {
        return isCrossEntity;
    }
}
