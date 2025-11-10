Feature: AO Application List

  @Regression @AdminPortal @Smoke @AO @EBL_MT5 @EIEHK
  Scenario: User aged 74 can submit an application without filling 3rd party witness form
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills mandatory information on personal information page
    And the user fills age "74" on the personal information page
    And the user clicks "Next To Contact Information" button on the personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user fills trading experience page
    When the user clicks "Submit" button on the trading experience page
    Then the user sees a record in "Pending Verification" status is created on the application list
