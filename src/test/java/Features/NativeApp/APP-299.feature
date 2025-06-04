Feature: Native App login



    @App @Smoke
    Scenario Outline: The user can log in with lv2 account
      Given the user launch the app
      And the user lands on the login page
      When the user fills email <email> and password <password> on App login page
      And the user clicks Login button on App login page
      And the user skips biometric validation
      Then the user sees "Open a Live Trading Accounts" button is displayed at the home page


      Examples:

        | email | password |
        | eieuat564@yopmail.com | Test1234@ |