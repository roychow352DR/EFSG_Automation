Feature: AO Application List


  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: Prompt error if DOB is below 18
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the "DOB Below 18" condition is satisfied
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user submits mandatory information on personal information page
    Then the user sees "Must be 18 years old above" error message displayed on personal information page
