Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK @XPro
  Scenario: CM status is Activated for the Approved record in AO
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user sees a record in "Approved" status on the application list
    And the user clicks "Customer Management" on the ao admin portal menu
    Then the user sees "Activated" status on customer management page