Feature: Use functional programming

  As a developer
  I want functional constructs
  So that I can do functional programming whenever I like

  Scenario: Map, filter and reduce
    Given a collections of the integers 1, 2, 3
    When I map using a function that adds 1
    And I filter integers greater than 2
    And I reduce
    Then the result is 7