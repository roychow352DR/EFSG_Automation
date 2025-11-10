Feature: AO Roles and Permission

  @Regression @AdminPortal @Roles&Permission @EBL_MT5 @EIEHK @XPro
  Scenario: Error prompted upon modify to existing role name
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "Role & Permission" on the ao admin portal menu
    When the user clicks detail button of roles "fullaccess" on the role and permission page
    And the user fills value "readonly" into the text field "roleName" on the role and permission page
    And the user clicks button "Update" on the ao role and permission page
    Then the user sees an error dialogue with wordings "Same Role is existed!" is prompted on the role and permission page