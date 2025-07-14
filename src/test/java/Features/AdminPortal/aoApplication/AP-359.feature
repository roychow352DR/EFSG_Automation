Feature: AO Application List


  @Test
  Scenario Outline: Account status in Spec. Approval for Pending Deposit after first approve reason
    Given the user logged in to Admin Portal as username <name> and password <password>
    When the user clicks detail button of "Pending Deposit" record on the application page
    And the user clicks "Activate live trading account" CTA button on the application information page
    And the user selects "Verify the Applicants in real person" as reason on the verify reason pop up
    And the user clicks "Confirm" button on the verify reason pop up
    Then the user sees a record in "Spec. Approval for Pending Deposit" status after approval

    Examples:
      | name      | password  |
      | aoadmin01 | P@ssw0rd! |

