Feature: AO Application List

  @Regression @AdminPortal @CM @Username @EBL_MT5
  Scenario: Remove CM existing validation on duplicated email address
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of "Activated" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    When the user fills value "qaautol3app@yopmail.com" in the text field "email" on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    And the user clicks "Next To Trading Experience" button on the CM employee & financial page
    And the user clicks "Update & Confirm" button on the CM trading experience page
    And the user clicks "Confirm" button on the CM trading experience page
    Then the user sees "Success Update" dialogue is prompted on the customer management page
