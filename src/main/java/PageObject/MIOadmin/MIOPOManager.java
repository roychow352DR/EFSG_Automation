package PageObject.MIOadmin;

import com.microsoft.playwright.Page;

public class MIOPOManager {

    private final Page page;
    public final MIOLoginPagePW mioLoginPagePW;
    public final DashBoardPagePW dashBoardPagePW;

    public MIOPOManager(Page page){
        this.page = page;
        this.mioLoginPagePW = new MIOLoginPagePW(page);
        this.dashBoardPagePW = new DashBoardPagePW(page);
    }

    public MIOLoginPagePW getLoginPage(){
        return mioLoginPagePW;
    }

    public DashBoardPagePW getDashboardPage(){
        return dashBoardPagePW;
    }
}
