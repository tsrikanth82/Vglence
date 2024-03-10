@Regression
Feature: Testing developerInfo GET API Error Path

  Scenario Outline: Send a GET request for <API> API With <SecID> and validate response
    When a GET request is sent to the service <API> endpoint with <fields> and <values>
    Then the response code should be <Status>
    Examples:
      | SecID                            | API           | fields      | values |Status|
      | DeveloperId does not exist in DB | developerInfo | developerId | 5361   |204   |
      | Invalid DeveloperId              | developerInfo | developerId | qwer   |400   |