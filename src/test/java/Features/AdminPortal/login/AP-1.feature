Feature: Admin Portal login

  @Regression @AdminPortal @EBL_MT5 @EIEHK @XPro @Login @EGM
  Scenario: Login with empty value
    Given the user lands on Admin Portal login page
    When the user fills in with username "" and password ""
    Then the user sees the Sign In button is unclickable

