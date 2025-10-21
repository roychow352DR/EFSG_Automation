Feature: Customer Management

  @Regression @AdminPortal @CM @EBL @EBL @EIE @XPro
  Scenario: All elements on CM employee & financial information page of Locked status are non-editable
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of "Locked" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    Then the user sees all elements are not editable on the CM employee and financial information page