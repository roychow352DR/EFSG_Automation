Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
  Scenario: User sees the default MT5 group obtain from eCRM
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    When the user clicks "AO Application List" on the ao admin portal menu
    And the user clicks detail button of newly created record on the application page
    Then the user sees text field "tradingGroup" displayed expected value as trade group info "tradingGroup" obtain from eCRM on the application information page
    