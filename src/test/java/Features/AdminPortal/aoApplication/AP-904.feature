Feature: AO Application List


  @Regression @L3CompanyCreation @AdminPortal @Smoke @AO @XPro @EGM
  Scenario: Company Account status in Approved after second approval
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user clicks the detail button for the application record with status "Pending Approval", created by "qaauto", and client type "LEVEL_3_COMPANY" on the application list page
    And the user clicks "Approve" button on the create company account pop up
    And the user selects verify reason "Pass eKYC" on the verify pop up on the create company account page
    And the user clicks "Confirm" button on the create company account pop up
    Then the user sees an existing record is updated to "Approved" status on the application list


