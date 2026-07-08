Feature: Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro @EGM
    Scenario: Ready only admin user cannot see Create button on the application list page
      Given the user logged in to Admin Portal as username "qaautoreadonly" and password "P@ssw0rd!"
      Then the user sees button "Create" is hidden on the application list page