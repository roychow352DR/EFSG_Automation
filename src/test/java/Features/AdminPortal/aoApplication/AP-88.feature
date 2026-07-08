Feature: AO Application List

  @Regression @AdminPortal @AO @Blacklist @EBL_MT5 @EIEHK @EGM
  Scenario: Individual - User sees blacklist label on AO Application Detail if First Name and Last Name matched with AO Blacklist
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the record in status "Pending Verification" with firstname "QA" and lastname "Blacklist" is created in the application list
    When the user clicks detail button of "Pending Verification" record on the application page
    Then the user sees "Blacklist" label is displayed on application information page