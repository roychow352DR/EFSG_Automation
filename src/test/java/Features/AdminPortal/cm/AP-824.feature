Feature: CM Username

  @Regression @AdminPortal @CM @Username @EBL_MT5
  Scenario: Company - Error toast prompted upon username already in use
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of "Activated" record with "LEVEL_3_COMPANY" client type on the customer management page
    And the user clicks "Next To User Information" button on the CM application information page
    And the user fills value "QaAutoL2" in the text field "username" on the CM user information page
    And the user clicks button "Next To Contact Information" on the CM user information page
    Then the user sees dialogue text "The username is already in use" is prompted on the CM user information page