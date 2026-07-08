Feature: AO User Management

  @Regression @AdminPortal @UserManagement @EBL_MT5 @EIEHK @XPro @EGM
    Scenario: Admin entity is not editable in user management
      Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
      And the user clicks "Admin Area" on the ao admin portal menu
      And the user clicks "User Management" on the ao admin portal menu
      When the user clicks detail button of username "qaautotest" on the user management page
      Then the user sees text field "entity" is not editable on the user management page