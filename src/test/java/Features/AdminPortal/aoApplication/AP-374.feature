Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5
  Scenario: The Default deposit withdrawal currency field is not editable on Trading Experience page in Pending Approval status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks the detail button for the application record with status "Pending Approval", created by "qaauto", and client type "LEVEL_3_INDIVIDUAL" on the application list page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next To Contact Information" button on the personal information page
    And the user clicks "Next to Employee and Financial Information" button on the contact information page
    And the user clicks "Next to Trading Experience" button on the employee & financial information page
    Then the user sees text field "settlementCurrency" is not editable on the trading experience page
