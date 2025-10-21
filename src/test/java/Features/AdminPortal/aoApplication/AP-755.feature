Feature: AO Application List

  @Regression @AdminPortal @AO @EBL
  Scenario: Edit records on Pending Verification status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the data created by "app" in status "Pending Verification" is found in the AO application list
    When the user clicks detail button of app client on the application page
