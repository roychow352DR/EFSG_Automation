Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: Back to edit position page after the confirmation dialogue of edit position is closed
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Me" on the app footer
      And the user toggles on trade confirmation on the app setting page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "edit" cta button on the app trade view
      And the user fills in the text field "Stop Loss" with direction "BUY" on the edit position page
      And the user taps button "Edit Position" on the instrument details page
      And the user taps button "x" on the confirmation pop up of edit position page
      Then the user sees "Edit Position" page
