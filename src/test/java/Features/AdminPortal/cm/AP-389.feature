Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5
  Scenario: The Default deposit/withdrawal currency field is editable on CM Trading Experience page in "Activated" status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of "Activated" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    And the user clicks "Next To Trading Experience" button on the CM employee & financial page
    Then the user sees dropdown "settlementCurrency" is editable on the CM trading experience page