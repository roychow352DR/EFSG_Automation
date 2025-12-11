Feature: Customer Management

  @Regression @AdminPortal @CM @Backend @EBL_MT5 @EIEHK @XPro
  Scenario: status equals to 1 in cm.trade database table for cm status in Activated account
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user sees a record in "Activated" status with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    Then "status" is "1" in CM "trade" database table where "account_id" retrieved by "Account ID"