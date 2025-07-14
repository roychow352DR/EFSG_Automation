Feature: AO Application List

 @Regression
  Scenario Outline: Prompt error if expiry date before current date
    Given the user logged in to Admin Portal as username <name> and password <password>
    And the user lands on Application Information page
    And the user fills mandatory information on application information page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user fills mandatory information on personal information page
    And the user fills expiry date "earlier" than current date
    When the user clicks "Next to Contact Information" button on the personal information page
    Then the user sees "New ID document expiry date should be valid for more than 6 months" error message displayed on personal information page

    Examples:
      | name | password |
      | aoadmin01 | P@ssw0rd! |