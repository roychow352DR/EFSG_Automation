Feature: AO User Management

  @Regression @AdminPortal @UserManagement @EBL_MT5 @EIEHK @XPro
    Scenario: Admin Role can be updated successfully
      Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
      And the user clicks "Admin Area" on the ao admin portal menu
      And the user clicks "User Management" on the ao admin portal menu
      When the user clicks detail button of username "qaautotest" on the user management page
      And the user changes entity role on the user management page
      And the user clicks button "Update" on the user management page
      And the user clicks detail button of username "qaautotest" on the user management page
      Then the user sees value of text field "entity_role" is updated on the user management page