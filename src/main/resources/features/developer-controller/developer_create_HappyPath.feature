@Regression
Feature: Testing Developer Create API Happy Path

  Scenario Outline: Send a JSON request with <SecID> Creating Developer and validate response
    Given I have a JSON request file named "developerCreate.json"
    And I replace values in JSON with following data:
      | mobileNumber   | <MobNum>  |
      | name           | <Name>    |
      | emailId        | <emailId> |
      | aadharNumber   | <AAdNum>  |
      | firmName       | <fName>   |
      | registrationNo | <regNo>   |
      | userName       | <usrName> |
      | password       | <pwd>     |
      | roleId         | <roleId>  |
    When a POST request is sent to the service developerCreate endpoint
    Then the response code should be <StatusCode>
    And the response message should contain "createdBy" as "VGLENCE"
    And the response message should contain "userType" as "DEVELOPER"
    And the response message should contain "status" as "P"
    Examples:
      | SecID            | MobNum     | AAdNum     | Name     | emailId        | regNo      | fName     | roleId | usrName | pwd          | StatusCode |
      | Valid fields for | 1238987675 | 9876543210 | Srikanth | test@gmail.com | REG1234567 | MyCompany | 10     | user123 | password1234 | 200        |
