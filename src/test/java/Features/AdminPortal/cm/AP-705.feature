Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @Username
  Scenario: Individual - the old username could be used by any other account in AO once the username changed in CM
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of "Activated" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user edit text field "username" on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    And the user clicks "Next To Trading Experience" button on the CM employee & financial page
    And the user clicks "Update & Confirm" button on the CM trading experience page
    And the user clicks "Confirm" button on the CM trading experience page
    And the user clicks detail button of amended record on the customer management page
    And the user performs first approval on cm page
    And the user logout Admin Portal
    And the user re-logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of amended record on the customer management page
    And the user performs second approval on cm page
    When the user clicks "AO Application List" on the ao admin portal menu
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills mandatory information on application information page
    And the user fills reusable data in the text field "username" on the application information page
    And the user clicks "Next To Personal Information" button on the application information page
    And the user fills personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user fills trading experience page
    And the user clicks "Submit" button on the trading experience page
    Then the user sees a record in "Pending Verification" status is created on the application list