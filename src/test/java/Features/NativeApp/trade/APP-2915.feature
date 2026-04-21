Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees an error message on edit position when the stop loss price of buy order is greater than current price minus BS point
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "edit" cta button on the position tab of app trade view
      And the user fills in the text field "Stop Loss" with direction "BUY" and the price is greater than current price minus BS point on the edit position page
      Then the user sees an error message "Invalid stop loss price" is displayed on the edit position page




