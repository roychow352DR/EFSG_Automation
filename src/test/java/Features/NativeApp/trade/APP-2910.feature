Feature: Native App AO Application

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The take profit price increased by 1 point on the edit position page after plus button is tapped
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "edit" cta button on the position tab of app trade view
      And the user fills in the text field "Take Profit" with direction "BUY" on the edit position page
      And the user taps button "+" of the "Take Profit" input text field on the edit position page
      Then the user sees the "Take Profit" price is increased by 1 point on the edit position page


