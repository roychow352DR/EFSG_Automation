Feature: AO Application List

  @Regression @AdminPortal @CM @Username @EBL_MT5
  Scenario: User can search customer management record by username
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user fills value "autol3app" on the customer management search field
    Then the customer management list displays "qaautol3app@yopmail.com" in the "Email" column as a filtered result