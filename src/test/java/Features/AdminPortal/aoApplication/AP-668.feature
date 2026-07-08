Feature: AO Application List

  @Regression @AdminPortal @AO @Username @EBL_MT5 @EGM
  Scenario: User can search application record by username
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user fills value "autol2" on the application search field
    Then the application list displays "ebluatl2@yopmail.com" in the "Email" column as a filtered result