Feature: AO Application List


  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK
  Scenario: Create Individual Account with status in Awaiting Response
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the "EDD" condition is satisfied
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user submits mandatory information on personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user fills trading experience page
    And the user clicks "Submit" button on the trading experience page
    Then the user sees a record in "Awaiting Response" status is created on the application list