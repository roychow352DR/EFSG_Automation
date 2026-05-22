Feature: AO Application List


  @Regression @L3IndividualCreation @AdminPortal @Smoke @AO @XPro @EGM
  Scenario: XPro/EGM - Account status in Approved after second approval
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user clicks the detail button for the application record with status "Pending Approval", created by "qaauto", and client type "LEVEL_3_INDIVIDUAL" on the application list page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user clicks "Next to Contact Information" button on the personal information page
    And the user clicks "Approve" button on the contact information page
    And the user selects "Pass eKYC" as verify reason on the verify pop up
    And the user clicks "Confirm" button on the trading experience page
    Then the user sees an existing record is updated to "Approved" status on the application list


