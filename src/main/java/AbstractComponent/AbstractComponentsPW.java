package AbstractComponent;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.commons.lang3.RandomStringUtils;
import utils.BaseTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;

public class AbstractComponentsPW {
    Page page;
    public String email;

    public AbstractComponentsPW(Page page) {
        this.page = page;
    }


    public Map<String, String> userinfoList() throws IOException {
        LocalDate localDate = LocalDate.now();
        String path = "//src//main//java//DataResources//GlobalData.properties";
        int randomEmailSeed = (int) (Math.random() * 10001);
        int randomPhoneNo = (int) (Math.random() * 10000001);
        Map<String, String> info = new HashMap<String, String>();
        info.put("email", "qaauto" + "_" + BaseTest.productEntity + "_" + randomEmailSeed + "@yopmail.com");
        info.put("companyEmail", "qaautocompany" + "_" + BaseTest.productEntity + "_" + randomEmailSeed + "@yopmail.com");
        info.put("existedEmail", "uatapproved@yopmail.com");
        info.put("phoneNumber", Integer.toString(randomPhoneNo));
        info.put("existedPhoneNumber", "96553209");
        info.put("entity", BaseTest.productEntity);
        info.put("promoCode", "Test");
        info.put("referCode", "Test123");
        info.put("countryCode", "+852");
        info.put("lastName", "Peter");
        info.put("firstName", "Chu");
        info.put("country", "Hong Kong, China");
        info.put("eddCountry", "Malaysia");
        info.put("nationality", "Hong Kong, China");
        info.put("gender", "Male");
        info.put("idType", "Passport");
        info.put("dateOfBirthYear", "1990");
        info.put("dateOfBirthDay", "1");
        info.put("dateOfBirthYearBelow18", Integer.toString(localDate.getYear() - 15));
        info.put("dateOfBirthYearThirdParty", Integer.toString(localDate.getYear() - 80));
        info.put("currentYear", Integer.toString(localDate.getYear()));
        info.put("id", randomString(6));
        info.put("passwordNo", randomString(6));
        info.put("addressLine1", "Mong Kok");
        info.put("city", "Kowloon");
        info.put("employStatus", "Employed ");
        info.put("industrial", "Education");
        info.put("annualIncome", "Less than $200,000 (Appx. USD 25,000)");
        info.put("netWorth", "Less than $40,000 (Appx. USD 5,000)");
        info.put("tradeFunds", "Employment");
        info.put("taxCountry", "Hong Kong, China");
        info.put("tradeEXP", "No");
        info.put("investEXP", "No");
        info.put("expiredYear", Integer.toString(localDate.getYear()));
        info.put("expiryDay", Integer.toString(localDate.get(ChronoField.DAY_OF_MONTH)));
        info.put("validExpiryYear", Integer.toString(localDate.getYear() + 2));
        info.put("username", randomString(10));
        info.put("companyLegal", BaseTest.productEntity + " Legal" + System.currentTimeMillis());
        return info;
    }

    public Map<String, String> blacklistInfoList() throws IOException {
        String path = "//src//main//java//DataResources//GlobalData.properties";
        int randomEmailSeed = (int) (Math.random() * 10001);
        int randomPhoneNo = (int) (Math.random() * 10000001);
        Map<String, String> blacklistInfo = new HashMap<String, String>();
        blacklistInfo.put("entity", BaseTest.getProperty(path, "entity"));
        blacklistInfo.put("email", "qaautoblacklist" + randomEmailSeed + "@yopmail.com");
        blacklistInfo.put("lastName", "Blacklist");
        blacklistInfo.put("firstName", "QA");
        blacklistInfo.put("id", randomString(6));
        blacklistInfo.put("passwordNo", randomString(6));
        blacklistInfo.put("idType", "ID Card");
        blacklistInfo.put("category", "Hacker");
        blacklistInfo.put("nationality", "Hong Kong, China");
        blacklistInfo.put("phoneNumber", Integer.toString(randomPhoneNo));
        blacklistInfo.put("countryCode", "+852");
        return blacklistInfo;
    }


    public String randomString(int length) {
        char letter = RandomStringUtils.randomAlphabetic(1).charAt(0);
        char digit = RandomStringUtils.randomNumeric(1).charAt(0);
        String combinedString = "" + letter + digit + RandomStringUtils.randomAlphanumeric(length - 2);

        List<Character> characters = new ArrayList<>();
        for (char c : combinedString.toCharArray()) {
            characters.add(c);
        }

        Collections.shuffle(characters);

        StringBuilder shuffledString = new StringBuilder();
        for (char c : characters) {
            shuffledString.append(c);
        }

        return shuffledString.toString();
    }

    public void waitForLocatorVisible(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    // click next page til the record display at application list based on status text
    public void getItemsByText(String text, Locator items, Locator nextPageBtn, String entity, Locator entityRow) {
        boolean isFound = false;
        waitForLocatorVisible(items.first());
        while (true) {
            int count = items.count();
            for (int i = 0; i < count; i++) {
                Locator locator = items.nth(i);
                Locator entityLocator = entityRow.nth(i);
                int countEntity = entityRow.count();
                String itemText = locator.textContent();
                String entityText = entityLocator.textContent();
                if (itemText != null && itemText.equalsIgnoreCase(text) && entityText.equalsIgnoreCase(entity)) {
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
            if (nextPageBtn.isEnabled()) {
                nextPageBtn.click();
                waitForLocatorVisible(items.first());
            } else {
                System.out.println("No more pages. " + text + " not found.");
                break;
            }
        }
    }

    public void getItemsByText(String text, Locator items, Locator nextPageBtn) {
        boolean isFound = false;
        waitForLocatorVisible(items.first());
        while (true) {
            int count = items.count();
            for (int i = 0; i < count; i++) {
                Locator locator = items.nth(i);
                String itemText = locator.textContent();

                if (itemText != null && itemText.equalsIgnoreCase(text)) {
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
            if (nextPageBtn.isEnabled()) {
                nextPageBtn.click();
                waitForLocatorVisible(items.first());
            } else {
                System.out.println("No more pages. " + text + " not found.");
                break;
            }
        }
    }

    public void getItemsByText(String text1, String text2, String entity, Locator items1, Locator items2, Locator entityRow, Locator nextPageBtn) {
        boolean isFound = false;
        waitForLocatorVisible(items1.first());
        while (true) {
            int count = items1.count();
            int count2 = items2.count();
            int countEntity = entityRow.count();
            for (int i = 0; i < count || i < count2 || i < countEntity; i++) {
                Locator locator = items1.nth(i);
                Locator locator2 = items2.nth(i);
                Locator entityLocator = entityRow.nth(i);
                String itemText = locator.textContent();
                String itemText2 = locator2.textContent();
                String entityText = entityLocator.textContent();
                if (itemText != null && itemText.equalsIgnoreCase(text1) && itemText2.equalsIgnoreCase(text2) && entityText.equalsIgnoreCase(entity)) {
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
            if (nextPageBtn.isEnabled()) {
                nextPageBtn.click();
                waitForLocatorVisible(items1.first());
            } else {
                System.out.println("No more pages. " + text1 + "and" + text2 + " not found.");
                break;
            }
        }
    }

    public String getInputValueByAttribute(Locator locator, String attribute, String attributeVal) {
        List<Locator> inputs = locator.all();
        for (Locator input : inputs) {
            if (input.getAttribute(attribute).equalsIgnoreCase(attributeVal)) {
                return input.inputValue();
            }
        }
        return attribute;
    }

    public String getApiEndpointDomain(String env) {
        return switch (env) {
            case "bauuat" -> "https://zmtezs56l2.execute-api.ap-southeast-1.amazonaws.com/uat/core-service/";
            case "mt5sit" -> "https://2f1lmm1qqi.execute-api.ap-southeast-1.amazonaws.com/sit/core-service/";
            case "mt5uat" -> "https://bau-uat-aocm-api.empfs.net/bau-uat/core-service/";
            default -> "";
        };
    }

    public String toFullWidth(String halfWidth) {
        if (halfWidth == null || halfWidth.isEmpty()) {
            return halfWidth;
        }

        StringBuilder fullWidthBuilder = new StringBuilder();
        for (char c : halfWidth.toCharArray()) {
            if (c >= '!' && c <= '~') {
                fullWidthBuilder.append((char) (c + 65248));
            } else if (c == ' ') { // Convert half-width space to full-width space
                fullWidthBuilder.append('\u3000'); // Full-width space Unicode
            } else {
                fullWidthBuilder.append(c);
            }
        }
        return fullWidthBuilder.toString();
    }

    public void selectUnselectedDropdownOption() {
        Locator unselectedOptions = page.locator("[role='option'][aria-selected='false']");
        unselectedOptions.first().click();
    }

    public boolean checkElementIsEditable(Locator locator) {
        for (int i = 0; i < locator.count(); i++) {
            boolean isEditable = locator.nth(i).isEditable();
            if (isEditable) {
                return true;
            }
        }
        return false;
    }

    public boolean checkElementIsEnable(Locator locator) {
        for (int i = 0; i < locator.count(); i++) {
            boolean isClickable = locator.nth(i).isEnabled();
            if (isClickable) {
                return true;
            }
        }
        return false;
    }

    public String setIBCode(String entity) {
        return switch (entity) {
            case "EBL_MT5" -> "fJ4HdL";
            case "EIEHK" -> "fJ4HdsL";
            case "XPro" -> "djakd";
            default -> "";
        };
    }
    public boolean getFilteredVal(String filterVal,Locator row,Locator col) {
        int counts = row.count();
            for (int i = 0; i < counts; i++) {
                String rowText = row.nth(i).locator(col).textContent();
                if (rowText.contains(filterVal)) {
                    return true;
                }
            }
        return false;
    }
}
