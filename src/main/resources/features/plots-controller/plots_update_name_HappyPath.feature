Feature: Testing POST APIs

  Scenario Outline: Send a POST request and validate response
    Given I have a JSON request file named "plots_update_name.json"
    And I replace values in JSON with following data:
      | developerLayoutId | <Dev_id>     |
      | mobileNumber      | <Mobile_Num> |
      | name              | <Name>       |
    When a POST request is sent to the service PlotsAPI endpoint
    Then the response code should be <StatusCode>
    And the response message should contain "<key>" as "<value>"
    Examples:
      |Scenario| Dev_id | Mobile_Num   | Name | StatusCode | key            | value                     |
      |With Valid Developer ID| 123    | 9876543210   | Srik | 200        | message        | Name updated successfully |
      |With Valid Developer ID| 123    | 9876543210   | Srik | 200        | message        | Name updated successfully |
      |With Valid Developer ID| 123    | 9876543210   | Srik | 200        | message        | Name updated successfully |
