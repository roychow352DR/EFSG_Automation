package PageObject.AdminPortalPW;

import com.microsoft.playwright.Page;

public class AOPOManager {
    private final Page page;
    public final AdminLoginPagePW adminPortalPagePW;
    public final ApplicationListPagePW applicationListPagePW;
    public final ApplicationInfoPagePW applicationInfoPagePW;
    public final PersonalInfoPagePW personalInfoPagePW;
    public final ContactInfoPagePW contactInfoPagePW;
    public final EmployeeFinInfoPagePW employeeFinInfoPagePW;
    public final TradingExpPagePW tradingExpPagePW;
    public final MenuPagePW menuPagePW;
    public final CustomerManagementPage customerManagementPage;
    public final CmApplicationInfoPage cmApplicationInfoPage;
    public final CmPersonalInfoPage cmPersonalInfoPage;
    public final CmContactInfoPage cmContactInfoPage;
    public final CmEmployeeFinInfoPage cmEmployeeFinInfoPage;
    public final CmTradingExpPage cmTradingExpPage;

    public AOPOManager(Page page) {
        this.page = page;
        this.adminPortalPagePW = new AdminLoginPagePW(page);
        this.applicationListPagePW = new ApplicationListPagePW(page);
        this.applicationInfoPagePW = new ApplicationInfoPagePW(page);
        this.personalInfoPagePW = new PersonalInfoPagePW(page);
        this.contactInfoPagePW = new ContactInfoPagePW(page);
        this.employeeFinInfoPagePW = new EmployeeFinInfoPagePW(page);
        this.tradingExpPagePW = new TradingExpPagePW(page);
        this.menuPagePW = new MenuPagePW(page);
        this.customerManagementPage = new CustomerManagementPage(page);
        this.cmApplicationInfoPage = new CmApplicationInfoPage(page);
        this.cmPersonalInfoPage = new CmPersonalInfoPage(page);
        this.cmContactInfoPage = new CmContactInfoPage(page);
        this.cmEmployeeFinInfoPage = new CmEmployeeFinInfoPage(page);
        this.cmTradingExpPage = new CmTradingExpPage(page);

    }

    public AdminLoginPagePW getAdminLoginPage() {
        return adminPortalPagePW;
    }

    public ApplicationListPagePW getApplicationListPage() {
        return applicationListPagePW;
    }

    public ApplicationInfoPagePW getApplicationInfoPage() {
        return applicationInfoPagePW;
    }

    public PersonalInfoPagePW getPersonalInfoPage() {
        return personalInfoPagePW;
    }

    public ContactInfoPagePW getContactInfoPage() {
        return contactInfoPagePW;
    }

    public EmployeeFinInfoPagePW getEmployeeFinInfoPage() {
        return employeeFinInfoPagePW;
    }

    public TradingExpPagePW getTradingExpPage() {
        return tradingExpPagePW;
    }

    public MenuPagePW getMenuPagePW() {
        return menuPagePW;
    }

    public CustomerManagementPage getCustomerManagementPage(){
        return customerManagementPage;
    }

    public CmApplicationInfoPage getCmApplicationInfoPage(){
        return cmApplicationInfoPage;
    }

    public CmPersonalInfoPage getCmPersonalInfoPage(){
        return cmPersonalInfoPage;
    }
    public CmContactInfoPage getCmContactInfoPage(){
        return cmContactInfoPage;
    }

    public CmEmployeeFinInfoPage getCmEmployeeInfoPage(){
        return cmEmployeeFinInfoPage;
    }

    public CmTradingExpPage getCmTradingExpPage(){
        return cmTradingExpPage;
    }


}
