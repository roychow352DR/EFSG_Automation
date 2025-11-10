Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
  Scenario: The account group is override after the user enters valid IB code
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills mandatory information on application information page
    And the user fills entity IB Code in the text field "upperIbAcc" on application information page
    When the user clicks "Next To Personal Information" button on the application information page
    And the user clicks "Ao Application List" on the ao admin portal menu
    And the user clicks detail button of newly created record with account type "Individual" on the application page
    Then the user sees text field "tradingGroup" displayed expected value as entity trade group info "tradingGroup" obtain from eCRM on the application information page
