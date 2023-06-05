# See
# https://github.com/intuit/karate#syntax-guide
# for how to write feature scenarios
Feature: As an api user I want to retrieve some github oldset user accounts with zero followers

  Scenario: Get all the oldest user accounts with zero followers
    Given url microserviceUrl
    And path '/oldestAccounts'
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    # see https://github.com/intuit/karate#schema-validation
    # Define the required schema
    * def oldestUserAccountSchema = { id : '#number', 'login' : '#string', 'html_url': '#string' }
    # The response should have an array of oldest user account objects
    And match response == '#[] oldestUserAccountSchema'
