Feature: AO Application List

  @Regression @AdminPortal @AO @Username @EBL
  Scenario: User can filtered application record by username
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks "Filter" button on the application page
    And the user fills value "autol3app" in the text field "username" on the application filter dialogue
    And the user clicks "Apply" button on the application page
    Then the application list displays "qaautol3app@yopmail.com" in the "Email" column as a filtered result