Feature: AO Application List

  @Regression @AdminPortal @Smoke @AO @EBL_MT5 @EIEHK
  Scenario: User aged > 74 requires filling 3rd party witness form after submit an application
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills mandatory information on personal information page
    And the user fills age "75" on the personal information page
    And the user clicks "Next To Contact Information" button on the personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user fills trading experience page
    And the user clicks "Submit" button on the trading experience page
    When the user clicks detail button of newly created record with account type "Individual" on the application page
    Then the user sees "Awaiting Response" label is displayed on application information page
    And the user sees text field label "Sumsub Witness Statement" is displayed on application information page
