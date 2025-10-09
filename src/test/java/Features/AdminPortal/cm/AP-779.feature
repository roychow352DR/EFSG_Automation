Feature: Customer Management

  @Regression @AdminPortal @CM @EBL @EIE @XPro
  Scenario: User sees mobile number change applied to CM database
    Given the user logged in to Admin Portal as username "aoadmin02" and password "P@ssw0rd!"
    And the user clicks "Customer Management" on the menu
    When the user clicks detail button of specific entity record on the customer management page
    And the user clicks "Next To Personal Information" button on the CM application information page
    And the user sees change value of "mobile" on the CM personal information page
    And the user clicks "Next To Contact Information" button on the CM personal information page
    And the user clicks "Next To Employee and Financial Information" button on the CM contact information page
    And the user clicks "Next To Trading Experience" button on the CM employee & financial page
    And the user clicks "Approve" button on the CM trading experience page
    And the user clicks "Confirm" button on the CM trading experience page
    Then "phone_num" is updated to modified value in CM "person_phone" database table where "profile_id" retrieved by "Profile ID"

