Feature: AO Blacklist

  @Regression @AdminPortal @Blacklist @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: User add blacklist record successfully
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "AO Blacklist" on the ao admin portal menu
    When the user clicks button "Add Blacklist" on the blacklist page
    And the user fills mandatory information on the blacklist dialogue
    And the user clicks button "Submit" on the blacklist page
    Then the user sees a new created blacklist record is listed on the blacklist page
