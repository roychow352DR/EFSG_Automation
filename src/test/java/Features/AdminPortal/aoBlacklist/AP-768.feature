Feature: AO Application List

  @Regression @AdminPortal @Blacklist @Username @EBL_MT5 @EGM
  Scenario: User can filtered by Entity on the blacklist page
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "AO Blacklist" on the ao admin portal menu
    When the user clicks button "Filter" on the blacklist page
    And the user clicks entity checkbox on the blacklist filter dialogue
    And the user clicks button "Apply" on the user management page
    Then the user sees relevant entity records displayed as filtered result on the blacklist page