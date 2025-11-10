Feature: AO Application List

  @Regression @AdminPortal @UserManagement @Username @EBL_MT5
  Scenario: User can filtered by Entity on the user management page
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Admin Area" on the ao admin portal menu
    And the user clicks "User Management" on the ao admin portal menu
    When the user clicks button "Filter" on the user management page
    And the user clicks entity checkbox on the user management filter dialogue
    And the user clicks button "Apply" on the user management page
    Then the user sees relevant entity records displayed as filtered result on the user management page