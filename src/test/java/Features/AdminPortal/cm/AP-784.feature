Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: Prompted error after the invalid referral IB code is entered on CM
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    And the user clicks detail button of specific entity record on the customer management page
    And the user clicks "Next to Personal Information" button on the CM application information page
    When the user fills value "invalidReferralIB" in the text field "upperIbAcc" on the CM personal information page
    And the user clicks "Next to Contact Information" button on the CM personal information page
    Then the user sees "Invalid Code" error message displayed on the CM personal information page