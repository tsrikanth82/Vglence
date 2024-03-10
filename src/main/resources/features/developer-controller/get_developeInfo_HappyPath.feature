@Regression
Feature: Testing developerInfo GET API Happy Path

  Scenario Outline: Send a GET request for <API> API With <SecID> and validate response

    When a GET request is sent to the service <API> endpoint with <fields> and <values>
    Then the response code should be 200
    And the response message should contain "createdBy" as "VGLENCE"
    And the response message should contain "userType" as "DEVELOPER"
    And the response message should contain "status" as "P"
    Examples:
      | SecID                | API           | fields      | values |
      | Valid developerId ID | developerInfo | developerId | 536    |
