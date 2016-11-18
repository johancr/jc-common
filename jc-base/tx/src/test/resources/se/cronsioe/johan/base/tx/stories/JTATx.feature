Feature: Support for JTA transaction

  As a developer
  I want JTA support for the transaction api
  So that I can use the transaction api in an application container

  Scenario: Transaction support
    Given a JTA transaction
    When a Foo is requested
    And another Foo is requested
    Then Foo is the same instance as another Foo
    And the Foo provider was only called once

