package PageObject.AdminPortal;

import AbstractComponent.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class TradingExperiencePage {

    WebDriver driver;
    AbstractComponents abs;

    public TradingExperiencePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        abs = new AbstractComponents(driver);
    }

    @FindBy(id = "mui-component-select-fiveOrMorTransactionLastThreeYears")
    WebElement tradeEXP;

    @FindBy(id = "mui-component-select-haveOtherTrade")
    WebElement investEXP;

    @FindBy(xpath = "//li[contains(@class,'css-sudvrv')]")
    List<WebElement> dropdownItems;

    @FindBy(xpath = "//button[contains(@class,'css-1m4mrb3-root-contained-root-contained')]")
    WebElement submitButton;

    @FindBy(xpath = "//span[contains(@class,'css-wmickx')]")
    List<WebElement> ctaButtons;

    @FindBy(xpath = "//button[contains(@class,'css-1amqxls-root-outlined-root-outlined')]")
    WebElement approveButton;

    @FindBy(id = "mui-component-select-verify")
    WebElement approvalDropdown;

    @FindBy(xpath = "//h3[contains(@class,'css-25mri5')]")
    WebElement approvalLabel;

    @FindBy(css = ".css-1u8h5t9 button")
    List<WebElement> buttonsOnVerify;


    public void selectTradeEXP() {
        tradeEXP.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("tradeEXP"));
    }

    public void selectInvestEXP() {
        investEXP.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("investEXP"));
    }

    public void fillTradeExperience() {
        selectTradeEXP();
        selectInvestEXP();
    }

    public void submitTradeExperience(String ctaButtonName) {
        submitButton.click();
    }

    public void clickApprove() {
        approveButton.click();
    }

    public void selectReason(String reason) {
        abs.waitUtilElementFind(approvalLabel);
        approvalDropdown.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", reason);
    }

    public void clickButtonOnVerify(String buttonName) {
        abs.clickButtonByText(buttonsOnVerify, buttonName);
    }


}
