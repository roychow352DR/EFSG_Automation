Feature: AO Application List

  @Regression
  Scenario Outline: Prompt error if mobile number already in used
    Given the user logged in to Admin Portal as username <name> and password <password>
    And the user lands on Application Information page
    And the user fills mandatory information on application information page
    And the user fills existing "phone" on application information page
    When the user clicks "Next to Personal Information" button on the application information page
    Then the user sees "This mobile number is already in use" error message displayed on application information page



    Examples:
      | name | password |
      | aoadmin01 | P@ssw0rd! |