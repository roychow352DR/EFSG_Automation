Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5
  Scenario: User not able to edit Default deposit/withdrawal currency. (Status Spec. Approval for Pending Deposit in AO have Status Locked in CM)
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the record in status "Pending Verification" is created in the application list
    And the user clicks detail button of newly created record with account type "Individual" on the application page
    And the user perform "first" approval on the application page
    And the user logout Admin Portal
    And the user re-logged in to Admin Portal as username "samuellai" and password "Aa12345678!"
    And the user clicks detail button of newly created record with account type "Individual" on the application page
    And the user perform "second" approval on the application page
    And the user clicks detail button of newly created record with account type "Individual" on the application page
    And the user clicks "Activate live trading account" button on the application information page
    And the user selects "Verify the Applicants in real person" as reason on the verify reason pop up on application information page
    And the user clicks "Confirm" button on the application information page
    And the user sees an existing record is updated to "Spec. Approval for Pending Deposit" status on the application list
    When the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of newly created record with account type "Individual" on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    And the user clicks "Next To Trading Experience" button on the CM employee & financial page
    Then the user sees dropdown "settlementCurrency" is not editable on the CM trading experience page
