Feature: Transaction support for JPA sessions
  As a developer
  I want a JPA session per transaction
  So that the state is consistent withing a transaction

Scenario: The JPA session follows the lifecycle of the transaction
Given a transaction
When I request a session
And the session is open
And I persist an object in the session
And transaction is committed
Then the session is closed
And the object is persisted in the database

Scenario: One session per transaction
Given a transaction
When I request a session
And I request another session
Then I get the same session

Scenario: Transaction is rolled back
Given a transaction
When I request a session
And the session is open
And I persist an object in the session
And transaction is rolled back
Then the session is closed
And the object is not persisted in database

Scenario: New entity manager provided if closed
Given a transaction
When I request a session
And I close the session
And I request a session
Then the session is open
