Feature: Testing POST APIs

  @Test1234
  Scenario Outline: Send a POST request and validate response
    Given I have a JSON request file named "plots_update_name.json"
    And I replace values in JSON with following data:
      | developerLayoutId | <Dev_id>     |
      | mobileNumber      | <Mobile_Num> |
      | name              | <Name>       |
    When a POST request is sent to the service PlotsAPI endpoint
    Then the response code should be <StatusCode>
    And the response message should contain "<key>" "<value>"
  #  And the response message should contain "Name updated successfully"
    Examples:
      | Dev_id | Mobile_Num   | Name | StatusCode | key            | value                     |
      | 123    | 9876543210   | Srik | 200        | message        | Name updated successfully |
      | 123    | 9876543210   | Srik | 200        | message        | Name updated successfully |
      | 123    | 9876543210   | Srik | 200        | message        | Name updated successfully |
      | 12322  | 987654321022 |      | 400        | defaultMessage | name is mandatory         |