package Data;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.NoSuchElementException;
import utils.GetPageElement;

public class PositionDetail {

    private final GetPageElement pageElement;

    public PositionDetail(AppiumDriver driver) {
        this.pageElement = new GetPageElement(driver);
    }

    public String getPositionValueByLabel(String label, String symbolDecimal) {
        String uiLabel = pageElement.mapPositionDetailsLabel(label);
        String rawValue = pageElement.resolveLabelValue(uiLabel);

        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Value not found for label: " + uiLabel);
        }

        return pageElement.normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }
}
