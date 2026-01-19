Feature: Native App AO Application

    @App @Smoke @Regression @Username @EBL_MT5
    Scenario: Prompted error message upon duplicate username is entered on sign up page
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app sign up page
      When the user fills value "autol2" in the text field "username" on the app sign up page
      Then the user sees error message "This username is already in use" is displayed at the app sign up page

