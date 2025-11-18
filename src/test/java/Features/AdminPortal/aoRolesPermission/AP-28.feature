Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: Read only admin user cannot see Add Role button on role and permission page
    Given the user logged in to Admin Portal as username "qaautoreadonly" and password "P@ssw0rd!"
    When the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    Then the user sees button "Add Role" is hidden on the role and permission page