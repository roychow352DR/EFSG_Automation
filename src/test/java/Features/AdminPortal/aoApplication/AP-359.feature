Feature: AO Application List


  @Regression @AdminPortal @AO
  Scenario: Account status in Spec. Approval for Pending Deposit after first approve reason
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user clicks detail button of "Pending Deposit" record on the application page
    And the user clicks "Activate live trading account" button on the application information page
    And the user selects "Verify the Applicants in real person" as reason on the verify reason pop up
    And the user clicks "Confirm" button on the application information page
    Then the user sees a record in "Spec. Approval for Pending Deposit" status is created on the application list

