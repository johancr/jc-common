Feature: Local transactions

  As a developer
  I want to have local transactions
  So that I can test code as if it was running in a container

  Scenario: Transaction listener listens to transaction

    Given an active transaction
    When I bind a transaction listener to the transaction
    And the transaction is committed
    Then the listener is notified
