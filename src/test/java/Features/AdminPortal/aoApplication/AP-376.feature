Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5
  Scenario: Application record is created with the settlement currency value in HKD
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user selects dropdown value "HKD" for the dropdown field "settlementCurrency" on the trading experience page
    When the user clicks "Submit" button on the trading experience page
    And the user clicks detail button of newly created record with account type "Individual" on the application page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next to Contact Information" button on the personal information page
    And the user clicks "Next to Employee and Financial Information" button on the contact information page
    And the user clicks "Next to Trading Experience" button on the employee & financial information page
    Then the user sees dropdown value "HKD" for the dropdown field "settlementCurrency" on the trading experience page