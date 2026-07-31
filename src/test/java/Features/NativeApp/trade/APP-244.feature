Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User can edit position successfully
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      And the user taps button "BUY" on the instrument details page
      And the user taps button "BUY" on the confirmation pop up
      When the user taps "edit" cta button on the app trade view
      And the user edit price type "Stop Loss" of the position on the instrument details page
      And the user edit price type "Take Profit" of the position on the instrument details page
      And the user taps button "Edit Position" on the instrument details page
      And the user taps button "Edit Position" on the confirmation pop up
      And the user taps "detail" cta button on the app trade view
      Then the user sees market order values are displayed correctly with the user input value on the position details page





