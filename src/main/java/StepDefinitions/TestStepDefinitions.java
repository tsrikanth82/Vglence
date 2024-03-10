package StepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.VglenceConstants;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;


public class TestStepDefinitions {
    private ObjectMapper objectMapper;
    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private Response response;
    private Scenario scenario;
    private JSONObject jsonObject;
    private int httpResponseStatusCode;
    private String httpResponseStatusMessage;
    private JsonNode jsonResponse;
    JsonNode responseEntity;
    Object respoObject;
    ResponseEntity<String> responseEntity2;

    @Given("I have a JSON request file named {string}")
    public void iHaveAJSONRequestFileNamed(String fileName) throws IOException {
        String filePath = "src/main/resources/request/" + fileName;
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        jsonObject = new JSONObject(jsonContent);
    }

    @Given("I replace values in JSON with following data:")
    public void iReplaceValuesInJSONWithFollowingData(Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
    }

    @Given("I replace values in JSON with empty data:")
    public void iReplaceValuesInJSONWithEmptyData(Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            if (entry.getValue() != null) {
                jsonObject.put(entry.getKey(), entry.getValue());
            } else {
                jsonObject.isNull(entry.getKey());
            }

        }
    }

    @When("a {} request is sent to the service {} endpoint")
    public void aPOSTRequestIsSentToTheServicePlotsAPIEndpoint(String httpMethod, String endpointConfiguration) throws IOException {
        HttpRequestBase httpRequest = buildRequest(httpMethod, endpointConfiguration, null);
        responseEntity2 = executeHttpRequest(httpRequest);
    }

    @When("a {} request is sent to the service {} endpoint with {} and {}")
    public void aGETRequestIsSentToTheServicePlotsAPIEndpoint(String httpMethod, String endpointConfiguration, String field, String value) throws IOException {
        HttpRequestBase httpRequest = buildRequest(httpMethod, endpointConfiguration, value);
        responseEntity2 = executeHttpRequest(httpRequest);
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int expectedStatusCode) {
        assertEquals(expectedStatusCode, httpResponseStatusCode);
    }

    @Then("the response message should contain {string} as {string}")
    public void theResponseMessageShouldContain2(String input, String statusMessage) throws JsonProcessingException {
        JsonNode jsonNode;
        assertThat(responseEntity2.toString(), notNullValue());
        respoObject = responseEntity2.getBody();
        if (respoObject != null) {
            jsonNode = convert(responseEntity2.getBody());
            validateNestedStringField(jsonNode, input, statusMessage);
        }
    }

    private void validateNestedStringField(JsonNode jsonNode, String field, String value) {
        // Iterate over the "errors" array and assert each "fieldName"
        JsonNode errorsNode = jsonNode.get("errors");
        if (errorsNode != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(String.valueOf(jsonNode), JsonObject.class);
            JsonArray errorsArray = jsonObject.getAsJsonArray("errors");
            for (JsonElement element : errorsArray) {
                JsonObject errorObject = element.getAsJsonObject();
                JsonElement defaultMessageElement = errorObject.get(field);
                if (defaultMessageElement != null && defaultMessageElement.getAsString().equals(value)) {
                    System.out.println("Match found!");
                }
            }
        } else {
            {
                String[] keys = field.split("[.]");
                var jsonValue = jsonNode;
                for (String key : keys) {
                    if (jsonValue.isArray()) {
                        jsonValue = jsonValue.get(Integer.parseInt(key));
                    } else {
                        jsonValue = jsonValue.findValue(key);
                    }
                    Assertions.assertEquals(value, jsonValue.asText());
                }
            }

        }
    }


//    private String getTargetUrl(String endpointConfiguration) throws IOException {
//        Properties endpointConfigurationMap = new Properties();
//        String filePath = "src/main/resources/endpoint.properties";
//        try (InputStream inputStream = new FileInputStream(filePath)) {
//            endpointConfigurationMap.load(inputStream);
//            String servicePath = (String) endpointConfigurationMap.get(endpointConfiguration);
//            return servicePath;
//        }
//    }

//    private void executeHttpRequest1(HttpRequestBase httpRequest) {
//
//        response = RestAssured.given().contentType(ContentType.JSON)
//                .body(jsonObject.toString()).post(String.valueOf(httpRequest));
//    }

    private HttpRequestBase buildRequest(String httpMethod, String endpointConfiguration, String value) throws IOException {
        HttpRequestBase httpRequest;
        Properties endpointConfigurationMap = new Properties();
        String filePath = "src/main/resources/endpoint.properties";

        try (InputStream inputStream = new FileInputStream(filePath)) {
            endpointConfigurationMap.load(inputStream);
        }
        String servicePath = (String) endpointConfigurationMap.get(endpointConfiguration);
        switch (httpMethod) {
            case "POST":
                httpRequest = new HttpPost((VglenceConstants.getTargetUrl() + servicePath));
                break;
            case "GET":
                httpRequest = new HttpGet((VglenceConstants.getTargetUrl() + servicePath + value));
                break;
            case "DELETE":
                httpRequest = new HttpDelete((VglenceConstants.getTargetUrl() + servicePath));
                break;
            default:
                throw new NotImplementedException("Cannot resolve httpMethod");
        }
        return httpRequest;
    }

    private ResponseEntity<String> executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
        httpRequest.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        System.out.println("Sending Request to Target Endpoint: " + httpRequest.toString());
        if (httpRequest instanceof HttpPost) {
            try {
                if (jsonObject != null) {
                    System.out.println("Request Body: \n" + jsonObject.toString(4));
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(jsonObject.toString()));
                }
            } catch (NullPointerException e) {
                System.out.println("JSON Body is empty: \n" + jsonObject.toString(4));
            }
        }
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpRequest);
        sleepThread(1000);
        var httpEntity = closeableHttpResponse.getEntity();
        if (httpEntity != null){
        if (httpEntity.getContentLength() != 0) {
            var response = EntityUtils.toString(httpEntity);

            JSONObject convertResponseString = new JSONObject(convert(response).toString());
            httpResponseStatusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            httpResponseStatusMessage = closeableHttpResponse.toString();
            if (StringUtils.isNoneEmpty(response)) {
                System.out.println("********** Response Body ************* \n" + convertResponseString.toString(4));

            }
            closeableHttpResponse.close();

            ResponseEntity<String> responseEntity2 = convertS(response);
            return responseEntity2;
        }
        } else {
            httpResponseStatusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            System.out.println("**********There is No Response Body ************* \n");
            return null;
        }
        httpResponseStatusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("**********There is No Response Body ************* \n");
        return null;
    }


    private static JsonNode convert(String response) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        // Parse String to JsonNode
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode;
    }

    public static ResponseEntity<String> convertS(String content) {
        // Assuming you want to return HTTP status OK (200)
        return ResponseEntity.ok(content);
    }

    private void sleepThread(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            //           log.info("Exception sleeping method");
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
