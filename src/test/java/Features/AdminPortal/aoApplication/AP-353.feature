Feature: AO Application List


  @Regression @L3Creation @AdminPortal @Smoke
  Scenario Outline: Account status in Pending Approval after first approval
    Given the user logged in to Admin Portal as username <name> and password <password>
    When the user clicks detail button of "Pending Verification" record on the application page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next to Contact Information" button on the personal information page
    And the user clicks "Next to Employee and Financial Information" button on the contact information page
    And the user clicks "Next to Trading Experience" button on the employee & financial information page
    And the user clicks "Verify" button on L2 trading experience page
    And the user selects "Pass eKYC" as verify reason on the verify pop up
    And the user clicks "Confirm" button on the verify pop up
    Then the user sees a record in "Pending Approval" status after first approval


    Examples:
      | name      | password  |
      | aoadmin01 | P@ssw0rd! |



