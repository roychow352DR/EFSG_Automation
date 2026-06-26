Feature: MIO Deposit Management

  @Smoke @Regression @MIO @EBL_MT5 @DepositManagement
  Scenario: : User sees the options saved in all the filters after closing the detail page
    Given the user lands on MIO Admin Portal login page
    And the user fills username "admin" and password "123456Aa!" on MIO Admin Portal login page
    When the user clicks Transactions Management > Deposit Management
    And the user selects all filters and some filters selects multiple options
    And the user clicks "Search" button then sort the filtered results by clicking a column header
    And the user selects "Detail" button of the first record
    And the user clicks "Cancel" button
    Then the user sees the options saved in all the filters