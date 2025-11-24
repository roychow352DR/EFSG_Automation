Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5
  Scenario: The Default deposit withdrawal currency field is editable on Trading Experience page in Awaiting Response status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks detail button of "Awaiting Response" record on the application page
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
