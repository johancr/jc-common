Feature: JTA support for transaction API

  As a developer
  I want a transaction API that supports JTA
  So that I can use the API both in a container and in unit tests

  Scenario: Transaction listener listens to transaction

    Given an active transaction
    When I bind a transaction listener to the transaction
    And the transaction is committed
    Then the listener is notified


  Scenario: Register resources to the transaction
    Given an active transaction
    When I bind a resource to the transaction
    Then I can lookup the resource while the transaction is active
