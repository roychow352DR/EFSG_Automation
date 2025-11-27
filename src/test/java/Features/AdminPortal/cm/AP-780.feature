Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @Username
  Scenario: Username value can be applied to CM Database after being modified
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of specific entity record on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user edit text field "username" on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    And the user clicks "Next To Trading Experience" button on the CM employee & financial page
    And the user clicks "Update & Confirm" button on the CM trading experience page
    And the user clicks "Confirm" button on the CM trading experience page
    And the user clicks detail button of specific entity record on the customer management page
    And the user performs first approval on cm page for the account type "INDIVIDUAL"
    And the user logout Admin Portal
    And the user re-logged in to Admin Portal as username "samuellai" and password "Aa12345678!"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of specific entity record on the customer management page
    And the user performs second approval on cm page for the account type "INDIVIDUAL"
    When the user clicks detail button of specific entity record on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    Then the user sees text field "username" value is updated on the CM personal information page
    And "username" is updated to modified value in CM "authentication" database table where "person_id" retrieved by "Person ID"

