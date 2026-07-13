Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: User sees change history after CM status updated to Pending Approval
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of specific entity record on the customer management page
    And the user performs first approval on customer management page
    And the user clicks detail button of modified record on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    Then the user sees "History" button on the CM personal information page
    And the user sees "History" dialogue on the CM personal information page upon click on the "History" button


