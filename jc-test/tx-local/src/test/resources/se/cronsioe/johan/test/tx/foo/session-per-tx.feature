Feature: Transaction support for FooSession
  As a developer
  I want a session per transaction
  So that the state is consistent withing a transaction

Scenario: The session follows the lifecycle of the transaction
Given a transaction
When I request a session
And the session is open
And transaction is committed
Then the session is closed

Scenario: One session per transaction
Given a transaction
When I request a session
And I request another session
Then I get the same session

Scenario: Create new session if session is closed
Given a transaction
When I request a session
And I close the session
And I request another session
Then I get a new session
