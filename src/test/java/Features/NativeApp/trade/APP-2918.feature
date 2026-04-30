Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees an error message on edit position when the stop loss price of sell order is smaller than current price plus BS point
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "SELL" position on the instrument details page
      When the user taps "edit" cta button on the app trade view
      And the user fills in the text field "Stop Loss" with direction "SELL" and the price is smaller than current price plus BS point on the edit position page
      Then the user sees an error message "Invalid stop loss price" is displayed on the edit position page




