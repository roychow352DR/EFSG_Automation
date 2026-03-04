package PageObject.MIOadmin;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class DepositManagementPage {

    private final Page page;
    private final AbstractComponentsPW abs;

    public Locator dropdown;
    public Locator dropdownOptions;
    public Locator header;

    public DepositManagementPage(Page page){
        this.page = page;
        this.abs = new AbstractComponentsPW(page);
        this.dropdown = page.locator(".css-1821gv5");
        this.dropdownOptions = page.getByRole(AriaRole.OPTION);
        this.header = page.locator("tr[class='MuiTableRow-root MuiTableRow-head css-1gqug66']");
    }

    public void clickDropdown(String dropdownName){
        page.waitForTimeout(2000);
        dropdown.filter(new Locator.FilterOptions().setHasText(dropdownName)).locator(".css-uge3vf").first().click();
        dropdownOptions.filter(new Locator.FilterOptions().setHas(page.getByText("Deposit",new Page.GetByTextOptions().setExact(true)))).click();
    }

    public void getColIndex(){
        Locator headerRow = page.locator("tr.MuiTableRow-root.MuiTableRow-head").first();
        Locator headers = headerRow.locator("th");

        int typeIndex1Based = -1;
        int n = headers.count();
        for (int i = 0; i < n; i++) {
           // String t = headers.nth(i).innerText().trim();
            if (headers.nth(i).textContent().equalsIgnoreCase("Type")) {
                typeIndex1Based = i + 1;
                break;
            }
        }
        System.out.println(typeIndex1Based);
    }

}
