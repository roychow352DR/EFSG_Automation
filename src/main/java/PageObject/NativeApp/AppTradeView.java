package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class AppTradeView {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static String stopLossPrice;
    public static String stopOrderPrice;
    public static String stopOrderType;
    public static String takeProfitPrice;
    public static String selectedDirection;
    public static String lotSize;
    public static String executedPrice;
    public static String validity;
    public static String editPrice;


    public AppTradeView(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"BUY\"]/parent::android.view.ViewGroup")
    WebElement buyButtonAos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"SELL\"]/parent::android.view.ViewGroup")
    WebElement sellButtonAos;


    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> rowsOnPositionTabAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> rowsOnPendingOrdersTabAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
    WebElement detailsButton;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement crossButtonAos;


    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]")
    WebElement cancelOrderButtonAos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Close Position\"])[2]/parent::android.view.ViewGroup")
    WebElement closePositionAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[11]")
    WebElement confirmClosePositionBtnAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.widget.TextView")
    WebElement dialogueTextAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    WebElement editPositionButtonAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]" +
            "/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/" +
            "android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup[2]")
    WebElement closePositionBtnInDetailsAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout" +
            "/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup" +
            "/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    WebElement closePositionBtnWithLotsAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout" +
            "/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup" +
            "/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement backBtnAos;


    public void selectDirection(String direction) {
        selectedDirection = direction;
        if (driver instanceof AndroidDriver) {
            switch (direction) {
                case "BUY" -> buyButtonAos.click();
                case "SELL" -> sellButtonAos.click();
            }
        }
    }


    public String getStopLossPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        switch (selectedDirection) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
            }
        }
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        stopLossPrice = formattedPrice;
        return formattedPrice;
    }

    public String getTakeProfitPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        switch (selectedDirection) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
            }
        }
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        takeProfitPrice = formattedPrice;
        return formattedPrice;
    }

    public String getStopOrderPrice(String direction, String stopOrderType) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        if (stopOrderType.contains("Stop")) {
            switch (selectedDirection) {
                case "BUY" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≥\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
                }
                case "SELL" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≤\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
                }
            }
        } else if (stopOrderType.contains("Limit")) {
            switch (selectedDirection) {
                case "BUY" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≤\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
                }
                case "SELL" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≥\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
                }
            }
        }
        stopOrderPrice = price;
        return price;
    }

    public void tapsButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Cancel Order")) {
                WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup"));
                abs.waitUtilElementFind(button);
                button.click();
            } else {
                driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
            }
        }
    }

    public void tapsButtonOnConfirm(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Position") || buttonName.contains("Modify") || buttonName.contains("Cancel Order")) {
                WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup"));
                abs.waitUtilElementFind(button);
                button.click();
            } else {
                driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
            }
        }
    }

//    public String getDetailValue(String value) {
//        List<WebElement> text = driver.findElements(By.className("android.widget.TextView"));
//        abs.waitUtilAllElementFind(text);
//        for (int i = 0; i < text.size(); i++) {
//            if (text.get(i).getText().equalsIgnoreCase(value)) {
//                if (value.equalsIgnoreCase("Volume")) {
//                    return text.get(i + 1).getText().split("Lots")[0].trim();
//                }
//                return text.get(i + 1).getText();
//            }
//        }
//        return null;
//    }

    public String getDetailValue(String value) {
        By locator = By.className("android.widget.TextView");
        for (int attempt = 1; attempt <= 5; attempt++) {
            try {
                List<WebElement> text = driver.findElements(locator);
                for (int i = 0; i < text.size(); i++) {
                    if (text.get(i).getText().equalsIgnoreCase(value)) {
                        if (value.equalsIgnoreCase("Volume")) {
                            return text.get(i + 1).getText().split("Lots")[0].trim();
                        }
                        return text.get(i + 1).getText();
                    }
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale elements while reading texts, retrying...");
            }
        }
        throw new RuntimeException("Unable to get texts from TextViews");

    }

    public String getPositionValue(String value, String symbolDecimal) {
        List<WebElement> elements = driver.findElements(By.className("android.widget.TextView"));
        List<String> texts = new ArrayList<>(elements.size());
        abs.waitUtilElementFind(elements.getFirst());
        for (WebElement el : elements) {
            texts.add(el.getText());
        }

        for (int i = 0; i < texts.size(); i++) {
            if (texts.get(i).equalsIgnoreCase(value)) {
                if (value.equalsIgnoreCase("Volume")) {
                    return texts.get(i + 1).split("Lots")[0].trim();
                }

                if (value.equalsIgnoreCase("Take Profit Price") || value.equalsIgnoreCase("Stop Loss Price")) {
                    return abs.normalizePriceToDecimals(texts.get(i + 1), symbolDecimal);
                }
                return texts.get(i + 1);
            }
        }
        return null;
    }

    public String getPositionValueWithRetry(String value, String symbolDecimal) {
        int attempts = 3;
        while (attempts-- > 0) {
            try {
                return getPositionValue(value, symbolDecimal); // the snapshot version above
            } catch (StaleElementReferenceException e) {
                if (attempts == 0) throw e;
                // small pause or just retry
            }
        }
        return null;
    }

    public String getValidationValue(String label) {
        return switch (label) {
//            case "Stop Loss Price" -> String.format("%.2f", Double.parseDouble(stopLossPrice));
//            case "Take Profit Price" -> String.format("%.2f", Double.parseDouble(takeProfitPrice));
            case "Stop Loss Price" -> stopLossPrice;
            case "Take Profit Price" -> takeProfitPrice;
            case "Direction" -> selectedDirection;
            case "Volume" -> lotSize;
            case "Stop Order Price" -> stopOrderPrice;
            case "Validity" -> validity;
            default -> null;
        };
    }

    public String getLotSize() {
        // return driver.findElement(By.className("android.widget.EditText")).getText();
        return driver.findElements(By.className("android.widget.EditText")).getFirst().getText();
    }

    public void getDirection() {
        List<WebElement> direction = driver.findElements(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup" +
                "/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/" +
                "android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup"));
        for (WebElement element : direction) {
            if (element.isSelected()) {
            }
        }
    }

    public boolean getPositionDetail(String positionDetail) {
        for (WebElement element : rowsOnPositionTabAos) {
            if (element.getText().equalsIgnoreCase(positionDetail)) {
                return true;
            }
        }

        return false;
    }

    public void getExecutedPrice() {
        executedPrice = getDetailValue("Price");
    }

    public void tapCtaButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "detail" -> detailsButton.click();
                case "close" -> {
                    abs.waitUtilElementFind(crossButtonAos);
                    crossButtonAos.click();
                }
                case "edit" -> editPositionButtonAos.click();
            }

        }
    }

    //        public void selectOrderType(String orderType) {
//        if (driver instanceof AndroidDriver) {
//            orderTypeDropdownBtn.click();
//            for (WebElement element : orderTypeOptions) {
//                if (element.getText().equalsIgnoreCase(orderType)) {
//                    element.click();
//                    break;
//                }
//            }
//        }
//    }

    public boolean getPendingOrdersDetail(String pendingOrderDetail) {
        for (WebElement element : rowsOnPendingOrdersTabAos) {
            if (element.getText().equalsIgnoreCase(pendingOrderDetail)) {
                return true;
            }
        }

        return false;
    }

    public void cancelOrder() {
        if (driver instanceof AndroidDriver) {
            crossButtonAos.click();
            abs.waitUtilElementFind(cancelOrderButtonAos);
            cancelOrderButtonAos.click();
        }
    }

    public void closePosition() {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementClickable(crossButtonAos);
            crossButtonAos.click();
            abs.waitUtilElementFind(closePositionAos);
            closePositionAos.click();
            confirmClosePositionBtnAos.click();
        }
    }

    public void closePositionInDetails() {
        if (driver instanceof AndroidDriver) {
            closePositionBtnInDetailsAos.click();
            closePositionBtnWithLotsAos.click();
            confirmClosePositionBtnAos.click();
        }
    }

    public List<String> stopOrderConfirmationPageValues() {
        List<String> values = new ArrayList<>();
        values.add("Stop Loss Price");
        values.add("Take Profit Price");
        values.add("Direction");
        values.add("Volume");
        values.add("Validity");
        return values;
    }

    public List<String> marketOrderConfirmationPageValues() {
        List<String> values = new ArrayList<>();
        if (!(stopLossPrice == null)) {
            values.add("Stop Loss Price");
        }
        if (!(takeProfitPrice == null)) {
            values.add("Take Profit Price");
        }
        values.add("Direction");
        values.add("Volume");
        return values;
    }

    public String getDialogueTextAos() {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(dialogueTextAos);
            return dialogueTextAos.getText();
        }
        return "";
    }

    public void cancelPendingOrderInDetail() throws InterruptedException {
        tapsButton("Cancel Order");
        Thread.sleep(500);
        tapsButtonOnConfirm("Cancel Order");
    }

    public boolean getPendingOrder() {
        if (driver instanceof AndroidDriver) {
            if (rowsOnPendingOrdersTabAos == null || rowsOnPendingOrdersTabAos.isEmpty()) {
                return false;
            }
            return rowsOnPendingOrdersTabAos.get(0).isDisplayed();
        }
        return false;
    }

    public boolean getPosition() {
        if (driver instanceof AndroidDriver) {
            if (rowsOnPositionTabAos == null || rowsOnPositionTabAos.isEmpty()) {
                return false;
            }
            return rowsOnPositionTabAos.get(0).isDisplayed();
        }
        return false;
    }

    public void setLotSize(String symbolLotSize) {
        lotSize = symbolLotSize;
    }

    public void tapBack() {
        if (driver instanceof AndroidDriver) {
            backBtnAos.click();
        }
    }
}
