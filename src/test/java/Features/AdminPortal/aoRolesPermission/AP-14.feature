Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: No Read access admin user sees Detail button is disabled on role and permission page
    Given the user logged in to Admin Portal as username "qaautonoread" and password "Test1234@"
    When the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    Then the user sees button "Detail" is disabled on the ao roles and permission page