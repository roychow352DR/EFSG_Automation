Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: Read only admin user sees Detail dialogue on role and permission page
    Given the user logged in to Admin Portal as username "qaautoreadonly" and password "P@ssw0rd!"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    When the user clicks button "Detail" on the ao role and permission page
    Then the user sees dialogue with heading "Detail/Edit Role" on the ao role and permission page