Feature: AO User Management

  @Regression @AdminPortal @UserManagement @EBL_MT5 @EIEHK @XPro
    Scenario: User login to admin portal successfully after the status changed to Active in user management
      Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
      And the user clicks "Admin Area" on the ao admin portal menu
      And the user clicks "User Management" on the ao admin portal menu
      When the user clicks detail button of username "qaautotest" on the user management page
      And the user modifies dropdown "status" to option "Active" on the user management page
      And the user clicks button "Update" on the user management page
      And the user logout Admin Portal
      And the user re-logged in to Admin Portal as username "qaautotest" and password "Test1234@"
      Then the user sees profile name "qaautotest" is displayed on the ao admin portal menu

