@Test1234
Feature: Testing Developer Developer Create Error Path


  Scenario Outline: Send a JSON request with empty field for <SecID> in Creating Developer and validate response
    Given I have a JSON request file named "developerCreate.json"
    And I replace values in JSON with empty data:
      | mobileNumber   | <MobNum>       |
      | name           | <Name>         |
      | emailId        | test@gmail.com |
      | aadharNumber   | 9876543210     |
      | firmName       | MyCompany      |
      | registrationNo | 67856543       |
      | userName       | <usrName>      |
      | password       | <pwd>          |
      | roleId         | <roleId>       |
    When a POST request is sent to the service developerCreate endpoint
    Then the response code should be 400
    And the response message should contain "<key>" as "<value>"
    And the response message should contain "<key1>" as "<value1>"
    Examples:
      | SecID        | MobNum     | Name     | roleId | usrName | pwd          | key       | value        | key1           | value1                         |
      | mobileNumber |            | Srikanth | 10     | user123 | password1234 | fieldName | mobileNumber | defaultMessage | mobileNumber must be 10 digits |
      | Name         | 9898765432 |          | 10     | user123 | password1234 | fieldName | name         | defaultMessage | name must be 10 digits         |
