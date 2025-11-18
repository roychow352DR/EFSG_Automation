Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK
  Scenario: Company Account status in Approved after second approved reason
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user clicks the detail button for the application record with status "Deposit Pending Approval", created by "qaauto", and client type "LEVEL_3_COMPANY" on the application list page
    And the user clicks "Approve activate live trading account" button on the application information page
    And the user selects "Verify the Applicants in real person" as reason on the verify reason pop up on application information page
    And the user clicks "Approve" button on the application information page
    Then the user sees an existing record is updated to "Approved" status on the application list