Feature: AO Application List


  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
  Scenario: Prompted error for invalid promotion code
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills mandatory information on application information page
    And the user fills "promo" code "Test" on application information page
    When the user clicks "Next to Personal Information" button on the application information page
    Then the user sees "Invalid Code" error message displayed on application information page
