Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: Admin user can remove role access
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    And the user clicks detail button of roles "qaautofullaccess" on the role and permission page
    And the user uncheck checkbox "Read" of the module "Role Setting Detail" on the role and permission page
    And the user clicks button "Update" on the ao role and permission page
    When the user logout Admin Portal
    And the user re-logged in to Admin Portal as username "qaautotest" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    Then the user sees button "Detail" is disabled based on role entity on the ao roles and permission page