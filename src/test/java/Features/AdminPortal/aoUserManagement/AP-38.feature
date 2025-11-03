Feature: Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
    Scenario: User who doesn't have User Management Read access sees the Detail button is disabled
      Given the user logged in to Admin Portal as username "qaautonoread" and password "Test1234@"
      When the user clicks "Admin Area" on the ao admin portal menu
      And the user clicks "User Management" on the ao admin portal menu
      Then the user sees button "Detail" is disabled on the user management page