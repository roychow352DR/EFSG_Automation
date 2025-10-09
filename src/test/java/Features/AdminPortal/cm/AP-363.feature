Feature: Customer Management

  @Regression @AdminPortal @CM @EBL @EIE @XPro
  Scenario: CM status is Locked for the Spec. Approval for Pending Deposit record in AO
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user sees a record in "Spec. Approval for Pending Deposit" status on the application list
    And the user clicks "Customer Management" on the menu
    Then the user sees "Locked" status on customer management page