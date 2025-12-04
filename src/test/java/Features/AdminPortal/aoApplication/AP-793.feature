Feature: AO Application List

  @Regression @AdminPortal @AO @Username @EBL_MT5
  Scenario: Create Company Account with status in Pending Verification with duplicated email address
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Company" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fill mandatory information on create company account page
    And the user fills value "qaautocompanyl3@yopmail.com" in the text field "email" on create company account page
    And the user clicks "Submit" button on the create company account page
    And the user clicks "Confirm" button on the create company account pop up
    When the user clicks detail button of newly created record with account type "Company" on the application page
    Then the user sees title "Company" is displayed at the company account detail page
    And the user sees status "Pending Verification" is displayed at the company account detail page