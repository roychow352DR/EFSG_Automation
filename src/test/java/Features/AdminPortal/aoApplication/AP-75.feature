Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
  Scenario: Cannot create Individual account with unchecked US Citizen checkbox
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills mandatory information on personal information page
    And the user uncheck "citizen" checkbox on personal information page
    When the user clicks "Next To Contact Information" button on the personal information page
    Then the user sees "For regulatory reasons, we do not accept citizens or residents of the United States." error message displayed on personal information page