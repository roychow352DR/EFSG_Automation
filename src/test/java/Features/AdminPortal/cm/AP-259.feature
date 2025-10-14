Feature: Customer Management

  @Regression @AdminPortal @CM @Backend @EBL @EIE @XPro
  Scenario: status equals to 5 in cm.trade database table for cm status in Pending Verification account
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user sees a record in "Pending Verification" status with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    Then "status" is "5" in CM "trade" database table where "account_id" retrieved by "Account ID"