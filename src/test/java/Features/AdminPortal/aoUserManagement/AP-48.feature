Feature: AO User Management

  @Regression @AdminPortal @UserManagement @EBL_MT5 @EIEHK @XPro
    Scenario: User who doesn't have User Management Export access sees the Detail button is disabled
      Given the user logged in to Admin Portal as username "qanoexport" and password "Test1234@"
      When the user clicks "Admin Area" on the ao admin portal menu
      And the user clicks "User Management" on the ao admin portal menu
      Then the user sees button "Detail" is hidden on the user management page