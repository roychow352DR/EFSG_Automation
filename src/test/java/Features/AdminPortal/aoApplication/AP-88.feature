Feature: AO Application List

  @Regression @AdminPortal @AO @Blacklist @EBL_MT5 @EIEHK @XPro
  Scenario: Individual - User sees blacklist label on AO Application Detail if First Name and Last Name matched with AO Blacklist
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills mandatory information on personal information page
    And the user fills blacklisted firstname "Blacklist Test" and lastname "QA" on personal information page
    And the user clicks "Next To Contact Information" button on the personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user fills trading experience page
    And the user clicks "Submit" button on the trading experience page
    When the user clicks detail button of "Pending Verification" record on the application page
    Then the user sees "Blacklist" label is displayed on application information page