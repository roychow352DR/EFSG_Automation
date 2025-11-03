Feature: Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
    Scenario: Admin entity is not editable in user management
      Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
      And the user clicks "Admin Area" on the ao admin portal menu
      And the user clicks "User Management" on the ao admin portal menu
      When the user clicks detail button of username "qaautotest" on the user management page
      Then the user sees text field "entity" is not editable on the user management page