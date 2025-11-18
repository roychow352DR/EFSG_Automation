Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: User can create single entity role
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    When the user clicks button "Add Role" on the ao role and permission page
    And the user fill in the role creation form on the ao role and permission page
    And the user clicks button "Submit" on the ao role and permission page
    Then the user sees a dialogue with wordings "Success Create" is prompted on the role and permission page
    And the user sees a new role is created on the ao role and permission page