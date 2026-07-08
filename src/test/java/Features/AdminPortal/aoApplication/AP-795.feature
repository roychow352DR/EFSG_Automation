Feature: AO Application List

  @Regression @AdminPortal @Smoke @AO @Username @EBL_MT5 @EGM
  Scenario: Individual - Username in Rejected status is reusable for applicant creation
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the record in status "Rejected" is created in the application list
    And value "username" is retrieved according to the param value "REJECTED" of param "status" from the ao page api
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills mandatory information on application information page
    And the user fills textField "username" retrieved from api endpoint on the application information page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user submit ao application from personal information page
    Then the user sees a record in "Pending Verification" status is created on the application list
