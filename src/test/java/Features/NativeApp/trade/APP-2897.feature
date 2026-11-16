Feature: Native App trade

    @App @Smoke @Regression @Trade @EIEHK @AppTest
    Scenario: User sees a product code is displayed correctly on edit position page
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "edit" cta button on the app trade view
      Then the user sees the trade symbol underneath the product name on the edit position page
      And the user sees the value "Product" is displayed correctly on the edit position page





