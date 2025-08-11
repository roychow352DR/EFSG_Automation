  Feature: Customer Management

    @Regression @AdminPortal @CM
    Scenario: CM status in Activated after second approval
      Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
      And the user clicks "Customer Management" on the menu
      When the user clicks detail button of "Pending Approval" record on the customer management page
      And the user clicks "Next To Personal Information" button on the CM application information page
      And the user clicks "Next To Contact Information" button on the CM personal information page
      And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
      And the user clicks "Next To Trading Experience" button on the CM employee & financial page
      And the user clicks "Approve" button on the CM trading experience page
      And the user clicks "Confirm" button on the CM trading experience page
      Then the user sees an existing record is updated to "Activated" status on the customer management list