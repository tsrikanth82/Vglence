Feature: Testing POST APIs


  Scenario Outline: Send a JSON request with <SecID> validate response
    Given I have a JSON request file named "plots_update_name.json"
    And I replace values in JSON with empty data:
      | developerLayoutId | <Dev_id>     |
      | mobileNumber      | <Mobile_Num> |
      | name              | <Name>       |
    When a POST request is sent to the service PlotsAPI endpoint
    Then the response code should be <StatusCode>
    And the response message should contain "<key>" as "<value>"
    Examples:
      | SecID                         | Dev_id | Mobile_Num | Name     | StatusCode | key            | value                          |
      | Name field as Empty              | 12322  | 9876543210 |          | 400        | message        | Name is mandatory              |
      | developerLayoutId field as Empty |        | 9876543210 | Srikanth | 400        | defaultMessage | developerLayoutId is mandatory |
      | mobileNumber field as Empty      | 12322  |            | Srikanth | 400        | defaultMessage | mobileNumber is mandatory      |


  Scenario Outline: Send a JSON request <Scenario> validate response
    Given I have a JSON request file named "plots_update_name.json"
    And I replace values in JSON with following data:
      | developerLayoutId | <Dev_id>     |
      | mobileNumber      | <Mobile_Num> |
      | name              | <Name>       |
    When a POST request is sent to the service PlotsAPI endpoint
    Then the response code should be <StatusCode>
    And the response message should contain "<key>" as "<value>"
    Examples:
      | Scenario                             | Dev_id | Mobile_Num | Name     | StatusCode | key            | value                          |
      | with out Name field tag              | 12322  | 9876543210 |          | 400        | defaultMessage | name is mandatory              |
      | with out developerLayoutId field tag |        | 9876543210 | Srikanth | 400        | defaultMessage | developerLayoutId is mandatory |
      | with out mobileNumber field tag      | 12322  |            | Srikanth | 400        | defaultMessage | mobileNumber is mandatory      |
