Feature: AO Blacklist

  @Regression @AdminPortal @Blacklist @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: User can update status successfully
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "AO Blacklist" on the ao admin portal menu
    When the user clicks detail button of status "Active" on the blacklist page
    And the user clicks radio button "Inactive" on the blacklist dialogue
    And the user clicks button "Update" on the blacklist page
    Then the user sees an existing record is updated to status "Inactive" on the blacklist page
