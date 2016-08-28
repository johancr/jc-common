Feature: Uniform behaviour

  As a developer
  I want an EntityManager to join a transaction, regardless if it's inside a container or during unit test
  So that I can test persistence without worrying if the behaviour is consistent or not

  Scenario Outline: Entity is persisted on commit
    Given an EntityManager in <environment>
    When an entity is persisted in <environment>
    And the transaction is committed in <environment>
    Then the entity has been persisted in <environment>

    Examples:
      | environment |
      | unit test   |
      | application container |

