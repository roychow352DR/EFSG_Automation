Feature: AO Application List


  @Regression @L3IndividualCreation @AdminPortal @Smoke @AO @XPro @EGM
  Scenario: XPro/EGM - Account status in Pending Approval after first approval
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks the detail button for the application record with status "Pending Verification", created by "qaauto", and client type "LEVEL_3_INDIVIDUAL" on the application list page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next to Contact Information" button on the personal information page
    And the user clicks "Verify" button on the contact information page
    And the user selects "Pass eKYC" as verify reason on the verify pop up
    And the user clicks "Confirm" button on the trading experience page
    Then the user sees an existing record is updated to "Pending Approval" status on the application list





