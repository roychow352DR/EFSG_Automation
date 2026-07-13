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

    public void submitChange() {
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")) {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        } else {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        }
        aopoManager.getTradingExpPage().clickButtonByText("Submit");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }

    public void cmFirstApproval() {
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")) {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        } else {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        }
        aopoManager.getTradingExpPage().clickButtonByText("Verify");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }

    public void cmSecondApproval() {
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")) {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        } else {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        }
        aopoManager.getTradingExpPage().clickButtonByText("Approve");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }

    public void cmReject() {
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")) {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        } else {
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        }
        aopoManager.getTradingExpPage().clickButtonByText("Reject");
        aopoManager.getCmTradingExpPage().fillReason("Test");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }

    public void submitPersonalInfoChange() {
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")) {
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
            aopoManager.getTradingExpPage().clickButtonByText("Update & Confirm");
            aopoManager.getTradingExpPage().clickButtonByText("Confirm");
        } else {
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getTradingExpPage().clickButtonByText("Submit");
            aopoManager.getTradingExpPage().clickButtonByText("Confirm");
        }
    }

    public void cmSecondApprovalFromPersonalInfo() {
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")) {
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        } else {
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        }
        aopoManager.getTradingExpPage().clickButtonByText("Approve");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }

    public void cmFirstApprovalFromPersonalInfo() {
        if (productEntity.equalsIgnoreCase("EIEHK") || productEntity.equalsIgnoreCase("EBL_MT5")) {
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        } else {
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        }
        aopoManager.getTradingExpPage().clickButtonByText("Verify");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }
}
