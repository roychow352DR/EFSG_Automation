package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
    public static String openPositionOpenPrice;
    public static String openOrderTargetPrice;
    public static String validity;
    public static String editPrice;
    public static int positionsCount = 0;
    public static int pendingOrdersCount = 0;


    public AppTradeView(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"BUY\"]/parent::android.view.ViewGroup")
    WebElement buyButtonAos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"SELL\"]/parent::android.view.ViewGroup")
    WebElement sellButtonAos;


    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
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
    List<WebElement> dialogueTextAos;

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

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[15]")
    WebElement closeDialogueBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> openPositionRecordDetailsAos;

    @FindBy(xpath = "//android.widget.HorizontalScrollView/android.view.ViewGroup/android.view.View[2]")
    WebElement positionsTabAos;



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
                abs.waitUntilElementFind(button);
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
                abs.waitUntilElementFind(button);
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.ignoring(StaleElementReferenceException.class);

        try {
            return wait.until(d -> {
                List<WebElement> elements = d.findElements(By.className("android.widget.TextView"));
                List<String> texts = new ArrayList<>();

                for (WebElement element : elements) {
                    String text = element.getText();
                    if (text != null && !text.trim().isEmpty()) {
                        texts.add(text.trim());
                    }
                }

                for (int i = 0; i < texts.size(); i++) {
                    String currentLabel = texts.get(i);

                    if (currentLabel != null && currentLabel.equalsIgnoreCase(value)) {
                        for (int j = i + 1; j < Math.min(i + 4, texts.size()); j++) {
                            String candidate = texts.get(j);

                            if (candidate == null || candidate.isBlank()) {
                                continue;
                            }

                            if (candidate.equalsIgnoreCase(value)) {
                                continue;
                            }

                            if (value.equalsIgnoreCase("Take Profit Price") || value.equalsIgnoreCase("Stop Loss Price")) {
                                return abs.normalizePriceToDecimals(candidate, symbolDecimal);
                            }

                            return abs.normalizeDialogueValue(value, candidate);
                        }
                    }
                }

                return null;
            });
        } catch (TimeoutException e) {
            return null;
        }
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
                case "detail" -> {
                    abs.waitUntilElementClickable(detailsButton);
                    detailsButton.click();
                }
                case "close" -> {
                    abs.waitUntilElementClickable(crossButtonAos);
                    crossButtonAos.click();
                }
                case "edit" -> {
                    abs.waitUntilElementClickable(editPositionButtonAos);
                    editPositionButtonAos.click();
                }
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
            if (AppSettingPage.isTradeConfirmNeeded) {
                abs.waitUntilElementFind(cancelOrderButtonAos);
                cancelOrderButtonAos.click();
            }
        }
    }

    public void closePosition() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementClickable(crossButtonAos);
            crossButtonAos.click();
            abs.waitUntilElementClickable(closePositionAos);
            closePositionAos.click();
            if (AppSettingPage.isTradeConfirmNeeded) {
                confirmClosePositionBtnAos.click();
            }
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
        String dialogText = "";
        if (driver instanceof AndroidDriver) {
            dialogText = abs.captureTransientText(
                    () -> dialogueTextAos,
                    Duration.ofSeconds(5)
            );
        }
        return dialogText;
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
            return rowsOnPendingOrdersTabAos.getFirst().isDisplayed();
        }
        return false;
    }

    public boolean getPosition() {
        if (driver instanceof AndroidDriver) {
            if (rowsOnPositionTabAos == null || rowsOnPositionTabAos.isEmpty()) {
                return false;
            }
            return rowsOnPositionTabAos.getFirst().isDisplayed();
        }
        return false;
    }

    public void setLotSize(String symbolLotSize) {
        lotSize = symbolLotSize;
    }

    public void tapBack() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementClickable(backBtnAos);
            backBtnAos.click();
        }
    }

    public void getOpenPositionOpenPrice() {
        By openPriceLocator = By.xpath(
                "(//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]" +
                        "/android.view.ViewGroup/android.view.ViewGroup[2]/parent::android.view.ViewGroup" +
                        "/android.widget.TextView)[3]"
        );
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        openPositionOpenPrice = wait.until(driver -> {
            WebElement element = driver.findElement(openPriceLocator);
            String text = element.getText();
            return !text.trim().isEmpty() ? text : null;
        });
    }

    public void getPendingOrderTargetPrice() {
        By targetPriceLocator = By.xpath("(//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]" +
                "/parent::android.view.ViewGroup/android.widget.TextView)[4]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        openOrderTargetPrice = wait.until(driver -> {
            WebElement element = driver.findElement(targetPriceLocator);
            String text = element.getText();
            return !text.trim().isEmpty() ? text : null;
        });
    }

    public void closeDialogue() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(closeDialogueBtnAos);
            closeDialogueBtnAos.click();
        }
    }

    public String getOpenPositionTime() {
        if (driver instanceof AndroidDriver) {
            return openPositionRecordDetailsAos.get(3).getText();
        }
        return null;
    }

    public boolean isOpenPositionDateValid() {
        return abs.dateValidator(getOpenPositionTime());
    }

    public void selectTab(String tabName){
        if (driver instanceof AndroidDriver) {
            switch (tabName) {
                case "Positions" -> {
                    abs.waitUntilElementClickable(positionsTabAos);
                    positionsTabAos.click();
                }
            }
        }
    }

    public int getNumberOfPositions(){
        By rowsOnPositionTab = By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
        );
        if (driver instanceof AndroidDriver) {
//            abs.waitUtilAllElementFind(rowsOnPositionTabAos);
//            return rowsOnPositionTabAos.size();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
            final int[] previous = {-1};
            final int[] stable = {0};

            return wait.until(d -> {
                int current = d.findElements(rowsOnPositionTab).size();

                if (current == previous[0]) {
                    stable[0]++;
                } else {
                    previous[0] = current;
                    stable[0] = 0;
                }

                return stable[0] >= 2 ? current : null;
            });
        }
        return 0;

    }

    public int getNumberOfPendingOrders(){
        By rowsOnPendingOrders = By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
        );
        if (driver instanceof AndroidDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
            final int[] previous = {-1};
            final int[] stable = {0};

            return wait.until(d -> {
                int current = d.findElements(rowsOnPendingOrders).size();

                if (current == previous[0]) {
                    stable[0]++;
                } else {
                    previous[0] = current;
                    stable[0] = 0;
                }

                return stable[0] >= 2 ? current : null;
            });
        }
        return 0;
    }

    public void selectList(String listName){
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + listName + "\"]/parent::android.view.ViewGroup")).click();
        }
    }


}
