  Feature: Customer Management

    @Regression @Smoke @AdminPortal @CM @EBL_MT5 @EIEHK @EGM
    Scenario: CM status in Pending Approval after first approval
      Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
      And the user clicks "Customer Management" on the ao admin portal menu
      When the user clicks detail button of "Pending Verification" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
      And the user performs first approval on customer management page
      Then the user sees an existing record is updated to "Pending Approval" status on the customer management list