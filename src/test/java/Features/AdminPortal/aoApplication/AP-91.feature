Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro @Blacklist @EGM
  Scenario: Individual - User cannot see blacklist label on AO Application Detail for single user in XPRO entity
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the "Cross Entity" condition is satisfied
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills mandatory information on personal information page
    And the user fills id "Y1345678" on personal information page
    And the user clicks "Next To Contact Information" button on the personal information page
    And the user fills mandatory information on contact information page
    And the user clicks "Submit" button on the contact information page
    And the user clicks detail button of "Pending Verification" record on the application page
    Then the user sees "Blacklist" label is not displayed on application information page