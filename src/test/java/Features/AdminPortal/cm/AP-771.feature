Feature: Customer Management

  @Regression @AdminPortal @CM @Backend
  Scenario: status equals to 2 in cm.person database table for Pending Deposit account
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user sees a record in "Pending Deposit" status on the application list
    Then "status" is "1" in CM "person" database table where "person_id" retrieved by "Person ID"