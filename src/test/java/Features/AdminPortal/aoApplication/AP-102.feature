Feature: AO Application List

  @Regression @AdminPortal @EBL_MT5 @EIEHK @XPro @AO
  Scenario: User sees error on the application page if ID number in use exceeded limit
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user created up to 4 account with existing ID "qaAutoId"
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills mandatory information on personal information page
    And the user fills id "qaAutoId" on personal information page
    And the user clicks "Next To Contact Information" button on the personal information page
    Then the user sees "This ID number has exceeded the upper limit of applications (4 of 4)" error message displayed on personal information page