package PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import AbstractComponent.AbstractComponents;

public class PersonalInfoPage {
    WebDriver driver;
    public PersonalInfoPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy (name="legalLastNameEn")
    WebElement lastName;

    @FindBy (name="legalFirstAndMiddleNameEn")
    WebElement firstName;

    @FindBy (xpath="//input[@value='Male']")
    WebElement gender;

    @FindBy (id="mui-component-select-jurisdictionOfResidence")
    WebElement country;


   /* public void clickEntityDropdown(String datavalue)
    {
        entityDropdown.click();
        driver.findElement(By.xpath("//li[@data-value='"+datavalue+"']")).click();
    } */

    public void fillInPersonalInfo() {
        AbstractComponents info = new AbstractComponents(driver);
        lastName.sendKeys(info.userinfoList().get("lastName"));
        firstName.sendKeys(info.userinfoList().get("firstName"));
        gender.click();
        country.click();
        info.scrolling(driver.findElement(By.xpath("//li[@data-value='"+info.userinfoList().get("country")+"']")));


    }


}
