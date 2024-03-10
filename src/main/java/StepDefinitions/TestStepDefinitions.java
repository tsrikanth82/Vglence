package StepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.VglenceConstants;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
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
import static org.junit.Assert.assertNotNull;


public class TestStepDefinitions {
    private ObjectMapper objectMapper;
    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private Response response;
    private Scenario scenario;
    private JSONObject jsonObject;
    private int httpResponseStatusCode;
    private String httpResponseStatusMessage;
    private JsonNode jsonResponse;

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


    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        response = RestAssured.get(endpoint);
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int expectedStatusCode) {
        //  response.then().statusCode(expectedStatusCode);
        assertEquals(expectedStatusCode, httpResponseStatusCode);
        //       assertEquals(expectedStatusCode,responseEntity);


    }



    @Then("the response message should contain {string}")
    public void theResponseMessageShouldContain(String expectedMessage) throws JsonProcessingException {
        // response.then().assertThat().body(org.hamcrest.Matchers.containsString(expectedMessage));
        JsonNode jsonNode = null;
        assertThat(responseEntity.toString(), notNullValue());
        // respoObject = responseEntity.getBody();
        jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(responseEntity));

    }



    @Then("the response message should contain {string} {string}")
    public void theResponseMessageShouldContain2(String input, String statusMessage) throws JsonProcessingException {
        // response.then().assertThat().body(org.hamcrest.Matchers.containsString(expectedMessage));
        JsonNode jsonNode;
        assertThat(responseEntity2.toString(), notNullValue());
        respoObject = responseEntity2.getBody();

        if (respoObject != null) {
            jsonNode = convert(responseEntity2.getBody());
            validateNestedStringField(jsonNode, input, statusMessage);
        }

    }

    private void validateNestedStringField(JsonNode jsonNode, String field, String value) {
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

    JsonNode responseEntity;

    @When("a {} request is sent to the service {} endpoint")
    public void aPOSTRequestIsSentToTheServicePlotsAPIEndpoint(String httpMethod, String endpointConfiguration) throws IOException {
        HttpRequestBase httpRequest = buildRequest(httpMethod, endpointConfiguration);
        String url = getTargetUrl(endpointConfiguration);
        //      responseEntity = this.makeHttpCall(httpMethod, httpRequest, jsonObject, url);


        responseEntity2 = executeHttpRequest(httpRequest);
        System.out.println(responseEntity);
    }

    private String getTargetUrl(String endpointConfiguration) throws IOException {
        Properties endpointConfigurationMap = new Properties();
        String filePath = "src/main/resources/endpoint.properties";

        try (InputStream inputStream = new FileInputStream(filePath)) {
            endpointConfigurationMap.load(inputStream);
            String servicePath = (String) endpointConfigurationMap.get(endpointConfiguration);
            return servicePath;
        }

    }


//    private ResponseEntity<String> makeHttpCall(String httpMethod, HttpRequestBase httpRequest, JSONObject jsonObject, String url) throws JsonProcessingException {
//
//            if (httpMethod.equalsIgnoreCase("POST")){
//                responseEntity =
//                        WebClient.create()
//                                .post()
//                                .uri(url)
//                                .body(Mono.just(jsonObject), String.class)
//                                .retrieve()
//                                ///.onStatus(HttpStatusCode::is5xxServerError, error -> Mono.empty())
//                                //.onStatus()
//                                .toEntity(String.class)
//                                .block();
//            }else if (httpMethod.equalsIgnoreCase("GET")) {
//                responseEntity = WebClient.create()
//                        .get()
//                        .uri(url)
//                        .retrieve()
//                        ///.onStatus(HttpStatusCode::is5xxServerError, error -> Mono.empty())
//                        //.onStatus()
//                        .toEntity(String.class)
//                        .block();
//            }else{
//                System.out.println("Httep Metho is not supported");
//            }
//            Object response = responseEntity != null ? responseEntity.getBody() : null;
//
//            jsonResponse = response !=null ? objectMapper.readTree(response.toString()) : null;
//
//            System.out.println(" *****Http Headers after API call ******* \n" + responseEntity.getHeaders());
//
//            if (jsonResponse !=null){
//            System.out.println(" *****Response received from Service ******* \n" + jsonResponse.toPrettyString());
//            }
//return responseEntity;
//    }

    @When("I send a POST request to {string}")
    public void iSendAPOSTRequestTo(String endpoint) {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body(jsonObject.toString()).post(endpoint);
    }


    private void executeHttpRequest1(HttpRequestBase httpRequest) {

        response = RestAssured.given().contentType(ContentType.JSON)
                .body(jsonObject.toString()).post(String.valueOf(httpRequest));
    }


    private HttpRequestBase buildRequest(String httpMethod, String endpointConfiguration) throws IOException {
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
                httpRequest = new HttpPost((VglenceConstants.getTargetUrl() + servicePath));
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
            if (jsonObject != null) {

                System.out.println("Request Body: \n" + jsonObject);
                ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(jsonObject.toString()));
            } else {
                assertNotNull(jsonObject);
                System.out.println("Request Body: \n" + jsonObject);
                ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(jsonObject.toString()));
            }
        }
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpRequest);
        sleepThread(1000);
        var httpEntity = closeableHttpResponse.getEntity();

       var response = EntityUtils.toString(httpEntity);

        httpResponseStatusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        httpResponseStatusMessage = closeableHttpResponse.toString();
        if (StringUtils.isNoneEmpty(response)) {
            System.out.println("Response Body: \n" + response.toString());
        }
        closeableHttpResponse.close();

        ResponseEntity<String> responseEntity2 = convertS(response);
        return responseEntity2;
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
