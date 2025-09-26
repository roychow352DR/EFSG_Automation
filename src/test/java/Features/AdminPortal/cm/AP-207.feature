Feature: Customer Management

  @Regression @AdminPortal @CM @EBL
  Scenario: All elements on CM application information page of Locked status are non-editable
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the menu
    When the user clicks detail button of "Locked" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    Then the user sees all elements are not editable on the CM application information page