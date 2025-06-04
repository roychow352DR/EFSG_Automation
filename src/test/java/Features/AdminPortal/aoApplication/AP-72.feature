Feature: AO Application List

  @Regression
  Scenario Outline: Prompt error if DOB is below 18
    Given the user logged in to Admin Portal as username <name> and password <password>
    And the user lands on Application Information page
    And the user fills mandatory information on application information page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user fills mandatory information with DOB below 18 on personal information page
    When the user clicks "Next to Contact Information" button on the personal information page
    Then the user sees "Must be 18 years old above" error message displayed on personal information page

    Examples:
      | name | password |
      | aoadmin01 | P@ssw0rd! |