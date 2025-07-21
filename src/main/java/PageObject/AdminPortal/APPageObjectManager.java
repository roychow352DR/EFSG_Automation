package PageObject.AdminPortal;

import org.openqa.selenium.WebDriver;

public class APPageObjectManager {
    AdminLoginPage adminLoginPage;
    public APPageObjectManager(WebDriver driver)
    {
        adminLoginPage = new AdminLoginPage(driver);
    }

    public AdminLoginPage getAdminLoginPage()
    {
        return adminLoginPage;
    }
}
