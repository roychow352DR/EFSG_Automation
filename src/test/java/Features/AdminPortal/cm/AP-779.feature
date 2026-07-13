Feature: Customer Management

  @Regression @AdminPortal @CM @EBL_MT5 @EIEHK @XPro @EGM
  Scenario: User sees mobile number change applied to CM database
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Customer Management" on the ao admin portal menu
    When the user clicks detail button of specific entity record on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user sees change value of "mobile" on the CM personal information page
    And the user performs second approval from personal info page on customer management page
    Then "phone_num" is updated to modified value in CM "person_phone" database table where "profile_id" retrieved by "Profile ID"

