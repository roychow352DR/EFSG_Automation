Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User can partially close position successfully
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "close" cta button on the app trade view
      And the user edit the lot size which is smaller than the current lot size on the close position page
      And the user taps button "Close Position" on the instrument details page
      And the user taps button "Close Position" on the confirmation pop up
      Then the user sees the message "Your position is closed" is displayed at the dialogue
      And the user sees correct lot size of the partially closed position on the position tab of app trade view

