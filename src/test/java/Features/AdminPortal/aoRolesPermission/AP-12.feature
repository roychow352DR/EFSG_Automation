Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: The count of No. of Admin Users is deducted after admin user is assigned to the role
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    And the user retrieves number of assigned admin user of the role "qanoexport" on the ao roles and permission page
    When the user clicks "User Management" on the ao admin portal menu
    And the user clicks detail button of username "qaautotest" on the user management page
    And the user changes entity role to value "fullaccess" on the user management page
    And the user clicks button "Update" on the user management page
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    Then the user sees the number of assigned user of the role "qanoexport" is "deducted" on the ao roles and permission page