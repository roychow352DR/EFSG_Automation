Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: Lv2 user sees a page with This services is for live trading account only after tapping on direction button
      Given the user launch the app
      And the l2 user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user taps direction "BUY" on the app trade view
      Then the user sees a page with a content "This service is for live trading account only."



