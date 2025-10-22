Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5
  Scenario: User able to edit Default deposit/withdrawal currency in "Awaiting Response" status after MT5 account is created by mobile app
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the data created by "app" in status "Awaiting Response" is found in the AO application list
    And the user clicks detail button of app client on the application page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next To Contact Information" button on the personal information page
    And the user clicks "Next to Employee and Financial Information" button on the contact information page
    And the user clicks "Next to Trading Experience" button on the employee & financial information page
    When the user edit text field "settlementCurrency" on the trading experience page
    And the user clicks "Submit" button on the trading experience page
    And the user clicks detail button of modified record on the application page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next To Contact Information" button on the personal information page
    And the user clicks "Next to Employee and Financial Information" button on the contact information page
    And the user clicks "Next to Trading Experience" button on the employee & financial information page
    Then the user sees text field "settlementCurrency" value is updated on the trading experience page
