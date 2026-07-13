Feature: CM Username

  @Regression @AdminPortal @CM @Username @EBL_MT5 @EGM
  Scenario: Individual - Error toast prompted upon username already in use
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of "Activated" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user fills value "QaAutoL2" in the text field "username" on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    Then the user sees dialogue text "The username is already in use" is prompted on the CM personal information page