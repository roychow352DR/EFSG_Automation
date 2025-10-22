Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK @XPro
  Scenario: User sees an error message upon uncheck declare US Citizen button
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of "Activated" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user uncheck "usCitizen" checkbox on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    Then the user sees "For regulatory reasons, we do not accept citizens or residents of the United States." error message displayed on the CM personal information page