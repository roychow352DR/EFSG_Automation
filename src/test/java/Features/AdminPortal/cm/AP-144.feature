  Feature: Customer Management

    @Regression @Smoke @AdminPortal @CM @EBL_MT5 @EIEHK @EGM
    Scenario: CM status in Activated after second approval
      Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
      And the user clicks "Customer Management" on the ao admin portal menu
      When the user clicks detail button of "Pending Approval" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
      And the user performs second approval on customer management page
      Then the user sees an existing record is updated to "Activated" status on the customer management list