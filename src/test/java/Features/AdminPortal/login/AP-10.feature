Feature: Admin Portal login


  @Regression @AdminPortal @Smoke @EBL_MT5 @EIEHK @XPro
  Scenario: Login as suspended account
    Given the user lands on Admin Portal login page
    And the user fills in with username "qaautosuspend" and password "Test1234@"
    When the user clicks Sign In button
    Then the user sees "User account is suspended! Please contact administration" message pop up