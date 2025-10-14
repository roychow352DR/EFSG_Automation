Feature: AO Blacklist

  @Regression @AdminPortal @Blacklist @EBL @EIE @XPro
  Scenario: User who has no Export access cannot see Export button
    Given the user logged in to Admin Portal as username "qaautoreadonly" and password "P@ssw0rd!"
    When the user clicks "AO Blacklist" on the ao admin portal menu
    Then the "Export" button is not displayed on the blacklist page