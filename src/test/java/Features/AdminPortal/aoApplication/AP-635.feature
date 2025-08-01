Feature: AO Application List

  @Regression @AdminPortal @AO
  Scenario: Create Individual Account with status in Awaiting Response
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user submits mandatory information with "EDD Country" on personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user fills trading experience page
    When the user clicks "Submit" button on the trading experience page
    Then the user sees a record in "Awaiting Response" status is created on the application list