Feature: AO Application List


  @Regression @L3Creation @AdminPortal @Smoke @AO @XPro
  Scenario: XPro - Account status in Pending Approval after first approval
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks detail button of "Pending Verification" record on the application page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next to Contact Information" button on the personal information page
    And the user clicks "Verify" button on the contact information page
    And the user selects "Pass eKYC" as verify reason on the verify pop up
    And the user clicks "Confirm" button on the trading experience page
    Then the user sees an existing record is updated to "Pending Approval" status on the application list





