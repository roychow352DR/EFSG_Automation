Feature: AO Application List

  @Regression @AdminPortal @AO @EBL @EIE @XPro
  Scenario: User sees account status in Approved after Active Live Trading Account is second approved
    Given the user logged in to Admin Portal as username "aoadmin02" and password "P@ssw0rd!"
    When the user clicks detail button of "Spec. Approval for Pending Deposit" record on the application page
    And the user clicks "Approve activate live trading account" button on the application information page
    And the user selects "Verify the Applicants in real person" as reason on the verify reason pop up
    And the user clicks "Approve" button on the application information page
    Then the user sees an existing record is updated to "Approved" status on the application list