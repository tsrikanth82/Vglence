@Regression
Feature: Testing Developer Update API Happy Path

  Scenario Outline: Send a JSON request with <SecID> validate response
    Given I have a JSON request file named "developerUpdate.json"
    And I replace values in JSON with following data:
      | developerId    | <Dev_id>  |
      | name           | <Name>    |
      | emailId        | <emailId> |
      | aadharNumber   | <AAdNum>  |
      | firmName       | <fName>   |
      | registrationNo | <regNo>   |
      | roleId         | <roleId>  |
    When a POST request is sent to the service developerUpdate endpoint
    Then the response code should be <StatusCode>
    And the response message should contain "<key>" as "<value>"
    Examples:
      | SecID                | Dev_id | AAdNum     | Name     | emailId        | regNo      | fName     | roleId | StatusCode | key            | value             |
      | with out Name field tag | 123  | 9876543210 | Srikanth | test@gmail.com | REG1234567 | MyCompany | 10     | 200        | defaultMessage | name is mandatory |
