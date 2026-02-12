package PageObject.MIOadmin;

import com.microsoft.playwright.Page;

public class MIOPOManager {

    private final Page page;
    private final MIOLoginPagePW mioLoginPagePW;
    private final DashBoardPagePW dashBoardPagePW;
    private final DepositManagementPage depositManagementPage;

    public MIOPOManager(Page page){
        this.page = page;
        this.mioLoginPagePW = new MIOLoginPagePW(page);
        this.dashBoardPagePW = new DashBoardPagePW(page);
        this.depositManagementPage = new DepositManagementPage(page);
    }

    public MIOLoginPagePW getLoginPage(){
        return mioLoginPagePW;
    }

    public DashBoardPagePW getDashboardPage(){
        return dashBoardPagePW;
    }

    public DepositManagementPage getDepositManagementPage() {return depositManagementPage;}
}
