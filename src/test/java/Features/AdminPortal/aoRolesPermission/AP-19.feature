Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario:  User cannot delete the role once at least 1 user assign to the role
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    When the user clicks detail button of roles "fullaccess" on the role and permission page
    And the user clicks button "Delete" on the ao role and permission page
    Then the user sees a dialogue with wordings "Not allow to delete!" is prompted on the role and permission page