Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @Username
  Scenario: Company - the old username could be used by any other account in AO once the username changed in CM
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of "Activated" record with "LEVEL_3_COMPANY" client type on the customer management page
    And the user clicks "Next To User Information" button on the CM application information page
    And the user edit text field "username" on the CM user information page
    And the user clicks button "Next To Contact Information" on the CM user information page
    And the user clicks "Update & Confirm" button on the CM contact information page
    And the user clicks "Confirm" button on the CM contact information page
    And the user clicks detail button of amended record on the customer management page
    And the user performs first approval on cm page for the account type "LEVEL_3_COMPANY"
    And the user logout Admin Portal
    And the user re-logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of amended record on the customer management page
    And the user performs second approval on cm page for the account type "LEVEL_3_COMPANY"
    When the user clicks "AO Application List" on the ao admin portal menu
    And the user clicks "Create Account" button on the application page
    And the user selects "Company" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fill mandatory information on create company account page
    And the user fills reusable data in the text field "username" on create company account page
    And the user clicks "Submit" button on the create company account page
    And the user clicks "Confirm" button on the create company account pop up
    And the user clicks detail button of newly created record with account type "Company" on the application page
    Then the user sees title "Company" is displayed at the create company account page
    And the user sees status "Pending Verification" is displayed at the create company account page