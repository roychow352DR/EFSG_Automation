Feature: AO Application List


  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: Prompt error if expiry date before current date
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the "Expired date before current date" condition is satisfied
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user submits mandatory information on personal information page
    Then the user sees "New ID document expiry date should be valid for more than 6 months" error message displayed on personal information page