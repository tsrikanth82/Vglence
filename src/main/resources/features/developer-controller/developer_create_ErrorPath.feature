@Regression
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
      | Name         | 9898765432 |          | 10     | user123 | password1234 | fieldName | name         | defaultMessage | name is mandatory              |
      | usrName      | 9898765432 | Srikanth | 10     |         | password1234 | fieldName | userName     | errors | User Name should have minimum of 6 and maximum of 20 characters         |
      | password     | 9898765432 | Srikanth | 10     | user123 |              | fieldName | password     | defaultMessage | Password is mandatory          |
      | roleId       | 9898765432 | Srikanth |        | user123 | password1234 | fieldName | roleId       | defaultMessage | Role is mandatory              |


#  Scenario Outline: Send a JSON request for User Name filed with <SecID> in Creating Developer API and validate response
#    Given I have a JSON request file named "developerCreate.json"
#    And I replace values in JSON with empty data:
#      | mobileNumber   | 9898765432     |
#      | name           | Name           |
#      | emailId        | test@gmail.com |
#      | aadharNumber   | 9876543210     |
#      | firmName       | MyCompany      |
#      | registrationNo | 67856543       |
#      | userName       | <usrName>      |
#      | password       | password1234   |
#      | roleId         | 10             |
#    When a POST request is sent to the service developerCreate endpoint
#    Then the response code should be 400
#    And the response message should contain "fieldName" as "userName"
#    And the response message should contain "defaultMessage" as "User Name is mandatory"
#    And the response message should contain "fieldName" as "userName"
#    And the response message should contain "defaultMessage" as "User Name should have minimum of 6 and maximum of 20 characters"
#    Examples:
#      | SecID        | usrName |
#      | mobileNumber |         |
#
