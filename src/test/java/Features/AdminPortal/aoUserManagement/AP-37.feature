Feature: Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
    Scenario: Ready only admin user sees Detail button on the user management page
      Given the user logged in to Admin Portal as username "qaautoreadonly" and password "P@ssw0rd!"
      When the user clicks "Admin Area" on the ao admin portal menu
      And the user clicks "User Management" on the ao admin portal menu
      Then the user sees button "Detail" on the user management page