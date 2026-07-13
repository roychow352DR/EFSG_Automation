Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: User sees an error message upon edit the Pending Verification status record
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of "Pending Verification" record with "LEVEL_3_INDIVIDUAL" client type on the customer management page
    And the user submits change on customer management page
    Then the user sees an error dialogue with wordings "The client information is being reviewed and cannot be modified. Please wait for the approval before operation." on the trading experience page
