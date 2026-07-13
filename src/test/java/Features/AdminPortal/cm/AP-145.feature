Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: CM status updated to Activated from Pending Approval after the rejection
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of "Pending Approval" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user rejects the submission on customer management page
    Then the user sees an existing record is updated to "Activated" status on the customer management list
