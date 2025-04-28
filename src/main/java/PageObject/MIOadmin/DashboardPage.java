package PageObject.MIOadmin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {
    WebDriver driver;
    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
       // abs = new AbstractComponents(driver);
    }

    @FindBy(css = ".css-1d3bbye")
    WebElement welcomeText;

    public String welcomeMsg()
    {
        return welcomeText.getText();
    }
}
