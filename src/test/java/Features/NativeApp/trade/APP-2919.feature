Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User can edit buy market position successfully upon take profit price is greater than market price plus BS point
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "edit" cta button on the position tab of app trade view
      And the user fills in the text field "Take Profit" with direction "BUY" on the edit position page
      And the user taps button "Edit Position" on the edit position page
      And the user taps button "Edit Position" on the confirmation pop up
      And the user taps "detail" cta button on the position tab of app trade view
      Then the user sees the value "Take Profit Price" is updated on the position details page



