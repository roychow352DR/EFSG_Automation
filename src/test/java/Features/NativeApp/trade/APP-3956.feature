Feature: Native App trade

    @App @Smoke @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The value of take profit predicted pnl is displayed correctly on the edit position page
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "edit" cta button on the app trade view
      And the user fills in the text field "Take Profit" with direction "BUY" on the edit position page
      Then the user sees the value "Predicted P/L" is displayed correctly on the edit position page



