Feature: Transaction scope
  As a developer
  I want a transaction scope
  So that I can test transaction functionality outside of an application container

Scenario: Start a new transaction
Given a transaction scope
When the transaction is begun
Then a transaction is provided

Scenario: Get current transaction
Given a transaction scope with a started transaction
When get transaction
Then the transaction is gotten

Scenario: Get transaction that does not exist
Given a transaction scope
When get transaction
Then an exception is trown

Scenario: Begin transaction when one already exists
Given a transaction scope with a started transaction
When the transaction is begun
Then no exception is thrown
