package PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import AbstractComponent.AbstractComponents;

public class ApplicatInformationPage  {
    WebDriver driver;

    public ApplicatInformationPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy (id="mui-component-select-entity")
    WebElement entityDropdown;

    @FindBy (name="email")
    WebElement emailField;

    @FindBy (name="mobile")
    WebElement phoneNumberField;

    @FindBy (name="promoCode")
    WebElement promoCode;

    @FindBy (name="upperIbAcc")
    WebElement referCode;

    @FindBy (id="mui-component-select-mobileCountryCode")
    WebElement countryCodeDropdown;

     @FindBy(xpath = "//span[text()='This email address is already in use']")
   // @FindBy(xpath = "//div[contains(@class,'css-h3o6is')]/div/span")
    WebElement emailInUseText;

   // @FindBy (xpath = "//div[contains(@class,'css-7vy65v')]/div/span")
   @FindBy (xpath = "//span[text()='This mobile number is already in use']")
   WebElement phoneInUseText;

    @FindBy (css = "button[class*='MuiButtonBase-root MuiButton-root MuiButton-contained']")
    WebElement nextButton;

    public void clickEntityDropdown(String datavalue)
    {
        entityDropdown.click();
        driver.findElement(By.xpath("//li[@data-value='"+datavalue+"']")).click();
    }

    public void fillInApplicantInfo()  {
        AbstractComponents info = new AbstractComponents(driver);
        int emailCount = 0;
        emailField.sendKeys(info.userinfoList().get("email"));
        countryCodeDropdown.click();

        WebElement hkCountryCode = driver.findElement(By.xpath("//li[@data-value='" + info.userinfoList().get("countryCode") + "']"));
        info.scrolling(hkCountryCode);
        hkCountryCode.click();
        phoneNumberField.sendKeys(info.userinfoList().get("phoneNumber"));
        //promoCode.sendKeys(info.userinfoList().get("promoCode"));
        //referCode.sendKeys(info.userinfoList().get("referCode"));
        nextButton.click();
         System.out.println(emailInUseText.getText());

        int count = 12;
        //userInfo.waitUtilElementFind(emailErrorText);
        while (emailInUseText.isDisplayed()) {
            String text = "autoeblsit" + count + "@yopmail.com";
            AbstractComponents.clearField(emailField, text);
            nextButton.click();
            count++;

        }

    }

        public void fillInValidEmail (WebElement emailErrorText,WebElement textField)
        {
            int count = 12;
            //userInfo.waitUtilElementFind(emailErrorText);
            while (emailErrorText.isDisplayed()) {
                String text = "autoeblsit" + count + "@yopmail.com";
                AbstractComponents.clearField(textField, text);
                nextButton.click();
                count++;
            }

         //   System.out.println(driver.switchTo().alert().getText());
           //System.out.println(emailInUseText.getText());
        }

    public void fillInValidPhoneNumber (WebElement phoneErrorText, WebElement textField2) {
        int number = 98765432;
        while (phoneErrorText.isDisplayed()) {
            AbstractComponents.clearField(textField2, Integer.toString(number));
            nextButton.click();
            number++;
        }


    }

}
