Feature: AO Application List

  @Regression @AdminPortal @AO @Username @EBL_MT5 @EGM
  Scenario: User can filtered by Entity on the application list
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks "Filter" button on the application page
    And the user clicks entity checkbox on the application filter dialogue
    And the user clicks "Apply" button on the application page
    Then the user sees relevant entity records displayed as filtered result on the application list