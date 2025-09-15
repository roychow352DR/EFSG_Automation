Feature: Customer Management

  @Regression @AdminPortal @CM
  Scenario: User sees change history after CM status updated to Pending Verification
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the menu
    When the user clicks detail button of specific entity record on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user edit text field "mobile" on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    And the user clicks "Next To Trading Experience" button on the CM employee & financial page
    And the user clicks "Update & Confirm" button on the CM trading experience page
    And the user clicks "Confirm" button on the CM trading experience page
    And the user clicks detail button of modified record on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    Then the user sees "History" button on the CM personal information page
    And the user sees "History" dialogue on the CM personal information page upon click on the "History" button


