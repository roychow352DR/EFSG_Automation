package PageObject;

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


}
