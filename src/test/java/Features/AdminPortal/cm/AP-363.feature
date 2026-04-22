Feature: Customer Management

  @Regression @Smoke @AdminPortal @CM @EBL_MT5 @EIEHK
  Scenario: CM status is Locked for the Spec. Approval for Pending Deposit record in AO
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user sees a record in "Spec. Approval for Pending Deposit" status on the application list
    And the user clicks "Customer Management" on the ao admin portal menu
    Then the user sees "Locked" status on customer management page