Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The estimated margin of RKGCNH is displayed correctly at Trade page if initial margin equals to 0
      Given the user launch the app
      And the user login as username "autol3qa" and password "Test1234@" on App login page
      And the initial margin is set to zero
      And the user taps button "Markets" on the app footer
      When the user taps symbol "RKGCNH" on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      Then the user sees the "Est. Margin" value is displayed correctly on the instrument details page


