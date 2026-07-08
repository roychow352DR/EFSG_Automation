Feature: AO Application List


  @Regression @Smoke @AdminPortal @AO @EBL_MT5 @EIEHK @EGM
  Scenario: Individual Account status in Spec. Approval for Pending Deposit after first approve reason
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks the detail button for the application record with status "Pending Deposit", created by "qaauto", and client type "LEVEL_3_INDIVIDUAL" on the application list page
    And the user clicks "Activate live trading account" button on the application information page
    And the user selects "Verify the Applicants in real person" as reason on the verify reason pop up on application information page
    And the user clicks "Confirm" button on the application information page
    Then the user sees an existing record is updated to "Spec. Approval for Pending Deposit" status on the application list

