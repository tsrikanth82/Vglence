Feature: Testing POST APIs


#  Scenario: Sending a POST request
#    Given I have a JSON request file named "plots_update_name"
#  # And the following JSON data:
#    And set fields
#      | Key               | Value      |
#      | developerLayoutId | 123        |
#      | mobileNumber      | 9876543210 |
#      | name              | Srik       |
##    When I send a POST request to "/api/endpoint" with the JSON data
##    Then the response should have HTTP code 200
##    And the response message should contain "success"



  Scenario: Test scenario
    Given a API call with "plots_update_name" request
   # And set fields
 # And I replace values with:
    And I replace values in JSON with following data:
      | developerLayoutId | <123>        |
      | mobileNumber      | <9876543210> |
      | name              | <Srik>       |
#    And generate random fields to requestPayload
    When a POST request is sent to the service PlotsAPI endpoint



#  @Test1234
#  Scenario: Test scenario
#    Given I have a JSON file named "plots_update_name" request
#   # And set fields
#    And I replace values with:
#      | Key               | Value      |
#      | developerLayoutId | 123        |
#      | mobileNumber      | 9876543210 |
#      | name              | Srik       |
#    When I perform some action
##    And generate random fields to requestPayload
##    When a POST request is sent to the service PlotsAPI endpoint