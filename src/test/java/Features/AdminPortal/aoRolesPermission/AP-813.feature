Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: User can delete the role successfully
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    When the user clicks detail button of roles "QaAutoRoleTest" on the role and permission page
    And the user clicks button "Delete" on the ao role and permission page
    And the user clicks button "Delete" on the ao role and permission page
    Then the user sees a dialogue with wordings "Success Delete" is prompted on the role and permission page
    And the user sees a role is deleted on the ao role and permission page