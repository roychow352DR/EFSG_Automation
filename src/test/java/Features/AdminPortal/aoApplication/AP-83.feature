Feature: AO Application List


  @Regression @AdminPortal @AO @EIEHK @XPro
  Scenario: Prompt error if mobile number already in used
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the "Exist Phone Number" condition is satisfied
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user submits mandatory information on application information page
    Then the user sees "This mobile number is already in use" error message displayed on application information page
