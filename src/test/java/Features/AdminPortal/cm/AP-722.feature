Feature: AO Application List

  @Regression @AdminPortal @CM @Username @EBL_MT5
  Scenario: User can filtered customer management record by username
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks button "Filter" on the customer management page
    And the user fills value "autol3app" in the text field "username" on the customer management filter dialogue
    And the user clicks button "Apply" on the customer management page
    Then the customer management list displays "qaautol3app@yopmail.com" in the "Email" column as a filtered result