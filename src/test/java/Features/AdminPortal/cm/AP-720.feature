Feature: CM Username

  @Regression @AdminPortal @CM @Username @EBL
  Scenario: Username text field is not editable in Pending Verification status on CM
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the menu
    When the user clicks detail button of "Pending Verification" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    Then the user sees text field "username" is not editable on the CM personal information page
