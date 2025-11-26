Feature: Customer Management


  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK
  Scenario: User sees error dialogue upon submit changes on the cm trading experience page if ID number in use exceeded limit
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user created up to 4 account with existing ID "qaLimitId"
    When the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of "Activated" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user selects ID Type "ID Card" on the CM personal information page
    And the user fills value "qaLimitId" in the text field "identificationNo" on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    Then the user sees an error dialogue with wordings "Identification number has reached the usage limit" is prompted on the CM trading experience page