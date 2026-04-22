Feature: AO Application List


  @Regression @Smoke @L3CompanyCreation @AdminPortal @Smoke @AO @EBL_MT5 @EIEHK
  Scenario: Company Account status in Pending Approval after first approval
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks the detail button for the application record with status "Pending Verification", created by "qaauto", and client type "LEVEL_3_COMPANY" on the application list page
    And the user clicks "Verify" button on the create company account pop up
    And the user selects verify reason "Pass eKYC" on the verify pop up on the create company account page
    And the user clicks "Confirm" button on the create company account pop up
    Then the user sees an existing record is updated to "Pending Approval" status on the application list





