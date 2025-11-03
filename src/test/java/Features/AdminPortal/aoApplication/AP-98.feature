Feature: Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK
    Scenario: Ready only admin user cannot submit the changes of application record
      Given the user logged in to Admin Portal as username "qaautoreadonly" and password "P@ssw0rd!"
      When the user clicks the detail button for the application record with status "Pending Verification", created by "qaauto", and client type "LEVEL_3_INDIVIDUAL" on the application list page
      And the user clicks "Next To Personal Information" button on the application information page
      And the user clicks "Next to Contact Information" button on the personal information page
      And the user clicks "Next to Employee and Financial Information" button on the contact information page
      And the user clicks "Next to Trading Experience" button on the employee & financial information page
      Then the user sees button "Submit" is disabled on the trading experience page