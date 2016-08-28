Feature: Dynamic dependency injection framework
  As a developer
  I want to inject dependencies that are on the classpath
  So that I can create a modular application


  Scenario: Use module provider to get bindings in order to get an instance of a service
    Given a ModuleProvider that provides a module with bindings for FooService
    When I get an instance of FooService
    Then I get the implementation that was bound in the provided module


  Scenario: Adding a module changes behaviour
    Given a ModuleProvider that provides a module with bindings for FooService
    When I add an additional module that overrides the bindings for FooService
    Then I get the implementation that overrode the previous module

