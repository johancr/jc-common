Feature: Module loader
  As a dependency injection framework
  I want to load Guice modules
  So that I can create bindings

  Scenario: Find modules in META-INF/services
    Given a file named com.google.inject.Module in META-INF/services
    When I request modules to load
    Then I can access the loaded modules


  Scenario: Loaded modules are cached
    Given a file named com.google.inject.Module in META-INF/services
    When I request modules to load
    And the file com.google.inject.Module is removed
    Then I get the same modules when loading again

