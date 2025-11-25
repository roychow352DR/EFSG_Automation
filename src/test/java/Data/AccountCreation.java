package Data;

import PageObject.AdminPortalPW.AOPOManager;
import utils.SetCondition;

import java.io.IOException;

import static utils.SetCondition.isThirdParty;

public class AccountCreation {

    AOPOManager aopoManager;

    public AccountCreation(AOPOManager aopoManager) {
        this.aopoManager = aopoManager;
    }

    public void createL1AccountIndividual() throws IOException {
        aopoManager.getApplicationListPage().createIndividual();
        aopoManager.getApplicationInfoPage().fillApplicationInfo(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
        aopoManager.getMenuPagePW().clickMenu("AO Application List");
    }

    public void createL1AccountRejected() throws IOException {
        aopoManager.getApplicationListPage().createIndividual();
        aopoManager.getApplicationInfoPage().fillApplicationInfo(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
        aopoManager.getTradingExpPage().clickButtonByText("Reject");
        aopoManager.getTradingExpPage().selectRejectReason("ID/Passport No. match with the EDD/AML list");
    }

    public void createL2AccountIndividual() throws IOException {
        aopoManager.getApplicationListPage().createIndividual();
        aopoManager.getApplicationInfoPage().fillApplicationInfo(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
        aopoManager.getPersonalInfoPage().fillPersonalInfo(SetCondition.isBelow18(),
                SetCondition.isExpired(),
                SetCondition.isExpiredBeforeCurrent(),
                SetCondition.isEdd(),
                isThirdParty());
        aopoManager.getContactInfoPage().fillContactInfo();
        aopoManager.getEmployeeFinInfoPage().fillEmployeeFinInfo();
        aopoManager.getTradingExpPage().fillTradingExp();
        aopoManager.getTradingExpPage().clickButtonByText("Submit");
    }

    public void aoFirstApproval(){
        aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
        aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
        aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        aopoManager.getTradingExpPage().clickButtonByText("Verify");
        aopoManager.getTradingExpPage().selectReason("Pass eKYC");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }

    public void aoSecondApproval(){
        aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
        aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
        aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        aopoManager.getTradingExpPage().clickButtonByText("Approve");
        aopoManager.getTradingExpPage().selectReason("Pass eKYC");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");
    }

    public void createL3AccountIndividual() throws IOException {
        try {
            createL2AccountIndividual();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        aopoManager.getApplicationListPage().clickDetailBtn("Pending Verification");
        aoFirstApproval();
        reLogin();
        aopoManager.getApplicationListPage().clickDetailBtn("Pending Approval");
        aoSecondApproval();
    }

    public void reLogin() throws IOException {
        aopoManager.getMenuPagePW().clickLogout();
        aopoManager.getAdminLoginPage().loginETE("aoadmin01", "P@ssw0rd!");
    }
}
