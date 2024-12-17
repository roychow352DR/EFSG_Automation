package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ApplicationPage {

    WebDriver driver;
    public ApplicationPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy (css = "button[class*='MuiButtonBase-root MuiButton-root MuiButton-outlined']")
    List<WebElement> buttons;

    @FindBy (xpath = "//h4[text()='Menu']")
    WebElement menu;

    public void clickButton(String clickButtonName)
    {
        for (int i=0;i<buttons.size();i++)
        {
            String buttonName = buttons.get(i).getText();
            if (buttonName.equalsIgnoreCase(clickButtonName))
            {
                buttons.get(i).click();
            }
        }
    }

    public WebElement menuTitle()
    {
        return menu;
    }


}
