package Data;

import PageObject.AdminPortalPW.AOPOManager;
import utils.BaseTest;

public class CmAccountStatus {

    AOPOManager aopoManager;
    public String productEntity;

    public CmAccountStatus(AOPOManager aopoManager) {
        this.aopoManager = aopoManager;
        this.productEntity = BaseTest.productEntity;
    }

    public void submitChange(){
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")){
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        }
        else {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        }
        aopoManager.getTradingExpPage().clickButtonByText("Update & Confirm");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }
}
